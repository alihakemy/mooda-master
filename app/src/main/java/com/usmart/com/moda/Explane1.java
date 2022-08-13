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

public class Explane1 extends Activity {
    Activity activity = Explane1.this;
    Button btn_Next;
    TextView Welcome, MainText;


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
        setContentView(R.layout.activity_explan1);

        Welcome = findViewById(R.id.Welcome);
        MainText = findViewById(R.id.MainText);
        btn_Next = findViewById(R.id.btn_Next);



        btn_Next.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Explane2.class);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);

        });

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }

}
