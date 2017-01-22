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
        stockRecordsFull = new ArrayList<StockRecord>(INITIAL_SAMPLES_NUMBER);
        stockRecords = new ArrayList<ChartPoint>(INITIAL_SAMPLES_NUMBER);
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
        long timeStep = stockRecords.get(1).date.getTime() - stockRecords.get(0).date.getTime();
        senokuSpanA = new ArrayList<>(stockRecords.size());
        int kijunOffset = 26;
        int tekanOffset = 9;
        for (int i = kijunOffset; i < stockRecords.size(); ++i){
            ChartPoint st = stockRecords.get(i);
            ChartPoint kijun = kijunSen.get(i-kijunOffset);
            ChartPoint tekan = tekanSen.get(i - tekanOffset);
            ChartPoint senoku = new ChartPoint(new Date(st.date.getTime() + 26*timeStep), avgClosePrice(kijun, tekan));
            senokuSpanA.add(senoku);
        }
    }

    void calculateSenkouSpanB(){
        long timeStep = stockRecords.get(1).date.getTime() - stockRecords.get(0).date.getTime();
        senokuSpanB = new ArrayList<>(stockRecords.size());
        int kijunOffset = 26;
        int offset = 52;
        for (int i = offset; i < stockRecords.size(); ++i){
            ChartPoint st = stockRecords.get(i);
            ChartPoint stMax = maxCloseInIdxRange(i-offset, i);
            ChartPoint stMin = minCloseInIdxRange(i-offset, i);
            double val = avgClosePrice(stMax, stMin);
            senokuSpanB.add(new ChartPoint(new Date(st.date.getTime() + kijunOffset*timeStep), val));
        }
    }

    void calculateChikouSpan(){
        long timeStep = stockRecords.get(1).date.getTime() - stockRecords.get(0).date.getTime();
        chikouSpan = new ArrayList<>(stockRecords.size());
        for(int i = 0; i<stockRecords.size(); ++i){
            ChartPoint sr = stockRecords.get(i);
            ChartPoint cp = new ChartPoint(new Date(sr.date.getTime()- 26*timeStep), sr.value);
            chikouSpan.add(cp);
        }
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
