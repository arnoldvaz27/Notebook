package com.arnoldvaz27.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        setContentView(R.layout.splash_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this,
                    MainActivity.class);
            SplashScreen.this.startActivity(i);
            SplashScreen.this.finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}