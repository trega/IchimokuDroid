package com.projects.trega.ichimokudroid.DataProvider;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.projects.trega.ichimokudroid.DownloadParametersBoundle;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class DataDownloader implements Response.Listener<byte[]>, Response.ErrorListener {
    DownloadParametersBoundle downloadParametersBoundle;
    private final String TAG = "DATA_DOWNLOADER";
    private final String baseDataCenterAddress = "https://stooq.com/";
    private final String appendix1 = "q/d/l/?s=";
    private final String appendix2 = "q/l/?s=";
    private final String suffix = "&d1=20160119&d2=20170119&i=d";
    private InputStreamVolleyRequest request;
    int count;
    File currentStockFile;
    DataCenter itsDataCenter;
    private RequestQueue mRequestQueue;

    public DataDownloader(DataCenter dataCenter) {
        itsDataCenter = dataCenter;
    }

    public Boolean downloadDataFile(DownloadParametersBoundle parametersBundle, Context ctx){
        downloadParametersBoundle = parametersBundle;
        String dataCenterAddress = baseDataCenterAddress + appendix1 + parametersBundle.symbol+
                "&d1="+parametersBundle.getPastDateDownloadString()+
                "&d2="+parametersBundle.getCurrentDateDownloadString()+
                "&i=d";
        request = new InputStreamVolleyRequest(Request.Method.GET, dataCenterAddress,
                DataDownloader.this, DataDownloader.this, null);
        queueNewRequest(ctx, request);
        return true;
    }

    public Boolean getLatestStockValue(Context ctx, String symbolName){
        String url = baseDataCenterAddress + appendix2 + symbolName + "&e=txt";
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Received Response: \n" + response);
                itsDataCenter.latestStockValueReceived(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "LatestValue: " + error.getMessage());
            }
        });
        queueNewRequest(ctx, strReq);
        return true;
    }

    private void queueNewRequest(Context ctx, Request request) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ctx);
        }
        mRequestQueue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "UNABLE TO DOWNLOAD FILE. ERROR:: " + error.getMessage());
        itsDataCenter.showToastMsg("UNABLE TO DOWNLOAD FILE: " + error.getMessage());
    }

    @Override
    public void onResponse(byte[] response) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (response!=null) {

                //Read file name from headers
                String content =request.responseHeaders.get("Content-Disposition")
                        .toString();
                StringTokenizer st = new StringTokenizer(content, "=");
                String[] arrTag = st.toArray();

                String filename = arrTag[1];
                filename = filename.replace(":", ".");
                filename+="_"+downloadParametersBoundle.getPastDateDownloadString();
                filename+="_"+downloadParametersBoundle.getCurrentDateDownloadString();
                Log.d("DEBUG::RESUME FILE NAME", filename);

                try{
                    long lenghtOfFile = response.length;

                    //covert reponse to input stream
                    InputStream input = new ByteArrayInputStream(response);
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//                    File path = new File(Environment.getExternalStorageState(), "IchimokuDroidData");
                    if(!path.exists()  && !path.mkdir())
                        throw new FileNotFoundException(path.toString() + " could not be created");
                    else
                        Log.d(TAG, path.toString() +" exists");
                    currentStockFile = new File(path, filename+".txt");
                    map.put("resume_path", currentStockFile.toString());
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(currentStockFile));
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }

                    output.flush();

                    output.close();
                    input.close();
                    itsDataCenter.fileAquireFinished(currentStockFile);
                }catch(IOException e){
                    e.printStackTrace();
                    itsDataCenter.showToastMsg(e.getMessage());

                }
            }
        } catch (Exception e) {
            Log.e("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
            itsDataCenter.showToastMsg("UNABLE TO DOWNLOAD FILE: " + e.getMessage());
        }
    }
}
