package com.projects.trega.ichimokudroid.DataProvider;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Date;

public class DataContainer {
    private static final int INITIAL_SAMPLES_NUMBER = 500;
    private ArrayList<ChartPoint> stockRecords;
    private ArrayList<ChartPoint> tekanSen;
    private ArrayList<ChartPoint> kijunSen;
    private ArrayList<ChartPoint> chikouSpan;
    private ArrayList<ChartPoint> senokuSpanA;
    private ArrayList<ChartPoint> senokuSpanB;
    private ArrayList<StockRecord> stockRecordsFull;

    public DataContainer(){
        stockRecordsFull = new ArrayList<StockRecord>();
        stockRecords = new ArrayList<ChartPoint>();
    }

    public void addStockRecord(ChartPoint record){
        stockRecords.add(record);
    }

    public int getDataLength() {
        return stockRecords.size();
    }

    public ChartPoint getStockRekord(int i) {
        return stockRecords.get(i);
    }

    ChartPoint maxCloseInIdxRange(int start, int end){
        double max = -1;
        int max_idx = -1;
        for (int i = start; i<end; ++i){
            ChartPoint st = stockRecords.get(i);
            if(max < st.value){
                max = st.value;
                max_idx = i;
            }
        }
        return stockRecords.get(max_idx);
    }

    ChartPoint minCloseInIdxRange(int start, int end){
        double min = 999999;
        int min_idx = -1;
        for (int i = start; i<end; ++i){
            ChartPoint st = stockRecords.get(i);
            if(min > st.value){
                min = st.value;
                min_idx = i;
            }
        }
        return stockRecords.get(min_idx);
    }

    double avgClosePrice(ChartPoint first, ChartPoint second){
        return (first.value + second.value)/2;
    }

    ArrayList<ChartPoint> calculateSen(int step){
        if (stockRecords.size() <= step){
            return null;
        }
        ArrayList<ChartPoint> senList = new ArrayList<>(stockRecords.size()-step);
        for (int i = step; i < stockRecords.size(); ++i){
            ChartPoint stMax = maxCloseInIdxRange(i-step, i);
            ChartPoint stMin = minCloseInIdxRange(i-step, i);
            double val = avgClosePrice(stMax, stMin);
            senList.add(new ChartPoint(stockRecords.get(i).date, val));
        }
        return senList;
    }

    void calculateSenkouSpanA(){
        senokuSpanA = new ArrayList<>(stockRecords.size());
        int kijunOffset = 26;
        int tekanOffset = 9;
        for (int i = kijunOffset; i < stockRecords.size(); ++i){
            ChartPoint st = stockRecords.get(i);
            ChartPoint kijun = kijunSen.get(i - kijunOffset);
            ChartPoint tekan = tekanSen.get(i - tekanOffset);
            double senokuAVal = avgClosePrice(kijun, tekan);
            Date senokuADate = calculateSenokuShiftedDate(i);
            senokuSpanA.add(new ChartPoint(senokuADate, senokuAVal));
        }
    }

    void calculateSenkouSpanB(){
        senokuSpanB = new ArrayList<>(stockRecords.size());
        int offset = 52;
        for (int i = offset; i < stockRecords.size(); ++i){
            ChartPoint st = stockRecords.get(i);
            ChartPoint stMax = maxCloseInIdxRange(i-offset, i);
            ChartPoint stMin = minCloseInIdxRange(i-offset, i);
            double senokuBVal = avgClosePrice(stMax, stMin);
            Date senokuADate = calculateSenokuShiftedDate(i);
            senokuSpanB.add(new ChartPoint(senokuADate, senokuBVal));
        }
    }

    Date calculateSenokuShiftedDate(int i){
        int dataLength = stockRecords.size();
        long timeStep = calculateTimeStep();
        int senokuShift = 26;
        Date senokuDate = null;
        if(i < (dataLength - senokuShift)){
            senokuDate = stockRecords.get(i+senokuShift).date;
        }else{
            senokuDate = new Date(stockRecords.get(dataLength-1).date.getTime() + timeStep*(senokuShift-(dataLength -i-1)));
        }
        return senokuDate;
    }

    void calculateChikouSpan(){
        long timeStep = calculateTimeStep();
        chikouSpan = new ArrayList<>(stockRecords.size());
        int chikouShift = 26;
        for(int i = 0; i<chikouShift; ++i){
            ChartPoint sr = stockRecords.get(i);
            ChartPoint cp = new ChartPoint(new Date(sr.date.getTime()- chikouShift*timeStep), sr.value);
            chikouSpan.add(cp);
        }
        for(int i = chikouShift; i<stockRecords.size(); ++i){
            ChartPoint sr = stockRecords.get(i);
            ChartPoint cp = new ChartPoint(stockRecords.get(i-chikouShift).date, sr.value);
            chikouSpan.add(cp);
        }
    }

    private long calculateTimeStep() {
        return stockRecords.get(stockRecords.size()-1).date.getTime() -
                    stockRecords.get(stockRecords.size()-2).date.getTime();
    }

    public void calculateTekanSen(){
        tekanSen = calculateSen(9);
    }
    void calculateKijunSen(){
        kijunSen = calculateSen(26);
    }

    public ArrayList<ChartPoint> getTekanSen() {
        return tekanSen;
    }

    public ArrayList<ChartPoint> getKijunSen() {
        return kijunSen;
    }

    public ArrayList<ChartPoint> getChikouSpan() {
        return chikouSpan;
    }

    public void addStockRecordFull(StockRecord stockRecord) {
        stockRecordsFull.add(stockRecord);
    }

    public ArrayList<ChartPoint> getSenokuSpanA() {
        return senokuSpanA;
    }

    public ArrayList<ChartPoint> getSenokuSpanB() { return senokuSpanB; }

    public ArrayList<ChartPoint> getStockRecords() { return stockRecords; }

    public String findEntryByDateStr(Date date) {
        String entry_string = new String();
        for(int i = 0; i <stockRecords.size(); ++i){
            ChartPoint dp = stockRecords.get(i);
            if(dp.date.equals(date))
                entry_string += new String ("\nClosing price: " + dp.value);
        }
        for(int i = 0; i <tekanSen.size(); ++i){
            ChartPoint dp = tekanSen.get(i);
            if(dp.date.equals(date))
                entry_string += new String ("\nTekan Sen: " + dp.value);
        }
        for(int i = 0; i <kijunSen.size(); ++i){
            ChartPoint dp = kijunSen.get(i);
            if(dp.date.equals(date))
                entry_string += new String ("\nKijun Sen: " + dp.value);
        }
        for(int i = 0; i <chikouSpan.size(); ++i){
            ChartPoint dp = chikouSpan.get(i);
            if(dp.date.equals(date))
                entry_string += new String ("\nChikou Span: " + dp.value);
        }
        for(int i = 0; i <senokuSpanA.size(); ++i){
            ChartPoint dp = senokuSpanA.get(i);
            if(dp.date.equals(date))
                entry_string += new String ("\nSenoku Span A: " + dp.value);
        }
        for(int i = 0; i <senokuSpanB.size(); ++i){
            ChartPoint dp = senokuSpanB.get(i);
            if(dp.date.equals(date))
                entry_string += new String ("\nSenoku Span B: " + dp.value);
        }
        return entry_string;
    }
}

//TODO: account for weekends in shifted indicators (I guess these should be const?) - we could always be taking dates from original serie
