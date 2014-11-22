package com.poc.callblocker.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.telephony.PhoneNumberUtils;

import com.poc.callblocker.model.BlacklistedContact;

/**
 * This is a singleton class and is used to manage BlackListedContacts table of database. Provide
 * the interfaces to add, remove or get the records from database.
 * 
 * @author Sayyad Abid
 */
public class BlacklistedContactsDb {
    private static BlacklistedContactsDb _instance;

    private final String TABLE_BLACKLISTED_CONTACT = "BlacklistedContacts";

    private final String COLUMN_BLACKLISTED_CONTACT_ID = "_id";
    private final String COLUMN_NAME = "NAME";
    private final String COLUMN_PHONE_NUMBER = "PHONE_NUMBER";

    private ArrayList< String > _blacklistedNumbers;

    /**
     * Private Constructor to implement singleton design pattern
     */
    private BlacklistedContactsDb() {
    }

    /**
     * This method is used to get the initialized instance of {@link BlacklistedContactsDb}
     * 
     * @return the initialized instance of {@link BlacklistedContactsDb}
     */
    public static BlacklistedContactsDb getInstance() {
        if ( _instance == null ) {
            _instance = new BlacklistedContactsDb( );
            _instance.getBlacklistedPhoneNumbers( );
        }
        return _instance;
    }

    /**
     * This method is used to add {@link BlacklistedContact} to database
     * 
     * @param blacklistedContact
     *            the {@link BlacklistedContact} to be added
     * @return the no of rows affected
     */
    public int add( BlacklistedContact blacklistedContact ) {
        ContentValues values = new ContentValues( );
        values.put( COLUMN_NAME, blacklistedContact.getName( ) );
        values.put( COLUMN_PHONE_NUMBER, blacklistedContact.getPhoneNumber( ) );

        int added = MasterDatabase.getInstance( ).add( TABLE_BLACKLISTED_CONTACT, null, values );
        if ( added != -1 ) {
            if ( _blacklistedNumbers == null ) {
                _blacklistedNumbers = new ArrayList< String >( );
            }
            _blacklistedNumbers.add( blacklistedContact.getPhoneNumber( ) );
        }
        return added;
    }

    /**
     * This method is used to remove the {@link BlacklistedContact} with input phone number from
     * database.
     * 
     * @param phoneNumber
     *            the phone number
     * @return the no of rows affected
     */
    public int remove( String phoneNumber ) {
        String whereClause = COLUMN_PHONE_NUMBER + "='" + phoneNumber + "'";
        int deleted = MasterDatabase.getInstance( ).delete( TABLE_BLACKLISTED_CONTACT, whereClause, null );
        if ( deleted != 0 ) {
            if ( _blacklistedNumbers != null ) {
                _blacklistedNumbers.remove( phoneNumber );
            }
        }
        return deleted;
    }

    /**
     * This method is used to get the Cursor to BlackListedContacts table
     * 
     * @return the Cursor to BlackListedContacts table
     */
    public Cursor getBlacklistedContactsCursor() {
        String[] columns = { COLUMN_BLACKLISTED_CONTACT_ID, COLUMN_NAME, COLUMN_PHONE_NUMBER };
        String orderBy = COLUMN_NAME + " ASC";
        Cursor cursor = MasterDatabase.getInstance( ).getRecords( TABLE_BLACKLISTED_CONTACT, columns, null, null, null, null, orderBy );
        return cursor;
    }

    /**
     * This method is used to get all the blacklisted phone numbers
     * 
     * @return the {@link ArrayList} of blacklisted phone numbers
     */
    public ArrayList< String > getBlacklistedPhoneNumbers() {
        if ( _blacklistedNumbers != null && _blacklistedNumbers.size( ) > 0 ) {
            return _blacklistedNumbers;
        }

        _blacklistedNumbers = new ArrayList< String >( );

        String[] columns = { COLUMN_PHONE_NUMBER };
        Cursor cursor = MasterDatabase.getInstance( ).getRecords( TABLE_BLACKLISTED_CONTACT, columns, null, null, null, null, null );
        try {
            if ( cursor != null && cursor.getCount( ) > 0 ) {
                while ( cursor.moveToNext( ) ) {
                    String phoneNumber = cursor.getString( 0 );
                    _blacklistedNumbers.add( phoneNumber );
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
        } finally {
            if ( cursor != null ) {
                cursor.close( );
            }
        }
        return _blacklistedNumbers;
    }

    /**
     * This method is used to get the count of blacklisted contacts
     * 
     * @return the count of blacklisted contacts
     */
    public int getBlacklistedContactsCount() {
        int count = 0;
        String[] columns = { COLUMN_BLACKLISTED_CONTACT_ID };
        Cursor cursor = MasterDatabase.getInstance( ).getRecords( TABLE_BLACKLISTED_CONTACT, columns, null, null, null, null, null );
        try {
            if ( cursor != null ) {
                count = cursor.getCount( );
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
        } finally {
            if ( cursor != null ) {
                cursor.close( );
            }
        }
        return count;
    }

    /**
     * Method to check if the input phone number is there in blacklist
     * 
     * @param phoneNumber
     *            the phone number to be checked
     * @return true if phone number is blacklisted, false otherwise
     */
    public boolean isBlacklistedNumber( String phoneNumber ) {
        getBlacklistedPhoneNumbers( );
        if ( _blacklistedNumbers != null && _blacklistedNumbers.size( ) > 0 ) {
            for ( String currPhoneNumber : _blacklistedNumbers ) {
                if ( PhoneNumberUtils.compare( phoneNumber, currPhoneNumber ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to get the name for input phone number
     * 
     * @param phoneNumber
     *            the phone number
     * @return the name for input phone number
     */
    public String getNameFromNumber( String phoneNumber ) {
        String name = null;
        getBlacklistedPhoneNumbers( );
        if ( _blacklistedNumbers != null && _blacklistedNumbers.size( ) > 0 ) {
            for ( String currPhoneNumber : _blacklistedNumbers ) {
                if ( PhoneNumberUtils.compare( phoneNumber, currPhoneNumber ) ) {
                    phoneNumber = currPhoneNumber;
                    break;
                }
            }
        }
        String[] columns = { COLUMN_NAME };
        String whereClause = COLUMN_PHONE_NUMBER + "='" + phoneNumber + "'";
        Cursor cursor = MasterDatabase.getInstance( ).getRecords( TABLE_BLACKLISTED_CONTACT, columns, whereClause, null, null, null, null );
        try {
            if ( cursor != null && cursor.getCount( ) > 0 ) {
                if ( cursor.moveToNext( ) ) {
                    name = cursor.getString( 0 );
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
        } finally {
            if ( cursor != null ) {
                cursor.close( );
            }
        }
        return name;
    }
}
