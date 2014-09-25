package com.productions.pieter.notificationanalyzer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

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
        List<NotificationAppView> list = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            list = dao.getOverviewLast24Hours();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int totalCount = 0;
        for (int i = 0; i < list.size(); i++) {
            totalCount += list.get(i).Notifications;
        }
        View viewHeader = inflater.inflate(R.layout.list_header, null);

        TextView titleCounter = (TextView) viewHeader.findViewById(R.id.title_counter);
        titleCounter.setText(Integer.toString(totalCount));

        NotificationAdapter adapter = new NotificationAdapter(view.getContext(), list);

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.addHeaderView(viewHeader);
        listView.setAdapter(adapter);

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
