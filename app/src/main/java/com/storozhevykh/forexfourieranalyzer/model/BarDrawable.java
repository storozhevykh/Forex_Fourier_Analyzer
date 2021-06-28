package com.storozhevykh.forexfourieranalyzer.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BarDrawable extends Drawable {
    private Paint paint;
    private Rect rect;

    private int left;
    private int top;
    private int right;
    private int bottom;

    private boolean fill;

    public BarDrawable(int left, int top, int right, int bottom, boolean fill) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.fill = fill;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        if (fill)
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        else
            paint.setStyle(Paint.Style.STROKE);
        rect = new Rect(left, top, right, bottom);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(rect, paint);
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
