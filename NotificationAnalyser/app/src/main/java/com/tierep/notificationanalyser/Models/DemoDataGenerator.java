package com.tierep.notificationanalyser.Models;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This utility class can be used to generate demo data.
 * Data is generated for the last 30 days, for a total of 500 notifications distributed random over
 * packages of some common apps.
 *
 * For displaying the data correctly in the app, it is required that these common apps are installed.
 *
 * Created by pieter on 04/10/14.
 */
public class DemoDataGenerator {
    private Context context = null;
    private DatabaseHelper databaseHelper = null;
    private List<Application> applications = new ArrayList<Application>();
    private List<Date> dates = new ArrayList<Date>();
    private Random random = new Random();

    public DemoDataGenerator(Context context) {
        this.context = context;
        applications.add(new Application("com.google.android.talk", false));
        applications.add(new Application("com.whatsapp", false));
        applications.add(new Application("com.facebook.orca", false));
        applications.add(new Application("com.facebook.katana", false));
        applications.add(new Application("com.instagram.android", false));
        applications.add(new Application("com.twitter.android", false));
        applications.add(new Application("com.joelapenna.foursquared", false));
        applications.add(new Application("com.google.android.gm", false));
        applications.add(new Application("com.google.android.apps.plus", false));
        applications.add(new Application("com.snapchat.android", false));
        applications.add(new Application("com.soundcloud.android", false));
        applications.add(new Application("org.telegram.messenger", false));
        applications.add(new Application("com.foursquare.robin", false));
        applications.add(new Application("com.linkedin.android", false));

        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 50; i++) {
            dates.add(cal.getTime());
            cal.add(Calendar.HOUR, -24);
        }
    }

    public void Generate(boolean emptyFirst) {
        try {
            ApplicationDao daoApp = getDatabaseHelper().getApplicationDao();
            NotificationItemDao daoNtf = getDatabaseHelper().getNotificationDao();

            if (emptyFirst) {
                TableUtils.clearTable(getDatabaseHelper().getConnectionSource(), Application.class);
                TableUtils.clearTable(getDatabaseHelper().getConnectionSource(), NotificationItem.class);
            }

            for (Application app : applications) {
                daoApp.create(app);
            }

            for (int i = 0; i < 5000; i++) {
                NotificationItem ntf = new NotificationItem(GenerateApplication().getPackageName(), GenerateDate());
                daoNtf.create(ntf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.close();
    }

    private Application GenerateApplication() {
        int randomNum = random.nextInt(applications.size());

        return applications.get(randomNum);
    }

    private Date GenerateDate() {
        int randomNum = random.nextInt(dates.size());

        return dates.get(randomNum);
    }

    private DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void close() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
