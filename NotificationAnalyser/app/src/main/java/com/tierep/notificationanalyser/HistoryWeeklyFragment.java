package com.tierep.notificationanalyser;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pieter on 25/10/14.
 */
public class HistoryWeeklyFragment extends HistoryFragment {
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("w");
    @Override
    protected List<NotificationDateView> getChartData(int items) throws SQLException {
        return this.getDatabaseHelper().getNotificationDao().getSummaryLastWeeks(items);
    }

    @Override
    protected List<NotificationAppView> getListViewDate(Date date) throws SQLException {
        return this.getDatabaseHelper().getNotificationDao().getOverviewWeek(date);
    }

    /**
     * Gets the SimpleDateFormat used for formatting the labels on the chart.
     *
     * @return the correct SimpleDateFormat.
     */
    @Override
    protected SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
