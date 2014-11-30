package com.tierep.notificationanalyser.models;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
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
    private List<AppMock> apps = new ArrayList<AppMock>();
    private List<Date> dates = new ArrayList<Date>();
    private Random random = new Random();
    private boolean hasGenerated = false;
    private int notificationsAmount = 500;
    private List<Integer> dayToSkip = Arrays.asList(23, 24, 25, 26, 27, 28);

    public DemoDataGenerator(Context context) {
        this.context = context;
        List<String> names = Arrays.asList("Maurice Cooper", "Jack Jaida", "Kinley Russell",
                "Nicky Izabelle", "Jerry Brooks", "Julyan Bella", "Ami Percival", "Larry Page",
                "Sergey Brin", "Eric Schmidt", "Mark Zuckerberg", "Marissa Mayer");
        List<String> empty = new LinkedList<String>();
        empty.add(null);
        apps.add(new AppMock(new Application("com.google.android.talk", false), names));
        apps.add(new AppMock(new Application("com.whatsapp", false), names));
        apps.add(new AppMock(new Application("com.facebook.orca", false), names));
        apps.add(new AppMock(new Application("com.facebook.katana", false), empty));
        apps.add(new AppMock(new Application("com.instagram.android", false), empty));
        apps.add(new AppMock(new Application("com.twitter.android", false), empty));
        apps.add(new AppMock(new Application("com.joelapenna.foursquared", false), empty));
        apps.add(new AppMock(new Application("com.google.android.gm", false), empty));
        apps.add(new AppMock(new Application("com.google.android.apps.plus", false), empty));
        apps.add(new AppMock(new Application("com.snapchat.android", false), empty));
        apps.add(new AppMock(new Application("com.soundcloud.android", false), empty));
        apps.add(new AppMock(new Application("org.telegram.messenger", false), empty));
        apps.add(new AppMock(new Application("com.foursquare.robin", false), empty));
        apps.add(new AppMock(new Application("com.linkedin.android", false), empty));

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 0);

        for (int i = 0; i < notificationsAmount; i++) {
            cal.add(Calendar.HOUR, random.nextInt(2) * (-1));
            cal.add(Calendar.MINUTE, random.nextInt(30));

            if (dayToSkip != null && !dayToSkip.contains(cal.get(Calendar.DAY_OF_MONTH))  ){
                dates.add(cal.getTime());
                Log.d("Added to dates: ", cal.getTime().toString());
            }
            else{
                i--;
            }


        }
    }

    public void Generate(boolean emptyFirst) {
        if (!hasGenerated) {
            try {
                ApplicationDao daoApp = getDatabaseHelper().getApplicationDao();
                NotificationItemDao daoNtf = getDatabaseHelper().getNotificationDao();

                if (emptyFirst) {
                    TableUtils.clearTable(getDatabaseHelper().getConnectionSource(), Application.class);
                    TableUtils.clearTable(getDatabaseHelper().getConnectionSource(), NotificationItem.class);
                }

                for (AppMock app : apps) {
                    daoApp.create(app.application);
                }

                for (int i = 0; i < notificationsAmount; i++) {
                    AppMock a = GenerateApplication();
                    NotificationItem ntf = new NotificationItem(a.application.getPackageName(), dates.get(i), GenerateApplicationMessage(a));
                    daoNtf.create(ntf);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.close();
        } else {
            hasGenerated = true;
        }
    }

    private AppMock GenerateApplication() {
        int randomNum = random.nextInt(apps.size());

        return apps.get(randomNum);
    }

    private String GenerateApplicationMessage(AppMock applicationMock) {
        int randomNum = random.nextInt(applicationMock.messages.size());

        return applicationMock.messages.get(randomNum);
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

    private class AppMock {
        public Application application;
        public List<String> messages;

        public AppMock(Application app, List<String> messages) {
            this.application = app;
            this.messages = messages;
        }
    }
}
