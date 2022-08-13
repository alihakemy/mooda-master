package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Locale;

import constants.Values;
import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;

public class About extends Activity {
    Activity activity = About.this;
    WebView webView;
    TextView MainTitle;
    String lang;

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
        getWindow().setStatusBarColor(Color.parseColor("#ca0000"));
        getWindow().getDecorView().setSystemUiVisibility(0);
        //*******************************************************
        setContentView(R.layout.activity_webview);
        init();

        MainTitle.setText(R.string.About);

        webView.loadUrl(Values.Link_page + "aboutapp/" + lang);
    }

    private void init() {
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        // ********************* Call Views  *****************
        MainTitle = findViewById(R.id.MainTitle);
        MainTitle.setText(R.string.About);
        webView = findViewById(R.id.webView);
        // ********************* Set Fonts *****************

        //********************* Pass Fonts *****************
        // *************************************************

        webView.setBackgroundColor(0x00000000);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

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

    public void gotURL(View v) {
        Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://u-smart.co/"));
        startActivity(browserIntent3);
    }


}
