package com.poc.callblocker.ui.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.poc.callblocker.R;
import com.poc.callblocker.database.BlockedCallsDb;
import com.poc.callblocker.ui.FrgBlockedCalls;

/**
 * Controller class for {@link FrgBlockedCalls}
 * 
 * @author Sayyad Abid
 */
public class BlockedCallsController implements OnClickListener {
    private final FrgBlockedCalls _fragment;

    /**
     * Parameterized Constructor
     * 
     * @param fragment
     *            the {@link FrgBlockedCalls} instance
     */
    public BlockedCallsController( FrgBlockedCalls fragment ) {
        this._fragment = fragment;
    }

    /**
     * Method to get the blocked call count from DB
     * 
     * @return the blocked call count from DB
     */
    public int getBlockedCallsCount() {
        return BlockedCallsDb.getInstance( ).getBlockedCallsCount( );
    }

    @Override
    public void onClick( View v ) {
        switch ( v.getId( ) ) {
        case R.id.iv_delete:
            int blockedCallId = (Integer) v.getTag( );
            int deleted = BlockedCallsDb.getInstance( ).remove( blockedCallId );
            if ( deleted != 0 ) {
                _fragment.setTitle( );
                _fragment.populateList( );
            }
            break;
        }
    }

}
