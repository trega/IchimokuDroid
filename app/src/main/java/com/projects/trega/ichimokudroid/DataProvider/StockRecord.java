package com.projects.trega.ichimokudroid.DataProvider;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockRecord {
    public Date date;
    public Double open;
    public Double high;
    public Double low;
    public Double close;
    public Integer volume;

    public StockRecord(String aDate, String aOpen, String aHigh, String aLow, String aClose, String aVolume){
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        try {
            date = df.parse(aDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        open = new Double(aOpen);
        high = new Double(aHigh);
        low = new Double(aLow);
        close = new Double(aClose);
        volume = new Integer(aVolume);
    }
}
