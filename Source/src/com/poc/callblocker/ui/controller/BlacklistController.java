package com.poc.callblocker.ui.controller;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.poc.callblocker.R;
import com.poc.callblocker.database.BlacklistedContactsDb;
import com.poc.callblocker.model.BlacklistedContact;
import com.poc.callblocker.ui.FrgBlacklist;
import com.poc.callblocker.utilities.CustomToast;

/**
 * Controller class for {@link FrgBlacklist}
 * 
 * @author Sayyad Abid
 */
public class BlacklistController implements OnClickListener {
    private final FrgBlacklist _fragment;

    /**
     * Parameterized Constructor
     * 
     * @param fragment
     *            the {@link FrgBlacklist} instance
     */
    public BlacklistController( FrgBlacklist fragment ) {
        this._fragment = fragment;
    }

    @Override
    public void onClick( View v ) {
        switch ( v.getId( ) ) {
        case R.id.iv_add:
            _fragment.showAddContactOptions( );
            break;

        case R.id.iv_delete:
            String phoneNumberToDelete = (String) v.getTag( );
            int deleted = BlacklistedContactsDb.getInstance( ).remove( phoneNumberToDelete );
            if ( deleted != 0 ) {
                _fragment.setTitle( );
                _fragment.populateList( );
            }
            break;

        case R.id.dlg_btn_add:
            String name = _fragment.getEnteredNameFromDialog( );
            String phoneNumber = _fragment.getEnteredPhoneNumberFromDialog( );
            if ( TextUtils.isEmpty( name ) ) {
                name = _fragment.getString( R.string.lbl_unknown );
            }
            if ( TextUtils.isEmpty( phoneNumber ) ) {
                CustomToast.show( _fragment.getActivity( ), R.string.err_enter_phone_number, CustomToast.LENGTH_LONG );
                return;
            }
            BlacklistedContact blacklistedContact = new BlacklistedContact( name, phoneNumber );
            int added = BlacklistedContactsDb.getInstance( ).add( blacklistedContact );
            if ( added != -1 ) {
                String message = String.format( _fragment.getString( R.string.msg_contact_added_to_blacklist ), 1, 1 );
                CustomToast.show( _fragment.getActivity( ), message, CustomToast.LENGTH_LONG );
                _fragment.setTitle( );
                _fragment.populateList( );
            }
            _fragment.dismissAddContactManuallyDialog( );
            break;

        case R.id.dlg_btn_cancel:
            _fragment.dismissAddContactManuallyDialog( );
            break;
        }
    }

    /**
     * Method to get the blacklisted contacts count from DB
     * 
     * @return the blacklisted contacts count from DB
     */
    public int getBlacklistedContactCount() {
        return BlacklistedContactsDb.getInstance( ).getBlacklistedContactsCount( );
    }
}
