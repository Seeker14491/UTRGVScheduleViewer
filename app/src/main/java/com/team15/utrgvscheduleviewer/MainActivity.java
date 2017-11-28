package com.team15.utrgvscheduleviewer;

import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference for the week view in the layout.
        WeekView mWeekView = findViewById(R.id.weekView);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(new EventClickListenerImpl());

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(new MonthChangeListenerImpl());

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(new EventLongPressListenerImpl());
    }

    class EventClickListenerImpl implements WeekView.EventClickListener {
        @Override
        public void onEventClick(WeekViewEvent event, RectF eventRect) {
        }
    }

    class MonthChangeListenerImpl implements MonthLoader.MonthChangeListener {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // TODO

            return new ArrayList<>();
        }
    }

    class EventLongPressListenerImpl implements WeekView.EventLongPressListener {
        @Override
        public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        }
    }
}
