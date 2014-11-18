package com.tierep.notificationanalyser.ui;

import com.tierep.notificationanalyser.NotificationAppView;
import com.tierep.notificationanalyser.NotificationDateView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pieter on 25/10/14.
 */
public class HistoryMonthlyFragment extends HistoryFragment {
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");
    @Override
    protected List<NotificationDateView> getChartData(int items) throws SQLException {
        return this.getDatabaseHelper().getNotificationDao().getSummaryLastMonths(items);
    }

    @Override
    protected List<NotificationAppView> getListViewDate(Date date) throws SQLException {
        return this.getDatabaseHelper().getNotificationDao().getOverviewMonth(date);
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
