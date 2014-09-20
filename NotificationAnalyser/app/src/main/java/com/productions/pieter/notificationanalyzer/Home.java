package com.productions.pieter.notificationanalyzer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


public class Home extends Activity {
    HomePagerAdapter homePagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homePagerAdapter = new HomePagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(homePagerAdapter);

//        ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        ActionBar.Tab tabOverview = actionBar.newTab().setText("Overview");
//        ActionBar.Tab tabHistory = actionBar.newTab().setText("History");
//
//        tabOverview.setTabListener(new NavigationTabListener<OverviewFragment>(this, R.id.fragment_overview, OverviewFragment.class));
//        tabHistory.setTabListener(new NavigationTabListener<HistoryFragment>(this, R.id.fragment_history, HistoryFragment.class));
//
//        actionBar.addTab(tabOverview);
//        actionBar.addTab(tabHistory);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
