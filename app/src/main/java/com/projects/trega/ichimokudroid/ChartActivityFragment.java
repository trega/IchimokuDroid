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
    private OnDataPointTapListener onDataPointTapListener;

    public enum EChartSerie {
        CLOSE_VAL,
        TEKAN_SEN,
        KIJUN_SEN,
        CHIKOU_SPAN,
        SENOKU_SPAN_A,
        SENOKU_SPAN_B
    }

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

    private void prepareCallbacks() {
        onDataPointTapListener = new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date((long)(dataPoint.getX()));
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
                Toast.makeText(getActivity(), series.getTitle() + ":\nX="+formattedDate + "\nY="+dataPoint.getY()
                        , Toast.LENGTH_LONG).show();
            }
        };
    }

    private void drawMainChart() {
        final int dataLength = itsDataCenter.getDataLength();
        prepareCallbacks();
        prepareSeries();
        prepareViewPort(dataLength);
        prepareLabels();
        addSeries();
        itsMainGraphView.getLegendRenderer().setVisible(true);
        itsMainGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
    }

    private LineGraphSeries<DataPoint> prepareGenericSeries(EChartSerie indicator){
        ArrayList<ChartPoint> indicatorData=null;
        String title = null;
        int color = 0;
        switch (indicator){
            case CHIKOU_SPAN:
                itsDataCenter.prepareChkouSpan();
                indicatorData = itsDataCenter.getChikouSpan();
                title = new String("Chikou Span");
                color = Color.LTGRAY;
                break;
            case SENOKU_SPAN_A:
                itsDataCenter.prepareSenokuASpan();
                indicatorData = itsDataCenter.getSenokuSpanA();
                title = new String("Senoku Span A");
                color = Color.CYAN;
                break;
            case SENOKU_SPAN_B:
                itsDataCenter.prepareSenokuBSpan();
                indicatorData = itsDataCenter.getSenokuSpanB();
                title = new String("Senoku Span B");
                color = Color.MAGENTA;
                break;
            case CLOSE_VAL:
                indicatorData = itsDataCenter.getStockRecords();
                title = "Close price";
                color = Color.BLUE;
                break;
            case TEKAN_SEN:
                itsDataCenter.prepareTekanSen();
                indicatorData = itsDataCenter.getTekanSen();
                title = "Tekan Sen";
                color = Color.GREEN;
                break;
            case KIJUN_SEN:
                itsDataCenter.prepareKijunSen();
                indicatorData = itsDataCenter.getKijunSen();
                title = "Kijun Sen";
                color = Color.RED;
                break;


        }
        return produceLineGraphSerie(indicatorData, title, color);
    }

    @NonNull
    private LineGraphSeries<DataPoint> produceLineGraphSerie(ArrayList<ChartPoint> indicatorData, String title, int color) {
        int dataLength = indicatorData.size();
        DataPoint dataPoints[] = new DataPoint[dataLength];
        for (int i = 0; i<dataLength; ++i){
            ChartPoint cp = indicatorData.get(i);
            dataPoints[i] = new DataPoint(cp.date, cp.value);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setTitle(title);
        series.setOnDataPointTapListener(onDataPointTapListener);
        series.setColor(color);
        return series;
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
        itsMainGraphView.getViewport().setMinX(itsDataCenter.getStockRecord(0).date.getTime());
        itsMainGraphView.getViewport().setMaxX(itsDataCenter.getStockRecord(dataLength-1).date.getTime());
        itsMainGraphView.getViewport().setXAxisBoundsManual(true);
    }

    private void addSeries() {
        itsMainGraphView.addSeries(closeSeries);
        itsMainGraphView.addSeries(tekanSenSeries);
        itsMainGraphView.addSeries(kijunSenSeries);
        itsMainGraphView.addSeries(chikouSpanSeries);
        itsMainGraphView.addSeries(senokuSpanASeries);
        itsMainGraphView.addSeries(senokuSpanBSeries);
    }

    private void prepareSeries() {
        tekanSenSeries = new LineGraphSeries<>();
        kijunSenSeries = new LineGraphSeries<>();
        chikouSpanSeries = new LineGraphSeries<>();
        senokuSpanASeries = new LineGraphSeries<>();
        senokuSpanBSeries = new LineGraphSeries<>();
        closeSeries = prepareGenericSeries(EChartSerie.CLOSE_VAL);
        tekanSenSeries = prepareGenericSeries(EChartSerie.TEKAN_SEN);
        kijunSenSeries = prepareGenericSeries(EChartSerie.KIJUN_SEN);
        chikouSpanSeries = prepareGenericSeries(EChartSerie.CHIKOU_SPAN);
        senokuSpanASeries = prepareGenericSeries(EChartSerie.SENOKU_SPAN_A);
        senokuSpanBSeries = prepareGenericSeries(EChartSerie.SENOKU_SPAN_B);
    }
}
