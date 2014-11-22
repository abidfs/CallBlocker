package com.poc.callblocker.utilities;

import com.poc.callblocker.MainApplication;

/**
 * Utility class to interact with device interface APIs
 * 
 * @author Sayyad Abid
 */
public class DeviceUtils {
    /**
     * Method to get the database directory path
     * 
     * @param dbName
     *            the database name
     * @return the database path for input database name
     */
    public static String getDatabaseDirectory( String dbName ) {
        return MainApplication.appContext.getDatabasePath( dbName ).getAbsolutePath( );
    }
}
