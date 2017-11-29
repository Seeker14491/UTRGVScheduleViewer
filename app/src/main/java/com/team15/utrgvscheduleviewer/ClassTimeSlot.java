package com.team15.utrgvscheduleviewer;

import android.graphics.Color;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ClassTimeSlot {
    public String subject;
    public int course;
    public int section;
    public int crn;

    // FIXME: use more type-safe types
    public String dayOfWeek;
    public String startTime;
    public String endTime;

    public String location;

    ClassTimeSlot(String subject, int course, int section, int crn, String dayOfWeek, String startTime, String endTime,
                  String location) {
        this.subject = subject;
        this.course = course;
        this.section = section;
        this.crn = crn;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    @Override
    public String toString() {
        return "ClassTimeSlot{" +
                "subject='" + subject + '\'' +
                ", course=" + course +
                ", section=" + section +
                ", crn=" + crn +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public void SetTime(String time, Calendar setTime) {
        int hour = Integer.parseInt(time.split(":")[0]);
        int min = Integer.parseInt(time.split(":")[1].split(" ")[0]);
        boolean isAm = time.split(":")[1].split(" ")[1].contains("am");


        setTime.set(Calendar.HOUR, hour);
        setTime.set(Calendar.MINUTE, min);

        if (isAm || hour == 12)
            setTime.set(Calendar.AM_PM, Calendar.AM);
        else
            setTime.set(Calendar.AM_PM, Calendar.PM);


    }

    public WeekViewEvent toWeekViewEvent() {

        Log.d("ClassTimeSlot", this.toString());
        // Parse time.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");

        // Initialize start and end time.
        Calendar now = Calendar.getInstance();

        Calendar startTime = (Calendar) now.clone();
        // startTime.setTimeInMillis(start.getTime());

        SetTime(this.startTime, startTime);

        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
        startTime.set(Calendar.MONTH, now.get(Calendar.MONTH));
        startTime.set(Calendar.DAY_OF_WEEK, Integer.parseInt(this.dayOfWeek)+1);
        Calendar endTime = (Calendar) startTime.clone();

        SetTime(this.endTime, endTime);
        endTime.set(Calendar.SECOND, 0);

        endTime.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
        endTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
        endTime.set(Calendar.DAY_OF_WEEK, startTime.get(Calendar.DAY_OF_WEEK));

        // Create an week view event.
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setName( this.subject + " " + this.course + "\n" + this.location);

        weekViewEvent.setStartTime(startTime);
        weekViewEvent.setEndTime(endTime);

       // weekViewEvent.setColor(Color.parseColor("Red"));


        Log.d("ClassTimeSlot:::s", startTime.getTime().toString());
        Log.d("ClassTimeSlot:::e", endTime.getTime().toString());


        return weekViewEvent;
    }
}
