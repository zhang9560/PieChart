package com.example.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yhzhang on 2015/12/22.
 */
public class PieChartView extends View {

    public static final int NO_HIGHLIGHT_BLOCK = -1;

    public static final float START_ANGLE_OFFSET = -90;

    public static class Block {

        public Block(int color, long weight) {
            this.color = color;
            this.weight = weight;
        }

        private int color;
        private long weight;
        private float startAngle;
        private float sweepAngle;
    }

    public PieChartView(Context context) {
        super(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBlocks != null && mBlocks.length > 0) {
            RectF rect = null;
            Paint arcPaint = new Paint();
            arcPaint.setAntiAlias(true);
            arcPaint.setStyle(Paint.Style.FILL);

            int color = 0;
            float startAngle = 0;
            float sweepAngle = 0;
            for (int i = 0; i < mBlocks.length; i++) {
                if (mTotalSweepAngle <= mBlocks[i].startAngle + mBlocks[i].sweepAngle - START_ANGLE_OFFSET) {
                    rect = createRect(canvas, i == mHighlightBlockIndex);
                    color = mBlocks[i].color;
                    startAngle = mBlocks[i].startAngle;
                    sweepAngle = mTotalSweepAngle - (mBlocks[i].startAngle - START_ANGLE_OFFSET);
                    break;
                } else {
                    arcPaint.setColor(mBlocks[i].color);
                    canvas.drawArc(createRect(canvas, i == mHighlightBlockIndex), mBlocks[i].startAngle, mBlocks[i].sweepAngle, true, arcPaint);
                }
            }

            if (rect != null) {
                arcPaint.setColor(color);
                canvas.drawArc(rect, startAngle, sweepAngle, true, arcPaint);
            }

            if (mTotalSweepAngle < 360) {
                mTotalSweepAngle += mSteps;
                invalidate();
            }
        }
    }

    public void setBlocks(Block[] blocks, int highlightBlockIndex) {
        mBlocks = blocks;
        mHighlightBlockIndex = highlightBlockIndex;
        mTotalSweepAngle = 1;

        if (mBlocks != null && mBlocks.length > 0) {
            long totalWeight = 0;
            float startAngle = START_ANGLE_OFFSET;

            for (int i = 0; i < mBlocks.length; i++) {
                totalWeight += mBlocks[i].weight;
            }

            for (int i = 0; i < mBlocks.length; i++) {
                mBlocks[i].startAngle = startAngle;
                mBlocks[i].sweepAngle = (float)((double)(360 * mBlocks[i].weight) / totalWeight);
                startAngle += mBlocks[i].sweepAngle;
            }
        }
    }

    public void setSteps(int steps) {
        mSteps = steps > 0 ? steps : 1;
    }

    public void reset() {
        mTotalSweepAngle = 1;
        invalidate();
    }

    private RectF createRect(Canvas canvas, boolean highlight) {
        float density = getContext().getResources().getDisplayMetrics().density;
        int padding = highlight ? (int)(2 * density) : (int)(8 * density);
        int width = canvas.getWidth() - padding * 2;
        int height = canvas.getHeight() - padding * 2;
        RectF rect;

        if (width < height) {
            int radius = width / 2;
            rect = new RectF(padding, height / 2 - radius + padding, width + padding , height / 2 + radius + padding);
        } else {
            int radius = height / 2;
            rect = new RectF(width / 2 - radius + padding, padding, width / 2 + radius + padding, height + padding);
        }

        return rect;
    }

    private Block[] mBlocks;
    private int mHighlightBlockIndex = NO_HIGHLIGHT_BLOCK;
    private float mTotalSweepAngle = 1;
    private int mSteps = 1;
}
