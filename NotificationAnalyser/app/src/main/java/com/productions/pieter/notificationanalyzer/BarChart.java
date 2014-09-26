package com.productions.pieter.notificationanalyzer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.productions.pieter.notificationanalyzer.Models.DatabaseHelper;
import com.productions.pieter.notificationanalyzer.Models.NotificationItemDao;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A View responsible for drawing the notification history of the last days on a canvas
 * in the form of a bar chart.
 *
 * Created by pieter on 24/09/14.
 */
public class BarChart extends View {
    private DatabaseHelper databaseHelper = null;
    private List<Bar> bars;
    private int maxNotifications = 0;
    private Paint paintBarZebra1 = new Paint();
    private Paint paintBarZebra2 = new Paint();
    private Paint paintBarSelected = new Paint();
    private BarChartListener barChartListener;

    public BarChart(Context context) {
        super(context);
        initBarChart();
    }

    /**
     * Constructor that allows the use of this view in XML.
     *
     * @param context
     * @param attrs
     */
    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBarChart();
    }

    /**
     * Constructor that allows the use of this view in XML.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBarChart();
    }

    private void initBarChart() {
        this.bars = new LinkedList<Bar>();
        this.paintBarZebra1.setColor(getResources().getColor(android.R.color.holo_blue_light));
        this.paintBarZebra2.setColor(getResources().getColor(android.R.color.holo_green_dark));
        this.paintBarSelected.setColor(getResources().getColor(android.R.color.white));

        if (this.isInEditMode()) {
            maxNotifications = 25;
            bars.add(new Bar(null, new NotificationDayView(new Date(), 10)));
            bars.add(new Bar(null, new NotificationDayView(new Date(), 15)));
            bars.add(new Bar(null, new NotificationDayView(new Date(), 25)));
            bars.add(new Bar(null, new NotificationDayView(new Date(), 18)));
            bars.add(new Bar(null, new NotificationDayView(new Date(), 7)));
        } else {
            try {
                NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
                List<NotificationDayView> list = dao.getSummaryLastDays();
                for (NotificationDayView nf : list) {
                    maxNotifications = nf.Notifications > maxNotifications ? nf.Notifications : maxNotifications;
                }

                for (int i = 0; i < list.size(); i++) {
                    NotificationDayView nf = list.get(i);

                    bars.add(new Bar(null, nf));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this.getContext(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getResources().getDimensionPixelSize(R.dimen.bar_chart_height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int bar_width = (int) getResources().getDimension(R.dimen.bar_chart_width_bar);
        for (int i = 0; i < bars.size(); i++) {
            if (bars.get(i).rect == null) {
                Bar bar = bars.get(i);
                int height = (int) ((float) bar.ntf.Notifications / (float) maxNotifications * canvas.getHeight());
                bars.get(i).rect = new Rect(bar_width * i, canvas.getHeight() - height, bar_width * (i + 1), canvas.getHeight());
            }
            canvas.drawRect(bars.get(i).rect, bars.get(i).isActive ?
                    paintBarSelected : i % 2 == 0 ? paintBarZebra1 : paintBarZebra2);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            // Set active state to touched
            for (int i = 0; i < bars.size(); i++) {
                int x = Math.round(event.getX());
                if (bars.get(i).rect.left <= x && x < bars.get(i).rect.right) {
                    // Empty active states
                    for (int j = 0; j < bars.size(); j++) {
                        bars.get(j).isActive = false;
                    }

                    bars.get(i).isActive = true;
                    barChartListener.onBarClick(bars.get(i).ntf.Date);
                    break;
                }
            }
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private class Bar {
        public Rect rect;
        public NotificationDayView ntf;
        public Boolean isActive;

        public Bar(Rect rect, NotificationDayView ntf) {
            this.rect = rect;
            this.ntf = ntf;
            this.isActive = false;
        }
    }

    public BarChartListener getBarChartListener() {
        return barChartListener;
    }

    public void setBarChartListener(BarChartListener barChartListener) {
        this.barChartListener = barChartListener;
    }
}
