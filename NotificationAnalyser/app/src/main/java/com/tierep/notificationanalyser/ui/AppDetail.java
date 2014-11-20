package com.tierep.notificationanalyser.ui;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.tierep.notificationanalyser.NotificationItemAdapter;
import com.tierep.notificationanalyser.R;
import com.tierep.notificationanalyser.models.Application;
import com.tierep.notificationanalyser.models.ApplicationDao;
import com.tierep.notificationanalyser.models.DatabaseHelper;
import com.tierep.notificationanalyser.models.NotificationItem;
import com.tierep.notificationanalyser.models.NotificationItemDao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This activity needs an intent that provides information about which application items should be
 * displayed, for which time period (day, week or month) and for which date.
 */
public class AppDetail extends Activity {
    public static final String EXTRA_PACKAGENAME = "com.tierep.notificationanalyser.ui.EXTRA_PACKAGENAME";
    public static final String EXTRA_INTERVALTYPE = "com.tierep.notificationanalyser.ui.EXTRA_INTERVALTYPE";
    public static final String EXTRA_DATESTRING = "com.tierep.notificationanalyser.ui.EXTRA_DATESTRING";

    public static final int FLAG_VIEW_DAILY = 0;
    public static final int FLAG_VIEW_WEEKLY = 1;
    public static final int FLAG_VIEW_MONTHLY = 2;

    private DatabaseHelper databaseHelper = null;
    private String packageName = null;
    private int intervalType;
    private Date date;

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        parseAdditionalData();

        ListView list = (ListView) findViewById(R.id.list_notification_items);
        list.addHeaderView(makeListHeader(), null, false);
        list.setAdapter(new NotificationItemAdapter(this, makeListObjects()));
    }

    private void parseAdditionalData() {
        packageName = getIntent().getStringExtra(AppDetail.EXTRA_PACKAGENAME);
        intervalType= getIntent().getIntExtra(AppDetail.EXTRA_INTERVALTYPE, 0);
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(getIntent().getStringExtra(AppDetail.EXTRA_DATESTRING));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private View makeListHeader() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View header = inflater.inflate(R.layout.list_app_detail_he, null);
        ImageView appImage = (ImageView) header.findViewById(R.id.app_detail_image);
        TextView appName = (TextView) header.findViewById(R.id.app_detail_name);

        String strAppName = null;
        Drawable icon = null;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, 0);
            strAppName = getPackageManager().getApplicationLabel(info).toString();
            icon = getPackageManager().getApplicationIcon(info);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        appName.setText(strAppName != null ? strAppName : packageName);
        if (icon != null) appImage.setImageDrawable(icon);

        return header;
    }

    private List<NotificationItem> makeListObjects() {
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            List<NotificationItem> objects;
            if (intervalType == 0) {
                objects = dao.getOverviewAppDay(date, packageName);
            } else if (intervalType == 1) {
                objects = dao.getOverviewAppWeek(date, packageName);
            } else {
                objects = dao.getOverviewAppMonth(date, packageName);
            }
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onCheckboxClicked(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();

        try {
            ApplicationDao dao = getDatabaseHelper().getApplicationDao();
            Application application = dao.queryForId(packageName);
            application.setIgnore(isChecked);
            dao.update(application);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
