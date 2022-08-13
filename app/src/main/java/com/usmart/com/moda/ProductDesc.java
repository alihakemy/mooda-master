package com.usmart.com.moda;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;

public class ProductDesc extends Activity {
    Activity activity = ProductDesc.this;
    ImageView HideAll;
    ProgressBar prog;
    TextView tv_Desc, MainTitle;
    String Desc;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        //*******************************************************
        Resources activityRes = getResources();
        Configuration activityConf = activityRes.getConfiguration();
        Locale newLocale = new Locale(LangHolder.getInstance().getData());
        activityConf.setLocale(newLocale);
        activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

        Resources applicationRes = getApplicationContext().getResources();
        Configuration applicationConf = applicationRes.getConfiguration();
        applicationConf.setLocale(newLocale);
        applicationRes.updateConfiguration(applicationConf,
                applicationRes.getDisplayMetrics());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#ca0000"));
        getWindow().getDecorView().setSystemUiVisibility(0);
        // ***********************************************
        setContentView(R.layout.activity_desc);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        Desc = getIntent().getExtras().getString("Description");

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        tv_Desc = findViewById(R.id.tv_Desc);
        MainTitle = findViewById(R.id.MainTitle);


        MainTitle.setText(R.string.Description);
        tv_Desc.setText(Desc + "");


    }

    @Override
    protected void onResume() {

        try {
            if (OnlineHolder.getInstance().getData().equals("1")) {
                OnlineHolder.getInstance().setData("0");
                finish();
                startActivity(getIntent());

            } else if (NetWork.isNetworkAvailable(activity) == false) {
                NetWork.gotoError(activity);
            }
        } catch (Exception e) {

        }
        super.onResume();

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }
}
