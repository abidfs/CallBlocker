package com.poc.callblocker;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * This class extends the {@link Application}. This also maintains the reference to application
 * context which can be used throughout the app.
 * 
 * @author Sayyad Abid
 */
public class MainApplication extends Application {
    public static Context appContext;
    public static String LOG_TAG = "CallBlocker";

    @Override
    public void onCreate() {
        super.onCreate( );
        appContext = this;
        Log.d( LOG_TAG, "MainApplication: onCreate" );
        Thread.setDefaultUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler( ) {
            /**
             * All the uncaught exceptions will come here.
             */
            @Override
            public void uncaughtException( Thread thread, Throwable ex ) {
                String error = Log.getStackTraceString( ex );
                Log.e( LOG_TAG, "MainApplication: uncaughtException: Exception->" + ex.getMessage( ) + " stackTrace->" + error );
                System.exit( 1 );
            }
        } );
    }

    /**
     * This function checks whether specified service is running or not.
     * 
     * @param serviceName
     *            String containing full path of service class.
     * @return true if service is running, false otherwise.
     */
    public static boolean isServiceRunning( String serviceName ) {
        if ( TextUtils.isEmpty( serviceName ) ) {
            return false;
        }
        if ( appContext != null ) {
            ActivityManager manager = (ActivityManager) appContext.getSystemService( Context.ACTIVITY_SERVICE );
            for ( RunningServiceInfo service : manager.getRunningServices( Integer.MAX_VALUE ) ) {
                if ( serviceName.equals( service.service.getClassName( ) ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to start a specified service.
     * 
     * @param seviceName
     *            the name of service to start.
     */
    public static void startService( Class seviceName ) {
        if ( appContext != null ) {
            Intent service = new Intent( appContext, seviceName );
            appContext.startService( service );
        }
    }
}
