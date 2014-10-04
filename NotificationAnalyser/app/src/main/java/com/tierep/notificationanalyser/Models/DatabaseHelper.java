package com.tierep.notificationanalyser.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also provides
 * the DAOs used by the other classes.
 *
 * Created by pieter on 21/09/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "notifications.db";

    private static final int DATABASE_VERSION = 5;

    private ApplicationDao applicationDao = null;
    private NotificationItemDao notificationDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. This call createTable statements here to
     * create the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NotificationItem.class);
            TableUtils.createTable(connectionSource, Application.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        // TODO update mechanisme van de database verbeteren
        try {
            TableUtils.dropTable(connectionSource, NotificationItem.class, true);
            TableUtils.dropTable(connectionSource, Application.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Database Access Object (DAO) for Application class. It will create it or
     * just give the cached value.
     */
    public ApplicationDao getApplicationDao() throws SQLException {
        if (applicationDao == null) {
            applicationDao = getDao(Application.class);
        }
        return applicationDao;
    }

    /**
     * Returns the Database Access Object (DAO) for NotificationItem class. It will create it or
     * just give the cached value.
     */
    public NotificationItemDao getNotificationDao() throws SQLException {
        if (notificationDao == null) {
            notificationDao = getDao(NotificationItem.class);
        }
        return notificationDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        applicationDao = null;
        notificationDao = null;
    }
}
