package com.poc.callblocker.ui.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.poc.callblocker.R;
import com.poc.callblocker.ui.FrgBottomBar;

/**
 * Controller class for {@link FrgBottomBar}
 * 
 * @author Sayyad Abid
 */
public class BottomBarController implements OnClickListener {
    private final FrgBottomBar _fragment;

    public final int TAB_BLACKLIST = 101;
    public final int TAB_BLOCKED_CALLS = 102;
    public final int TAB_SETTINGS = 103;

    /**
     * Parameterized Constructor
     * 
     * @param fragment
     *            the {@link FrgBottomBar} instance
     */
    public BottomBarController( FrgBottomBar fragment ) {
        _fragment = fragment;
    }

    @Override
    public void onClick( View v ) {
        switch ( v.getId( ) ) {
        case R.id.tab_blacklist:
            if ( _fragment.getSelectedTab( ) != TAB_BLACKLIST ) {
                _fragment.startBlacklist( );
                _fragment.setSelectedTab( R.id.tab_blacklist );
            }
            break;

        case R.id.tab_blocked_calls:
            if ( _fragment.getSelectedTab( ) != TAB_BLOCKED_CALLS ) {
                _fragment.startBlockedCalls( );
                _fragment.setSelectedTab( R.id.tab_blocked_calls );
            }
            break;

        case R.id.tab_settings:
            if ( _fragment.getSelectedTab( ) != TAB_SETTINGS ) {
                _fragment.startSettings( );
                _fragment.setSelectedTab( R.id.tab_settings );
            }
            break;
        }
    }

}
