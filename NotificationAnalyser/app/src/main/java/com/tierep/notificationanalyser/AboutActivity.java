package com.tierep.notificationanalyser;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;


public class AboutActivity extends DrawerActivity {

    public AboutActivity() {
        super(R.layout.activity_about);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView about_version = (TextView) findViewById(R.id.about_version);
        if (about_version.isInEditMode()) {
            about_version.setText("Version 1.0");
        } else {
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                about_version.setText("Version " + pInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
