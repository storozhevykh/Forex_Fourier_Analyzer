package com.storozhevykh.forexfourieranalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout layout;
    Animation lastAnimation;
    TextView textGetStarted;
    Animation startAnimation;
    boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textWelcome = findViewById(R.id.text_welcome);
        textGetStarted = findViewById(R.id.text_start);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.start_app);
        textWelcome.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(this, R.anim.get_started);
        animation.setStartOffset(3000 + 90 * 10 * 3);
        textGetStarted.startAnimation(animation);

        layout = findViewById(R.id.start_layout);
        drawLines();


        //layout.addView(new Point(this, 200, 700));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastAnimation != null) {
            if (lastAnimation.hasEnded()) {
                Log.i("point", "lastAnimation.hasEnded");
                setContentView(R.layout.start_working);
            }
        }
    }

    private void drawLines() {

        //layout.addView(new FourierLines(this, 100, 700));
        double pi = Math.PI;
        drawPoints(80, Color.RED, 3000, pi / 2);
        drawPoints(40, Color.BLUE, 3000 + 90 * 10, pi);
        drawPoints(60, Color.GREEN, 3000 + 90 * 10 * 2, 3 * pi / 2);
    }

    private void drawPoints(int amplitude, int color, int timeOffset, double phase) {
        int y;
        double omega = 0.01;
        TextView[] textPoints = new TextView[90];
        for (int i = 0; i < textPoints.length; i++) {
            textPoints[i] = new TextView(getApplicationContext());
            textPoints[i].setText(".");
            textPoints[i].setTextSize(40);
            textPoints[i].setTextColor(color);
            //textPoints[i].setVisibility(View.INVISIBLE);
            y = (int) Math.round(800 + amplitude * Math.sin(omega * (100 + i * 10) + phase));
            textPoints[i].setX(100 + i * 10);
            textPoints[i].setY(y);
            layout.addView(textPoints[i]);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
            animation.setStartOffset(timeOffset + i * 10);
            textPoints[i].startAnimation(animation);
            if (color == Color.GREEN && i == textPoints.length - 1) {
                lastAnimation = animation;
            }
        }
    }

    public void start_click(View view) {
        textGetStarted.setTextColor(getResources().getColor(R.color.get_started_clicked));
        textGetStarted.setBackgroundColor(getResources().getColor(R.color.get_started));

        startAnimation = AnimationUtils.loadAnimation(this, R.anim.starting);
        layout.startAnimation(startAnimation);

        Timer timer = new Timer(50);
        timer.start();
    }

    private class Timer extends CountDownTimer {

        public Timer(long countDownInterval) {
            super(Integer.MAX_VALUE, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (startAnimation.hasEnded()) {
                setContentView(R.layout.start_working);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.starting2);
                layout.startAnimation(animation);
                cancel();
            }
        }

        @Override
        public void onFinish() {
        }
    }

    private class FourierLines extends View {

        private int x;
        private int y;
        private double omega = 0.01;
        private double pi = Math.PI;
        boolean calculated = false;

        public FourierLines(Context context, int x, int y) {
            super(context);
            this.x = x;
            this.y = y;
            //Log.i("point", "x = " + x + "; y = " + y);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStrokeWidth(10);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Log.i("point", "running...");
                    for (int i = 0; i < 900; i+=10) {
                        y = (int) Math.round(700 + 50 * Math.sin(omega * (x + i) + pi/2));
                        //Log.i("point", "x = " + (omega * (x + i) + pi/2) + "; y = " + y);
                        canvas.drawPoint(x + i,y,p);
                        try {
                            Log.i("point", "sleep...");
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FourierLines.this.invalidate();
                    }
                    calculated = true;
                }
            }).start();

            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!calculated)
                    invalidate();
                }
            }).start();*/

            //invalidate();
        }
    }

}