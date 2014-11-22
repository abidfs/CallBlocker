package com.poc.callblocker.ui.controller;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.poc.callblocker.R;
import com.poc.callblocker.ui.FrgSettings;
import com.poc.callblocker.utilities.AppSettings;

/**
 * Controller class for {@link FrgSettings}
 * 
 * @author Sayyad Abid
 */
public class SettingsController implements OnCheckedChangeListener {

    @Override
    public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
        switch ( buttonView.getId( ) ) {
        case R.id.tb_show_dialog_for_unknown_numbers:
            AppSettings.getInstance( ).setShowDialogForUnknowNumbers( isChecked );
            break;

        case R.id.tb_show_notification_for_blocked_calls:
            AppSettings.getInstance( ).setShowNotificationForBlockedCalls( isChecked );
            break;
        }
    }

}
