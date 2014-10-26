package com.tierep.notificationanalyser;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class DrawerActivity extends Activity implements NotificationAccessDialogFragment.NotificationAccessDialogFragmentListener {
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerItems;
    private ActionBarDrawerToggle drawerToggle;
    private String currentTitle;
    private int layoutId;

    public DrawerActivity(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        try {
            ActivityInfo ai = this.getPackageManager().getActivityInfo(this.getComponentName(), 0);
            String appLabel = ai.loadLabel(getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
                R.drawable.ic_navigation_drawer,
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

        //new DemoDataGenerator(this).Generate(true);
    }

    private void selectItem(int position) {
        Intent intent = null;
        if (position == 0) {
            if (this.getClass() != TodayActivity.class){
                intent = new Intent(this, TodayActivity.class);
            }
        } else if (position == 1) {
            if (this.getClass() != HistoryViewPagerActivity.class) {
                intent = new Intent(this, HistoryViewPagerActivity.class);
            }
        } else if (position == 2) {
            if (this.getClass() != AboutActivity.class) {
                intent = new Intent(this, AboutActivity.class);
            }
        }

        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
        if (intent != null) startActivity(intent);
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
