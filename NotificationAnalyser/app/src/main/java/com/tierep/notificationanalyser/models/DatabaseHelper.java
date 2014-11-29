package com.tierep.notificationanalyser.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also provides
 * the DAOs used by the other classes.
 * <p/>
 * Created by pieter on 21/09/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "notifications.db";

    private static final int DATABASE_VERSION = 2;

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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Do changes from version 1 to 2
            try {
                NotificationItemDao dao = getNotificationDao();
                dao.executeRawNoArgs("ALTER TABLE " + NotificationItem.FIELD_TABLE_NAME + " ADD COLUMN " + NotificationItem.FIELD_MESSAGE + " TEXT");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if (oldVersion < 3) {
            // Do changes from version 2 to 3
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
