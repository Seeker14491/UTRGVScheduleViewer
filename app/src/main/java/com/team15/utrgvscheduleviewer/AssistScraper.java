package com.team15.utrgvscheduleviewer;

import android.graphics.Color;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssistScraper {
    private static Pattern classTimeSlotPattern
            = Pattern.compile("(\\S+) (\\d+)-(\\d+)<br>(\\d+)\\D+<br>(\\d+:\\d+\\s+.m)-(\\d+:\\d+\\s+.m)<br>" +
            "(\\S+\\s+\\S+)");

    public static List<ClassTimeSlot> getClassTimeSlots(String userName, String pass) throws IOException {
        Connection.Response loginResponse = Jsoup.connect("https://mywebsis.utrgv.edu/PROD/twbkwbis.P_ValLogin")
                .referrer("https://mywebsis.utrgv.edu/PROD/twbkwbis.P_WWWLogin")
                .data("sid", userName)
                .data("PIN", pass)

                // The server checks this to see if cookies are enabled. We can't log on without this.
                .cookie("TESTID", "set")

                .method(Connection.Method.POST)
                .execute();

        Log.d("AssisScrape",  loginResponse.cookies().toString());
        Document scheduleDocument = Jsoup.connect("https://mywebsis.utrgv.edu/PROD/bwskfshd.P_CrseSchd")
                .referrer("https://mywebsis.utrgv.edu/PROD/twbkwbis.P_GenMenu?name=bmenu.P_RegMnu")
                .cookies(loginResponse.cookies())
                .get();

        Elements scheduleElements = scheduleDocument.getElementsByAttributeValueStarting("href", "/PROD/bwskfshd" +
                ".P_CrseSchdDetl");

        ArrayList<ClassTimeSlot> classTimeSlots = new ArrayList<>();
        for (Element e : scheduleElements) {
            int dayId = e.parent().elementSiblingIndex();

            Matcher matcher = classTimeSlotPattern.matcher(e.html());

            // FIXME: don't fail silently
            if (matcher.matches()) {
                String subject = matcher.group(1);
                int course = Integer.parseInt(matcher.group(2));
                int section = Integer.parseInt(matcher.group(3));
                int crn = Integer.parseInt(matcher.group(4));

                // FIXME: use proper day of week type, or at least a String of the day's name
                String dayOfWeek = String.valueOf(dayId);

                String startTime = matcher.group(5);
                String endTime = matcher.group(6);
                String location = matcher.group(7);

                classTimeSlots.add(
                        new ClassTimeSlot(subject, course, section, crn, dayOfWeek, startTime, endTime, location));
            }
        }

        return classTimeSlots;
    }


}
