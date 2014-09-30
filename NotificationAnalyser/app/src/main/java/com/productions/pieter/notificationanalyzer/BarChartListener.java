package com.productions.pieter.notificationanalyzer;

import java.util.Date;

/**
 * Created by pieter on 25/09/14.
 */
public interface BarChartListener {
    public void onBarClick(Date date, int position);
    public void onIntervalChanged(Date first, Date end);
}
