package com.poc.callblocker.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.poc.callblocker.model.BlockedCall;

/**
 * This is a singleton class and is used to manage BlockedCalls table of database. Provide the
 * interfaces to add, remove or get the records from database.
 * 
 * @author Sayyad Abid
 */
public class BlockedCallsDb {
    private static BlockedCallsDb _instance;

    private final String TABLE_BLOCKED_CALLS = "BlockedCalls";

    private final String COLUMN_BLOCKED_CALL_ID = "_id";
    private final String COLUMN_NAME = "NAME";
    private final String COLUMN_PHONE_NUMBER = "PHONE_NUMBER";
    private final String COLUMN_TIMESTAMP = "TIMESTAMP";
    private final String COLUMN_STATUS = "STATUS";

    /**
     * Private Constructor to implement singleton design pattern
     */
    private BlockedCallsDb() {
    }

    /**
     * This method is used to get the initialized instance of {@link BlockedCallsDb}
     * 
     * @return the initialized instance of {@link BlockedCallsDb}
     */
    public static BlockedCallsDb getInstance() {
        if ( _instance == null ) {
            _instance = new BlockedCallsDb( );
        }
        return _instance;
    }

    /**
     * This method is used to add {@link BlockedCall} to database
     * 
     * @param blockedCall
     *            the {@link BlockedCall} to be added
     * @return the no of rows affected
     */
    public int add( BlockedCall blockedCall ) {
        ContentValues values = new ContentValues( );
        values.put( COLUMN_NAME, blockedCall.getName( ) );
        values.put( COLUMN_PHONE_NUMBER, blockedCall.getPhoneNumber( ) );
        values.put( COLUMN_TIMESTAMP, blockedCall.getTimestamp( ) );
        values.put( COLUMN_STATUS, blockedCall.getReadStatus( ) );

        return MasterDatabase.getInstance( ).add( TABLE_BLOCKED_CALLS, null, values );
    }

    /**
     * This method is used to remove the {@link BlockedCall} with input id from database.
     * 
     * @param phoneNumber
     *            the phone number
     * @return the no of rows affected
     */
    public int remove( long blockedCallId ) {
        String whereClause = COLUMN_BLOCKED_CALL_ID + "=" + blockedCallId + "";
        return MasterDatabase.getInstance( ).delete( TABLE_BLOCKED_CALLS, whereClause, null );
    }

    /**
     * This method is used to get the Cursor to BlockedCalls table
     * 
     * @return the Cursor to BlockedCalls table
     */
    public Cursor getBlockedCallsCursor() {
        String[] columns = { COLUMN_BLOCKED_CALL_ID, COLUMN_NAME, COLUMN_PHONE_NUMBER, COLUMN_TIMESTAMP };
        String orderBy = COLUMN_TIMESTAMP + " DESC";
        Cursor cursor = MasterDatabase.getInstance( ).getRecords( TABLE_BLOCKED_CALLS, columns, null, null, null, null, orderBy );
        return cursor;
    }

    /**
     * This method is used to get the count of blocked calls
     * 
     * @return the count of blocked calls
     */
    public int getBlockedCallsCount() {
        int count = 0;
        String[] columns = { COLUMN_BLOCKED_CALL_ID };
        Cursor cursor = MasterDatabase.getInstance( ).getRecords( TABLE_BLOCKED_CALLS, columns, null, null, null, null, null );
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
     * This method is used to get the count of unread blocked calls
     * 
     * @return the count of unread blocked calls
     */
    public int getUnreadBlockedCallsCount() {
        int count = 0;
        String[] columns = { COLUMN_BLOCKED_CALL_ID };
        String whereClause = COLUMN_STATUS + "=" + BlockedCall.STATUS_UNREAD;
        Cursor cursor = MasterDatabase.getInstance( ).getRecords( TABLE_BLOCKED_CALLS, columns, whereClause, null, null, null, null );
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
     * This method is used to mark all unread blocked calls are read.
     */
    public void resetBlockedCallsCount() {
        ContentValues values = new ContentValues( );
        values.put( COLUMN_STATUS, BlockedCall.STATUS_READ );
        String whereClause = COLUMN_STATUS + "=" + BlockedCall.STATUS_UNREAD;
        MasterDatabase.getInstance( ).update( TABLE_BLOCKED_CALLS, values, whereClause, null );
    }
}
