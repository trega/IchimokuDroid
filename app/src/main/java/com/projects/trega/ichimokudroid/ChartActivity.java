package com.projects.trega.ichimokudroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.projects.trega.ichimokudroid.DataProvider.DataCenter;
import com.projects.trega.ichimokudroid.DataProvider.StockRecord;

import java.io.File;

public class ChartActivity extends AppCompatActivity {

    DataCenter itsDataCenter = null;
    File itsStockFile = null;
    private String symbolName;
    private ChartActivityFragment itsChartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String stockDataFileName = intent.getStringExtra(CommonInterface.STOCK_DATA_FILE_NAME);
        symbolName = intent.getStringExtra(CommonInterface.STOCK_DATA_SYMBOL);
        this.setTitle(symbolName);
        prepareChartData(stockDataFileName);

    }

    void getLatestStockValue(ChartActivityFragment chartActivityFragment){
        itsChartFragment = chartActivityFragment;
        itsDataCenter.getLatestStockValue(symbolName);

    }

    private void prepareChartData(String stockDataFileName) {
        itsStockFile = new File(stockDataFileName);
        itsDataCenter = new DataCenter(this);
        itsDataCenter.parseStockDataFile(itsStockFile);
    }

    public DataCenter getDataCenter() {
        return itsDataCenter;
    }

    public void latestStockValueReceived(StockRecord sr) {
        if(itsChartFragment !=null){
            itsChartFragment.updateCurrentStockValue(sr);
        }else{
            Toast.makeText(getApplicationContext(), "Chart Fragment could not be resolved",
                    Toast.LENGTH_LONG).show();
        }
    }
}
