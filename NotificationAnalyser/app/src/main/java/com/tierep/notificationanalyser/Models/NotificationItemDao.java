package com.tierep.notificationanalyser.models;

import com.j256.ormlite.dao.Dao;
import com.tierep.notificationanalyser.NotificationAppView;
import com.tierep.notificationanalyser.NotificationDateView;

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

    public List<NotificationAppView> getOverviewWeek(Date date) throws SQLException;

    public List<NotificationAppView> getOverviewMonth(Date date) throws SQLException;

    /**
     * @param days The number of previous days to fetch (counting backwards, starting from today).
     * @return An ordered ascending list on the date.
     * @throws SQLException
     */
    public List<NotificationDateView> getSummaryLastDays(int days) throws SQLException;

    /**
     * @param weeks The number of previous weeks to fetch (counting backwards, starting from this week).
     * @return An ordered ascending list on the date.
     * @throws SQLException
     */
    public List<NotificationDateView> getSummaryLastWeeks(int weeks) throws SQLException;

    /**
     * @param months The number of previous months to fetch (counting backwards, starting from this month).
     * @return An ordered ascending list on the date.
     * @throws SQLException
     */
    public List<NotificationDateView> getSummaryLastMonths(int months) throws SQLException;
}
