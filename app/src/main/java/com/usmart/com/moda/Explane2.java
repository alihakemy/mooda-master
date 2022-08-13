package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;

public class Explane2 extends Activity {
    Activity activity = Explane2.this;
    TextView Welcome, MainText, Title, btn_Skip, btn_Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //*******************************************************
        Resources res = getApplicationContext().getResources();
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //*******************************************************
        setContentView(R.layout.activity_explan2);

        Title = findViewById(R.id.Title);
        MainText = findViewById(R.id.MainText);
        btn_Next = findViewById(R.id.btn_Next);
        btn_Skip = findViewById(R.id.btn_Skip);


        btn_Next.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Explane3.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
        });
        btn_Skip.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Home.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
        });

    }

    @Override
    public void onBackPressed() {

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }

}
