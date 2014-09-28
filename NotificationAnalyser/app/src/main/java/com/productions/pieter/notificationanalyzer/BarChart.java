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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private Date currentSelectedDate = null;

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
        this.paintBarZebra1.setColor(getResources().getColor(android.R.color.holo_blue_light));
        this.paintBarZebra2.setColor(getResources().getColor(android.R.color.holo_green_dark));
        this.paintBarSelected.setColor(getResources().getColor(android.R.color.white));

        if (this.isInEditMode()) {
            maxNotifications = 25;
            this.bars = new LinkedList<Bar>();
            bars.add(new Bar(null, new NotificationDayView(new Date(), 25)));
            bars.add(new Bar(null, new NotificationDayView(new Date(), 15)));
            bars.add(new Bar(null, new NotificationDayView(new Date(), 25)));
            bars.add(new Bar(null, new NotificationDayView(new Date(), 18)));
            bars.add(new Bar(null, new NotificationDayView(new Date(), 25)));
        } else {
            this.update();
        }
    }

    /**
     * Method for updating the chart. Indicates to the chart that the underlying data has changed
     * and thus that the data has to processed.
     */
    public void update() {
        // TODO er moet getest worden dat de chart wel degelijk geupdate wordt bij een resume life cycle event
        try {
            this.bars = new LinkedList<Bar>();
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            List<NotificationDayView> list = dao.getSummaryLastDays();
            for (NotificationDayView nf : list) {
                maxNotifications = nf.Notifications > maxNotifications ? nf.Notifications : maxNotifications;
            }

            for (int i = 0; i < list.size(); i++) {
                NotificationDayView nf = list.get(i);

                if (currentSelectedDate != null) {
                    Calendar date1 = new GregorianCalendar();
                    date1.setTime(nf.Date);
                    Calendar date2 = new GregorianCalendar();
                    date2.setTime(this.currentSelectedDate);
                    bars.add(new Bar(null, nf, date1.get(Calendar.DATE) == date2.get(Calendar.DATE)));
                } else {
                    bars.add(new Bar(null, nf));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    /**
     * Gets the first date that is displayed on the chart.
     * @return
     */
    public Date getFirstDate() {
        if (bars.size() > 0) {
            return bars.get(0).ntf.Date;
        } else {
            return null;
        }
    }

    /**
     * Gets the last date that is displayed on the chart.
     * @return
     */
    public Date getLastDate() {
        if (bars.size() > 0) {
            return bars.get(bars.size() - 1).ntf.Date;
        } else {
            return null;
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
                    barChartListener.onBarClick(bars.get(i).ntf.Date, i);
                    this.currentSelectedDate = bars.get(i).ntf.Date;
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
            this(rect, ntf, false);
        }

        public Bar(Rect rect, NotificationDayView ntf, Boolean isActive) {
            this.rect = rect;
            this.ntf = ntf;
            this.isActive = isActive;
        }
    }

    public BarChartListener getBarChartListener() {
        return barChartListener;
    }

    public void setBarChartListener(BarChartListener barChartListener) {
        this.barChartListener = barChartListener;
    }
}
