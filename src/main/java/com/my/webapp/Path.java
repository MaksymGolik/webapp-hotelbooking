package com.my.webapp;

public class Path {
    /*
     * Pathways for all registered users
     */
    public static final String CABINET_PATH = "/cabinet";
    public static final String CHANGE_BOOKING_STATUS_PATH = "/changeBookingStatus";


    /*
     * Pathways only for registered Role GUEST
     */
    public static final String MAKE_BOOKING_PATH = "/makeBooking";
    public static final String BOOKING_DETAILS_PATH = "/bookingDetails";
    public static final String BOOKING_INFO_BRIDGE_PATH = "/bookingInfoBridge";
    public static final String CABINET_GUEST_BOOKING_PATH = "/myBookings";

    /*
     * Pathways only for registered Role ADMIN
     */
    public static final String CABINET_ADMIN_PANEL_PATH = "/adminPanel";
}
