package com.poc.callblocker.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.poc.callblocker.R;
import com.poc.callblocker.database.BlockedCallsDb;
import com.poc.callblocker.ui.controller.BlockedCallsController;
import com.poc.callblocker.utilities.DateTimeUtils;

/**
 * This class extends {@link Fragment} and is used to display UI for blocked calls
 * 
 * @author Sayyad Abid
 */
public class FrgBlockedCalls extends Fragment {
    private BlockedCallsController _controller;
    private ListView _lvBlockedCalls;
    private BlockedCallsAdapter _adapter;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.blocked_calls, container, false );
        _controller = new BlockedCallsController( FrgBlockedCalls.this );

        _lvBlockedCalls = (ListView) view.findViewById( R.id.lv_blocked_calls );
        return view;
    }

    @Override
    public void onResume() {
        setTitle( );
        populateList( );
        super.onResume( );
    }

    /**
     * Method to set the title
     */
    public void setTitle() {
        int blockedCallsCount = _controller.getBlockedCallsCount( );
        String title = String.format( getString( R.string.title_blocked_calls ), blockedCallsCount );
        ( (ScrMain) getActivity( ) ).setTitle( title );
    }

    /**
     * Method to populate the blocked calls list
     */
    public void populateList() {
        /**
         * Fetching records from database may take time, hence calling it on new thread.
         */
        new Handler( ).post( new Runnable( ) {
            @Override
            public void run() {
                Cursor cursor = BlockedCallsDb.getInstance( ).getBlockedCallsCursor( );
                if ( _adapter == null ) {
                    _adapter = new BlockedCallsAdapter( getActivity( ), cursor );
                    _lvBlockedCalls.setAdapter( _adapter );
                } else {
                    _adapter.changeCursor( cursor );
                }
            }
        } );
    }

    private class BlockedCallsAdapter extends CursorAdapter {
        private final LayoutInflater _inflater;
        // Sat 25 Jul 2014 09:17 pm
        private final String TIMESTAMP_FORMAT = "EEE dd MMM yyyy h:mm a";

        public BlockedCallsAdapter( Context context, Cursor c ) {
            super( context, c, true );
            _inflater = (LayoutInflater) getActivity( ).getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        }

        @Override
        public void bindView( View view, Context context, Cursor cursor ) {
            /**
             * Here we are setting our data that means, take the data from the cursor and put it in
             * views
             */
            TextView tvName = (TextView) view.findViewById( R.id.tv_name );
            tvName.setText( cursor.getString( 1 ) );

            TextView tvPhoneNumber = (TextView) view.findViewById( R.id.tv_phone_number );
            tvPhoneNumber.setText( cursor.getString( 2 ) );

            long timestamp = cursor.getLong( 3 );
            String formattedTime = DateTimeUtils.getFormattedTime( TIMESTAMP_FORMAT, timestamp );
            TextView tvTimeStamp = (TextView) view.findViewById( R.id.tv_timestamp );
            tvTimeStamp.setText( formattedTime );

            ImageView ivDelete = (ImageView) view.findViewById( R.id.iv_delete );
            ivDelete.setTag( cursor.getInt( 0 ) );
            ivDelete.setOnClickListener( _controller );
        }

        @Override
        public View newView( Context context, Cursor cursor, ViewGroup parent ) {
            /**
             * When the view will be created for first time, we need to tell the adapters, how each
             * item will look.
             */
            View view = _inflater.inflate( R.layout.blocked_call_list_item, parent, false );
            return view;
        }
    }

    @Override
    public void onStop() {
        BlockedCallsDb.getInstance( ).resetBlockedCallsCount( );
        super.onStop( );
    }

    @Override
    public void onDestroy() {
        if ( _adapter != null ) {
            Cursor cursor = _adapter.getCursor( );
            if ( cursor != null ) {
                cursor.close( );
            }
        }
        super.onDestroy( );
    }
}
