package com.tierep.notificationanalyser.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * The 'Notifications' table.
 *
 * Created by pieter on 21/09/14.
 */
@DatabaseTable(tableName = "Notifications", daoClass = NotificationItemDaoImpl.class)
public class NotificationItem {
    public static final String FIELD_TABLE_NAME = "Notifications";
    public static final String FIELD_KEY = "Id";
    public static final String FIELD_PACKAGE_NAME = "PackageName";
    public static final String FIELD_DATE = "Date";
    public static final String FIELD_MESSAGE = "Message";


    @DatabaseField(columnName = FIELD_KEY, generatedId = true)
    private Integer Key;
    @DatabaseField(columnName = FIELD_PACKAGE_NAME, canBeNull = false)
    private String PackageName;
    @DatabaseField(columnName = FIELD_DATE, canBeNull = false)
    private Date Date;
    @DatabaseField(columnName = FIELD_MESSAGE, canBeNull = true)
    private String Message;

    public NotificationItem() {
        //ORMLite needs a no-arg constructor.
    }

    public NotificationItem(String packageName, Date date, String message) {
        this.PackageName = packageName;
        this.Date = date;
        this.Message = message;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        this.Date = date;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
