package com.poc.callblocker;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;
import com.poc.callblocker.database.BlacklistedContactsDb;
import com.poc.callblocker.database.BlockedCallsDb;
import com.poc.callblocker.model.BlockedCall;
import com.poc.callblocker.ui.ScrUnknownNumberActions;
import com.poc.callblocker.utilities.AppSettings;
import com.poc.callblocker.utilities.CallLogManager;
import com.poc.callblocker.utilities.ContactManager;
import com.poc.callblocker.utilities.NotificationManager;
import com.poc.callblocker.utilities.UiConstants;

/**
 * This is a singleton class that handles the call related broadcasts and take the action depending
 * on the settings. Blocking a call from blacklisted number, displaying "block this number" dialog
 * for unknown number are some of the responsibilities of this class.
 * 
 * @author Sayyad Abid
 */
public class CallBlocker {
    private static CallBlocker _instance;

    /**
     * Private Constructor to implement singleton design pattern
     */
    private CallBlocker() {

    }

    /**
     * Method to get the initialized instance of {@link CallBlocker}
     * 
     * @return the initialized instance of {@link CallBlocker}
     */
    public static CallBlocker getInstance() {
        if ( _instance == null ) {
            _instance = new CallBlocker( );
        }
        return _instance;
    }

    /**
     * This method is used to handle the phone ringing event. Checks if the input phone number is
     * blacklisted, if yes, blocks a call and adds an entry to blocked calls database. Also displays
     * notification for blocked call if setting is enabled.
     * 
     * @param phoneNumber
     *            the incoming call's phone number
     */
    public void handleRingingEvent( final String phoneNumber ) {
        /**
         * Handling Ringing event on new thread since searching whether a contact is in blacklist
         * can be time consuming task. Moreover the ringing event is received from broadcast, so
         * waiting in broadcast receiver is not a good practice.
         */
        new Thread( new Runnable( ) {
            @Override
            public void run() {
                boolean isBlacklistedNumber = BlacklistedContactsDb.getInstance( ).isBlacklistedNumber( phoneNumber );
                if ( isBlacklistedNumber ) {
                    _blockCall( phoneNumber );
                    /**
                     * Display notification if showNotificationForBlockedCalls is enabled in
                     * settings
                     */
                    if ( AppSettings.getInstance( ).showNotificationForBlockedCalls( ) ) {
                        _displayCallBlockedNotification( );
                    }
                }
            }
        } ).start( );
    }

    /**
     * This method is used to handle the phone idle event. Checks if the last call was from unknown
     * number, if yes then checks if this number is already blacklisted if not then displays a
     * dialog to add this number to blacklist.
     * 
     * @param phoneNumber
     *            the last call's phone number
     */
    public void handleIdleEvent( final String phoneNumber ) {
        /**
         * Handling phone state idle event on new thread since searching whether a contact is in
         * blacklist/contacts can be time consuming task. Moreover the phone state idle event is
         * received from broadcast, so waiting in broadcast receiver is not a good practice.
         */
        new Thread( new Runnable( ) {
            @Override
            public void run() {
                if ( AppSettings.getInstance( ).showDialogForUnknowNumbers( ) ) {
                    ContactManager contactManager = new ContactManager( );
                    boolean isPresentInContacts = contactManager.isNumberPresent( phoneNumber );
                    if ( !isPresentInContacts ) {
                        /**
                         * If phone number is not present in contacts, check if it's already
                         * blacklisted if not, show block contact dialog.
                         */
                        boolean isBlacklistedNumber = BlacklistedContactsDb.getInstance( ).isBlacklistedNumber( phoneNumber );
                        if ( !isBlacklistedNumber ) {
                            Intent intent = new Intent( MainApplication.appContext, ScrUnknownNumberActions.class );
                            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                            intent.addFlags( Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS );
                            intent.putExtra( UiConstants.PHONE_NUMBER, phoneNumber );
                            MainApplication.appContext.startActivity( intent );
                        }
                    }
                }
            }
        } ).start( );
    }

    /**
     * This method is used to block the call. Uses java reflection to get access to
     * ITelephonyManager's endCall method
     * 
     * @param phoneNumber
     *            the incoming call's phone number
     */
    private void _blockCall( String phoneNumber ) {
        boolean isCallRejected = false;

        TelephonyManager telephony = (TelephonyManager) MainApplication.appContext.getSystemService( Context.TELEPHONY_SERVICE );
        ITelephony telephonyService = null;
        try {
            /**
             * Java reflection to gain access to TelephonyManager's ITelephony getter.
             */
            Class< ? > c = Class.forName( telephony.getClass( ).getName( ) );
            Method m = c.getDeclaredMethod( "getITelephony" );
            m.setAccessible( true );
            telephonyService = (ITelephony) m.invoke( telephony );
            Log.i( MainApplication.LOG_TAG, "CallBlocker: _blockCall: Blocking ringing call using ITelephony" );
            isCallRejected = telephonyService.endCall( );

        } catch ( Exception e ) {
            Log.e( MainApplication.LOG_TAG, "CallBlocker: _blockCall: Exception in Rejecting ringing call using ITelephony. " + e.getMessage( ) + e.toString( ) + Log.getStackTraceString( e ) );
            e.printStackTrace( );
        }

        Log.i( MainApplication.LOG_TAG, "CallBlocker: _blockCall: call rejection using ITelephony " + isCallRejected );

        /**
         * On some devices (Manufacturer is LG) the ITelephoney's endCall method does not work. Use
         * another method to block call
         */
        if ( !isCallRejected ) {
            Log.i( MainApplication.LOG_TAG, "CallBlocker: _blockCall: call not rejected in first try. Trying other method of rejection." );
            try {
                _reject( );
            } catch ( Exception e ) {
                Log.i( MainApplication.LOG_TAG, "CallBlocker: _blockCall: call not rejected by any method. " + e.getMessage( ) );
                e.printStackTrace( );
            }
        }

        _addBlockedCallToDb( phoneNumber );

        /**
         * Removed last missed call entry from call logs
         */
        new Thread( ) {
            public void run() {
                try {
                    Thread.sleep( 2000 );
                } catch ( InterruptedException e ) {
                    Log.e( MainApplication.LOG_TAG, "CallBlocker: _blockCall: InterruptedException ->" + e.getMessage( ) );
                }
                CallLogManager callLogManager = new CallLogManager( );
                callLogManager.removeMissedCallEntry( );
            };
        }.start( );
    }

    /**
     * This function rejects call using headset intent method. First sends headset plugged in
     * broadcast, the sends headset key pressed broadcast. This is as good as user is manually
     * disconnecting the call by pressing headset button. Once the call is disconnected, it again
     * send the headset removed broadcast.
     */
    private void _reject() {
        Intent headSetUnPluggedintent = new Intent( Intent.ACTION_HEADSET_PLUG );
        headSetUnPluggedintent.addFlags( Intent.FLAG_RECEIVER_REGISTERED_ONLY );
        headSetUnPluggedintent.putExtra( "state", 1 );
        headSetUnPluggedintent.putExtra( "name", "Headset" );
        MainApplication.appContext.sendOrderedBroadcast( headSetUnPluggedintent, null );

        Intent buttonDown = new Intent( Intent.ACTION_MEDIA_BUTTON );
        buttonDown.putExtra( Intent.EXTRA_KEY_EVENT, new KeyEvent( KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK ) );
        MainApplication.appContext.sendOrderedBroadcast( buttonDown, "android.permission.CALL_PRIVILEGED" );

        headSetUnPluggedintent = new Intent( Intent.ACTION_HEADSET_PLUG );
        headSetUnPluggedintent.addFlags( Intent.FLAG_RECEIVER_REGISTERED_ONLY );
        headSetUnPluggedintent.putExtra( "state", 0 );
        headSetUnPluggedintent.putExtra( "name", "Headset" );
        MainApplication.appContext.sendOrderedBroadcast( headSetUnPluggedintent, null );
    }

    /**
     * This method is used to add blocked call to database.
     * 
     * @param phoneNumber
     *            the blocked call's phone number
     */
    private void _addBlockedCallToDb( String phoneNumber ) {
        String name = BlacklistedContactsDb.getInstance( ).getNameFromNumber( phoneNumber );
        if ( TextUtils.isEmpty( name ) ) {
            name = MainApplication.appContext.getString( R.string.lbl_unknown );
        }
        BlockedCall blockedCall = new BlockedCall( name, phoneNumber, System.currentTimeMillis( ), BlockedCall.STATUS_UNREAD );
        BlockedCallsDb.getInstance( ).add( blockedCall );
    }

    /**
     * This method is used to display notification for blocked call.
     */
    private void _displayCallBlockedNotification() {
        String notificationTitle = MainApplication.appContext.getString( R.string.app_name );

        int unreadBlockedCallsCount = BlockedCallsDb.getInstance( ).getUnreadBlockedCallsCount( );
        String message = MainApplication.appContext.getResources( ).getQuantityString( R.plurals.msg_blocked_calls, unreadBlockedCallsCount, unreadBlockedCallsCount );

        NotificationManager.getInstance( ).displayNotification( notificationTitle, message );
    }
}
