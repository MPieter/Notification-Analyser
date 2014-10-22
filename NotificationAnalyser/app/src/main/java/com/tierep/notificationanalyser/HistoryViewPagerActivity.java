package com.tierep.notificationanalyser;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * Created by pieter on 18/10/14.
 */
public class HistoryViewPagerActivity extends DrawerActivity {
    private HistoryPagerAdapter historyPagerAdapter;
    private ViewPager mViewPager;

    public HistoryViewPagerActivity() {
        super(R.layout.viewpager_history);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        historyPagerAdapter = new HistoryPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(historyPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                getActionBar().setSelectedNavigationItem(position);
            }
        });

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                setCurrentItem(tab.getPosition());
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

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
    }
}
