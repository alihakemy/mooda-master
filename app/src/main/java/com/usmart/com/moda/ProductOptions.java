package com.usmart.com.moda;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import customLists.CustomListOptions;
import customLists.CustomListProperty;
import dataInLists.DataInProduct;
import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;

public class ProductOptions extends Activity {
    Activity activity = ProductOptions.this;

    ArrayList<DataInProduct.Properties> _Data = new ArrayList<>();
    ListView lv;
    CustomListProperty adapter2;
    ImageView HideAll;
    ProgressBar prog;
    TextView noData, MainTitle;
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
        // ***********************************************
        setContentView(R.layout.activity_listview_options);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        _Data = (ArrayList<DataInProduct.Properties>) getIntent().getExtras().get("Options");

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        lv = findViewById(R.id.listViewOrders);
        noData = findViewById(R.id.noData);
        MainTitle = findViewById(R.id.MainTitle);

        adapter2 = new CustomListProperty(activity, _Data);
        lv.setAdapter(adapter2);

        MainTitle.setText(R.string.Options);
        lv.setOnItemClickListener((parent1, view, position, id) -> {

        });


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
