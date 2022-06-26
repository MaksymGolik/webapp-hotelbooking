package com.my.utils;

import com.my.db.entity.Booking;
import com.my.db.entity.BookingStatus;
import com.my.db.entity.User;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static Pattern emailPattern;
    private static Pattern namePattern;
    private static Pattern phonePattern;
    private static Pattern datePattern;

    static {
        emailPattern = Pattern.compile("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$");
        namePattern = Pattern.compile("^[a-zA-Za-яА-Я]+$");
        phonePattern = Pattern.compile("^\\+{1}3?8?(0\\d{9})$");
        datePattern = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]):\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    }

    public static boolean validateUser(User user){
        boolean result = true;
        Matcher matcher;
        matcher = emailPattern.matcher(user.getEmail());
        if(!matcher.matches() || user.getEmail().length()>40){
            result = false;
        } else {
            matcher = namePattern.matcher(user.getName());
            if(!matcher.matches()|| user.getName().length()<2 || user.getName().length() > 40){
                result = false;
            }
        }
        return result;
    }

    public static boolean validatePeriod(String period){
        boolean result = true;
        Matcher matcher = datePattern.matcher(period);
        if(!matcher.matches()){
            result = false;
        } else {
            String[] periodArray = period.split(":");
            Date dateFrom = Date.valueOf(periodArray[0].trim());
            Date dateTo = Date.valueOf(periodArray[1].trim());
            if(dateFrom.compareTo(dateTo)!=-1){
                result = false;
            }
        }
        return result;
    }

    public static boolean validateBooking (Booking booking){
        boolean result = true;
        Matcher matcher = namePattern.matcher(booking.getGuestSurname());
        if(!matcher.matches() || booking.getGuestSurname().length()>40){
            result = false;
        } else {
            matcher = namePattern.matcher(booking.getGuestName());
            if(!matcher.matches() || booking.getGuestName().length()>40){
                result = false;
            } else {
                matcher = phonePattern.matcher(booking.getGuestPhoneNumber());
                if(!matcher.matches() || booking.getGuestPhoneNumber().length()>20){
                    result = false;
                }
            }
        }
        return result;
    }

    public static boolean validateNewStatus (BookingStatus oldStatus, BookingStatus newStatus){
        List<BookingStatus.StatusName> furtherForCreated =
                Arrays.asList(BookingStatus.StatusName.CANCELED,BookingStatus.StatusName.CONFIRMED, BookingStatus.StatusName.DENIED);
        List<BookingStatus.StatusName> furtherForConfirmed =
                Arrays.asList(BookingStatus.StatusName.CANCELED, BookingStatus.StatusName.PAID);
        List<BookingStatus.StatusName> furtherForPaid =
                Arrays.asList(BookingStatus.StatusName.CANCELLATION_REQUESTED, BookingStatus.StatusName.COMPLETED);
        List<BookingStatus.StatusName> furtherCancellationRequested = Arrays.asList(BookingStatus.StatusName.CANCELED);

        switch (oldStatus.getName()){
            case CREATED: if(!furtherForCreated.contains(newStatus.getName())) {
                return false;
            } break;
            case CONFIRMED: if(!furtherForConfirmed.contains(newStatus.getName())){
                return false;
            } break;
            case PAID: if(!furtherForPaid.contains(newStatus.getName())){
                return false;
            } break;
            case CANCELLATION_REQUESTED: if(!furtherCancellationRequested.contains(newStatus.getName())){
                return false;
            } break;
            default: return false;
        }
        return true;
    }
}
