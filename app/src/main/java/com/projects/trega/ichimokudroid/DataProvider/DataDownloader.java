package com.projects.trega.ichimokudroid.DataProvider;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.projects.trega.ichimokudroid.DownloadParametersBoundle;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class DataDownloader implements Response.Listener<byte[]>, Response.ErrorListener {
    DownloadParametersBoundle downloadParametersBoundle;
    private final String TAG = "DATA_DOWNLOADER";
    private final String baseDataCenterAddress = "http://stooq.com/";
    private final String appendix1 = "q/d/l/?s=";
    private final String suffix = "&d1=20160119&d2=20170119&i=d";
//    private final String dataCenterAddress = "http://stooq.com/q/d/l/?s=cdr&d1=20160119&d2=20170119&i=d";
    private InputStreamVolleyRequest request;
    int count;
    File currentStockFile;
    DataCenter itsDataCenter;

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
        RequestQueue mRequestQueue = Volley.newRequestQueue(ctx, new HurlStack());
        mRequestQueue.add(request);
        return true;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "UNABLE TO DOWNLOAD FILE. ERROR:: " + error.getMessage());
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

                }
            }
        } catch (Exception e) {
            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
        }
    }
}
