package com.projects.trega.ichimokudroid.DataProvider;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.R.attr.value;

public class FileParser {
    public StockRecord parseSingleRecord(String line){
        StockRecord stockRecord = null;
        if (line != null) {
            String[] RowData = line.split(",");
            stockRecord = new StockRecord(RowData[1] + " " + RowData[2],RowData[3],RowData[4],
                                RowData[5],RowData[6], RowData[7], "yyyyMMdd hhmmss");
        }
        return stockRecord;
    }
    public void parseFile(File stockFile, DataContainer container){
        FileInputStream is = null;
        try {
            is = new FileInputStream(stockFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            StockRecord stockRecord;
            ChartPoint chartPoint;
            //Remove header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split(",");
                stockRecord = new StockRecord(RowData[0],RowData[1],RowData[2],RowData[3],RowData[4],RowData[5]);
                chartPoint = new ChartPoint(RowData[0], RowData[4]);
                container.addStockRecord(chartPoint);
                container.addStockRecordFull(stockRecord);
            }

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
