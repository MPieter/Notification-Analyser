package com.tierep.notificationanalyser;

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
public class NotificationAdapter extends ArrayAdapter<NotificationAppView> {
    public NotificationAdapter(Context context, List<NotificationAppView> objects) {
        super(context, 0, objects);
        this.sort(new Comparator<NotificationAppView>() {
            @Override
            public int compare(NotificationAppView notificationAppView, NotificationAppView notificationAppView2) {
                return notificationAppView2.Notifications.compareTo(notificationAppView.Notifications);
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_element, null);
        TextView appName = (TextView) view.findViewById(R.id.app_name);
        TextView appCount = (TextView) view.findViewById(R.id.app_count);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.app_progress_bar);
        ImageView imageView = (ImageView) view.findViewById(R.id.app_image);

        PackageManager packageManager = view.getContext().getPackageManager();
        NotificationAppView nv = this.getItem(position);
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
