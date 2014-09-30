package com.productions.pieter.notificationanalyzer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.productions.pieter.notificationanalyzer.Models.DatabaseHelper;
import com.productions.pieter.notificationanalyzer.Models.NotificationItemDao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class OverviewFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        View viewHeader = inflater.inflate(R.layout.list_header_day_count, null);

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.addHeaderView(viewHeader, null, false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        List<NotificationAppView> list = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            list = dao.getOverviewToday();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int totalCount = 0;
        for (int i = 0; i < list.size(); i++) {
            totalCount += list.get(i).Notifications;
        }

        TextView titleCounter = (TextView) this.getActivity().findViewById(R.id.title_counter);
        titleCounter.setText(Integer.toString(totalCount));

        NotificationAdapter adapter = new NotificationAdapter(this.getActivity(), list);

        ListView listView = (ListView) this.getActivity().findViewById(R.id.list_view);
        listView.setAdapter(adapter);
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
