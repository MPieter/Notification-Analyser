package com.productions.pieter.notificationanalyzer;

import android.app.Activity;
import android.os.Bundle;


public class Ignored_Apps extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ignored_apps);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
