package com.tierep.notificationanalyser.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.tierep.notificationanalyser.R;
import com.tierep.notificationanalyser.models.Application;
import com.tierep.notificationanalyser.models.ApplicationDao;
import com.tierep.notificationanalyser.models.DatabaseHelper;

import java.sql.SQLException;


public class AppDetail extends Activity {
    private DatabaseHelper databaseHelper = null;
    private String packageName = null;

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

        ImageView appImage = (ImageView) findViewById(R.id.app_detail_image);
        TextView appName = (TextView) findViewById(R.id.app_detail_name);

        packageName = getIntent().getStringExtra(Intent.EXTRA_SUBJECT);
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
