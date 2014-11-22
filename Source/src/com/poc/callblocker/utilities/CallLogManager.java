package com.poc.callblocker.utilities;

import java.util.ArrayList;
import java.util.Vector;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

import com.poc.callblocker.MainApplication;
import com.poc.callblocker.R;
import com.poc.callblocker.model.ContactListEntry;

/**
 * This class is used to get the call logs.
 * 
 * @author Sayyad Abid
 */
public class CallLogManager {
    public CallLogManager() {

    }

    /**
     * This method returns the call log details.
     * 
     * @return An vector of type {@link ContactListEntry} containing the name and number for each
     *         call log entry.
     */
    public ArrayList< ContactListEntry > getCallLogs( ArrayList< String > ignoreNumbers ) {
        Cursor cursor = null;
        ArrayList< ContactListEntry > callLog = null;
        try {
            String[] projection = new String[] { android.provider.CallLog.Calls.NUMBER, android.provider.CallLog.Calls.CACHED_NAME };
            cursor = MainApplication.appContext.getContentResolver( ).query( Uri.parse( "content://call_log/calls" ), projection, null, null, android.provider.CallLog.Calls.DATE + " DESC" );
            if ( cursor == null ) {
                return null;
            }

            Log.i( MainApplication.LOG_TAG, "CallLogManager: getCallLogs: Cursor Initialized" );
            callLog = new ArrayList< ContactListEntry >( );
            int i = 1;
            String strNoName = MainApplication.appContext.getString( R.string.lbl_unknown );
            while ( cursor.moveToNext( ) ) {

                String phoneNumber = cursor.getString( 0 );
                String name = cursor.getString( 1 );
                if ( name == null || name.equals( "" ) ) {
                    name = strNoName;
                }
                if ( !TextUtils.isEmpty( phoneNumber ) ) {
                    /**
                     * Since phone log may contain duplicate entries, check if we already have this
                     * number in vector before adding it.
                     */
                    boolean isPresent = _isPresent( ignoreNumbers, phoneNumber );
                    if ( isPresent ) {
                        continue;
                    }

                    ignoreNumbers.add( phoneNumber );
                    Log.i( MainApplication.LOG_TAG, "CallLogManager : getCallLogs : Entry" + i + " phoneNumber ->" + phoneNumber + " name ->" + name );
                    i++;

                    ContactListEntry contactListEntry = new ContactListEntry( "", phoneNumber, name );
                    callLog.add( contactListEntry );
                }
            }
        } catch ( Exception ex ) {
            Log.e( MainApplication.LOG_TAG, "CallLogManager : getCallLogs : Error in initializing cursor" );
            ex.printStackTrace( );
        } finally {
            if ( cursor != null ) {
                cursor.close( );
                cursor = null;
            }
        }
        return callLog;
    }

    /**
     * This method is used to check whether the input number is present in input {@link Vector}.
     * 
     * @param numbers
     *            the {@link Vector} containing phone numbers
     * @param phoneNumber
     *            the phone number to be searched in vector
     * @return true is input phone number is present, false otherwise.
     */
    private boolean _isPresent( ArrayList< String > numbers, String phoneNumber ) {
        if ( numbers == null ) {
            return false;
        }
        int size = numbers.size( );
        for ( int i = 0; i < size; i++ ) {
            boolean isSame = PhoneNumberUtils.compare( numbers.get( i ), phoneNumber );
            if ( isSame ) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to remove last missed call entry. Since if we reject the call
     * programatically sometimes it goes to Missed calls.
     */
    public void removeMissedCallEntry() {
        final String[] projection = null;
        final String selection = null;
        final String[] selectionArgs = null;
        final String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor cursor = null;
        try {
            cursor = MainApplication.appContext.getContentResolver( ).query( Calls.CONTENT_URI, projection, selection, selectionArgs, sortOrder );

            if ( cursor != null && cursor.getCount( ) > 0 ) {
                cursor.moveToFirst( );

                String callLogID = cursor.getString( cursor.getColumnIndex( android.provider.CallLog.Calls._ID ) );
                String callType = cursor.getString( cursor.getColumnIndex( android.provider.CallLog.Calls.TYPE ) );
                String isCallNew = cursor.getString( cursor.getColumnIndex( android.provider.CallLog.Calls.NEW ) );

                if ( Integer.parseInt( callType ) == CallLog.Calls.MISSED_TYPE && Integer.parseInt( isCallNew ) > 0 ) {
                    MainApplication.appContext.getContentResolver( ).delete( Calls.CONTENT_URI, CallLog.Calls._ID + "=?", new String[] { callLogID } );
                }
            }
        } catch ( Exception ex ) {
            Log.e( MainApplication.LOG_TAG, "CallLogManager: removeMissedCallEntry: Exception in removing missed call entry" );
        } finally {
            if ( cursor != null ) {
                cursor.close( );
            }
        }
    }
}