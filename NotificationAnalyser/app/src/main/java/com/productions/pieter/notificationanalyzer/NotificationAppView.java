package com.productions.pieter.notificationanalyzer;

/**
 * Created by pieter on 17/09/14.
 */
public class NotificationAppView {
    public String AppName;
    public Integer Notifications;
    public Integer MaxNotifications;

    public NotificationAppView() {
        this.AppName = "";
        this.Notifications = 0;
        this.MaxNotifications = 0;
    }

    /**
     *
     * @param appName The name of the app.
     * @param notifications The number of notifications from the app.
     * @param maxNotifications The maximum number of notifications from any app.
     */
    public NotificationAppView(String appName, Integer notifications, Integer maxNotifications) {
        this.AppName = appName;
        this.Notifications = notifications;
        this.MaxNotifications = maxNotifications;
    }
}
