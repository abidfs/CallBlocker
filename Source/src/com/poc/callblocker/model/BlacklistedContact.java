package com.poc.callblocker.model;

/**
 * Data model class to hold the Blacklisted Contact's information
 * 
 * @author Sayyad Abid
 */
/**
 * @author SAYYAD
 * 
 */
public class BlacklistedContact {
    private long _id;
    private String _name;
    private String _phoneNumber;

    /**
     * Parameterized constructor
     * 
     * @param name
     *            the name
     * @param phoneNumber
     *            the phone number
     */
    public BlacklistedContact( String name, String phoneNumber ) {
        _name = name;
        _phoneNumber = phoneNumber;
    }

    /**
     * Parameterized constructor
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     * @param phoneNumber
     *            the phone number
     */
    public BlacklistedContact( long id, String name, String phoneNumber ) {
        _id = id;
        _name = name;
        _phoneNumber = phoneNumber;
    }

    /**
     * @return the _id
     */
    public long getId() {
        return _id;
    }

    /**
     * @param id
     *            the _id to set
     */
    public void setId( long id ) {
        this._id = id;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName( String name ) {
        this._name = name;
    }

    /**
     * @return the phone number
     */
    public String getPhoneNumber() {
        return _phoneNumber;
    }

    /**
     * @param phoneNumber
     *            the _phoneNumber to set
     */
    public void setPhoneNumber( String phoneNumber ) {
        this._phoneNumber = phoneNumber;
    }
}
