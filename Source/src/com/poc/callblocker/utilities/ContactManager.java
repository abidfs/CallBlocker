package com.poc.callblocker.utilities;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;
import android.util.Log;

import com.poc.callblocker.MainApplication;
import com.poc.callblocker.R;
import com.poc.callblocker.model.ContactListEntry;

/**
 * This class is used for searching and retrieving contact details.
 * 
 * @author Sayyad Abid
 */
public class ContactManager {

    /**
     * This method is used to check if the input phone number is present in phone book.
     * 
     * @param number
     *            The number to search with.
     * @return true if number is found in contacts, false otherwise
     */
    public boolean isNumberPresent( String number ) {
        if ( TextUtils.isEmpty( number ) ) {
            return false;
        }

        ContentResolver contentResolver = MainApplication.appContext.getContentResolver( );
        Cursor cursor = contentResolver.query( Uri.withAppendedPath( PhoneLookup.CONTENT_FILTER_URI, number ), new String[] { PhoneLookup.LOOKUP_KEY }, null, null, null );
        String uid = _toString( cursor );
        if ( cursor != null ) {
            cursor.close( );
        }
        return uid != null;
    }

    /**
     * Returns a String representation of the first row in the first column of given cursor.
     * 
     * @param cursor
     *            The cursor representing the input data.
     * @return A String containing toString() representation of data present in first row of first
     *         column of the cursor.
     */
    private String _toString( Cursor cursor ) {
        if ( cursor == null || !cursor.moveToFirst( ) ) {
            return null;
        }
        String data = cursor.getString( 0 );
        cursor.close( );
        return data;
    }

    /**
     * Returns all the contacts present in the device.
     * 
     * @return A Vector of type {@link ContactListEntry} containing all contacts present in the
     *         device.
     */
    public ArrayList< ContactListEntry > getAllContacts( ArrayList< String > ignoreNumbers ) {

        String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER };
        ContentResolver contentResolver = MainApplication.appContext.getContentResolver( );
        Cursor cursor = contentResolver.query( ContactsContract.Contacts.CONTENT_URI, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC" );
        if ( cursor == null ) {
            return null;
        }

        ArrayList< ContactListEntry > contacts = new ArrayList< ContactListEntry >( );

        cursor.moveToFirst( );
        int idIndex = cursor.getColumnIndex( ContactsContract.Contacts._ID );
        int nameIndex = cursor.getColumnIndex( ContactsContract.Contacts.DISPLAY_NAME );
        int hasNoIndex = cursor.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER );

        while ( !cursor.isAfterLast( ) ) {
            String uid = cursor.getString( idIndex );
            String name = cursor.getString( nameIndex );
            if ( name == null || name.equals( "" ) ) {
                name = MainApplication.appContext.getString( R.string.lbl_unknown );
            }
            String nextPhoneNumber = null;
            if ( Integer.parseInt( cursor.getString( hasNoIndex ) ) > 0 ) {
                Cursor pCur = contentResolver.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { uid }, null );

                if ( pCur != null ) {
                    while ( pCur.moveToNext( ) ) {
                        try {
                            nextPhoneNumber = "" + pCur.getString( pCur.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER ) );
                        } catch ( Exception ex ) {
                            Log.e( MainApplication.LOG_TAG, "ContactManager : getAllContacts : Exception->" + ex.getMessage( ) );
                            ex.printStackTrace( );
                        }
                        if ( !TextUtils.isEmpty( nextPhoneNumber ) && !ignoreNumbers.contains( _getFormattedNumber( nextPhoneNumber ) ) ) {
                            ignoreNumbers.add( nextPhoneNumber );
                            ContactListEntry currentContact = new ContactListEntry( uid, nextPhoneNumber, name );
                            contacts.add( currentContact );
                        }
                    }
                    pCur.close( );
                }
            }

            cursor.moveToNext( );
        }

        cursor.close( );
        return contacts;
    }

    private String _getFormattedNumber( String phoneNumber ) {
        String formattedPhoneNumber = new String( );
        formattedPhoneNumber = phoneNumber.replace( "-", "" );
        formattedPhoneNumber = formattedPhoneNumber.replace( " ", "" );
        formattedPhoneNumber = formattedPhoneNumber.replace( "(", "" );
        formattedPhoneNumber = formattedPhoneNumber.replace( ")", "" );
        return formattedPhoneNumber;
    }
}