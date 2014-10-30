package com.tierep.notificationanalyser;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by pieter on 18/10/14.
 */
public class HistoryViewPagerActivity extends DrawerActivity {
    HistoryDailyFragment dailyFrag;
    HistoryWeeklyFragment weeklyFrag;
    HistoryMonthlyFragment monthlyFrag;

    public HistoryViewPagerActivity() {
        super(R.layout.viewpager_history);
        dailyFrag = new HistoryDailyFragment();
        weeklyFrag = new HistoryWeeklyFragment();
        monthlyFrag = new HistoryMonthlyFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                //setCurrentItem(tab.getPosition());
                int position = tab.getPosition();
                if (position == 0) {
                    fragmentTransaction.replace(R.id.frame_layout, dailyFrag);
                } else if(position == 1) {
                    fragmentTransaction.replace(R.id.frame_layout, weeklyFrag);
                } else if(position == 2) {
                    fragmentTransaction.replace(R.id.frame_layout, monthlyFrag);
                }
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                // No special actions.
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                // User selected the already selected tab. In this case do nothing.
            }
        };
        ActionBar.Tab tabDaily = actionBar.newTab().setText(R.string.history_daily).setTabListener(tabListener);
        ActionBar.Tab tabWeekly = actionBar.newTab().setText(R.string.history_weekly).setTabListener(tabListener);
        ActionBar.Tab tabMonthly = actionBar.newTab().setText(R.string.history_monthly).setTabListener(tabListener);

        actionBar.addTab(tabDaily);
        actionBar.addTab(tabWeekly);
        actionBar.addTab(tabMonthly);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
