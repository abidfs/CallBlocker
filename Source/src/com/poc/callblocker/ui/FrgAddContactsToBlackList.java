package com.poc.callblocker.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.poc.callblocker.R;
import com.poc.callblocker.ui.controller.AddContactToBlacklistController;
import com.poc.callblocker.utilities.CustomProgressDialog;
import com.poc.callblocker.utilities.CustomToast;
import com.poc.callblocker.utilities.UiConstants;

/**
 * This class extends {@link Fragment} and is used to display UI to add contacts to blacklist
 * 
 * @author Sayyad Abid
 */
public class FrgAddContactsToBlackList extends Fragment {
    private AddContactToBlacklistController _controller;
    private ListView _lvBlacklist;
    private final int _addFrom;

    /**
     * Parameterized Constructor
     * 
     * @param addFrom
     *            the from param indicating form where to add the contacts (phonebook/call logs)
     */
    public FrgAddContactsToBlackList( int addFrom ) {
        _addFrom = addFrom;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.add_contacts_to_blacklist, container, false );
        _controller = new AddContactToBlacklistController( FrgAddContactsToBlackList.this, _addFrom );

        _lvBlacklist = (ListView) view.findViewById( R.id.lv_contacts );

        Button btnAdd = (Button) view.findViewById( R.id.btn_add );
        btnAdd.setOnClickListener( _controller );

        Button btnCancel = (Button) view.findViewById( R.id.btn_cancel );
        btnCancel.setOnClickListener( _controller );

        return view;
    }

    @Override
    public void onResume() {
        String title = null;
        if ( _addFrom == UiConstants.ADD_CONTACT_TO_BL_FROM_CALL_LOGS ) {
            title = getString( R.string.title_call_logs );
        } else {
            title = getString( R.string.title_contacts );
        }
        ( (ScrMain) getActivity( ) ).setTitle( title );
        super.onResume( );

        new LoadNumbersTask( ).execute( );
    }

    private class LoadNumbersTask extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute( );
            int msgResId = -1;
            if ( _addFrom == UiConstants.ADD_CONTACT_TO_BL_FROM_PHONE_BOOK ) {
                msgResId = R.string.msg_loading_contacts;
            } else {
                msgResId = R.string.msg_loading_logs;
            }
            CustomProgressDialog.show( FrgAddContactsToBlackList.this.getActivity( ), msgResId );
        }

        @Override
        protected Void doInBackground( Void... params ) {
            _controller.populateList( );
            return null;
        }

        @Override
        protected void onPostExecute( Void result ) {
            super.onPostExecute( result );
            try {
                _lvBlacklist.setAdapter( _controller );
                CustomProgressDialog.dismiss( FrgAddContactsToBlackList.this.getActivity( ) );
                if ( _controller.getCount( ) == 0 ) {
                    CustomToast.show( FrgAddContactsToBlackList.this.getActivity( ), R.string.msg_no_entries_present, CustomToast.LENGTH_LONG );
                }
            } catch ( Exception e ) {
                e.printStackTrace( );
            }
        }
    }

    public void gotoBlacklist() {
        ( (ScrMain) getActivity( ) ).startBlacklist( );
        ( (ScrMain) getActivity( ) ).toggleBottomBarVisibility( true );
    }
}
