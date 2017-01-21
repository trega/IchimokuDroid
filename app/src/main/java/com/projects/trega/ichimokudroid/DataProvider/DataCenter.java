package com.projects.trega.ichimokudroid.DataProvider;

import com.projects.trega.ichimokudroid.MainActivity;

import java.io.File;

/**
 * Created by tomek on 21.01.2017.
 *
 * Main data provider class managing the lifecycle of all remaining data center components
 */

public class DataCenter {
    MainActivity itsActivity;
    DataDownloader itsDataDownloader;
    FileParser itsFileParser;
    DataContainer itsDataContainer;
    File stockFile;


    public DataCenter(MainActivity mainActivity) {
        itsActivity = mainActivity;
        itsDataDownloader = new DataDownloader(this);
        itsFileParser = new FileParser();
        itsDataContainer = new DataContainer();
    }


    public void acquireData() {
        itsDataDownloader.downloadDataFile(itsActivity.getApplicationContext());
    }

    public void fileAquireFinished(File currentStockFile) {
        stockFile = currentStockFile;
        itsFileParser.parseFile(stockFile, itsDataContainer);
    }
}
