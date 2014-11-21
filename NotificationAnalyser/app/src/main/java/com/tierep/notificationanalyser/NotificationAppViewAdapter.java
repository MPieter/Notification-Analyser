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
 * An adapter for displaying NotificationAppView objects in a ListView.
 *
 * Created by pieter on 17/09/14.
 */
public class NotificationAppViewAdapter extends ArrayAdapter<NotificationAppView> {
    public NotificationAppViewAdapter(Context context, List<NotificationAppView> objects) {
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

        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_element, null);
            holder = new ViewHolder();
            holder.appName = (TextView) view.findViewById(R.id.app_name);
            holder.appCount = (TextView) view.findViewById(R.id.app_count);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.app_progress_bar);
            holder.imageView = (ImageView) view.findViewById(R.id.app_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PackageManager packageManager = getContext().getPackageManager();
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

        if (str_appName != null) holder.appName.setText(str_appName);
        else holder.appName.setText(nv.AppName);
        holder.appCount.setText(Integer.toString(nv.Notifications));
        holder.progressBar.setProgress((int) ((double) nv.Notifications / (double) nv.MaxNotifications * 100));
        if (icon != null) holder.imageView.setImageDrawable(icon);

        return view;
    }

    private static class ViewHolder {
        TextView appName;
        TextView appCount;
        ProgressBar progressBar;
        ImageView imageView;
    }
}
