package com.productions.pieter.notificationanalyzer;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * NotificationItem DAO.
 *
 * Created by pieter on 24/09/14.
 */
public interface NotificationItemDao extends Dao<NotificationItem, Integer> {
    public List<NotificationView> getSummaryLast24Hours() throws SQLException;
}
