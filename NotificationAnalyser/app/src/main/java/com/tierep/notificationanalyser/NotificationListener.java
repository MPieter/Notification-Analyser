package com.tierep.notificationanalyser;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.tierep.notificationanalyser.models.Application;
import com.tierep.notificationanalyser.models.ApplicationDao;
import com.tierep.notificationanalyser.models.DatabaseHelper;
import com.tierep.notificationanalyser.models.NotificationItem;

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
        if (!sbn.isOngoing()) {
            storeNotification(sbn);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn.isOngoing()) {
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

            String appName = packageName;
            try {
                ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, 0);
                appName = getPackageManager().getApplicationLabel(info).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String message = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (sbn.getNotification().extras != null) {
                    message = sbn.getNotification().extras.getString(
                            Notification.EXTRA_TITLE);
                    if (message == null || "".equals(message)) {
                        message = sbn.getNotification().extras.getString(
                                Notification.EXTRA_TEXT);
                    } else if (message.equals(appName)) {
                        String otherMsg = sbn.getNotification().extras.getString(
                                Notification.EXTRA_TEXT);
                        if (otherMsg != null && !"".equals(otherMsg)) {
                            message = otherMsg;
                        }
                    }
                }
            }

            if (message == null || "".equals(message)) {
                message = sbn.getNotification().tickerText.toString();
            } else if (message.equals(appName)) {
                String otherMsg = sbn.getNotification().tickerText.toString();
                if (otherMsg != null && !"".equals(otherMsg)) {
                    message = otherMsg;
                }
            }

            Dao<NotificationItem, Integer> dao = getDatabaseHelper().getNotificationDao();
            NotificationItem newItem = new NotificationItem(packageName, new Date(sbn.getPostTime()), message);
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
