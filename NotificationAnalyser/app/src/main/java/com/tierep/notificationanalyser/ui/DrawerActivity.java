package com.tierep.notificationanalyser.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tierep.notificationanalyser.NotificationListener;
import com.tierep.notificationanalyser.R;

public class DrawerActivity extends Activity implements NotificationAccessDialogFragment.NotificationAccessDialogFragmentListener {
    private static final String STATE_ITEM_SELECT = "selectedItem";

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerItems;
    private ActionBarDrawerToggle drawerToggle;
    private String currentTitle;
    private int selectedItem;

    private TodayFragment todayFragment;
    private HistoryViewPagerFragment historyFragment;
    private AboutFragment aboutFragment;

    public DrawerActivity() {
        todayFragment = new TodayFragment();
        historyFragment = new HistoryViewPagerFragment();
        aboutFragment = new AboutFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        currentTitle = getTitle().toString();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerItems = getResources().getStringArray(R.array.navigation_drawer_list);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.navigation_drawer_list_item, drawerItems));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectItem(i);
            }
        });
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(currentTitle);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        if (!NotificationListener.isNotificationAccessEnabled) {
            DialogFragment dialog = new NotificationAccessDialogFragment();
            dialog.show(getFragmentManager(), "notificationAccessDialog");
        }
        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(STATE_ITEM_SELECT);
        } else {
            selectedItem = 0;
        }
        selectItem(selectedItem);
        //new DemoDataGenerator(this).Generate(true);
    }

    private void selectItem(int position) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (position == 0) {
            ft.replace(R.id.frame_layout, todayFragment);
            currentTitle = getResources().getString(R.string.title_fragment_today);
        } else if (position == 1) {
            ft.replace(R.id.frame_layout, historyFragment);
            currentTitle = getResources().getString(R.string.title_fragment_history);
        } else if (position == 2) {
            ft.replace(R.id.frame_layout, aboutFragment);
            currentTitle = getResources().getString(R.string.title_activity_about);
        }
        selectedItem = position;
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getActionBar().setTitle(currentTitle);
        }

        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
        ft.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_ignored_apps) {
            Intent intent = new Intent(this, IgnoredApps.class);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_ITEM_SELECT, selectedItem);

        super.onSaveInstanceState(outState);
    }
}
