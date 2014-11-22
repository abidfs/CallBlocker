package com.poc.callblocker.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.poc.callblocker.MainApplication;

/**
 * This is a singleton class used to maintain the application settings. Uses Android
 * {@link SharedPreferences}
 * 
 * @author Sayyad Abid
 */
public class AppSettings {
    private static AppSettings _instance;

    private SharedPreferences _sharedPreferences;
    private Editor _sharedPrefEditor;

    private static final String SHARED_PREFERENCE_NAME = "CallBlockerPreferences";

    private String KEY_SHOW_DIALOG_FOR_UNKNOWN_NUMBERS = "SHOW_DIALOG_FOR_UNKNOWN_NUMBERS";
    private String KEY_SHOW_NOTIFICATION_FOR_BLOCKED_CALLS = "SHOW_NOTIFICATION_FOR_BLOCKED_CALLS";

    /**
     * Private constructor
     */
    private AppSettings() {

    }

    /**
     * Method to get the initialized instance of {@link AppSettings}.
     * 
     * @return the initialized instance of {@link AppSettings}
     */
    public static AppSettings getInstance() {
        if ( _instance == null ) {
            _instance = new AppSettings( );
        }
        return _instance;
    }

    /**
     * Method to get the {@link SharedPreferences} object
     * 
     * @return the {@link SharedPreferences} object
     */
    private SharedPreferences _getSharedPreferences() {
        if ( _sharedPreferences == null ) {
            _sharedPreferences = MainApplication.appContext.getSharedPreferences( SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE );
        }
        return _sharedPreferences;
    }

    /**
     * Method to get the {@link SharedPreferences.Editor} object
     * 
     * @return the {@link SharedPreferences.Editor} object
     */
    private Editor _getEditor() {
        if ( _sharedPrefEditor == null ) {
            _sharedPrefEditor = _getSharedPreferences( ).edit( );
        }
        return _sharedPrefEditor;
    }

    /**
     * Method to check whether to show dialog for unknown numbers.
     * 
     * @return true if show dialog for unknown numbers, false otherwise
     */
    public boolean showDialogForUnknowNumbers() {
        return _getSharedPreferences( ).getBoolean( KEY_SHOW_DIALOG_FOR_UNKNOWN_NUMBERS, true );
    }

    /**
     * Method to change the setting to show dialog for unknown numbers
     * 
     * @param show
     *            boolean indicating whether to show dialog for unknown numbers
     */
    public void setShowDialogForUnknowNumbers( boolean show ) {
        _getEditor( ).putBoolean( KEY_SHOW_DIALOG_FOR_UNKNOWN_NUMBERS, show ).commit( );
    }

    /**
     * Method to check whether to show notification for blocked calls
     * 
     * @return true if show notification for blocked calls, false otherwise
     */
    public boolean showNotificationForBlockedCalls() {
        return _getSharedPreferences( ).getBoolean( KEY_SHOW_NOTIFICATION_FOR_BLOCKED_CALLS, true );
    }

    /**
     * Method to change the setting to show notification for blocked calls
     * 
     * @param show
     *            boolean indicating whether to show notification for blocked calls
     */
    public void setShowNotificationForBlockedCalls( boolean show ) {
        _getEditor( ).putBoolean( KEY_SHOW_NOTIFICATION_FOR_BLOCKED_CALLS, show ).commit( );
    }
}
