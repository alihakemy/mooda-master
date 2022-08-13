package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListOrders;
import customLists.CustomListOrdersMain;
import dataInLists.DataInOrders;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserHolder;


public class MyOrders extends Activity {

    Activity activity = MyOrders.this;
    SharedPreferences Login;
    DataInOrders _Data = new DataInOrders();

    GridView lv;
    CustomListOrdersMain adapter2;
    TextView MainTitle;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //	Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
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
       // getWindow().getDecorView().setSystemUiVisibility(0);
        // ***********************************************
        setContentView(R.layout.activity_listview_orders);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);

        lv = findViewById(R.id.listViewOrders);

        lv.setNumColumns(1);
        adapter2 = new CustomListOrdersMain(activity, new ArrayList<>());
        lv.setAdapter(adapter2);

        MainTitle = findViewById(R.id.MainTitle);

        MainTitle.setText(R.string.MyOrders);

      /*  lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(activity, OrderDetails.class);
            i.putExtra("ID", _Data.data.get(position).orders.id);
            startActivity(i);
        });*/

        loadOrders();

    }



    private void loadOrders() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "orders/" + lang + "/v1";

        client.addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                + " " + UserHolder.getInstance().getData().token.access_token);

        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
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
                    Type t = new TypeToken<DataInOrders>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    adapter2.addAll(_Data.data);
                    adapter2.notifyDataSetChanged();

                } catch (Exception e) {

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
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                super.onFinish();

            }
        });

    }


    private void loadMsg(String MSG) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        Text.setText(MSG + "");
        No.setVisibility(View.GONE);


        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.setTextSize(12);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

                startActivity(new Intent(activity, Login.class));
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                startActivity(new Intent(activity, Home.class));
                return true;
            case R.id.navigation_offer:
                startActivity(new Intent(activity, Offers.class));
                return true;
            case R.id.navigation_search:
                startActivity(new Intent(activity, Search.class));
                return true;
            case R.id.navigation_account:
                startActivity(new Intent(activity, AccountMenu.class));
                // overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                return true;
            case R.id.navigation_more:
                // startActivity(new Intent(activity, AccountMenu.class));
                // overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                return true;
        }
        return false;
    };


    public void gotoBack(View v) {
        onBackPressed();
    }
    public void gotoCart(View v) {
        startActivity(new Intent(activity, Cart.class));
    }

}
