package com.projects.trega.ichimokudroid.DataProvider;

import java.util.ArrayList;

public class DataContainer {
    private static final int INITIAL_SAMPLES_NUMBER = 500;
    private ArrayList<StockRecord> stockRecords;

    public DataContainer(){
        stockRecords = new ArrayList<StockRecord>(INITIAL_SAMPLES_NUMBER);
    }

    public void addStockRecord(StockRecord record){
        stockRecords.add(record);
    }
}
