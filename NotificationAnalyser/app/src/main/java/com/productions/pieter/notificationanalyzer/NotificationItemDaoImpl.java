package com.productions.pieter.notificationanalyzer;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public List<NotificationAppView> getOverviewLast24Hours() throws SQLException {
        // TODO enkel laatste 24u selecteren
        String rawQuery = "SELECT " + NotificationItem.FIELD_APPNAME
                + ", COUNT(*) FROM " + NotificationItem.FIELD_TABLE_NAME
                + " GROUP BY " + NotificationItem.FIELD_APPNAME;
        return this.getOverviewGeneric(rawQuery);
    }

    @Override
    public List<NotificationAppView> getOverviewDay(Date date) throws SQLException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = df.format(date);

        String rawQuery = "SELECT " + NotificationItem.FIELD_APPNAME
                + ", COUNT(*) FROM " + NotificationItem.FIELD_TABLE_NAME
                + " WHERE strftime('%d-%m-%Y'," + NotificationItem.FIELD_DATE + ") = '" + dateString + "'"
                + " GROUP BY " + NotificationItem.FIELD_APPNAME;

        return this.getOverviewGeneric(rawQuery);
    }


    private List<NotificationAppView> getOverviewGeneric(String rawQuery) throws SQLException {
        List<NotificationAppView> list = new LinkedList<NotificationAppView>();
        int maxCount = 0;

        GenericRawResults<String[]> rawResults = this.queryRaw(rawQuery);
        List<String[]> results = rawResults.getResults();

        for (int i = 0; i < results.size(); i++) {
            int ntfCount = Integer.parseInt(results.get(i)[1]);
            maxCount = ntfCount > maxCount ? ntfCount : maxCount;
        }
        for (int i = 0; i < results.size(); i++) {
            list.add(new NotificationAppView(results.get(i)[0], Integer.parseInt(results.get(i)[1]), maxCount));
        }
        return list;
    }

    @Override
    public List<NotificationDayView> getSummaryLastDays() throws SQLException {
        LinkedList<NotificationDayView> list = new LinkedList<NotificationDayView>();
        GenericRawResults<String[]> rawResults = this.queryRaw(
                "SELECT " + NotificationItem.FIELD_DATE
                        + ", COUNT(*) FROM " + NotificationItem.FIELD_TABLE_NAME
                        + " GROUP BY strftime('%d-%m-%Y', " + NotificationItem.FIELD_DATE + ")");
        List<String[]> results = rawResults.getResults();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        for (int i = 0; i < results.size(); i++) {
            try {
                Date date = formatter.parse(results.get(i)[0]);
                Integer notifications = Integer.parseInt(results.get(i)[1]);
                list.add(new NotificationDayView(date, notifications));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return list;
    }
}
