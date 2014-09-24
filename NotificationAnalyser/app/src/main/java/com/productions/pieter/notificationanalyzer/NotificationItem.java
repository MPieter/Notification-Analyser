package com.productions.pieter.notificationanalyzer;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by pieter on 21/09/14.
 */
@DatabaseTable(tableName = "Notifications", daoClass = NotificationItemDaoImpl.class)
public class NotificationItem {
    public static final String FIELD_TABLE_NAME = "Notifications";
    public static final String FIELD_KEY = "Key";
    public static final String FIELD_APPNAME = "AppName";
    public static final String FIELD_DATE = "Date";


    @DatabaseField(columnName = FIELD_KEY, generatedId = true)
    private Integer Key;
    @DatabaseField(columnName = FIELD_APPNAME, canBeNull = false)
    private String AppName;
    @DatabaseField(columnName = FIELD_DATE, canBeNull = false)
    private Date Date;

    public NotificationItem() {
        //ORMLite needs a no-arg constructor.
    }

    public NotificationItem(String appName, Date date) {
        this.AppName = appName;
        this.Date = date;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }
}
