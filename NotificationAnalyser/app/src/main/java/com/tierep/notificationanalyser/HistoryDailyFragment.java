package com.tierep.notificationanalyser;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by pieter on 25/10/14.
 */
public class HistoryDailyFragment extends HistoryFragment {
    @Override
    protected List<NotificationDateView> getChartData(int items) throws SQLException {
        return this.getDatabaseHelper().getNotificationDao().getSummaryLastDays(items);
    }

    @Override
    protected List<NotificationAppView> getListViewDate(Date date) throws SQLException {
        return this.getDatabaseHelper().getNotificationDao().getOverviewDay(date);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.showDayListView(new Date());
    }
}
