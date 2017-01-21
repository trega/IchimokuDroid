package com.projects.trega.ichimokudroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.projects.trega.ichimokudroid.DataProvider.DataCenter;
import com.projects.trega.ichimokudroid.DataProvider.StockRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChartActivityFragment extends Fragment {
    GraphView itsMainGraphView;
    ChartActivity itsActivity;
    DataCenter itsDataCenter;

    public ChartActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootFragmentView = inflater.inflate(R.layout.fragment_chart, container, false);
        itsMainGraphView = (GraphView) rootFragmentView.findViewById(R.id.main_graph);
        return rootFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        itsActivity = (ChartActivity) getActivity();
        itsDataCenter = itsActivity.getDataCenter();
        drawMainChart();
    }

    private void drawMainChart() {
        final int dataLength = itsDataCenter.getDataLength();
//        dataLength=50;
        DataPoint dataPoints[] = new DataPoint[dataLength];
        for (int i = 0; i<dataLength; ++i){
            StockRecord sr = itsDataCenter.getStockRecord(i);
            dataPoints[i] = new DataPoint(sr.date, sr.close);
//            dataPoints[i] = new DataPoint(i, sr.close);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setTitle("Close");
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date((long)(dataPoint.getX()));
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
                Toast.makeText(getActivity(), series.getTitle() + ":\nX="+formattedDate + "\nY="+dataPoint.getY()
                        , Toast.LENGTH_LONG).show();
            }
        });
        //series.setDrawDataPoints(true);


        itsMainGraphView.getViewport().setScalable(true);
        itsMainGraphView.getViewport().setScrollable(true);
        itsMainGraphView.getViewport().setScalableY(true);
        itsMainGraphView.getViewport().setScrollableY(true);
        // set manual x bounds to have nice steps
        itsMainGraphView.getViewport().setMinX(itsDataCenter.getStockRecord(0).date.getTime());
        itsMainGraphView.getViewport().setMaxX(itsDataCenter.getStockRecord(dataLength-1).date.getTime());
        itsMainGraphView.getViewport().setXAxisBoundsManual(true);
        itsMainGraphView.getGridLabelRenderer().setHumanRounding(false);


        // set date label formatter
        itsMainGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(itsMainGraphView.getContext()));
//        itsMainGraphView.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
//

        itsMainGraphView.addSeries(series);
        itsMainGraphView.getLegendRenderer().setVisible(true);
        itsMainGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
    }
}
