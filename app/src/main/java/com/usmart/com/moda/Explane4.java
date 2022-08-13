package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Explane4 extends Activity {
    Activity activity = Explane4.this;
    Button btn_Next ;
    TextView MainText , Title;



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
        setContentView(R.layout.activity_explan4);

        Title = findViewById(R.id.Title);
        MainText = findViewById(R.id.MainText);
        btn_Next = findViewById(R.id.btn_Next);


        btn_Next.setOnClickListener(v -> {
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
