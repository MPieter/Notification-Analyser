package com.tierep.notificationanalyser.ui;

import android.content.Intent;

import com.tierep.notificationanalyser.NotificationAppView;
import com.tierep.notificationanalyser.NotificationDateView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pieter on 25/10/14.
 */
public class HistoryDailyFragment extends HistoryFragment {
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM");

    @Override
    protected void startAppDetailActivity(String appName, Date date) {
        Intent intent = new Intent(getActivity(), AppDetail.class);
        intent.putExtra(AppDetail.EXTRA_PACKAGENAME, appName);
        intent.putExtra(AppDetail.EXTRA_INTERVALTYPE, AppDetail.FLAG_VIEW_DAILY);
        intent.putExtra(AppDetail.EXTRA_DATESTRING, new SimpleDateFormat("yyyy-MM-dd").format(date));
        startActivity(intent);
    }

    @Override
    protected List<NotificationDateView> getChartData(int items) throws SQLException {
        return this.getDatabaseHelper().getNotificationDao().getSummaryLastDays(items);
    }

    @Override
    protected List<NotificationAppView> getListViewDate(Date date) throws SQLException {
        return this.getDatabaseHelper().getNotificationDao().getOverviewDay(date);
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
