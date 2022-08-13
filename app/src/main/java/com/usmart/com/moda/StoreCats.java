package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListCats;
import dataInLists.DataInAllCats;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserHolder;

public class StoreCats extends Activity {
    Activity activity = StoreCats.this;
    DataInAllCats _Data;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll, iv_Ad, Logo , Logo2;
    ProgressBar prog;
    TextView MainTitle, tv_Title;
    ExpandableHeightListView lvMain;
    CustomListCats adapterCats;
    String lang;
    int StoreID;


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
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));

        // ***********************************************
        setContentView(R.layout.activity_cats_levels);

        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        StoreID = getIntent().getExtras().getInt("StoreID");

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        iv_Ad = findViewById(R.id.iv_Ad);
        Logo = findViewById(R.id.Logo);
        Logo2 = findViewById(R.id.Logo2);
        tv_Title = findViewById(R.id.tv_Title);
        MainTitle = findViewById(R.id.MainTitle);
        lvMain = findViewById(R.id.lvMain);


        adapterCats = new CustomListCats(activity, new ArrayList<>());

        lvMain.setExpanded(true);

        lvMain.setAdapter(adapterCats);
        lvMain.setOnItemClickListener((parent, view, position, id) -> startActivity(new Intent(activity, Products.class)
                .putExtra("StoreID", StoreID)
                .putExtra("CatID", _Data.data.categories.get(position).id)
                .putExtra("CatName", _Data.data.categories.get(position).title)
        ));
        MainTitle.setText("");

        loadMainCats();
    }

    private void loadMainCats() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "store/categories/" + StoreID + "/" + lang + "/v1";
        Log.i("TestApp", Url);
        client.addHeader("Content-Type", "application/json");
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                    + " " + UserHolder.getInstance().getData().token.access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                skeletonScreen = Skeleton.bind(lvMain)
                        .load(R.layout.singel_load_list)
                        .show();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("TestApp", arg1);
                super.onSuccess(arg0, arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInAllCats>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    MainTitle.setText(_Data.data.store.name + "");
                    tv_Title.setText(_Data.data.store.name + "");
                    DisplayImageOptions options, options2;
                    options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.def)
                            .showImageForEmptyUri(R.mipmap.def)
                            .showImageOnFail(R.mipmap.def)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();

                    options2 = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.def_icon)
                            .showImageForEmptyUri(R.mipmap.def_icon)
                            .showImageOnFail(R.mipmap.def_icon)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();
                    ImageLoader.getInstance().displayImage(Values.Link_ImageHomeAds + _Data.data.store.cover, iv_Ad, options);
                    ImageLoader.getInstance().displayImage(Values.Link_ImageHomeCats + _Data.data.store.logo, Logo, options2);
                    ImageLoader.getInstance().displayImage(Values.Link_ImageHomeCats + _Data.data.store.logo, Logo2, options2);
                    //  BreadCrumb.setText(R.string.Cats);
                  /*  Log.i("TestApp", 5 % 3 + "");
                    Log.i("TestApp", 4 % 3 + "");
                    if ((_Data.data.sub_categories.size() % 3) != 0) {
                        for (int i = 0; i < (_Data.data.sub_categories.size() % 3); i++) {
                            DataInCatsLevels Obj = new DataInCatsLevels();
                            _Data.data.sub_categories.add(Obj.new Category(-1));
                        }
                    }*/

                    adapterCats.addAll(_Data.data.categories);
                    adapterCats.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.i("TestApp_11", e.getMessage());
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
                skeletonScreen.hide();
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
        //navigation.setSelectedItemId(R.id.navigation_account);
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
        super.onBackPressed();
    }

    public void gotoMenu(View v) {
        startActivity(new Intent(activity, Menu.class));
    }

    public void gotoCart(View v) {

    }

    public void gotoAction(View v) {

    }

    public void gotoSearch(View v) {

    }
}
