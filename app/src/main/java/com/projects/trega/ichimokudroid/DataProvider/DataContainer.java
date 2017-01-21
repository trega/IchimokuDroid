package com.projects.trega.ichimokudroid.DataProvider;

import java.util.ArrayList;

public class DataContainer {
    private static final int INITIAL_SAMPLES_NUMBER = 500;
    private ArrayList<StockRecord> stockRecords;
    private ArrayList<ChartPoint> tekanSen;

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

    void calculateTekanSen(){

    }
}
