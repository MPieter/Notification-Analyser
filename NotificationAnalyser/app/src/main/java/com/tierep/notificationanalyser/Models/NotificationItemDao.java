package com.tierep.notificationanalyser.Models;

import com.j256.ormlite.dao.Dao;
import com.tierep.notificationanalyser.NotificationAppView;
import com.tierep.notificationanalyser.NotificationDayView;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * NotificationItem DAO.
 *
 * Created by pieter on 24/09/14.
 */
public interface NotificationItemDao extends Dao<NotificationItem, Integer> {
    public List<NotificationAppView> getOverviewToday() throws SQLException;

    public List<NotificationAppView> getOverviewDay(Date date) throws SQLException;

    /**
     *
     * @param days The number of previous days to fetch (counting backwards, starting from today).
     * @return An ordered ascending list on the date.
     * @throws SQLException
     */
    public List<NotificationDayView> getSummaryLastDays(int days) throws SQLException;
}
