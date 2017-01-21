package com.projects.trega.ichimokudroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.projects.trega.ichimokudroid.DataProvider.ChartPoint;
import com.projects.trega.ichimokudroid.DataProvider.DataCenter;
import com.projects.trega.ichimokudroid.DataProvider.StockRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChartActivityFragment extends Fragment {
    GraphView itsMainGraphView;
    ChartActivity itsActivity;
    DataCenter itsDataCenter;
    private LineGraphSeries<DataPoint> tekanSenSeries;
    private LineGraphSeries<DataPoint> kijunSenSeries;
    private LineGraphSeries<DataPoint> closeSeries;
    private LineGraphSeries<DataPoint> chikouSpanSeries;
    private LineGraphSeries<DataPoint> senokuSpanASeries;
    private LineGraphSeries<DataPoint> senokuSpanBSeries;

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
        OnDataPointTapListener onDataPointTapListener = new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date((long)(dataPoint.getX()));
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
                Toast.makeText(getActivity(), series.getTitle() + ":\nX="+formattedDate + "\nY="+dataPoint.getY()
                        , Toast.LENGTH_LONG).show();
            }
        };
        final int dataLength = itsDataCenter.getDataLength();
        tekanSenSeries = new LineGraphSeries<>();
        kijunSenSeries = new LineGraphSeries<>();
        chikouSpanSeries = new LineGraphSeries<>();
        senokuSpanASeries = new LineGraphSeries<>();
        senokuSpanBSeries = new LineGraphSeries<>();
        closeSeries = prepareCloseSeries(dataLength, onDataPointTapListener);
        prepareViewPort(dataLength);
        prepareLabels();
        prepareSenSeries(onDataPointTapListener);
        prepareChikouSpanSeries(onDataPointTapListener);
        prepareSenokuASpanSeries(onDataPointTapListener);
        prepareSenokuBSpanSeries(onDataPointTapListener);
        itsMainGraphView.addSeries(closeSeries);
        itsMainGraphView.addSeries(tekanSenSeries);
        itsMainGraphView.addSeries(kijunSenSeries);
        itsMainGraphView.addSeries(chikouSpanSeries);
        itsMainGraphView.addSeries(senokuSpanASeries);
        itsMainGraphView.addSeries(senokuSpanBSeries);
        itsMainGraphView.getLegendRenderer().setVisible(true);
        itsMainGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
    }

    private void prepareChikouSpanSeries(OnDataPointTapListener onDataPointTapListener) {
        itsDataCenter.prepareChkouSpan();
        ArrayList<ChartPoint> chikouSpan = itsDataCenter.getChikouSpan();
        int dataLength = chikouSpan.size();
        DataPoint dataPoints[] = new DataPoint[dataLength];
        for (int i = 0; i<dataLength; ++i){
            ChartPoint cp = chikouSpan.get(i);
            dataPoints[i] = new DataPoint(cp.date, cp.value);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setTitle("Chikou Span");
        series.setOnDataPointTapListener(onDataPointTapListener);
        //series.setDrawDataPoints(true);
        series.setColor(Color.GRAY);
        chikouSpanSeries = series;
    }

    private void prepareSenokuASpanSeries(OnDataPointTapListener onDataPointTapListener) {
        itsDataCenter.prepareSenokuASpan();
        ArrayList<ChartPoint> senokuASpan = itsDataCenter.getSenokuSpanA();
        int dataLength = senokuASpan.size();
        DataPoint dataPoints[] = new DataPoint[dataLength];
        for (int i = 0; i<dataLength; ++i){
            ChartPoint cp = senokuASpan.get(i);
            dataPoints[i] = new DataPoint(cp.date, cp.value);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setTitle("Senoku Span A");
        series.setOnDataPointTapListener(onDataPointTapListener);
        //series.setDrawDataPoints(true);
        series.setColor(Color.CYAN);
        senokuSpanASeries = series;
    }

    private void prepareSenokuBSpanSeries(OnDataPointTapListener onDataPointTapListener) {
        itsDataCenter.prepareSenokuBSpan();
        ArrayList<ChartPoint> senokuBSpan = itsDataCenter.getSenokuSpanB();
        int dataLength = senokuBSpan.size();
        DataPoint dataPoints[] = new DataPoint[dataLength];
        for (int i = 0; i<dataLength; ++i){
            ChartPoint cp = senokuBSpan.get(i);
            dataPoints[i] = new DataPoint(cp.date, cp.value);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setTitle("Senoku Span B");
        series.setOnDataPointTapListener(onDataPointTapListener);
        //series.setDrawDataPoints(true);
        series.setColor(Color.MAGENTA);
        senokuSpanBSeries = series;
    }

    private void prepareLabels() {
        itsMainGraphView.getGridLabelRenderer().setHumanRounding(false);
        itsMainGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(itsMainGraphView.getContext()));
    }

    private void prepareViewPort(int dataLength) {
        itsMainGraphView.getViewport().setScalable(true);
        itsMainGraphView.getViewport().setScrollable(true);
        itsMainGraphView.getViewport().setScalableY(true);
        itsMainGraphView.getViewport().setScrollableY(true);
        // set manual x bounds to have nice steps
        itsMainGraphView.getViewport().setMinX(itsDataCenter.getStockRecord(0).date.getTime());
        itsMainGraphView.getViewport().setMaxX(itsDataCenter.getStockRecord(dataLength-1).date.getTime());
        itsMainGraphView.getViewport().setXAxisBoundsManual(true);
    }

    private void prepareSenSeries(OnDataPointTapListener onDataPointTapListener) {
        itsDataCenter.prepareSenSeries();
        ArrayList<ChartPoint> tekanSen = itsDataCenter.getTekanSen();
        ArrayList<ChartPoint> kijunSen = itsDataCenter.getKijunSen();

        tekanSenSeries = prepareSenSerie(onDataPointTapListener, tekanSen, "Tekan Sen", Color.GREEN);
        kijunSenSeries = prepareSenSerie(onDataPointTapListener, kijunSen, "Kijun Sen", Color.RED);
    }

    @NonNull
    private LineGraphSeries<DataPoint> prepareSenSerie(OnDataPointTapListener onDataPointTapListener, ArrayList<ChartPoint> data, String title, int color) {
        int dataLength = data.size();
        DataPoint dataPoints[] = new DataPoint[dataLength];
        for (int i = 0; i<dataLength; ++i){
            ChartPoint cp = data.get(i);
            dataPoints[i] = new DataPoint(cp.date, cp.value);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setTitle(title);
        series.setOnDataPointTapListener(onDataPointTapListener);
        //series.setDrawDataPoints(true);
        series.setColor(color);
        return series;
    }

    @NonNull
    private LineGraphSeries<DataPoint> prepareCloseSeries(int dataLength, OnDataPointTapListener onDataPointTapListener) {
        DataPoint dataPoints[] = new DataPoint[dataLength];
        for (int i = 0; i<dataLength; ++i){
            ChartPoint sr = itsDataCenter.getStockRecord(i);
            dataPoints[i] = new DataPoint(sr.date, sr.value);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setTitle("Close");
        series.setOnDataPointTapListener(onDataPointTapListener);
        //series.setDrawDataPoints(true);
        return series;
    }
}
