package com.tierep.notificationanalyser;

import java.util.Date;

/**
 * A view model that will be used for displaying a bar chart of the history of
 * the notifications count during the last days.
 *
 * Created by pieter on 24/09/14.
 */
public class NotificationDateView {
    public Date Date;
    public Integer Notifications;

    public NotificationDateView() {
        this.Date = null;
        this.Notifications = 0;
    }

    public NotificationDateView(Date date, Integer notifications) {
        this.Date = date;
        this.Notifications = notifications;
    }
}
