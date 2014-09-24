package com.productions.pieter.notificationanalyzer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Comparator;
import java.util.List;

/**
 * Created by pieter on 17/09/14.
 */
public class NotificationAdapter extends ArrayAdapter<NotificationView> {
    private int totalCount = 0;

    public NotificationAdapter(Context context, List<NotificationView> objects) {
        super(context, 0, objects);
        this.sort(new Comparator<NotificationView>() {
            @Override
            public int compare(NotificationView notificationView, NotificationView notificationView2) {
                return notificationView2.Notifications.compareTo(notificationView.Notifications);
            }
        });
        this.insert(new NotificationView("", 0, 0), 0); // Insert extra element to make room for header
        for (int i = 0; i < objects.size(); i++) {
            totalCount += objects.get(i).Notifications;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (position == 0) {
            View view = inflator.inflate(R.layout.list_header, null);

            TextView titleCounter = (TextView) view.findViewById(R.id.title_counter);
            titleCounter.setText(Integer.toString(totalCount));
            return view;
        } else {
            View view = inflator.inflate(R.layout.list_element, null);
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView appCount = (TextView) view.findViewById(R.id.app_count);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.app_progress_bar);
            ImageView imageView = (ImageView) view.findViewById(R.id.app_image);

            PackageManager packageManager = view.getContext().getPackageManager();
            NotificationView nv = this.getItem(position);
            String str_appName = null;
            Drawable icon = null;
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(nv.AppName, 0);
                str_appName = packageManager.getApplicationLabel(appInfo).toString();
                icon = packageManager.getApplicationIcon(appInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (str_appName != null) appName.setText(str_appName);
            else appName.setText(nv.AppName);
            appCount.setText(Integer.toString(nv.Notifications));
            progressBar.setProgress((int) ((double) nv.Notifications / (double) nv.MaxNotifications * 100));
            if (icon != null) imageView.setImageDrawable(icon);

            return view;
        }
    }
}
