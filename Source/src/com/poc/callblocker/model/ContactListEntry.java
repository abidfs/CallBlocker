package com.poc.callblocker.model;

/**
 * Data model class to maintain the contact entry (uid , phoneNumber). This is used while getting
 * the records from phone book or call logs
 * 
 * @author Sayyad Abid
 */
public class ContactListEntry {
    /**
     * uid can be null if number entered manually or selected from call log. It will have value if
     * contact is selected from phone-book.
     */
    private String _uid;
    private String _phoneNumber;
    private String _name;

    /**
     * Parameterized constructor, initializes specified member variables.
     * 
     * @param uid
     *            String containing UID for contact, null if contact is not from address book.
     * @param phoneNumber
     *            String containing phone number. First phone number id of corresponding contact
     *            have multiple entries of phone number.
     * @param name
     *            String containing name corresponding to contact, null if name is not present.
     */
    public ContactListEntry( String uid, String phoneNumber, String name ) {
        _uid = uid;
        _phoneNumber = phoneNumber;
        _name = name;
    }

    /**
     * 
     * @return The _uid
     */
    public String getUid() {
        return this._uid;
    }

    public void setUid( String uid ) {
        this._uid = uid;
    }

    /**
     * 
     * @return The _phoneNumber
     */
    public String getPhoneNumber() {
        return this._phoneNumber;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }
}