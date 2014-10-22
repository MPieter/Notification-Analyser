package com.tierep.notificationanalyser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.tierep.notificationanalyser.Models.DatabaseHelper;
import com.tierep.notificationanalyser.Models.NotificationItemDao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class TodayActivity extends DrawerActivity {
    private DatabaseHelper databaseHelper = null;

    public TodayActivity() {
        super(R.layout.activity_today);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View viewHeader = getLayoutInflater().inflate(R.layout.list_header_day_count, null);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.addHeaderView(viewHeader, null, false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TodayActivity.this, AppDetail.class);
                NotificationAppView clickedApp = (NotificationAppView) adapterView.getAdapter().getItem(i);
                intent.putExtra(Intent.EXTRA_SUBJECT, clickedApp.AppName);
                startActivity(intent);
            }
        });
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
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
        for (int i = 0; i < list.size(); i++) {
            totalCount += list.get(i).Notifications;
        }

        TextView titleCounter = (TextView) findViewById(R.id.title_counter);
        titleCounter.setText(Integer.toString(totalCount));
        TextView titleCounterSuffix = (TextView) findViewById(R.id.title_counter_suffix);
        if (totalCount == 1) {
            titleCounterSuffix.setText(R.string.title_counter_suffix_single);
        } else {
            titleCounterSuffix.setText(R.string.title_counter_suffix_plural);
        }

        NotificationAdapter adapter = new NotificationAdapter(this, list);

        ListView listView = (ListView) findViewById(R.id.list_view);
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
