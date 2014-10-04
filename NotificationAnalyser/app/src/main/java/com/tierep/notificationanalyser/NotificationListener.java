package com.tierep.notificationanalyser;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.tierep.notificationanalyser.Models.Application;
import com.tierep.notificationanalyser.Models.ApplicationDao;
import com.tierep.notificationanalyser.Models.DatabaseHelper;
import com.tierep.notificationanalyser.Models.NotificationItem;

import java.sql.SQLException;
import java.util.Date;

public class NotificationListener extends NotificationListenerService {
    public static boolean isNotificationAccessEnabled = false;

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
        if (sbn.isOngoing() == false) {
            storeNotification(sbn);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn.isOngoing() == true) {
            storeNotification(sbn);
        }
    }

    private void storeNotification(StatusBarNotification sbn) {
        try {
            String packageName = sbn.getPackageName();
            ApplicationDao applicationDao = getDatabaseHelper().getApplicationDao();
            if (!applicationDao.idExists(packageName)) {
                Application application = new Application(packageName, false);
                applicationDao.create(application);
            }

            Dao<NotificationItem, Integer> dao = getDatabaseHelper().getNotificationDao();
            NotificationItem newItem = new NotificationItem(packageName, new Date(sbn.getPostTime()));
            dao.create(newItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = super.onBind(intent);
        isNotificationAccessEnabled = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean onUnbind = super.onUnbind(intent);
        isNotificationAccessEnabled = false;
        return onUnbind;
    }
}
