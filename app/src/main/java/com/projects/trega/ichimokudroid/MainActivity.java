package com.projects.trega.ichimokudroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.trega.ichimokudroid.DataProvider.DataCenter;
import com.projects.trega.ichimokudroid.DataProvider.DataDownloader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.projects.trega.ichimokudroid.CommonInterface.*;

public class MainActivity extends AppCompatActivity {
    static String TAG ="MAIN_ACTIVITY";
    DataDownloader itsDataDownloader;
    DataCenter itsDataCenter;
    File itsStockFile;

    private String symbolName;
    private String dataFileName;
    private String dataFilePath;
    private DownloadParametersBoundle downloadParametersBundle;
    private final String[] symbols ={
            "ACP", "ACT", "ALI", "ALM", "ALR", "APR", "ATT", "BRI", "BZW", "CAR", "CCC", "CDR",
            "CEZ", "CIE", "CLN", "COG", "CPS", "CPS", "DPL", "ENA", "ENG", "EUR", "GNB", "GPW",
            "IPT", "JSW", "KGH", "KRU", "KSG", "KTY", "KZS", "LPP", "LTS", "LVC", "LWB", "MBK",
            "MEX", "MIL", "MLK", "MRB", "MRC", "NET", "OIL", "OPF", "OPL", "OPN", "PBG", "PDZ",
            "PEO", "PFL", "PGN", "PKN", "PKO", "PND", "PXM", "PZU", "RDL", "RDN", "RES", "SEN",
            "SFS", "SNS", "TIM", "TPE", "TRK", "TRN", "UNI", "WAS", "WPL", "YOL"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itsDataCenter = new DataCenter(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeSymbolEditBox();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractDownloadParameters();

                File tmpFile = new File(dataFilePath);
                if(!tmpFile.exists()) {
                    itsDataCenter.acquireData(downloadParametersBundle);
                }
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

    private void initializeSymbolEditBox() {
        AutoCompleteTextView symbolNameEt = (AutoCompleteTextView) findViewById(R.id.symbolValEt);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,symbols);
        symbolNameEt.setThreshold(0);
        symbolNameEt.setAdapter(adapter);
        symbolNameEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView)v).showDropDown();
                return true;
            }
        });
    }

    private void extractDownloadParameters() {
        AutoCompleteTextView symbolNameEt = (AutoCompleteTextView) findViewById(R.id.symbolValEt);
        symbolName = symbolNameEt.getText().toString();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime = c.getTime();
        c.add(Calendar.YEAR, -2);
        Date yearBackTime = c.getTime();
        String formattedDateCurrent = df.format(currentTime);
        String[] currentDateArray = formattedDateCurrent.split("-");
        String formattedDatePast = df.format(yearBackTime);
        String[] pastDateArray = formattedDatePast.split("-");
        downloadParametersBundle = new DownloadParametersBoundle(
                symbolName, currentDateArray[0], currentDateArray[1], currentDateArray[2],
                pastDateArray[0], pastDateArray[1], pastDateArray[2]);
        dataFileName = symbolName + "_d.csv_"+downloadParametersBundle.getPastDateDownloadString()+
                "_"+downloadParametersBundle.getCurrentDateDownloadString()+".txt";
        dataFilePath = "/storage/emulated/0/Documents/" + dataFileName;

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
        intent.putExtra(STOCK_DATA_FILE_NAME, stockFile.getAbsolutePath());
        intent.putExtra(STOCK_DATA_SYMBOL, symbolName);
        startActivity(intent);
    }
}
