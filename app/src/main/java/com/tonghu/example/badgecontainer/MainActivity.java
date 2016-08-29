package com.tonghu.example.badgecontainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private BadgeContainer badgeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        badgeContainer = (BadgeContainer) findViewById(R.id.badgeContainer);
        badgeContainer.setNum(80);
    }
}
