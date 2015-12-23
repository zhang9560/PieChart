package com.example.piechart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PieChartView chartView = (PieChartView)findViewById(R.id.chart);
        PieChartView.Block block1 = new PieChartView.Block(Color.RED, 21);
        PieChartView.Block block2 = new PieChartView.Block(Color.BLUE, 45);
        PieChartView.Block block3 = new PieChartView.Block(Color.GREEN, 32);
        PieChartView.Block block4 = new PieChartView.Block(Color.CYAN, 18);
        chartView.setBlocks(new PieChartView.Block[] {block1, block2, block3, block4}, 2);
    }
}
