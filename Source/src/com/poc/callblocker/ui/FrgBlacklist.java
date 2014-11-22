package com.poc.callblocker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.poc.callblocker.R;
import com.poc.callblocker.database.BlacklistedContactsDb;
import com.poc.callblocker.ui.controller.BlacklistController;
import com.poc.callblocker.utilities.UiConstants;

/**
 * This class extends {@link Fragment} and is used to manage blacklist
 * 
 * @author Sayyad Abid
 */
public class FrgBlacklist extends Fragment {
    private BlacklistController _controller;
    private ListView _lvBlacklist;
    private BlacklistAdapter _adapter;
    private FrgDlgAddContactManually _addContactManuallyDialog;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.blacklist, container, false );
        _controller = new BlacklistController( this );
        _lvBlacklist = (ListView) view.findViewById( R.id.lv_blacklist );
        return view;
    }

    @Override
    public void onResume() {
        setTitle( );
        ( (ScrMain) getActivity( ) ).toggleAddButtonVisibility( true, _controller );
        populateList( );
        super.onResume( );
    }

    /**
     * Method to set the title
     */
    public void setTitle() {
        int blacklistedContactsCount = _controller.getBlacklistedContactCount( );
        String title = String.format( getString( R.string.title_blacklisted_contacts ), blacklistedContactsCount );
        ( (ScrMain) getActivity( ) ).setTitle( title );
    }

    /**
     * Method to show options dialog to add contacts from.
     */
    public void showAddContactOptions() {
        AddContactOptionsDialog optionsDialog = new AddContactOptionsDialog( );
        optionsDialog.show( getFragmentManager( ), "" );
    }

    /**
     * This class extends {@link DialogFragment} and is used to show options dialog to add contacts
     * from.
     * 
     * @author Sayyad Abid
     */
    public class AddContactOptionsDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog( Bundle savedInstanceState ) {
            AlertDialog.Builder builder = new AlertDialog.Builder( getActivity( ) );
            builder.setItems( R.array.options_add_contact_to_blacklist, new DialogInterface.OnClickListener( ) {
                @Override
                public void onClick( DialogInterface arg0, int arg1 ) {
                    switch ( arg1 ) {
                    case 0:
                        _startAddContactToBlacklist( UiConstants.ADD_CONTACT_TO_BL_FROM_PHONE_BOOK );
                        break;

                    case 1:
                        _startAddContactToBlacklist( UiConstants.ADD_CONTACT_TO_BL_FROM_CALL_LOGS );
                        break;

                    case 2:
                        showDialogToAddContactManually( );
                        break;
                    }
                }
            } );
            return builder.create( );
        }
    }

    /**
     * Method to show add contact dialog
     */
    public void showDialogToAddContactManually() {
        _addContactManuallyDialog = new FrgDlgAddContactManually( _controller );
        _addContactManuallyDialog.show( getFragmentManager( ), "" );
    }

    /**
     * Method to dismiss add contacts dialog
     */
    public void dismissAddContactManuallyDialog() {
        if ( _addContactManuallyDialog != null && _addContactManuallyDialog.isAdded( ) ) {
            _addContactManuallyDialog.dismiss( );
        }
    }

    /**
     * Method to get the entered name from add contact dialog
     * 
     * @return the entered name from add contact dialog
     */
    public String getEnteredNameFromDialog() {
        return _addContactManuallyDialog.getEnteredName( );
    }

    /**
     * Method to get the entered phone number from add contact dialog
     * 
     * @return the entered phone number from add contact dialog
     */
    public String getEnteredPhoneNumberFromDialog() {
        return _addContactManuallyDialog.getEnteredPhoneNumber( );
    }

    /**
     * Method to start {@link FrgAddContactsToBlackList} fragment passing it a param indicating from
     * where to load contacts
     * 
     * @param addFrom
     *            the from param
     */
    private void _startAddContactToBlacklist( int addFrom ) {
        FragmentTransaction transaction = getFragmentManager( ).beginTransaction( );
        FrgAddContactsToBlackList frgBlacklist = new FrgAddContactsToBlackList( addFrom );
        transaction.replace( R.id.container, frgBlacklist, UiConstants.TAG_ADD_CONTACT_TO_BLACKLIST );
        transaction.commit( );

        ( (ScrMain) getActivity( ) ).toggleBottomBarVisibility( false );
    }

    /**
     * Method to populate the blacklist
     */
    public void populateList() {
        /**
         * Fetching records from database may take time, hence calling it on new thread.
         */
        new Handler( ).post( new Runnable( ) {
            @Override
            public void run() {
                Cursor cursor = BlacklistedContactsDb.getInstance( ).getBlacklistedContactsCursor( );
                if ( _adapter == null ) {
                    _adapter = new BlacklistAdapter( getActivity( ), cursor );
                    _lvBlacklist.setAdapter( _adapter );
                } else {
                    _adapter.changeCursor( cursor );
                }
            }
        } );
    }

    private class BlacklistAdapter extends CursorAdapter {
        private final LayoutInflater _inflater;

        public BlacklistAdapter( Context context, Cursor c ) {
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

            String phoneNumber = cursor.getString( 2 );
            TextView tvPhoneNumber = (TextView) view.findViewById( R.id.tv_phone_number );
            tvPhoneNumber.setText( phoneNumber );

            ImageView ivDelete = (ImageView) view.findViewById( R.id.iv_delete );
            ivDelete.setTag( phoneNumber );
            ivDelete.setOnClickListener( _controller );
        }

        @Override
        public View newView( Context context, Cursor cursor, ViewGroup parent ) {
            /**
             * When the view will be created for first time, we need to tell the adapters, how each
             * item will look.
             */
            View view = _inflater.inflate( R.layout.blacklist_list_item, parent, false );
            return view;
        }
    }

    @Override
    public void onDestroy() {
        ( (ScrMain) getActivity( ) ).toggleAddButtonVisibility( false, null );
        if ( _adapter != null ) {
            Cursor cursor = _adapter.getCursor( );
            if ( cursor != null ) {
                cursor.close( );
            }
        }
        super.onDestroy( );
    }
}
