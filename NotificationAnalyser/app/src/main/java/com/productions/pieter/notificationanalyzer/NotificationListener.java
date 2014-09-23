package com.productions.pieter.notificationanalyzer;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.Date;

public class NotificationListener extends NotificationListenerService {
    private DatabaseHelper databaseHelper = null;

    public NotificationListener() {
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            Dao<NotificationItem, Integer> dao = getDatabaseHelper().getNotificationDao();
            NotificationItem newItem = new NotificationItem(sbn.getPackageName(), new Date(sbn.getPostTime()));
            dao.create(newItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        // Do nothing for the moment
    }
}
