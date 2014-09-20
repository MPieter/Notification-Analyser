package com.productions.pieter.notificationanalyzer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by pieter on 20/09/14.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            Fragment fOverview = new OverviewFragment();
            return fOverview;
        } else if (i == 1) {
            Fragment fHistory = new HistoryFragment();
            return fHistory;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) { return "Overview"; }
        else if (position == 1) { return "History"; }
        else { return "Error"; }
        // TODO internationalize
    }
}
