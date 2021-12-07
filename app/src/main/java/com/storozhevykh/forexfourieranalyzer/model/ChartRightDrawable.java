package com.storozhevykh.forexfourieranalyzer.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChartRightDrawable extends Drawable {

    private int height;
    private int dashIncrement;
    private int dashNum;
    private double maxHigh;
    private double priceIncrement;

    private Paint paint;
    private Paint textPaint;

    private Path path;

    public ChartRightDrawable(int height, int dashIncrement, int dashNum, double maxHigh, double priceIncrement) {
        this.height = height;
        this.dashIncrement = dashIncrement;
        this.dashNum = dashNum;
        this.maxHigh = maxHigh;
        this.priceIncrement = priceIncrement;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new Paint();
        textPaint.setColor(Color.DKGRAY);
        textPaint.setStrokeWidth(1);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(28);

        path = new Path();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawLine(0, 0, 0, height, paint);
        //canvas.drawLine(0, dash_1, 10, dash_1, paint);
        for (int i = 1; i <= dashNum; i++) {
            canvas.drawLine(0, dashIncrement * i, 10, dashIncrement * i, paint);
            path.reset();
            path.moveTo(16, dashIncrement * i);
            path.lineTo(200, dashIncrement * i);
            String priceStr = String.format("%.5f", maxHigh - priceIncrement * i);
            canvas.drawTextOnPath(priceStr.substring(0, Math.min(7, priceStr.length())), path, 0, 8, textPaint);
        }
        path.reset();
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
