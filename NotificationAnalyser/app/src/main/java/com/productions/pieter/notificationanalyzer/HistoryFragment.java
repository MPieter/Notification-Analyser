package com.productions.pieter.notificationanalyzer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.productions.pieter.notificationanalyzer.Models.DatabaseHelper;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;
    private Date currentSelectedDate = null;
    private BarChart barChart = null;

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
        barChart = (BarChart) viewListHeader.findViewById(R.id.bar_chart);
        barChart.setBarChartListener(new BarChartListener() {
            @Override
            public void onBarClick(Date date) {
                currentSelectedDate = date;
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
        listHistory.addHeaderView(viewListHeader, null, false);
        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), AppDetail.class);
                NotificationAppView clickedApp = (NotificationAppView) adapterView.getAdapter().getItem(i);
                intent.putExtra(Intent.EXTRA_SUBJECT, clickedApp.AppName);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ListView listHistory = (ListView) this.getActivity().findViewById(R.id.list_view_history);
        if (currentSelectedDate != null) {
            Calendar calSelected = new GregorianCalendar();
            calSelected.setTime(currentSelectedDate);
            Calendar calToday = Calendar.getInstance();
            if (calSelected.get(Calendar.DATE) == calToday.get(Calendar.DATE)) {
                try {
                    List<NotificationAppView> objects = getDatabaseHelper().getNotificationDao().getOverviewDay(currentSelectedDate);
                    listHistory.setAdapter(new NotificationAdapter(this.getActivity(), objects));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            listHistory.setAdapter(new NotificationAdapter(this.getActivity(), new LinkedList<NotificationAppView>()));
        }
        barChart.update();
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
