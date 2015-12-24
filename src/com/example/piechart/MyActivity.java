package com.example.piechart;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MyActivity extends Activity {

    private class GetDirSizeTask extends AsyncTask<File, Integer, Long> {

        @Override
        protected Long doInBackground(File... args) {
            if (args[0] != null && args[0].isDirectory()) {
                mRootDir = args[0];
                mDirQueue.add(args[0]);
            }

            File dir;
            while ((dir = mDirQueue.poll()) != null && !isCancelled()) {
                File[] files = dir.listFiles();

                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isDirectory()) {
                            mDirQueue.add(files[i]);
                        } else {
                            mFileList.add(files[i]);
                        }
                    }
                }
            }


            long totalSize = 0;
            for (int i = 0; i < mFileList.size(); i++) {
                if (!isCancelled()) {
                    totalSize += mFileList.get(i).length();
                    publishProgress(i * 100 / mFileList.size());
                }
            }

            return totalSize;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (!isCancelled()) {
                mProgressText.setText(String.format("%d%%", values[0]));
            }
        }

        @Override
        protected void onPostExecute(Long result) {
            if (!isCancelled() && mRootDir != null) {
                long dirSize = result;
                long total = mRootDir.getTotalSpace();
                long left = mRootDir.getFreeSpace();

                PieChartView.Block block1 = new PieChartView.Block(Color.RED, dirSize);
                PieChartView.Block block2 = new PieChartView.Block(Color.BLUE, total - dirSize - left);
                PieChartView.Block block3 = new PieChartView.Block(Color.GREEN, left);
                mChartView.setBlocks(new PieChartView.Block[] {block1, block2, block3}, 0);
                mChartView.setSteps(4);
                mChartView.invalidate();
            }
        }

        private File mRootDir;
        private Queue<File> mDirQueue = new ArrayDeque<>();
        private List<File> mFileList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/com.zenmen.zhangxin");
        new GetDirSizeTask().execute(dir);

        mChartView = (PieChartView)findViewById(R.id.chart);
        mProgressText = (TextView)findViewById(R.id.progress);
    }

    private PieChartView mChartView;
    private TextView mProgressText;
}
