package com.poc.callblocker.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.poc.callblocker.MainApplication;
import com.poc.callblocker.utilities.DeviceUtils;

/**
 * This is a singleton class and is used to interact with the SQLite database. Creates a blank
 * database for the first launch of application by copying it from assets folder. This is also
 * responsible for adding, removing or getting the records from any table.
 * 
 * @author Sayyad Abid
 */
public class MasterDatabase {
    private static MasterDatabase _instance;

    private DatabaseHelper _databseHelper;
    private SQLiteDatabase _sqliteDatabase;

    private static final String DATABSE_NAME = "callblocker.sql";
    private static final String DATABSE_PATH = DeviceUtils.getDatabaseDirectory( DATABSE_NAME );
    private static final int DATABSE_VERSION = 1;

    /**
     * Private constructor.
     */
    private MasterDatabase() {
        _initializeDataBase( );
        _databseHelper = new DatabaseHelper( MainApplication.appContext, DATABSE_NAME, null, DATABSE_VERSION );
        openDataBase( );
    }

    /**
     * Method to get the initialized object of type {@link MasterDatabase}
     * 
     * @return the initialized object of type {@link MasterDatabase}
     */
    public static MasterDatabase getInstance() {
        if ( _instance == null ) {
            _instance = new MasterDatabase( );
        }
        return _instance;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    private void _initializeDataBase() {
        boolean dbExist = _isDataBaseExists( );
        Log.d( MainApplication.LOG_TAG, "MasterDatabase: _initializeDataBase: dbExist->" + dbExist );
        if ( dbExist ) {
            /**
             * Do nothing - database already exist
             */
        } else {

            /**
             * By calling this method and empty database will be created into the default system
             * path of your application so we are able to overwrite that database with our database.
             */
            _databseHelper = new DatabaseHelper( MainApplication.appContext, DATABSE_NAME, null, DATABSE_VERSION );
            _databseHelper.getReadableDatabase( );
            _databseHelper.close( );
            try {
                copyDataBase( );
            } catch ( IOException e ) {
                Log.e( MainApplication.LOG_TAG, "MasterDatabase: _initializeDataBase: Exception->" + e.getMessage( ) );
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the
     * application.
     * 
     * @return true if it exists, false if it does not
     */
    private boolean _isDataBaseExists() {
        boolean exists = false;
        try {
            File file = new File( DATABSE_PATH );
            if ( file != null && file.exists( ) && file.length( ) > 0 ) {
                exists = true;
            }
        } catch ( SQLiteException e ) {
            /**
             * Database does't exist yet.
             */
            Log.e( MainApplication.LOG_TAG, "MasterDatabase: DatabaseHelper: _isDataBaseExists: Exception->" + e.getMessage( ) );
            exists = false;
        }
        return exists;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled. This is done by transferring byte
     * stream.
     */
    private void copyDataBase() throws IOException {
        /**
         * Open your local db as the input stream
         */
        InputStream inputStream = MainApplication.appContext.getAssets( ).open( DATABSE_NAME );

        /**
         * Open the empty db as the output stream
         */
        OutputStream outputStream = new FileOutputStream( DATABSE_PATH );

        /**
         * transfer bytes from the input file to the output file
         */
        byte[] buffer = new byte[1024];
        int length;
        while ( ( length = inputStream.read( buffer ) ) > 0 ) {
            outputStream.write( buffer, 0, length );
        }

        /**
         * Close the streams
         */
        outputStream.flush( );
        outputStream.close( );
        inputStream.close( );

    }

    /**
     * Method to open writable database
     * 
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        _sqliteDatabase = _databseHelper.getWritableDatabase( );
    }

    /**
     * Method to close the previously opened database
     */
    public synchronized void close() {
        if ( _sqliteDatabase != null ) {
            _sqliteDatabase.close( );
        }
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper( Context context, String name, CursorFactory factory, int version ) {
            super( context, name, factory, version );
        }

        @Override
        public void onCreate( SQLiteDatabase db ) {
        }

        @Override
        public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        }
    }

    /**
     * Method to get records from database.
     * 
     * @param table
     *            the table name
     * @param columns
     *            the array of column names to be retrieved
     * @param selection
     *            the selection(where clause)
     * @param selectionArgs
     *            the selection arguments (conditions used in where clause)
     * @param groupBy
     *            the group by clause
     * @param having
     *            the having clause
     * @param orderBy
     *            the sort order
     * @return the Cursor for matching records, null otherwise
     */
    public Cursor getRecords( String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy ) {
        Cursor cursor = null;
        cursor = _sqliteDatabase.query( table, columns, selection, selectionArgs, groupBy, having, orderBy );
        return cursor;
    }

    /**
     * Method to add records to database
     * 
     * @param table
     *            the table name
     * @param nullColumnHack
     *            the null column hack
     * @param values
     *            the Content values
     * @return the no of rows affected in table
     */
    public int add( String table, String nullColumnHack, ContentValues values ) {
        try {
            return (int) _sqliteDatabase.insert( table, nullColumnHack, values );
        } catch ( Exception e ) {
            Log.e( MainApplication.LOG_TAG, "MasterDatabase: add: Exception->" + e.getMessage( ) );
        }
        return -1;
    }

    /**
     * Method to update records to database
     * 
     * @param table
     *            the table name
     * @param values
     *            the Content values
     * @param whereClause
     *            the where clause
     * @param whereArgs
     *            the where clause conditions
     * @return the no of rows affected in table
     */
    public int update( String table, ContentValues values, String whereClause, String[] whereArgs ) {
        int res = (int) _sqliteDatabase.update( table, values, whereClause, whereArgs );
        return res;
    }

    /**
     * Method to delete records from database.
     * 
     * @param table
     *            the table name
     * @param whereClause
     *            the where clause
     * @param whereArgs
     *            the where clause conditions
     * @return the no of rows affected in table
     */
    public int delete( String table, String whereClause, String[] whereArgs ) {
        return (int) _sqliteDatabase.delete( table, whereClause, whereArgs );
    }
}