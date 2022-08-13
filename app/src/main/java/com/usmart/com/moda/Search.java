package com.usmart.com.moda;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListNameAutoComp;
import dataInLists.DataInSearch;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserHolder;

public class Search extends Activity {
    Activity activity = Search.this;
    AutoCompleteTextView edit_Search;
    CustomListNameAutoComp adapter;
    DataInSearch _Data = new DataInSearch();
    SharedPreferences Login;
    TextView MainTitle;
    ImageView HideAll;
    ProgressBar prog;

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
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        //*******************************************************
        setContentView(R.layout.activity_search_by_name);

        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }


        edit_Search = findViewById(R.id.edit_Search);
        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        MainTitle = findViewById(R.id.MainTitle);

        MainTitle.setText(R.string.Search);

        adapter = new CustomListNameAutoComp(activity, new ArrayList<>());
        edit_Search.setThreshold(3);
        edit_Search.setAdapter(adapter);
        edit_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.i("TestApp", charSequence.length() + "");
                if (charSequence.length() >= 3) {
                    loadHome(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void loadHome(String search) {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;
        String S = "";
        try {
            S = URLEncoder.encode(search, "UTF-8").replace(" ", "%20");
        } catch (UnsupportedEncodingException e) {

        }
        Url = Values.Link_service + "search/" + lang + "/v1?search=" + S;
        Log.i("TestApp", Url);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + Values.Authorization_User);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                HideAll.setVisibility(View.VISIBLE);
                prog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInSearch>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);
                    if (_Data.data.size() > 0) {
                        adapter.clear();
                        adapter.addAll(_Data.data);
                    } else {
                        adapter.clear();
                       /* DataInSearch.ProductDetails XX = new DataInSearch.ProductDetails();
                        ArrayList<DataInSearch.ProductDetails> List = new ArrayList<>();
                        DataInSearch o = new DataInSearch();
                        DataInSearch.ProductDetails obj = o.new ProductDetails(0, getResources().getString(R.string.NoSearch), "");
                        List.add(obj);
                        adapter.addAll(List);*/
                        Log.i("TestApp", adapter.getItem(0).title + "");

                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {

                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                super.onFailure(arg0);
                Log.i("TestApp", arg0.getMessage());
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                super.onFinish();
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {

        try {
            if (LoginHolder.getInstance().getData().equals("login")) {
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "login");
                editor.putString("UserID", FaceIdHolder.getInstance().getData());
                editor.putString("User", UserHolder.getInstance().getData().toString());
                editor.commit();

            } else {
                Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                LoginHolder.getInstance().setData("logout");
                FaceIdHolder.getInstance().setData("0");
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "logout");
                editor.putString("UserID", "0");
                editor.putString("User", "");
                editor.commit();

                //startActivity(new Intent(activity, Login.class));
            }
        } catch (Exception e) {

        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void gotoBack(View v) {
        onBackPressed();
    }

}
