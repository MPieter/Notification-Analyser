package com.productions.pieter.notificationanalyzer;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationListener extends NotificationListenerService {
    public NotificationListener() {
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.last_app_notification), "Whatsapp");
        editor.commit();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        // Do nothing for the moment
    }
}
