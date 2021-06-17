package com.storozhevykh.forexfourieranalyzer.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storozhevykh.forexfourieranalyzer.R;

public class MainActivity extends AppCompatActivity {

    RelativeLayout layout;
    TextView textGetStarted;
    Animation startAnimation;

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

        layout = findViewById(R.id.start_layout); //start_layout == activity_main
        drawLines();
    }

    private void drawLines() {
        // Draw animated Fourier lines
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
            y = (int) Math.round(800 + amplitude * Math.sin(omega * (100 + i * 10) + phase));
            textPoints[i].setX(100 + i * 10);
            textPoints[i].setY(y);
            layout.addView(textPoints[i]);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
            animation.setStartOffset(timeOffset + i * 10);
            textPoints[i].startAnimation(animation);
        }
    }

    public void start_click(View view) {
        // Listener for click on "Get Started" button
        textGetStarted.setTextColor(getResources().getColor(R.color.get_started_clicked));
        textGetStarted.setBackground(getResources().getDrawable(R.drawable.set_default_anim_border));

        startAnimation = AnimationUtils.loadAnimation(this, R.anim.starting);
        layout.startAnimation(startAnimation);

        // Timer is used to delay the open of the new activity
        Timer timer = new Timer(5);
        timer.start();
    }

    private class Timer extends CountDownTimer {

        public Timer(long countDownInterval) {
            super(Integer.MAX_VALUE, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (startAnimation.hasEnded()) {
                setContentView(R.layout.start_working); // set empty layout for smooth transition to BigMenuActivity
                Intent intent = new Intent(MainActivity.this, BigMenuActivity.class);
                startActivity(intent);
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