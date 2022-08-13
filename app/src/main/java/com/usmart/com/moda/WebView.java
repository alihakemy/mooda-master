package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Locale;

import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;

public class WebView extends Activity {
    Activity activity = WebView.this;
    android.webkit.WebView webView;
    TextView MainTitle;
    String lang;
    String Link;

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
        //*******************************************************
        setContentView(R.layout.activity_webview);
        init();
        Link = getIntent().getExtras().getString("Link");
        MainTitle.setText("");

        webView.loadUrl(Link + "");
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
