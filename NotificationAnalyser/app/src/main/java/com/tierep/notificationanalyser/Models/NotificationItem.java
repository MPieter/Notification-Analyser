package com.tierep.notificationanalyser.Models;

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


    @DatabaseField(columnName = FIELD_KEY, generatedId = true)
    private Integer Key;
    @DatabaseField(columnName = FIELD_PACKAGE_NAME, canBeNull = false)
    private String PackageName;
    @DatabaseField(columnName = FIELD_DATE, canBeNull = false)
    private Date Date;

    public NotificationItem() {
        //ORMLite needs a no-arg constructor.
    }

    public NotificationItem(String packageName, Date date) {
        this.PackageName = packageName;
        this.Date = date;
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
}
