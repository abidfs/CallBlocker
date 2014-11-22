package com.poc.callblocker.ui.controller;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.poc.callblocker.R;
import com.poc.callblocker.database.BlacklistedContactsDb;
import com.poc.callblocker.model.BlacklistedContact;
import com.poc.callblocker.ui.ScrUnknownNumberActions;
import com.poc.callblocker.utilities.CustomToast;

/**
 * Controller class for {@link ScrUnknownNumberActions} activity
 * 
 * @author Sayyad Abid
 */
public class UnknownNumberActionsController implements OnClickListener {
    private final ScrUnknownNumberActions _screen;

    /**
     * Parameterized constructor
     * 
     * @param screen
     *            the {@link ScrUnknownNumberActions} instance
     */
    public UnknownNumberActionsController( ScrUnknownNumberActions screen ) {
        _screen = screen;
    }

    @Override
    public void onClick( View v ) {
        switch ( v.getId( ) ) {
        case R.id.dlg_btn_block:
            _screen.dismissHandleUnknownNumbersDialog( );
            _screen.showDialogToAddContactManually( );
            break;

        case R.id.dlg_btn_ignore:
            _screen.dismissHandleUnknownNumbersDialog( );
            _screen.finish( );
            break;

        case R.id.dlg_btn_add:
            String name = _screen.getEnteredNameFromDialog( );
            String phoneNumber = _screen.getEnteredPhoneNumberFromDialog( );
            if ( TextUtils.isEmpty( name ) ) {
                name = _screen.getString( R.string.lbl_unknown );
            }
            if ( TextUtils.isEmpty( phoneNumber ) ) {
                CustomToast.show( _screen, R.string.err_enter_phone_number, CustomToast.LENGTH_LONG );
                return;
            }
            BlacklistedContact blacklistedContact = new BlacklistedContact( name, phoneNumber );
            int added = BlacklistedContactsDb.getInstance( ).add( blacklistedContact );
            if ( added != -1 ) {
                String message = String.format( _screen.getString( R.string.msg_contact_added_to_blacklist ), 1, 1 );
                CustomToast.show( _screen, message, CustomToast.LENGTH_LONG );
            }
            _screen.dismissAddContactManuallyDialog( );
            _screen.finish( );
            break;

        case R.id.dlg_btn_cancel:
            _screen.dismissHandleUnknownNumbersDialog( );
            _screen.finish( );
            break;
        }
    }

}
