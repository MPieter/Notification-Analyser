package com.tierep.notificationanalyser.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.tierep.notificationanalyser.NotificationAdapter;
import com.tierep.notificationanalyser.NotificationAppView;
import com.tierep.notificationanalyser.R;
import com.tierep.notificationanalyser.models.DatabaseHelper;
import com.tierep.notificationanalyser.models.NotificationItemDao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class TodayFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View main = inflater.inflate(R.layout.fragment_today, null);
        View viewHeader = inflater.inflate(R.layout.list_header_day_count, null);

        ListView listView = (ListView) main.findViewById(R.id.list_view);
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

        return main;
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
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
        for (NotificationAppView aList : list) {
            totalCount += aList.Notifications;
        }

        TextView titleCounter = (TextView) getActivity().findViewById(R.id.title_counter);
        titleCounter.setText(Integer.toString(totalCount));
        TextView titleCounterSuffix = (TextView) getActivity().findViewById(R.id.title_counter_suffix);
        if (totalCount == 1) {
            titleCounterSuffix.setText(R.string.title_counter_suffix_single);
        } else {
            titleCounterSuffix.setText(R.string.title_counter_suffix_plural);
        }

        NotificationAdapter adapter = new NotificationAdapter(getActivity(), list);

        ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
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
