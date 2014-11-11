package com.tierep.notificationanalyser;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
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


public abstract class HistoryFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;
    private Date currentSelectedDate = null;
    private int selectedXIndex;
    private int selectedDataSetIndex;
    private BarChart chart;
    protected Paint paintWhite = new Paint();

    public HistoryFragment() {
        this.paintWhite.setColor(Color.WHITE);
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

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

        chart = new BarChart(getActivity());
        chart.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, ((int) getResources().getDimension(R.dimen.bar_chart_height))));
        chart.setDrawBarShadow(false);
        chart.setDrawLegend(false);
        chart.setDescription("");
        chart.setDrawGridBackground(false);
        chart.setDrawHorizontalGrid(false);
        chart.setDrawVerticalGrid(false);
        chart.setDrawXLabels(true);
        chart.setDrawYLabels(false);
        chart.getXLabels().setCenterXLabelText(true);
        chart.setValueTextColor(Color.WHITE);
        chart.setPinchZoom(false);
        chart.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Integer.toString((int) value);
            }
        });
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex) {
                showDayListView((Date) e.getData());
                selectedXIndex = e.getXIndex();
                selectedDataSetIndex = dataSetIndex;
            }

            @Override
            public void onNothingSelected() {

            }
        });
        listHistory.addHeaderView(chart, null, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ListView listHistory = (ListView) this.getActivity().findViewById(R.id.list_view_history);
        try {
            List<NotificationDateView> rawData = this.getChartData(14);
            ArrayList<String> xVals = new ArrayList<String>(rawData.size());
            ArrayList<BarEntry> yVals = new ArrayList<BarEntry>(rawData.size());
            for (int i = 0; i < rawData.size(); i++) {
                Date currentDate = rawData.get(i).Date;
                xVals.add(i, getDateFormat().format(currentDate));
                yVals.add(i, new BarEntry(rawData.get(i).Notifications.floatValue(), i, currentDate));
            }
            BarDataSet dataSet = new BarDataSet(yVals, "test");
            BarData data = new BarData(xVals, dataSet);
            chart.setData(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (currentSelectedDate != null) {
            showDayListView(currentSelectedDate);
            chart.highlightValue(selectedXIndex, selectedDataSetIndex);
        } else {
            listHistory.setAdapter(new NotificationAdapter(this.getActivity(), new LinkedList<NotificationAppView>()));
        }
        try {
            ListView listView = (ListView) getActivity().findViewById(R.id.list_view_history);
            TextView textView = (TextView) getActivity().findViewById(R.id.history_empty);
            if (getDatabaseHelper().getApplicationDao().queryForEq(Application.FIELD_IGNORE, false).size() > 0) {
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract List<NotificationDateView> getChartData(int items) throws SQLException;

    protected abstract List<NotificationAppView> getListViewDate(Date date) throws SQLException;

    /**
     * Gets the SimpleDateFormat used for formatting the labels on the chart.
     * @return the correct SimpleDateFormat.
     */
    protected abstract SimpleDateFormat getDateFormat();

    protected void showDayListView(Date date) {
        this.currentSelectedDate = date;
        ListView listView = (ListView) getActivity().findViewById(R.id.list_view_history);
        try {
            List<NotificationAppView> objects = this.getListViewDate(date);
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
