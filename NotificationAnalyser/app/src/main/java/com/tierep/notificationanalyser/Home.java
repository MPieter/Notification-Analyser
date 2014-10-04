package com.tierep.notificationanalyser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class Home extends Activity implements NotificationAccessDialogFragment.NotificationAccessDialogFragmentListener {
    HomePagerAdapter homePagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homePagerAdapter = new HomePagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(homePagerAdapter);
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
                ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                viewPager.setCurrentItem(tab.getPosition());
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
        ActionBar.Tab tabOverview = actionBar.newTab().setText(getResources().getString(R.string.title_fragment_today)).setTabListener(tabListener);
        ActionBar.Tab tabHistory = actionBar.newTab().setText(getResources().getString(R.string.title_fragment_history)).setTabListener(tabListener);

        actionBar.addTab(tabOverview);
        actionBar.addTab(tabHistory);

        if (NotificationListener.isNotificationAccessEnabled == false) {
            DialogFragment dialog = new NotificationAccessDialogFragment();
            dialog.show(getFragmentManager(), "notificationAccessDialog");
        }

        //new DemoDataGenerator(this).Generate(true);
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
        if (id == R.id.action_ignored_apps) {
            Intent intent = new Intent(this, Ignored_Apps.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Something went wrong. Enable manually", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing
    }
}
