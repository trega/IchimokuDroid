package com.projects.trega.ichimokudroid;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private View rootFragmentView;
    private TextView latestDateValueTv;
    private TextView latestStockValueTv;

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
    private void initializeFloatingButtons() {
        FloatingActionButton fab = (FloatingActionButton) rootFragmentView.findViewById(R.id.zoomBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itsMainGraphView.getViewport().setMinX(itsDataCenter.getStockRecord(
                        itsDataCenter.getDataLength() - 90).date.getTime());
                itsMainGraphView.getViewport().setMaxX(senokuSpanBSeries.getHighestValueX());
                itsMainGraphView.getViewport().setXAxisBoundsManual(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootFragmentView = inflater.inflate(R.layout.fragment_chart, container, false);
        itsMainGraphView = (GraphView) rootFragmentView.findViewById(R.id.main_graph);
        latestDateValueTv = (TextView)rootFragmentView.findViewById(R.id.DateValueTv);
        latestStockValueTv = (TextView)rootFragmentView.findViewById(R.id.LatestPriceValueTv);
        initializeFloatingButtons();
        return rootFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        itsActivity = (ChartActivity) getActivity();
        itsDataCenter = itsActivity.getDataCenter();
        drawMainChart();
        itsActivity.getLatestStockValue(this);
    }

    public void updateCurrentStockValue(final StockRecord sr){
        final String formattedDate = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(sr.date);
        itsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                latestDateValueTv.setText(formattedDate);
                latestStockValueTv.setText(sr.close.toString());
            }
        });
    }

    private void prepareCallbacks() {
        onDataPointTapListener = new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {

                Date date = new Date((long)(dataPoint.getX()));
                String entryStr = itsDataCenter.findEntryByDateStr(date);
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Date: " + formattedDate + entryStr)
                        .setTitle("Entry Details").setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
    }

    private void drawMainChart() {
        final int dataLength = itsDataCenter.getDataLength();
        prepareCallbacks();
        prepareSeries();
        prepareViewPort(dataLength);
        prepareLabels();
        setThicknessAll(3);
        adjustStyling();

        addSeries();
        itsMainGraphView.getLegendRenderer().setVisible(true);
        itsMainGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        itsMainGraphView.getLegendRenderer().setFixedPosition(0,0);
    }

    private void adjustStyling() {
        return;
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        paint.setStrokeWidth(10);
//        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
//        senokuSpanASeries.setCustomPaint(paint);
//        itsMainGraphView.getGraphContentHeight();

    }

    private void setThicknessAll(int thickness) {
        closeSeries.setThickness(thickness);
        tekanSenSeries.setThickness(thickness);
        kijunSenSeries.setThickness(thickness);
        chikouSpanSeries.setThickness(thickness);
        senokuSpanASeries.setThickness(thickness);
        senokuSpanBSeries.setThickness(thickness);
    }

    private LineGraphSeries<DataPoint> prepareGenericSeries(EChartSerie indicator){
        ArrayList<ChartPoint> indicatorData=null;
        String title = null;
        int color = 0;
        switch (indicator){
            case CHIKOU_SPAN:
                itsDataCenter.prepareChkouSpan();
                indicatorData = itsDataCenter.getChikouSpan();
                title = "Chikou Span";
                color = Color.LTGRAY;
                break;
            case SENOKU_SPAN_A:
                itsDataCenter.prepareSenokuASpan();
                indicatorData = itsDataCenter.getSenokuSpanA();
                title = "Senoku Span A";
                color = Color.CYAN;
                break;
            case SENOKU_SPAN_B:
                itsDataCenter.prepareSenokuBSpan();
                indicatorData = itsDataCenter.getSenokuSpanB();
                title = "Senoku Span B";
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
//        series.setOnDataPointTapListener(onDataPointTapListener);
        series.setColor(color);
        return series;
    }

    private void prepareLabels() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        itsMainGraphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));
        itsMainGraphView.getGridLabelRenderer().setHumanRounding(false);
        itsMainGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(itsMainGraphView.getContext()));
        itsMainGraphView.getGridLabelRenderer().setNumHorizontalLabels(7);
    }

    private void prepareViewPort(int dataLength) {
        itsMainGraphView.getViewport().setScalable(true);
        itsMainGraphView.getViewport().setScrollable(true);
        itsMainGraphView.getViewport().setScalableY(true);
        itsMainGraphView.getViewport().setScrollableY(true);
        itsMainGraphView.getViewport().setMinX(chikouSpanSeries.getLowestValueX());
        itsMainGraphView.getViewport().setMaxX(senokuSpanBSeries.getHighestValueX());
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
        closeSeries.setOnDataPointTapListener(onDataPointTapListener);
        tekanSenSeries = prepareGenericSeries(EChartSerie.TEKAN_SEN);
        kijunSenSeries = prepareGenericSeries(EChartSerie.KIJUN_SEN);
        chikouSpanSeries = prepareGenericSeries(EChartSerie.CHIKOU_SPAN);
        senokuSpanASeries = prepareGenericSeries(EChartSerie.SENOKU_SPAN_A);
        senokuSpanBSeries = prepareGenericSeries(EChartSerie.SENOKU_SPAN_B);
    }
}
