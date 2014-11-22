package com.poc.callblocker.ui.controller;

import java.util.ArrayList;

import com.poc.callblocker.R;
import com.poc.callblocker.database.BlacklistedContactsDb;
import com.poc.callblocker.model.BlacklistedContact;
import com.poc.callblocker.model.ContactListEntry;
import com.poc.callblocker.ui.FrgAddContactsToBlackList;
import com.poc.callblocker.utilities.CallLogManager;
import com.poc.callblocker.utilities.ContactManager;
import com.poc.callblocker.utilities.CustomToast;
import com.poc.callblocker.utilities.UiConstants;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Controller class for {@link FrgAddContactsToBlackList} {@link Fragment}
 * 
 * @author Sayyad Abid
 */
public class AddContactToBlacklistController extends BaseAdapter implements OnItemClickListener, OnCheckedChangeListener, OnClickListener {
    private ArrayList< ContactListEntry > _entries = new ArrayList< ContactListEntry >( );
    private FrgAddContactsToBlackList _fragment;
    private final LayoutInflater _inflater;
    private ArrayList< String > _selectedNumbers;
    private final int _addFrom;

    /**
     * Parameterized Constructor
     * 
     * @param fragment
     *            the {@link FrgAddContactsToBlackList} instance
     * @param addFrom
     *            the from param indicating whether to populate contacts from phone book or call
     *            logs
     */
    public AddContactToBlacklistController( FrgAddContactsToBlackList fragment, int addFrom ) {
        _fragment = fragment;
        _inflater = (LayoutInflater) _fragment.getActivity( ).getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        _selectedNumbers = new ArrayList< String >( );
        _addFrom = addFrom;
    }

    /**
     * Method to populate the list
     */
    public void populateList() {
        ArrayList< String > alreadyBlackListedNumbers = BlacklistedContactsDb.getInstance( ).getBlacklistedPhoneNumbers( );
        if ( _addFrom == UiConstants.ADD_CONTACT_TO_BL_FROM_CALL_LOGS ) {
            CallLogManager callLogManager = new CallLogManager( );
            _entries = callLogManager.getCallLogs( alreadyBlackListedNumbers );

        } else {
            ContactManager contactManager = new ContactManager( );
            _entries = contactManager.getAllContacts( alreadyBlackListedNumbers );
        }
    }

    @Override
    public int getCount() {
        if ( _entries != null ) {
            return _entries.size( );
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem( int position ) {
        return _entries.get( position );
    }

    @Override
    public long getItemId( int position ) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = _inflater.inflate( R.layout.contact_list_item, parent, false );
        }

        ContactListEntry contact = _entries.get( position );
        TextView tvName = (TextView) convertView.findViewById( R.id.tv_name );
        tvName.setText( contact.getName( ) );

        String phoneNumber = contact.getPhoneNumber( );
        TextView tvPhoneNumber = (TextView) convertView.findViewById( R.id.tv_phone_number );
        tvPhoneNumber.setText( phoneNumber );

        CheckBox cbSelect = (CheckBox) convertView.findViewById( R.id.cb_select );
        cbSelect.setTag( phoneNumber );
        if ( _selectedNumbers.contains( phoneNumber ) ) {
            cbSelect.setChecked( true );
        } else {
            cbSelect.setChecked( false );
        }
        cbSelect.setOnCheckedChangeListener( AddContactToBlacklistController.this );

        return convertView;
    }

    @Override
    public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
        switch ( buttonView.getId( ) ) {
        case R.id.cb_select:
            String phoneNumber = (String) buttonView.getTag( );
            if ( isChecked ) {
                if ( !_selectedNumbers.contains( phoneNumber ) ) {
                    _selectedNumbers.add( phoneNumber );
                }
            } else {
                _selectedNumbers.remove( phoneNumber );
            }
            break;
        }
    }

    @Override
    public void onItemClick( AdapterView< ? > arg0, View arg1, int arg2, long arg3 ) {

    }

    @Override
    public void onClick( View v ) {
        switch ( v.getId( ) ) {
        case R.id.btn_add:
            _addSelectedContactsToBlacklist( );
            break;

        case R.id.btn_cancel:
            _fragment.gotoBlacklist( );
            break;
        }
    } 

    /**
     * Method to add selected contacts to Blacklist
     */
    private void _addSelectedContactsToBlacklist() {
        int count = 0;
        if ( _selectedNumbers != null && _selectedNumbers.size( ) > 0 ) {
            for ( String phoneNumber : _selectedNumbers ) {
                for ( ContactListEntry contactListEntry : _entries ) {
                    if ( phoneNumber.equals( contactListEntry.getPhoneNumber( ) ) ) {
                        BlacklistedContact blacklistedContact = new BlacklistedContact( contactListEntry.getName( ), phoneNumber );
                        int added = BlacklistedContactsDb.getInstance( ).add( blacklistedContact );
                        if ( added != -1 ) {
                            count++;
                        }
                    }
                }
            }

            String message = String.format( _fragment.getString( R.string.msg_contact_added_to_blacklist ), count, _selectedNumbers.size( ) );
            CustomToast.show( _fragment.getActivity( ), message, CustomToast.LENGTH_LONG );
        }
    }

}
