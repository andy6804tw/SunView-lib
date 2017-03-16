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
        sunView.setStartTime("6:05");
        sunView.setEndTime("18:01");
        sunView.setCurrentTime("13:47");
        sunView.setArcSolidColor(getResources().getColor(R.color.ArcSolidColor));//拱型內部顏色
        sunView.setArcColor(getResources().getColor(R.color.ArcColor));//拱形虛線顏色
        sunView.setBottomLineColor(getResources().getColor(R.color.BottomLineColor));//拱形底線顏色
        sunView.setTimeTextColor(getResources().getColor(R.color.TimeTextColor));//字體顏色
        sunView.setSunColor(getResources().getColor(R.color.SunColor));//太陽顏色


    }
}
