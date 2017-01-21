package com.projects.trega.ichimokudroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.projects.trega.ichimokudroid.DataProvider.DataCenter;
import com.projects.trega.ichimokudroid.DataProvider.StockRecord;

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
        int dataLength = itsDataCenter.getDataLength();
//        dataLength=10;
        DataPoint dataPoints[] = new DataPoint[dataLength];
        for (int i = 0; i<dataLength; ++i){
            StockRecord sr = itsDataCenter.getStockRecord(i);
            dataPoints[i] = new DataPoint(sr.date, sr.close);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        itsMainGraphView.addSeries(series);

        // set date label formatter
        itsMainGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
//        itsMainGraphView.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
//
//        // set manual x bounds to have nice steps
//        itsMainGraphView.getViewport().setMinX(itsDataCenter.getStockRecord(0).date.getTime());
//        itsMainGraphView.getViewport().setMaxX(itsDataCenter.getStockRecord(dataLength-1).date.getTime());
//        itsMainGraphView.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        itsMainGraphView.getGridLabelRenderer().setHumanRounding(false);
    }
}
