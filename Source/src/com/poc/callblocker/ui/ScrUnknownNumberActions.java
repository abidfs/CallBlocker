package com.poc.callblocker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.poc.callblocker.R;
import com.poc.callblocker.ui.controller.UnknownNumberActionsController;
import com.poc.callblocker.utilities.UiConstants;

/**
 * This class is used while displaying the add unknown number to blacklist popup. Declared as
 * {@link Activity} with theme as dialog in manifest.
 * 
 * @author Sayyad Abid
 */
public class ScrUnknownNumberActions extends FragmentActivity {
    private UnknownNumberActionsController _controller;

    private FrgDlgUnknownNumberActions _unknownNumberActionsDialog;
    private FrgDlgAddContactManually _addContactManuallyDialog;
    private String _phoneNumber;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.unknown_number_actions );
        _controller = new UnknownNumberActionsController( this );
        _phoneNumber = getIntent( ).getStringExtra( UiConstants.PHONE_NUMBER );
        showDialogToHandleUnknowNumbers( );
    }

    /**
     * Method to show popup to take action on unknown numbers
     */
    public void showDialogToHandleUnknowNumbers() {
        _unknownNumberActionsDialog = new FrgDlgUnknownNumberActions( _controller );
        _unknownNumberActionsDialog.show( getSupportFragmentManager( ), "" );
    }

    /**
     * Method to hide unknown number actions popup
     */
    public void dismissHandleUnknownNumbersDialog() {
        if ( _unknownNumberActionsDialog != null && _unknownNumberActionsDialog.isAdded( ) ) {
            _unknownNumberActionsDialog.dismiss( );
        }
    }

    /**
     * Method to show popup to add unknown number to blacklist.
     */
    public void showDialogToAddContactManually() {
        _addContactManuallyDialog = new FrgDlgAddContactManually( _controller, _phoneNumber );
        _addContactManuallyDialog.show( getSupportFragmentManager( ), "" );
    }

    /**
     * Method to hide add unknown number to blacklist popup.
     */
    public void dismissAddContactManuallyDialog() {
        if ( _addContactManuallyDialog != null && _addContactManuallyDialog.isAdded( ) ) {
            _addContactManuallyDialog.dismiss( );
        }
    }

    /**
     * Method to get the entered name from popup
     * 
     * @return the entered name from popup
     */
    public String getEnteredNameFromDialog() {
        return _addContactManuallyDialog.getEnteredName( );
    }

    /**
     * Method to get the entered phone number from popup
     * 
     * @return the entered phone number from popup
     */
    public String getEnteredPhoneNumberFromDialog() {
        return _addContactManuallyDialog.getEnteredPhoneNumber( );
    }
}
