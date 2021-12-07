package com.storozhevykh.forexfourieranalyzer.model;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.database.App;

import java.util.List;

public class BarDrawable extends Drawable {
    private Paint paint;
    private Paint fourierPaint;
    private Paint baseLinePaint;
    private Paint harmonicPaint;
    private Rect rect;
    private Rect fourierRect;

    private int left;
    private int top;
    private int right;
    private int bottom;
    private int fourier;
    private int nextFourier;
    private int baseLine;
    private int nextBaseLine;
    private List<Integer> harmonics;
    private List<Integer> nextHarmonics;
    private int drawableSize;

    private boolean fill;

    @SuppressLint("ResourceAsColor")
    public BarDrawable(int left, int top, int right, int bottom, boolean fill, int fourier, int nextFourier, int baseLine, int nextBaseLine, int drawableSize,
                       List<Integer> harmonics, List<Integer> nextHarmonics) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.fill = fill;
        this.fourier = fourier;
        this.nextFourier = nextFourier;
        this.baseLine = baseLine;
        this.nextBaseLine = nextBaseLine;
        this.drawableSize = drawableSize;
        this.harmonics = harmonics;
        this.nextHarmonics = nextHarmonics;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        if (fill)
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        else
            paint.setStyle(Paint.Style.STROKE);
        rect = new Rect(left, top, right, bottom);

        fourierPaint = new Paint();
        fourierPaint.setColor(Color.RED);
        fourierPaint.setStrokeWidth(4);
        fourierPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fourierRect = new Rect(0, fourier + 4, drawableSize, fourier - 4);

        baseLinePaint = new Paint();
        baseLinePaint.setColor(Color.BLUE);
        baseLinePaint.setStrokeWidth(5);
        baseLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        harmonicPaint = new Paint();
        harmonicPaint.setColor(App.getInstance().getResources().getColor(R.color.variant));
        harmonicPaint.setStrokeWidth(2);
        harmonicPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(rect, paint);

        if (fourier > 0 && nextFourier > 0)
            canvas.drawLine(0, nextFourier, drawableSize, fourier, fourierPaint);

        if (baseLine > 0 && nextBaseLine > 0)
            canvas.drawLine(0, nextBaseLine, drawableSize, baseLine, baseLinePaint);

        if (harmonics != null && nextHarmonics != null) {
            for (int i = 0; i < nextHarmonics.size(); i++) {
                canvas.drawLine(0, nextHarmonics.get(i), drawableSize, harmonics.get(i), harmonicPaint);
            }
        }
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
