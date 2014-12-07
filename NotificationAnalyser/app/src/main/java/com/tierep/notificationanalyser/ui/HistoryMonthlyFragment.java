package com.tierep.notificationanalyser.ui;

import android.content.Intent;

import com.tierep.notificationanalyser.NotificationAppView;
import com.tierep.notificationanalyser.NotificationDateView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by pieter on 25/10/14.
 */
public class HistoryMonthlyFragment extends HistoryFragment {
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");

    @Override
    protected void startAppDetailActivity(String appName, Date date) {
        Intent intent = new Intent(getActivity(), AppDetail.class);
        intent.putExtra(AppDetail.EXTRA_PACKAGENAME, appName);
        intent.putExtra(AppDetail.EXTRA_INTERVALTYPE, AppDetail.FLAG_VIEW_MONTHLY);
        intent.putExtra(AppDetail.EXTRA_DATESTRING, new SimpleDateFormat("yyyy-MM-dd").format(date));
        startActivity(intent);
    }

    @Override
    protected List<NotificationDateView> getChartData(int items) throws SQLException {
        List<NotificationDateView> notificationDateViews= this.getDatabaseHelper().getNotificationDao().getSummaryLastMonths(items);
        //add months without notifications to finalData list
        List<NotificationDateView> completeNotificationDateViews = new ArrayList<NotificationDateView>();
        for (int i = 0; i < notificationDateViews.size(); i++) {
            completeNotificationDateViews.add(completeNotificationDateViews.size(), notificationDateViews.get(i));
            if (i < notificationDateViews.size() - 1) {
                Calendar nextNotificationDate = Calendar.getInstance();
                nextNotificationDate.setTime(notificationDateViews.get(i + 1).Date);
                nextNotificationDate.set(Calendar.HOUR_OF_DAY, 0);
                nextNotificationDate.set(Calendar.MINUTE, 0);
                nextNotificationDate.set(Calendar.SECOND, 0);
                nextNotificationDate.set(Calendar.MILLISECOND, 0);

                Calendar nextCalendarDate = Calendar.getInstance();
                nextCalendarDate.setTime(notificationDateViews.get(i).Date);
                nextCalendarDate.add(Calendar.MONTH, 1);
                nextCalendarDate.set(Calendar.HOUR_OF_DAY, 0);
                nextCalendarDate.set(Calendar.MINUTE, 0);
                nextCalendarDate.set(Calendar.SECOND, 0);
                nextCalendarDate.set(Calendar.MILLISECOND, 0);

                while (nextCalendarDate.get(Calendar.MONTH) != nextNotificationDate.get(Calendar.MONTH)){
                    NotificationDateView emptyEntry = new NotificationDateView();
                    emptyEntry.Date = nextCalendarDate.getTime();
                    completeNotificationDateViews.add(completeNotificationDateViews.size(), emptyEntry);
                    nextCalendarDate.add(Calendar.MONTH, 1);
                }
            }
        }

        return completeNotificationDateViews;
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
