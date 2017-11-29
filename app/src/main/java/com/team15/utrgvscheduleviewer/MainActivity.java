package com.team15.utrgvscheduleviewer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    final ArrayList<WeekViewEvent> ll  = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference for the week view in the layout.
       final WeekView mWeekView = findViewById(R.id.weekView);


        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(new EventClickListenerImpl());

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(new MonthChangeListenerImpl());

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(new EventLongPressListenerImpl());

      //  mWeekView.setNumberOfVisibleDays(1);

        new AsyncTask<String, Integer, String[]>() {
            @Override
            protected String[] doInBackground(String... strings) {
                try {
                    for(ClassTimeSlot t : AssistScraper.getClassTimeSlots(MainActivity.this.getIntent().getStringExtra("user"), MainActivity.this.getIntent().getStringExtra("password")))
                    {
                        ll.add(t.toWeekViewEvent());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return new String[0];
            }

            @Override
            protected void onPostExecute(String[] strings) {
                mWeekView.notifyDatasetChanged();
            }
        }.execute();


    }

    class EventClickListenerImpl implements WeekView.EventClickListener {
        @Override
        public void onEventClick(WeekViewEvent wevent, RectF eventRect) {

            Toast.makeText(getApplicationContext(), "Class Starts At: " +  wevent.getStartTime().getTime().toString(), Toast.LENGTH_SHORT).show();

        }
    }

    /** Returns Calendar Base URI, supports both new and old OS. */
    private String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") : Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
    }
    public static Calendar getFirstDay(int i2, int i, int weekday) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, i2);
        c.set(Calendar.YEAR, i);
        c.set(Calendar.DAY_OF_MONTH, 1);
        int day = c.get(Calendar.DAY_OF_WEEK);
        while (day != weekday) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            day = c.get(Calendar.DAY_OF_WEEK);
        }
        return c;
    }

    class MonthChangeListenerImpl implements MonthLoader.MonthChangeListener {

        @Override
        public List<WeekViewEvent> onMonthChange(int i, int i2) {
            List<WeekViewEvent> newEvents= new ArrayList<WeekViewEvent>();
            List<WeekViewEvent> events = MainActivity.this.ll;
            for (WeekViewEvent event : events) {
                Calendar dateTime = event .getStartTime();
                Calendar dateEndTime = event .getEndTime();
                Calendar monCal = getFirstDay(i2 - 1, i, dateTime.get(Calendar.DAY_OF_WEEK));
                int hday = dateTime.get(Calendar.HOUR_OF_DAY);
                int mday = dateTime.get(Calendar.MINUTE);
                int ehday = dateEndTime.get(Calendar.HOUR_OF_DAY);
                int emday = dateEndTime.get(Calendar.MINUTE);
                for (int k = monCal.get(Calendar.DAY_OF_MONTH); k <= monCal.getActualMaximum(Calendar.DAY_OF_MONTH); k += 7) {
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.MONTH, i2 - 1);
                    startTime.set(Calendar.DAY_OF_MONTH, k);
                    startTime.set(Calendar.YEAR, i);
                    startTime.set(Calendar.HOUR_OF_DAY, hday);
                    startTime.set(Calendar.MINUTE, mday);
                    startTime.set(Calendar.SECOND, 0);
                    startTime.set(Calendar.MILLISECOND, 0);

                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, ehday);
                    endTime.set(Calendar.MINUTE, emday - 1);
                    endTime.set(Calendar.MONTH, i2 - 1);
                    endTime.set(Calendar.SECOND, 59);
                    endTime.set(Calendar.MILLISECOND, 999);


                    WeekViewEvent newEvent = new WeekViewEvent(1, event .getName(), startTime, endTime);
                    newEvent.setColor(event .getColor());
                    newEvents.add(newEvent);
                }
            }

            return newEvents;
        }
    }

    class EventLongPressListenerImpl implements WeekView.EventLongPressListener {
        @Override
        public void onEventLongPress(WeekViewEvent wevent, RectF eventRect) {
            Calendar cal = wevent.getStartTime();
            Intent intent = new Intent(Intent.ACTION_EDIT);

            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", false);
            intent.putExtra("rrule", "FREQ=DAILY");
            intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
            intent.putExtra("title", wevent.getName() + " Class Reminder!! ");
            startActivity(intent);
        }
    }
}
