package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInSocialMedia;
import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;


public class FirstAd extends Activity {
    Activity activity = FirstAd.this;
    ImageView MainPhoto;
    Button btn_Next;
    String lang;
    DataInSocialMedia _Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getWindow().setStatusBarColor(Color.parseColor("#0070E0"));
        getWindow().getDecorView().setSystemUiVisibility(0);
        //*******************************************************
        setContentView(R.layout.activity_ads);
        init();
    }

    private void init() {
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        // ********************* Call Views  *****************

        MainPhoto = findViewById(R.id.MainPhoto);
        btn_Next = findViewById(R.id.btn_Next);

        // *************************************************

        btn_Next.setOnClickListener(v -> startActivity(new Intent(activity, Home.class)));

        loadData();

    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "main_ad/" + lang + "/v1/";
        client.addHeader("Content-Type", "application/json");

        client.addHeader("Authorization", "" + Values.Authorization_User);

        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInSocialMedia>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    DisplayImageOptions options;
                    options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.def2)
                            .showImageForEmptyUri(R.mipmap.def2)
                            .showImageOnFail(R.mipmap.def2)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();

                    ImageLoader.getInstance().displayImage(Values.Link_Image + _Data.data.image, MainPhoto, options);

                } catch (Exception e) {
                    Log.i("TestApp", e.getMessage());
                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                super.onFailure(arg0);
                NetWork.gotoError(activity);
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                super.onFinish();

            }
        });

    }

    @Override
    protected void onResume() {
        try {
            if (OnlineHolder.getInstance().getData().equals("1")) {
                OnlineHolder.getInstance().setData("0");
                finish();
                startActivity(getIntent());
                //overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
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

    public void gotoSearch(View v) {
        startActivity(new Intent(activity, Search.class));
    }

    public void gotoCart(View v) {
        startActivity(new Intent(activity, Notis.class));
    }

    public void gotURL(View v) {
        Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://u-smart.co/"));
        startActivity(browserIntent3);
    }

}
