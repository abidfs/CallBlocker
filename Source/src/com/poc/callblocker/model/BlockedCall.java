package com.poc.callblocker.model;

/**
 * Data model class to hold the blocked call info
 * 
 * @author Sayyad Abid
 */
public class BlockedCall {
    private long _id = -1;
    private String _name;
    private String _phoneNumber;
    private long _timestamp;
    private long _readStatus;

    public static final int STATUS_READ = 101;
    public static final int STATUS_UNREAD = 102;

    /**
     * Parameterized constructor
     * 
     * @param name
     *            the name
     * @param phoneNumber
     *            the phone number
     * @param timestamp
     *            the timestamp
     * @param readStatus
     *            the read status
     */
    public BlockedCall( String name, String phoneNumber, long timestamp, int readStatus ) {
        _name = name;
        _phoneNumber = phoneNumber;
        _timestamp = timestamp;
        _readStatus = readStatus;
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
     * @param timestamp
     *            the timestamp
     * @param readStatus
     *            the read status
     */
    public BlockedCall( long id, String name, String phoneNumber, long timestamp, int readStatus ) {
        _id = id;
        _name = name;
        _phoneNumber = phoneNumber;
        _timestamp = timestamp;
        _readStatus = readStatus;
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
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name
     *            the _name to set
     */
    public void setName( String name ) {
        this._name = name;
    }

    /**
     * @return the _phoneNumber
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

    /**
     * @return the _timestamp
     */
    public long getTimestamp() {
        return _timestamp;
    }

    /**
     * @param timestamp
     *            the _timestamp to set
     */
    public void setTimestamp( long timestamp ) {
        this._timestamp = timestamp;
    }

    /**
     * @return the _readStatus
     */
    public long getReadStatus() {
        return _readStatus;
    }

    /**
     * @param readStatus
     *            the _readStatus to set
     */
    public void setReadStatus( long readStatus ) {
        this._readStatus = readStatus;
    }
}
