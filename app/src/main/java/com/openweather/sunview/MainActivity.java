package com.openweather.sunview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.openweather.sunviewlibrary.SunView;

public class MainActivity extends AppCompatActivity {
    private SunView sunView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sunView=(SunView)findViewById(R.id.sunView);
        sunView.setCurrentTime("17:35");
        sunView.setStartTime("6:05");
        sunView.setEndTime("18:01");
        sunView.setArcSolidColor("#6effe1a2");


    }
}
