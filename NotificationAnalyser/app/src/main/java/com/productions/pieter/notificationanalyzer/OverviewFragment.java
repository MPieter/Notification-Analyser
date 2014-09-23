package com.productions.pieter.notificationanalyzer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

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
        int totalCount = 0;
        int maxCount = 0;
        List<NotificationView> list = new LinkedList<NotificationView>();
        try {
            // TODO enkel laatste 24u selecteren
            Dao<NotificationItem, Integer> dao = getDatabaseHelper().getNotificationDao();
            GenericRawResults<String[]> rawResults = dao.queryRaw(
                    "SELECT " + NotificationItem.FIELD_APPNAME
                            + ", COUNT(*) FROM " + NotificationItem.FIELD_TABLE_NAME
                            + " GROUP BY " + NotificationItem.FIELD_APPNAME);
            List<String[]> results = rawResults.getResults();

            for (int i = 0; i < results.size(); i++) {
                int ntfCount = Integer.parseInt(results.get(i)[1]);
                totalCount += ntfCount;
                maxCount = ntfCount > maxCount ? ntfCount : maxCount;
            }
            for (int i = 0; i < results.size(); i++) {
                list.add(new NotificationView(results.get(i)[0], Integer.parseInt(results.get(i)[1]), maxCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NotificationAdapter adapter = new NotificationAdapter(view.getContext(), list);

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        TextView titleCounter = (TextView) view.findViewById(R.id.title_counter);
        titleCounter.setText(Integer.toString(totalCount));

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
