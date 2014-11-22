package com.poc.callblocker.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.poc.callblocker.CallBlockerService;
import com.poc.callblocker.MainApplication;
import com.poc.callblocker.R;
import com.poc.callblocker.utilities.UiConstants;

/**
 * The main activity of class.
 * 
 * @author Sayyad Abid
 */
public class ScrMain extends FragmentActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

        boolean openBlockedContactList = getIntent( ).getBooleanExtra( UiConstants.FLAG_OPEN_BLOCKED_CALLS_LIST, false );
        if ( openBlockedContactList ) {
            _startBlockedCalls( );
        } else {
            startBlacklist( );
        }
        if ( !MainApplication.isServiceRunning( CallBlockerService.class.getName( ) ) ) {
            MainApplication.startService( CallBlockerService.class );
        }
    }

    @Override
    protected void onResume() {
        super.onResume( );
    }

    /**
     * Method to start blacklist {@link Fragment}
     */
    public void startBlacklist() {
        FragmentTransaction transaction = getSupportFragmentManager( ).beginTransaction( );
        FrgBlacklist frgBlacklist = new FrgBlacklist( );
        transaction.replace( R.id.container, frgBlacklist, UiConstants.TAG_BLACKLIST );
        transaction.commit( );

        Fragment fragment = getSupportFragmentManager( ).findFragmentById( R.id.bottom_bar );
        if ( fragment != null && fragment instanceof FrgBottomBar ) {
            ( (FrgBottomBar) fragment ).setSelectedTab( R.id.tab_blacklist );
        }
    }

    /**
     * Method to start blocked calls {@link Fragment}
     */
    private void _startBlockedCalls() {
        FragmentTransaction transaction = getSupportFragmentManager( ).beginTransaction( );
        FrgBlockedCalls frgBlockedContacts = new FrgBlockedCalls( );
        transaction.replace( R.id.container, frgBlockedContacts, UiConstants.TAG_BLOCKED_CONTACTS );
        transaction.commit( );

        Fragment fragment = getSupportFragmentManager( ).findFragmentById( R.id.bottom_bar );
        if ( fragment != null && fragment instanceof FrgBottomBar ) {
            ( (FrgBottomBar) fragment ).setSelectedTab( R.id.tab_blocked_calls );
        }
    }

    /**
     * Method to set the title
     */
    public void setTitle( int strResId ) {
        TextView tvTitle = (TextView) findViewById( R.id.tv_title );
        tvTitle.setText( strResId );
    }

    /**
     * Method to set the title
     * 
     * @param strTitle
     *            using string resource
     */
    public void setTitle( String strTitle ) {
        TextView tvTitle = (TextView) findViewById( R.id.tv_title );
        tvTitle.setText( strTitle );
    }

    /**
     * Method to toggle the add button visibility
     * 
     * @param visible
     *            boolean indicating whether to show or hide the add button
     * @param listener
     *            the {@link OnClickListener} for button
     */
    public void toggleAddButtonVisibility( boolean visible, View.OnClickListener listener ) {
        ImageView ivAdd = (ImageView) findViewById( R.id.iv_add );
        if ( visible ) {
            ivAdd.setVisibility( View.VISIBLE );
        } else {
            ivAdd.setVisibility( View.INVISIBLE );
        }
        ivAdd.setOnClickListener( listener );
    }

    /**
     * Method to toggle the visibility of bottom bar
     * 
     * @param visible
     *            boolean indicating whether to show or hide the bottom bar
     */
    public void toggleBottomBarVisibility( final boolean visible ) {
        runOnUiThread( new Runnable( ) {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager( );
                Fragment bottomBar = fragmentManager.findFragmentById( R.id.bottom_bar );
                if ( visible ) {
                    fragmentManager.beginTransaction( ).show( bottomBar ).commit( );
                } else {
                    fragmentManager.beginTransaction( ).hide( bottomBar ).commit( );
                }
            }
        } );
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager( );
        if ( ( manager.findFragmentByTag( UiConstants.TAG_ADD_CONTACT_TO_BLACKLIST ) ) != null ) {
            startBlacklist( );
            toggleBottomBarVisibility( true );
        } else {
            System.exit( 0 );
        }
    }
}
