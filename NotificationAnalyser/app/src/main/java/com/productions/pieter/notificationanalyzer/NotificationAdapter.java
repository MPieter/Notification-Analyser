package com.productions.pieter.notificationanalyzer;

import android.content.Context;
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
    public NotificationAdapter(Context context, List<NotificationView> objects) {
        super(context, 0, objects);
        this.sort(new Comparator<NotificationView>() {
            @Override
            public int compare(NotificationView notificationView, NotificationView notificationView2) {
                return notificationView2.Notifications.compareTo(notificationView.Notifications);
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.list_element, null);
        TextView appName = (TextView) view.findViewById(R.id.app_name);
        TextView appCount = (TextView) view.findViewById(R.id.app_count);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.app_progress_bar);
        ImageView imageView = (ImageView) view.findViewById(R.id.app_image);

        NotificationView nv = this.getItem(position);
        appName.setText(nv.AppName);
        appCount.setText(Integer.toString(nv.Notifications));
        progressBar.setProgress((int) ((double)nv.Notifications / (double)nv.MaxNotifications * 100));
        try {
            Drawable icon = view.getContext().getPackageManager().getApplicationIcon(nv.AppName);
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return view;
    }
}
