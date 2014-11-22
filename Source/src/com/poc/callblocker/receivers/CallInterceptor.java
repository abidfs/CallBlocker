package com.poc.callblocker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.poc.callblocker.CallBlocker;
import com.poc.callblocker.MainApplication;

/**
 * This class extends {@link BroadcastReceiver} and is responsible for receiving call related
 * events. Notifies event to the application.
 * 
 * @author Sayyad Abid
 */
public class CallInterceptor extends BroadcastReceiver {

    private static boolean _isRinging = false;
    private static String _phoneNumber = null;

    /**
     * This is an overridden method.<br>
     * Will be called whenever broadcast related to phone state is received.
     * 
     * @param context
     *            The Context in which the receiver is running.
     * @param intent
     *            The Intent being received.
     */
    @Override
    public void onReceive( Context context, Intent intent ) {

        Bundle bundle = intent.getExtras( );
        Log.i( MainApplication.LOG_TAG, "CallInterceptor: onReceive: Start Bundle->" + bundle );
        if ( bundle == null ) {
            return;
        }

        String state = bundle.getString( TelephonyManager.EXTRA_STATE );
        String phoneNumber = intent.getStringExtra( Intent.EXTRA_PHONE_NUMBER );
        if ( !TextUtils.isEmpty( phoneNumber ) ) {
            _phoneNumber = phoneNumber;
        }
        Log.i( MainApplication.LOG_TAG, "CallInterceptor: onReceive: State->" + state + " phoneNumber->" + phoneNumber + " _phoneNumber->" + _phoneNumber );

        /**
         * Phone is ringing
         */
        if ( state != null && state.equals( TelephonyManager.EXTRA_STATE_RINGING ) ) {
            _isRinging = true;
            _phoneNumber = bundle.getString( TelephonyManager.EXTRA_INCOMING_NUMBER );
            _handleStateRinging( _phoneNumber );

        } else if ( state != null && state.equals( TelephonyManager.EXTRA_STATE_OFFHOOK ) ) {

            /**
             * If in call (either picked up an incoming call or making an outgoing call)
             */
            _handleStateOffhook( _phoneNumber );

        } else if ( state != null && state.equals( TelephonyManager.EXTRA_STATE_IDLE ) ) {

            _handleStateIdle( _phoneNumber );
        } else if ( state == null ) {
            /**
             * Outgoing call
             */
            TelephonyManager telephonyManager = (TelephonyManager) MainApplication.appContext.getSystemService( Context.TELEPHONY_SERVICE );
            int curState = telephonyManager.getCallState( );
            if ( curState == TelephonyManager.CALL_STATE_OFFHOOK ) {
                if ( _phoneNumber != null ) {
                    _handleStateOffhook( _phoneNumber );
                }
            }
        }
    }

    /**
     * This method will handle RINGING state.
     * 
     * @param phoneNumber
     */
    private void _handleStateRinging( String phoneNumber ) {
        Log.i( MainApplication.LOG_TAG, "CallInterceptor: _handleStateRinging: phone is ringing phoneNumber->" + phoneNumber );
        CallBlocker.getInstance( ).handleRingingEvent( phoneNumber );
    }

    /**
     * This method will handle the OFFHOOK state. (Either incoming call is picked up or user is
     * dialing a number)
     * 
     * @param phoneNumber
     *            phoneNumber of incoming / outgoing call.
     */
    private void _handleStateOffhook( String phoneNumber ) {
        Log.i( MainApplication.LOG_TAG, "CallInterceptor: _handleStateOffhook: phoneNumber-> " + phoneNumber + " isRinging->" + _isRinging );

        if ( _isRinging ) {
            /**
             * If phone was ringing and user picked up the call and it was not waiting missed call
             */
            Log.i( MainApplication.LOG_TAG, "CallInterceptor: _handleStateRinging: User picked up the call" );
            // _isRinging = false;

        } else {
            /**
             * Phone was not ringing means user is dialing a number AND HENCE OFFHOOK
             */
            Log.i( MainApplication.LOG_TAG, "CallInterceptor: _handleStateRinging: User is dialing a number" );
        }
    }

    /**
     * This method will handle the idle state. (When call is disconnected / not picked up)
     * 
     * @param phoneNumber
     *            the phone number of last call
     */
    private void _handleStateIdle( String phoneNumber ) {
        Log.i( MainApplication.LOG_TAG, "CallInterceptor: _handleStateIdle: Active Call Disconnected _isRinging->" + _isRinging );
        /**
         * This is a missed call
         */
        if ( _isRinging ) {
            _isRinging = false;
            CallBlocker.getInstance( ).handleIdleEvent( phoneNumber );
        }
    }
}