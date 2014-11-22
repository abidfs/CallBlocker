package com.poc.callblocker.utilities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.poc.callblocker.MainApplication;
import com.poc.callblocker.R;
import com.poc.callblocker.ui.ScrMain;

/**
 * Singleton class to handle notifications
 * 
 * @author Sayyad Abid
 */
public class NotificationManager {
    public static final int NOTIFICATION_ID = 1;
    private android.app.NotificationManager _notificationManager;
    private NotificationCompat.Builder _notificationBuilder;

    private static NotificationManager _instance;

    /**
     * Private constructor
     */
    private NotificationManager() {

    }

    /**
     * Method to get the initialized instance of {@link NotificationManager}
     * 
     * @return the initialized instance of {@link NotificationManager}
     */
    public static NotificationManager getInstance() {
        if ( _instance == null ) {
            _instance = new NotificationManager( );
        }
        return _instance;
    }

    /**
     * Method to display notification
     * 
     * @param title
     *            the notification title (mostly app name)
     * @param msg
     *            the notificaiton message
     */
    public void displayNotification( String title, String msg ) {
        if ( _notificationManager == null ) {
            _notificationManager = (android.app.NotificationManager) MainApplication.appContext.getSystemService( Context.NOTIFICATION_SERVICE );
        }

        Intent homeIntent = new Intent( MainApplication.appContext, ScrMain.class );
        homeIntent.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
        homeIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        homeIntent.putExtra( UiConstants.FLAG_OPEN_BLOCKED_CALLS_LIST, true );
        PendingIntent contentIntent = PendingIntent.getActivity( MainApplication.appContext, 0, homeIntent, 0 );
        _notificationBuilder = new NotificationCompat.Builder( MainApplication.appContext ).setSmallIcon( R.drawable.ic_launcher ).setContentTitle( title )
                .setStyle( new NotificationCompat.BigTextStyle( ).bigText( msg ) ).setContentText( msg );
        _notificationBuilder.setDefaults( Notification.DEFAULT_SOUND );
        _notificationBuilder.setContentIntent( contentIntent );
        _notificationBuilder.setAutoCancel( true );
        _notificationManager.notify( NOTIFICATION_ID, _notificationBuilder.build( ) );
    }

    /**
     * Method to clear notification
     */
    public void clearNotifications() {
        if ( _notificationManager != null ) {
            _notificationManager.cancel( NOTIFICATION_ID );
        }
    }
}
