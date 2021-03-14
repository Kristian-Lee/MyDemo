package com.example.mydemo.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mydemo.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bnve.enableAnimation(true);
        bnve.enableShiftingMode(true);
        bnve.enableItemShiftingMode(true);
        bnve.setTextSize(9);
    }
}