package com.productions.pieter.notificationanalyzer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this.getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        View viewListHeader = inflater.inflate(R.layout.list_history_header, null);
        BarChart barChart = (BarChart) viewListHeader.findViewById(R.id.bar_chart);
        barChart.setBarChartListener(new BarChartListener() {
            @Override
            public void onBarClick(Date date) {
                ListView listView = (ListView) getActivity().findViewById(R.id.list_view_history);
                try {
                    List<NotificationAppView> objects = getDatabaseHelper().getNotificationDao().getOverviewDay(date);
                    listView.setAdapter(new NotificationAdapter(getActivity(), objects));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        ListView listHistory = (ListView) view.findViewById(R.id.list_view_history);
        listHistory.addHeaderView(viewListHeader);
        listHistory.setAdapter(new NotificationAdapter(view.getContext(), new LinkedList<NotificationAppView>()));
        return view;
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
