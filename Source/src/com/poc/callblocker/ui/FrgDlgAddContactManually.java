package com.poc.callblocker.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.poc.callblocker.R;

/**
 * This class extends {@link DialogFragment} and is used to display popup to add contact to
 * blacklist
 * 
 * @author Sayyad Abid
 */
public class FrgDlgAddContactManually extends DialogFragment {
    private Dialog _addContactManuallyDialg;
    private View.OnClickListener _onClickListener;
    private String _phoneNumberToAdd;

    /**
     * Parameterized Construstor
     * 
     * @param listener
     *            the button {@link OnClickListener}
     */
    public FrgDlgAddContactManually( View.OnClickListener listener ) {
        _onClickListener = listener;
    }

    /**
     * Parameterized Construstor
     * 
     * @param listener
     *            the {@link Button} {@link OnClickListener}
     * @param phoneNumberToAdd
     *            the number to be added.
     */
    public FrgDlgAddContactManually( View.OnClickListener listener, String phoneNumberToAdd ) {
        _onClickListener = listener;
        _phoneNumberToAdd = phoneNumberToAdd;
    }

    @Override
    public void onStart() {
        super.onStart( );
        Dialog dialog = getDialog( );
        if ( dialog != null ) {
            dialog.getWindow( ).setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        }
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        _addContactManuallyDialg = new Dialog( getActivity( ) );
        _addContactManuallyDialg.setCanceledOnTouchOutside( false );
        _addContactManuallyDialg.setContentView( R.layout.dlg_add_number_to_blacklist );
        _addContactManuallyDialg.setTitle( R.string.dlg_title_add_contact_to_blacklist );

        if ( !TextUtils.isEmpty( _phoneNumberToAdd ) ) {
            EditText etPhoneNumber = (EditText) _addContactManuallyDialg.findViewById( R.id.et_phone_number );
            etPhoneNumber.setText( _phoneNumberToAdd );
            etPhoneNumber.setEnabled( false );
        }
        Button btnAdd = (Button) _addContactManuallyDialg.findViewById( R.id.dlg_btn_add );
        btnAdd.setOnClickListener( _onClickListener );

        Button btnCancel = (Button) _addContactManuallyDialg.findViewById( R.id.dlg_btn_cancel );
        btnCancel.setOnClickListener( _onClickListener );
        return _addContactManuallyDialg;
    }

    /**
     * Method to get the entered name from dialog
     * 
     * @return the entered name from dialog
     */
    public String getEnteredName() {
        EditText etName = (EditText) _addContactManuallyDialg.findViewById( R.id.et_name );
        return etName.getText( ).toString( );
    }

    /**
     * Method to get the entered phone number from dialog
     * 
     * @return the entered phone number from dialog
     */
    public String getEnteredPhoneNumber() {
        EditText etPhoneNumber = (EditText) _addContactManuallyDialg.findViewById( R.id.et_phone_number );
        return etPhoneNumber.getText( ).toString( );
    }
}
