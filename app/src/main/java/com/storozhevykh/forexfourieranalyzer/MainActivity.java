package com.storozhevykh.forexfourieranalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textWelcome = (TextView) findViewById(R.id.text_welcome);
        /*Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.scale);*/
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.start_app);

        textWelcome.startAnimation(animation3);
        //textWelcome.startAnimation(animation2);
    }

    private void drawLines() {
        
    }
}