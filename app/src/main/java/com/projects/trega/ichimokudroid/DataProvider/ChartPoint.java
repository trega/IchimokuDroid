package com.projects.trega.ichimokudroid.DataProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChartPoint {
    public Date date;
    public double value;
    public ChartPoint(String aDate, String aVal){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = df.parse(aDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        value = new Double(aVal);
    }

    public ChartPoint(Date date, double val) {
        this.date=date;
        this.value=val;
    }
}
