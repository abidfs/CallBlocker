package com.poc.callblocker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This class extends Android {@link Service} which starts when application is launched first time
 * and when phone restarts. This service is meant to be running all the time.
 * 
 * @author Sayyad Abid
 */
public class CallBlockerService extends Service {

    @Override
    public IBinder onBind( Intent arg0 ) {

        return null;
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        Log.i( MainApplication.LOG_TAG, "CallBlockerService: onStartCommand" );
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy( );
        Log.i( MainApplication.LOG_TAG, "CallBlockerService: onDestroy: Service destroyed ReStarting Service....." );
        /**
         * In order to keep application running all the time, restart the service if it is destroyed
         * due to any reason.
         */
        Intent intent = new Intent( );
        intent.setClass( getApplicationContext( ), CallBlockerService.class );
        getApplicationContext( ).startService( intent );

    }

    /**
     * This method is called when application is removed from recent applications on ICS onwards
     * device. So our application gets killed. So we explicitly disconnect socket in that case.
     */
    @Override
    public void onTaskRemoved( Intent rootIntent ) {
        super.onTaskRemoved( rootIntent );
        Log.i( MainApplication.LOG_TAG, "CallBlockerService: onTaskRemoved : Start" );
    }
}