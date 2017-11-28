package com.team15.utrgvscheduleviewer;

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
                  String location)
    {
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
}
