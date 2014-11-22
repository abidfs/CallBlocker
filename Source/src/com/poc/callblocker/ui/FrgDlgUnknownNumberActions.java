package com.poc.callblocker.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.poc.callblocker.R;

/**
 * This class extends {@link DialogFragment} and is used to display confirmation popup to add
 * unknown number to blacklist
 * 
 * @author Sayyad Abid
 */
public class FrgDlgUnknownNumberActions extends DialogFragment {
    private Dialog _unknownNumberActionsDialg;
    private View.OnClickListener _onClickListener;

    /**
     * Parameterized Constructor
     * 
     * @param listener
     *            the button {@link OnClickListener}
     */
    public FrgDlgUnknownNumberActions( View.OnClickListener listener ) {
        _onClickListener = listener;
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
        _unknownNumberActionsDialg = new Dialog( getActivity( ) );
        _unknownNumberActionsDialg.setCanceledOnTouchOutside( false );
        _unknownNumberActionsDialg.setContentView( R.layout.dlg_unknow_number_actions );
        _unknownNumberActionsDialg.setTitle( R.string.app_name );

        Button btnBlock = (Button) _unknownNumberActionsDialg.findViewById( R.id.dlg_btn_block );
        btnBlock.setOnClickListener( _onClickListener );

        Button btnIgnore = (Button) _unknownNumberActionsDialg.findViewById( R.id.dlg_btn_ignore );
        btnIgnore.setOnClickListener( _onClickListener );
        return _unknownNumberActionsDialg;
    }
}
