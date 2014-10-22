package com.tierep.notificationanalyser;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.tierep.notificationanalyser.Models.Application;
import com.tierep.notificationanalyser.Models.DatabaseHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;
    private Date currentSelectedDate = null;
    private int currentSelectedBarPosition = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM");
    private View headerDayCount = null;
    private int layoutId;

    public HistoryFragment() {
        this.layoutId = R.layout.fragment_history;
    }


    public HistoryFragment(int layoutId) {
        this.layoutId = layoutId;
    }

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
        View view = inflater.inflate(layoutId, container, false);

        ListView listHistory = (ListView) view.findViewById(R.id.list_view_history);
        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), AppDetail.class);
                NotificationAppView clickedApp = (NotificationAppView) adapterView.getAdapter().getItem(i);
                intent.putExtra(Intent.EXTRA_SUBJECT, clickedApp.AppName);
                startActivity(intent);
            }
        });

        com.github.mikephil.charting.charts.BarChart chart = (com.github.mikephil.charting.charts.BarChart) inflater.inflate(R.layout.list_header_barchart, null);
        chart.setDrawBarShadow(false);
        chart.setDrawLegend(false);
        chart.setDescription("");
        chart.setDrawGridBackground(false);
        chart.setDrawHorizontalGrid(false);
        chart.setDrawVerticalGrid(false);
        chart.setDrawXLabels(true);
        chart.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Integer.toString((int)value);
            }
        });
        chart.setValueTextColor(Color.WHITE);
        try {
            List<NotificationDayView> rawData = getDatabaseHelper().getNotificationDao().getSummaryLastDays(14);
            ArrayList<String> xVals = new ArrayList<String>(rawData.size());
            ArrayList<BarEntry> yVals = new ArrayList<BarEntry>(rawData.size());
            for (int i = 0; i < rawData.size(); i++) {
                xVals.add(i, dateFormat.format(rawData.get(i).Date));
                yVals.add(i, new BarEntry(rawData.get(i).Notifications.floatValue(), i));
            }
            BarDataSet dataSet = new BarDataSet(yVals, "test");
            BarData data = new BarData(xVals, dataSet);
            chart.setData(data);
            listHistory.addHeaderView(chart, null, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ListView listHistory = (ListView) this.getActivity().findViewById(R.id.list_view_history);
        if (currentSelectedDate != null) {
            showDayListView(currentSelectedDate);
        } else {
            listHistory.setAdapter(new NotificationAdapter(this.getActivity(), new LinkedList<NotificationAppView>()));
        }
        try {
//            ListView listView = (ListView) getActivity().findViewById(R.id.list_view_history);
            TextView textView = (TextView) getActivity().findViewById(R.id.history_empty);
            if (getDatabaseHelper().getApplicationDao().queryForEq(Application.FIELD_IGNORE, false).size() > 0) {
//                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            } else {
//                listView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showDayListView(Date date) {
        ListView listView = (ListView) getActivity().findViewById(R.id.list_view_history);
        try {
            List<NotificationAppView> objects = getDatabaseHelper().getNotificationDao().getOverviewDay(date);
            listView.setAdapter(new NotificationAdapter(getActivity(), objects));
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
