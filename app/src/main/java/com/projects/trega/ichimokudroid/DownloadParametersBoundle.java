package com.projects.trega.ichimokudroid;

/**
 * Created by tomek on 22.01.2017.
 */

public  class DownloadParametersBoundle {
    public String symbol;
    public String currentYear;
    public String currentMonth;
    public String currentDay;
    public String pastYear;
    public String pastMonth;
    public String pastDay;
    DownloadParametersBoundle(String symbol, String currentYear, String currentMonth, String currentDay,
                              String pastYear, String pastMonth, String pastDay){
        this.symbol=symbol;
        this.currentYear=currentYear;
        this.currentMonth=currentMonth;
        this.currentDay=currentDay;
        this.pastYear=pastYear;
        this.pastMonth=pastMonth;
        this.pastDay=pastDay;
    }

    public String getCurrentDateDownloadString(){
        return currentYear+currentMonth+currentDay;
    }
    public String getPastDateDownloadString(){
        return  pastYear+pastMonth+pastDay;
    }

}
