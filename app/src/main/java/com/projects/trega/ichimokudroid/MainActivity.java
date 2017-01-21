package com.projects.trega.ichimokudroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.projects.trega.ichimokudroid.DataProvider.DataCenter;
import com.projects.trega.ichimokudroid.DataProvider.DataDownloader;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    static String TAG ="MAIN_ACTIVITY";
    DataDownloader itsDataDownloader;
    DataCenter itsDataCenter;
    File itsStockFile;

    private String defaultDataFilePath = "/storage/emulated/0/Documents/cdr_d.csv.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itsDataCenter = new DataCenter(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File tmpFile = new File(defaultDataFilePath);
                if(!tmpFile.exists())
                    itsDataCenter.acquireData();
                else {
                    itsStockFile=tmpFile;
                    String message = "File already exists, not downloading again: " + itsStockFile.getAbsolutePath();
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.show();
                    Log.d(TAG, message);
                    dataReady(itsStockFile);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void dataReady(File stockFile) {
        itsStockFile = stockFile;
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra(CommonInterface.STOCK_DATA_FILE_NAME, stockFile.getAbsolutePath());
        startActivity(intent);
    }
}
