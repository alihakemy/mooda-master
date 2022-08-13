package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListFavAds;
import dataInLists.DataInFavAds;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserHolder;

public class MyFav extends Activity {

    Activity activity = MyFav.this;
    SharedPreferences Login;
    DataInFavAds _Data = new DataInFavAds();
    GridView lv;
    CustomListFavAds adapter2;
    TextView MainTitle;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    TextView noData;
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
    //    getWindow().getDecorView().setSystemUiVisibility(0);
        // ***********************************************
        setContentView(R.layout.activity_listview_fav);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        findViewById(R.id.LeftSide).setVisibility(View.GONE);

        MainTitle = findViewById(R.id.MainTitle);

        MainTitle.setText(R.string.MyFavs);
        noData = findViewById(R.id.noData);

        lv = findViewById(R.id.listViewOrders);
        adapter2 = new CustomListFavAds(activity, new ArrayList<>());
        lv.setAdapter(adapter2);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Log.i("TestApp", position + "");
            Intent i = new Intent(activity, Product.class);
            i.putExtra("ID", _Data.data.get(position).id);
            startActivity(i);

        });


        loadData();

    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "favorites/" + lang + "/v1";
        Log.i("TestApp", Url);
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                    + " " + UserHolder.getInstance().getData().token.access_token);
            Log.i("TestApp", "" + UserHolder.getInstance().getData().token.token_type
                    + " " + UserHolder.getInstance().getData().token.access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                skeletonScreen = Skeleton.bind(lv)
                        .load(R.layout.singel_load_list)
                        .show();
                /*HideAll.setVisibility(View.VISIBLE);
                prog.setVisibility(View.VISIBLE);*/
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                Log.i("TestApp", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInFavAds>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);
                    adapter2.clear();
                    adapter2.notifyDataSetChanged();
                    if (_Data.data.size() > 0) {
                        adapter2.addAll(_Data.data);
                        adapter2.notifyDataSetChanged();
                        lv.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }


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
                skeletonScreen.hide();
               /* HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);*/
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
        super.onBackPressed();
    }

    public void gotoSearch(View v) {
        startActivity(new Intent(activity, Search.class));
    }

    public void gotoAction(View v) {

    }


}
