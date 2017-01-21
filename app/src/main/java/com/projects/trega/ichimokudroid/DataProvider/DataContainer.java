package com.projects.trega.ichimokudroid.DataProvider;

import java.util.ArrayList;

public class DataContainer {
    private static final int INITIAL_SAMPLES_NUMBER = 500;
    private ArrayList<StockRecord> stockRecords;
    private ArrayList<ChartPoint> tekanSen;
    private ArrayList<ChartPoint> kijunSen;

    public DataContainer(){
        stockRecords = new ArrayList<StockRecord>(INITIAL_SAMPLES_NUMBER);
    }

    public void addStockRecord(StockRecord record){
        stockRecords.add(record);
    }

    public int getDataLength() {
        return stockRecords.size();
    }

    public StockRecord getStockRekord(int i) {
        return stockRecords.get(i);
    }

    public void prepareSenLists(){
        calculateTekanSen();
        calculateKijunSen();
    }

    StockRecord maxCloseInIdxRange(int start, int end){
        double max = -1;
        int max_idx = -1;
        for (int i = start; i<end; ++i){
            StockRecord st = stockRecords.get(i);
            if(max < st.close){
                max = st.close;
                max_idx = i;
            }
        }
        return stockRecords.get(max_idx);
    }

    StockRecord minCloseInIdxRange(int start, int end){
        double min = 999999;
        int min_idx = -1;
        for (int i = start; i<end; ++i){
            StockRecord st = stockRecords.get(i);
            if(min > st.close){
                min = st.close;
                min_idx = i;
            }
        }
        return stockRecords.get(min_idx);
    }

    double avgClosePrice(StockRecord first, StockRecord second){
        return (first.close + second.close)/2;
    }

    ArrayList<ChartPoint> calculateSen(int step){
        ArrayList<ChartPoint> senList = new ArrayList<>(stockRecords.size()-step);
        for (int i = step; i < stockRecords.size(); ++i){
            StockRecord stMax = maxCloseInIdxRange(i-step, i);
            StockRecord stMin = minCloseInIdxRange(i-step, i);
            double val = avgClosePrice(stMax, stMin);
            senList.add(new ChartPoint(stockRecords.get(i).date, val));
        }
        return senList;
    }

    void calculateTekanSen(){
        tekanSen = calculateSen(9);
    }
    void calculateKijunSen(){
        kijunSen = calculateSen(24);
    }

    public ArrayList<ChartPoint> getTekanSen() {
        return tekanSen;
    }

    public ArrayList<ChartPoint> getKijunSen() {
        return kijunSen;
    }
}
