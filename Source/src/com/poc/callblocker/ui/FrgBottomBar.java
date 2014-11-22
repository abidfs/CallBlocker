package com.poc.callblocker.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.poc.callblocker.MainApplication;
import com.poc.callblocker.R;
import com.poc.callblocker.ui.controller.BottomBarController;
import com.poc.callblocker.utilities.UiConstants;

/**
 * This class extends {@link Fragment} and is used display the bottom bar
 * 
 * @author Sayyad Abid
 */
public class FrgBottomBar extends Fragment {
    private BottomBarController _controller;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.bottom_bar, container, false );

        _controller = new BottomBarController( this );

        Button btnBlacklist = (Button) view.findViewById( R.id.tab_blacklist );
        btnBlacklist.setOnClickListener( _controller );
        btnBlacklist.setSelected( true );

        Button btnBlockedCalls = (Button) view.findViewById( R.id.tab_blocked_calls );
        btnBlockedCalls.setOnClickListener( _controller );

        Button btnSettings = (Button) view.findViewById( R.id.tab_settings );
        btnSettings.setOnClickListener( _controller );

        return view;
    }

    @Override
    public void onResume() {
        Log.d( MainApplication.LOG_TAG, "FrgBottomBar: onResume" );
        // setSelectedTab(R.id.tab_search);
        super.onResume( );
    }

    /**
     * Method to get the selected tab
     * 
     * @return the selected tab
     */
    public int getSelectedTab() {
        FragmentManager manager = getActivity( ).getSupportFragmentManager( );
        if ( manager.findFragmentByTag( UiConstants.TAG_BLACKLIST ) != null ) {
            return _controller.TAB_BLACKLIST;
        } else if ( manager.findFragmentByTag( UiConstants.TAG_BLOCKED_CONTACTS ) != null ) {
            return _controller.TAB_BLOCKED_CALLS;
        } else if ( manager.findFragmentByTag( UiConstants.TAG_SETTINGS ) != null ) {
            return _controller.TAB_SETTINGS;
        }
        return _controller.TAB_BLACKLIST;
    }

    /**
     * Method to start the blacklist {@link Fragment}
     */
    public void startBlacklist() {
        FragmentTransaction transaction = getFragmentManager( ).beginTransaction( );
        FrgBlacklist frgBlacklist = new FrgBlacklist( );
        transaction.replace( R.id.container, frgBlacklist, UiConstants.TAG_BLACKLIST );
        transaction.commit( );
    }

    /**
     * Method to start the blocked calls {@link Fragment}
     */
    public void startBlockedCalls() {
        FragmentTransaction transaction = getFragmentManager( ).beginTransaction( );
        FrgBlockedCalls frgBlockedCalls = new FrgBlockedCalls( );
        transaction.replace( R.id.container, frgBlockedCalls, UiConstants.TAG_BLOCKED_CONTACTS );
        transaction.commit( );
    }

    /**
     * Method to start the settings {@link Fragment}
     */
    public void startSettings() {
        FragmentTransaction transaction = getFragmentManager( ).beginTransaction( );
        FrgSettings frgSettings = new FrgSettings( );
        transaction.replace( R.id.container, frgSettings, UiConstants.TAG_SETTINGS );
        transaction.commit( );
    }

    /**
     * Method to set the selected tab
     * 
     * @param tabId
     *            the tab id to be set selected
     */
    public void setSelectedTab( int tabId ) {
        View view = getView( );
        Button tabBlacklist = (Button) view.findViewById( R.id.tab_blacklist );
        Button tabBlockedContacts = (Button) view.findViewById( R.id.tab_blocked_calls );
        Button tabSettings = (Button) view.findViewById( R.id.tab_settings );

        tabBlacklist.setSelected( false );

        tabBlockedContacts.setSelected( false );

        tabSettings.setSelected( false );

        if ( tabId == tabBlacklist.getId( ) ) {
            tabBlacklist.setSelected( true );
        } else if ( tabId == tabBlockedContacts.getId( ) ) {
            tabBlockedContacts.setSelected( true );
        } else if ( tabId == tabSettings.getId( ) ) {
            tabSettings.setSelected( true );
        }
    }
}
