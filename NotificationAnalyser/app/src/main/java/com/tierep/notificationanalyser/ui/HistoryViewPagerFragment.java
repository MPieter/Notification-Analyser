package com.tierep.notificationanalyser.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tierep.notificationanalyser.R;

/**
 * Activity responsible for the view of the history. It displays history fragments for day, week and
 * month view with the use of an action bar.
 *
 * Created by pieter on 18/10/14.
 */
public class HistoryViewPagerFragment extends Fragment {
    private HistoryDailyFragment dailyFrag;
    private HistoryWeeklyFragment weeklyFrag;
    private HistoryMonthlyFragment monthlyFrag;

    public HistoryViewPagerFragment() {
        dailyFrag = new HistoryDailyFragment();
        weeklyFrag = new HistoryWeeklyFragment();
        monthlyFrag = new HistoryMonthlyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (actionBar.getTabCount() != 3) {
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                    //setCurrentItem(tab.getPosition());
                    int position = tab.getPosition();
                    if (position == 0) {
                        fragmentTransaction.replace(R.id.frame_layout, dailyFrag);
                    } else if (position == 1) {
                        fragmentTransaction.replace(R.id.frame_layout, weeklyFrag);
                    } else if (position == 2) {
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

            actionBar.removeAllTabs();
            actionBar.addTab(tabDaily);
            actionBar.addTab(tabWeekly);
            actionBar.addTab(tabMonthly);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_history_viewpager, container, false);
    }
}
