package com.poc.callblocker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.poc.callblocker.CallBlockerService;
import com.poc.callblocker.MainApplication;

/**
 * This class is used to capture BOOT_COMPLETE event. Starts the {@link CallBlockerService} as soon
 * as the BOOT_COMPLETE event is received.
 * 
 * @author Sayyad Abid
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive( Context context, Intent intent ) {
        Log.d( MainApplication.LOG_TAG, "BootReceiver: onReceive" );
        Intent service = new Intent( context, CallBlockerService.class );
        context.startService( service );
    }
}