package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Locale;

import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;

public class Payment extends Activity {
    Activity activity = Payment.this;
    WebView webView;
    TextView MainTitle;
    String lang;
    String Utl;

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
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        Utl = getIntent().getExtras().getString("ResData");
        // ********************* End Calendar *****************

        MainTitle = findViewById(R.id.MainTitle);
      //  MainTitle.setText(R.string.Payment2);
        webView = findViewById(R.id.webView);
        webView.setBackgroundColor(0x00000000);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(Utl + "");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                Log.i("PaymentUrl", url);
                if(url.contains("api/pay/success")){
                    Intent i = new Intent(activity, FinishOrder.class);
                    startActivity(i);
                }else if(url.contains("api/pay/error")){

                }
                return false; // then it is not handled by default action
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

    public void gotURL(View v) {
        Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://u-smart.co/"));
        startActivity(browserIntent3);
    }


}
