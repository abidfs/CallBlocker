package com.poc.callblocker.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for handling date-time.
 * 
 * @author Sayyad Abid
 */
public class DateTimeUtils {
    /**
     * Method to get the current date-time in the input format.
     * 
     * @param dateTimeFormat
     *            the date-time format
     * @param timestamp
     *            the timestamp to be formatted
     * @return the current date-time in the input format
     */
    public static String getFormattedTime( String dateTimeFormat, long timestamp ) {
        Date date = new Date( timestamp );
        SimpleDateFormat sdf = new SimpleDateFormat( dateTimeFormat, Locale.US );
        return sdf.format( date );
    }
}
