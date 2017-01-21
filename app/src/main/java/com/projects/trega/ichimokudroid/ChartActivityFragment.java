package com.projects.trega.ichimokudroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChartActivityFragment extends Fragment {
    GraphView itsMainGraphView;

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
        drawMainChart();
    }

    private void drawMainChart() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        itsMainGraphView.addSeries(series);
    }
}
