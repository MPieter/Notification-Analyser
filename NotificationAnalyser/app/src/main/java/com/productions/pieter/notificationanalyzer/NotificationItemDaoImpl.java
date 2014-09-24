package com.productions.pieter.notificationanalyzer;

import android.app.Notification;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pieter on 24/09/14.
 */
public class NotificationItemDaoImpl extends BaseDaoImpl<NotificationItem, Integer> implements NotificationItemDao {
    public NotificationItemDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, NotificationItem.class);
    }

    @Override
    public List<NotificationView> getSummaryLast24Hours() throws SQLException {
        // TODO enkel laatste 24u selecteren
        List<NotificationView> list = new LinkedList<NotificationView>();
        int maxCount = 0;

        GenericRawResults<String[]> rawResults = this.queryRaw(
                "SELECT " + NotificationItem.FIELD_APPNAME
                        + ", COUNT(*) FROM " + NotificationItem.FIELD_TABLE_NAME
                        + " GROUP BY " + NotificationItem.FIELD_APPNAME);
        List<String[]> results = rawResults.getResults();

        for (int i = 0; i < results.size(); i++) {
            int ntfCount = Integer.parseInt(results.get(i)[1]);
            maxCount = ntfCount > maxCount ? ntfCount : maxCount;
        }
        for (int i = 0; i < results.size(); i++) {
            list.add(new NotificationView(results.get(i)[0], Integer.parseInt(results.get(i)[1]), maxCount));
        }
        return list;
    }
}
