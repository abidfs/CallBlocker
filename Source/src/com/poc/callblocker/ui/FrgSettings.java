package com.poc.callblocker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.poc.callblocker.R;
import com.poc.callblocker.ui.controller.SettingsController;
import com.poc.callblocker.utilities.AppSettings;

/**
 * This class extends {@link Fragment} and is used to display settings UI
 * 
 * @author Sayyad Abid
 */
public class FrgSettings extends Fragment {
    private SettingsController _controller;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.settings, container, false );
        _controller = new SettingsController( );
        _initializeUi( view );
        return view;
    }

    private void _initializeUi( View view ) {
        ToggleButton tbShowDialogForUnknownNumbers = (ToggleButton) view.findViewById( R.id.tb_show_dialog_for_unknown_numbers );
        tbShowDialogForUnknownNumbers.setChecked( AppSettings.getInstance( ).showDialogForUnknowNumbers( ) );
        tbShowDialogForUnknownNumbers.setOnCheckedChangeListener( _controller );

        ToggleButton tbShowNotificationForBlockedCalls = (ToggleButton) view.findViewById( R.id.tb_show_notification_for_blocked_calls );
        tbShowNotificationForBlockedCalls.setChecked( AppSettings.getInstance( ).showNotificationForBlockedCalls( ) );
        tbShowNotificationForBlockedCalls.setOnCheckedChangeListener( _controller );
    }

    @Override
    public void onResume() {
        Activity activity = getActivity( );
        ( (ScrMain) activity ).setTitle( R.string.title_settings );
        super.onResume( );
    }
}
