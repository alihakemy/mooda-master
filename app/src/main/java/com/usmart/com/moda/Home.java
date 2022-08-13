package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleObserver;

import com.daimajia.slider.library.Animations.DescriptionAnimation;

import customLists.SliderLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.ethanhua.skeleton.ViewSkeletonScreen;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import constants.Values;
import customLists.CustomListHomeAds;
import customLists.CustomSlider;
import dataInLists.DataInAllCats;
import dataInLists.DataInGlobal;
import dataInLists.DataInHome;
import helpers.ForceUpgradeManager;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.UpdateHolder;
import helpers.UserHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.ExpandableHeightGridView;

public class Home extends Activity implements LifecycleObserver {
    Activity activity = Home.this;
    DataInHome _Data = new DataInHome();
    HashMap<String, String> url_maps = new HashMap<>();
    HashMap<String, Integer> file_maps = new HashMap<>();
    SliderLayout mDemoSlider;
    ImageView HideAll;
    ProgressBar prog;
    String lang;
    TextView UserName, Mobile, tv_CartNum, App_Version;
    AdvanceDrawerLayout drawer;
    NavigationView NavigationView;
    RelativeLayout All;
    ExpandableHeightGridView lvMain;
    CustomListHomeAds adapterAds;
    private ForceUpgradeManager forceUpgradeManager;

    String twitter, instagram, whatsapp, facebook, snapChat;

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
        setContentView(R.layout.activity_home);

        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        mDemoSlider = findViewById(R.id.slider);

        tv_CartNum = findViewById(R.id.tv_CartNum);
        App_Version = findViewById(R.id.App_Version);
        App_Version.setText("Version : " + Values.Edit_App_Version + "");

        lvMain = findViewById(R.id.lvMain);
        lvMain.setExpanded(true);
        adapterAds = new CustomListHomeAds(activity, new ArrayList<>());
        lvMain.setAdapter(adapterAds);
        lvMain.setOnItemClickListener((parent, view, position, id) -> startActivity(new Intent(activity, StoreCats.class).putExtra("StoreID", _Data.data.content.get(position).id)));
        loadHome();


        drawer = findViewById(R.id.drawer_layout);
        drawer.setViewScale(Gravity.RIGHT, 0.9f);
        drawer.setRadius(Gravity.RIGHT, 500);
        drawer.setViewElevation(Gravity.RIGHT, 20f);
        All = findViewById(R.id.All);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(activity, drawer,
                null, R.string.OK, R.string.Yes) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                //  ActionBarHome.setBackgroundColor(Color.WHITE);
                All.setBackgroundColor(Color.WHITE);
                //   MenuButton.setImageResource(R.mipmap.ic_drawer);

            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // ActionBarHome.setBackground(getResources().getDrawable(R.drawable.border_top_right15_0));
                All.setBackground(getResources().getDrawable(R.drawable.border_top_right15));
                // MenuButton.setImageResource(R.mipmap.arrow);
            }
        };
        drawer.addDrawerListener(mDrawerToggle);

        NavigationView = findViewById(R.id.nav_view);
        View headerView = NavigationView.getHeaderView(0);
        UserName = headerView.findViewById(R.id.UserName);
        Mobile = headerView.findViewById(R.id.Mobile);
        Mobile.setVisibility(View.GONE);
        NavigationView.setItemIconTintList(null);
        if (LoginHolder.getInstance().getData().equals("login")) {
            UserName.setText(UserHolder.getInstance().getData().name + "");
            Mobile.setText(UserHolder.getInstance().getData().email + "");
        }
        NavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_01:
                    startActivity(new Intent(activity, Home.class));
                    break;
                case R.id.menu_02:
                    startActivity(new Intent(activity, AccountMenu.class));
                    break;
                case R.id.menu_03:
                    startActivity(new Intent(activity, Cart.class));
                    break;
                case R.id.menu_04:
                    startActivity(new Intent(activity, Offers.class));
                    break;
                case R.id.menu_05:
                    startActivity(new Intent(activity, Notis.class));
                    break;
                case R.id.menu_06:
                    startActivity(new Intent(activity, About.class));
                    break;
                case R.id.menu_07:
                    // startActivity(new Intent(activity, MyFavs.class));
                    break;
                case R.id.menu_08:
                    startActivity(new Intent(activity, Conditions.class));
                    break;
                case
                        R.id.menu_09:
                    startActivity(new Intent(activity, AddRequest.class));
                    break;
                case R.id.menu_10:
                    startActivity(new Intent(activity, Contact.class));
                    break;
                case R.id.menu_11:
                    startActivity(new Intent(activity, ReturnPolicy.class));
                    break;
                case R.id.menu_12:
                    startActivity(new Intent(activity, Delivery.class));
                    break;
            }
            return true;
        });


    }

    private void loadHome() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "home/" + lang + "/v1/";
        Log.i("TestApp", Url);
        client.addHeader("Content-Type", "application/json");
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
                super.onStart();
                HideAll.setVisibility(View.VISIBLE);
                prog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("TestApp", arg1);
                super.onSuccess(arg0, arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInHome>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    loadAds(_Data.data.slider);
                    adapterAds.clear();
                    adapterAds.notifyDataSetChanged();


                    adapterAds.addAll(_Data.data.content);
                    adapterAds.notifyDataSetChanged();

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
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                //loadCats();
            }
        });

    }

    private void loadAds(ArrayList<DataInHome.Ads> Data) {

        try {
            int x = 0;
            for (DataInHome.Ads _data : Data) {
                url_maps.put(" " + x + " - " + _data.content, Values.Link_Image + _data.image);
                file_maps.put(" " + x + " - " + _data.content, x);
                x++;
            }

            for (String name : url_maps.keySet()) {
                CustomSlider textSliderView = new CustomSlider(activity);
                textSliderView.getView().findViewById(com.daimajia.slider.library.R.id.description_layout)
                        .setBackgroundColor(Color.TRANSPARENT);

                textSliderView.image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(slider -> {
                            try {
                                if (Data.get(file_maps.get(name)).type == 1) {
                                   /* Intent i = new Intent(activity, Product.class);
                                    i.putExtra("ID", Integer.parseInt(Data.get(file_maps.get(name)).content));
                                    startActivity(i);*/
                                } else {
                                    Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                            Data.get(file_maps.get(name)).content));
                                    startActivity(browserIntent3);
                                }

                            } catch (Exception e) {
                                // Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                // add your extra information
                ///   textSliderView.getBundle().putString("extra", name + x);
                mDemoSlider.addSlider(textSliderView);

            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            mDemoSlider.setCustomIndicator(findViewById(R.id.custom_indicator));
            /// mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(6000);

        } catch (Exception e) {
            Log.i("TestLog", e.getMessage());
        }
    }

    private void socialMedia() {
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "getsocialmedia/" + LangHolder.getInstance().getData() + "/v1";
//        String json = new StringBuilder()
//                .append("{")
//                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
//                        Settings.Secure.ANDROID_ID) + "\"")
//                .append("}").toString();
//
//        RequestBody body = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                json
//        );
        Request request = new Request.Builder()
                .url(Url)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "" + Values.Authorization_User)
//                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();

                    Result = g.fromJson(response.body().string(), DataInGlobal.class);
                    Log.i("TestApp_5", Result.success + "gerg");

                    runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message, false);
                            }
                        } else {
                            instagram = Result.data.instegram;
                            whatsapp = Result.data.app_phone;
                            facebook = Result.data.facebook;
                            snapChat = Result.data.snap_chat;
                            twitter = Result.data.twitter;
                        }
                    });

                } catch (Exception e) {
                    Log.i("TestApp_5", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

        });
    }

    private void CartCount() {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/count/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Values.Authorization_User)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Result = g.fromJson(response.body().string(), t);


                    runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message, false);
                            }
                        } else {
                            tv_CartNum.setText(Result.data.count + "");
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    @Override
    protected void onResume() {
        // writeFileOnInternalStorage(activity, "braodcast.txt", "Test");
        CartCount();
        socialMedia();
        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.setTextSize(12);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
//        if (UpdateHolder.getInstance().getData()) {
//            onUpdateNeeded();
//        }
        super.onResume();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                //startActivity(new Intent(activity, Home.class));
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
                if (lang.equals("ar")) {
                    drawer.openDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
                // startActivity(new Intent(activity, AccountMenu.class));
                // overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                return true;
        }
        return false;
    };

    private void onUpdateNeeded() {

        //     if (temp != null) {
        androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("New version available")
                .setCancelable(false)
                .setMessage("Please update app for seamless experience.")
                .setPositiveButton("Continue",
                        (dialog1, which) -> redirectStore()).create();
        dialog.show();
        //  }
    }

    private void redirectStore() {
        Uri updateUrl = Uri.parse("market://details?id=" + getPackageName());
        final Intent intent = new Intent(Intent.ACTION_VIEW, updateUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadMsg(String MSG, boolean cart) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        No.setText(R.string.OK);
        Yes.setText(R.string.Login);
        Text.setText(MSG + "");
        Yes.setVisibility(View.GONE);


        No.setOnClickListener(v -> {
            dialog.dismiss();
            if (cart) {
                CartCount();
            }
        });
        /*No.setOnClickListener(v -> {
            dialog.dismiss();

            Intent i = new Intent(activity, Login.class);
            i.putExtra("ProdID", ID);
            startActivity(i);

        });*/
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    public void gotoBack(View v) {
        onBackPressed();
    }


    private void loadClose() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);


        Title.setText(R.string.ExitTitle);
        Text.setText(R.string.ExitMeg);

        Yes.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            finish();
            startActivity(intent);
        });
        No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public void gotoWhatsApp(View v) {
        String url = "https://api.whatsapp.com/send?phone="+whatsapp;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        //startActivity(new Intent(activity, Facebook.class));
    }

    public void gotoTwitter(View v) {
        openWebPage(twitter);
        //startActivity(new Intent(activity, Twitter.class));
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void gotoInstagram(View v) {
        openWebPage(instagram);
    }


    public void gotoCart(View v) {
        startActivity(new Intent(activity, Cart.class));
    }

    public void gotoMenu(View v) {
        if (lang.equals("ar")) {
            drawer.openDrawer(Gravity.RIGHT);
        } else {
            drawer.openDrawer(Gravity.LEFT);
        }

    }

    public void gotoSearch(View v) {
        startActivity(new Intent(activity, Search.class));
    }

    public void gotoSnapChat(View view) {
        openWebPage(snapChat);
    }
}
