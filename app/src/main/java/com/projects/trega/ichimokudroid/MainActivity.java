package com.projects.trega.ichimokudroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.projects.trega.ichimokudroid.DataProvider.DataCenter;
import com.projects.trega.ichimokudroid.DataProvider.DataDownloader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.projects.trega.ichimokudroid.CommonInterface.STOCK_DATA_FILE_NAME;
import static com.projects.trega.ichimokudroid.CommonInterface.STOCK_DATA_SYMBOL;

public class MainActivity extends AppCompatActivity  implements FavoritesFragment.OnFavoriteFragmentInteractionListener{
    private static final int MY_PERMISSION_STORAGE = 123;
    static String TAG ="MAIN_ACTIVITY";
    DataDownloader itsDataDownloader;
    DataCenter itsDataCenter;
    File itsStockFile;

    private String symbolName;
    private String dataFileName;
    private String dataFilePath;
    private DownloadParametersBoundle downloadParametersBundle;
    private final String[] symbols = StockSymbols.SYMBOLS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itsDataCenter = new DataCenter(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeSymbolEditBox();


        initializeFloatingButtons();
        checkPermissionStorage();
    }

    private void initializeFloatingButtons() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractDownloadParameters();
                if(symbolName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }
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

        FloatingActionButton butCleanStorage = (FloatingActionButton) findViewById(R.id.butCleanStorage);
        butCleanStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomDialogBoxStyle);
                builder.setMessage("Do you want to remove all files in:\n"+getString(R.string.storagePath ))
                        .setTitle("Clear data storage").setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        File storageDir = new File (getString(R.string.storagePath));
                        for(File file: storageDir.listFiles())
                            if (!file.isDirectory())
                                file.delete();
                    }
                })                ;
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initializeSymbolEditBox() {
        AutoCompleteTextView symbolNameEt = (AutoCompleteTextView) findViewById(R.id.symbolValEt);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,symbols);
        symbolNameEt.setThreshold(1);
        symbolNameEt.setAdapter(adapter);
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
        dataFilePath = getString(R.string.storagePath) + dataFileName;

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissionStorage(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
        }

    }
}
