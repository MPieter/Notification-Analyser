package com.productions.pieter.notificationanalyzer;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * NotificationItem DAO.
 *
 * Created by pieter on 24/09/14.
 */
public interface NotificationItemDao extends Dao<NotificationItem, Integer> {
    public List<NotificationAppView> getOverviewLast24Hours() throws SQLException;

    public List<NotificationAppView> getOverviewDay(Date date) throws SQLException;

    public List<NotificationDayView> getSummaryLastDays() throws SQLException;
}
