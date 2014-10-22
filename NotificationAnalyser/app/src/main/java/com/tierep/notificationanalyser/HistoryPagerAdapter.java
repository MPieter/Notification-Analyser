package com.tierep.notificationanalyser;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

/**
 * Created by pieter on 20/09/14.
 */
public class HistoryPagerAdapter extends FragmentStatePagerAdapter {
    Fragment fDaily = null;
    Fragment fWeekly = null;
    Fragment fMonthly = null;

    public HistoryPagerAdapter(FragmentManager fm) {
        super(fm);
        fDaily = new HistoryFragment();
        fWeekly = new HistoryFragment();
        fMonthly = new HistoryFragment();
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fDaily;
        } else if (i == 1) {
            return fWeekly;
        } else if (i == 2) {
            return fMonthly;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
