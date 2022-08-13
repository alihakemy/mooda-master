package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import constants.Values;
import customLists.CustomListMainOffers;
import customLists.CustomSlider;
import customLists.SliderLayout;
import dataInLists.DataInGlobal;
import dataInLists.DataInHome;
import dataInLists.DataInOffers;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Offers extends Activity {
    Activity activity = Offers.this;
    DataInOffers _Data = new DataInOffers();
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    HashMap<String, String> url_maps = new HashMap<>();
    HashMap<String, Integer> file_maps = new HashMap<>();
    SliderLayout mDemoSlider;
    TextView MainTitle;
    ExpandableHeightListView lvOffers;
    CustomListMainOffers adapterOffers;
    TextView tv_CartNum;
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
        getWindow().setStatusBarColor(Color.parseColor("#23293F"));
        getWindow().getDecorView().setSystemUiVisibility(0);
        // ***********************************************
        setContentView(R.layout.activity_offers);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);
        mDemoSlider = findViewById(R.id.slider);
        lvOffers = findViewById(R.id.listViewOrders);

        lvOffers.setExpanded(true);
        adapterOffers = new CustomListMainOffers(activity, new ArrayList<>());
        lvOffers.setAdapter(adapterOffers);

        tv_CartNum = findViewById(R.id.tv_CartNum);

        MainTitle.setText(R.string.Offers);

        loadData();
    }


    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;
        Url = Values.Link_service + "offers/" + lang + "/v1";
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

                skeletonScreen = Skeleton.bind(lvOffers)
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
                    Type t = new TypeToken<DataInOffers>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    loadAds(_Data.data.slider);
                    adapterOffers.addAll(_Data.data.offers);
                    adapterOffers.notifyDataSetChanged();


                } catch (Exception e) {
                    Log.i("TestApp_Error", e.getMessage());
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

    private void CartCount() {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/count/" + LangHolder.getInstance().getData() + "/v1";
        Log.i("TestApp", Url);
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
                //Log.i("TestAppCart",response.body().string());
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
                                loadMsg(Result.message);
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

    private void loadAds(ArrayList<DataInOffers.Slider> Data) {

        try {
            int x = 0;
            for (DataInOffers.Slider _data : Data) {
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
        CartCount();
        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_offer);
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
               // startActivity(new Intent(activity, Offers.class));
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

    }

    public void gotoCart(View v) {
        startActivity(new Intent(activity, Cart.class));
    }

    public void gotoSearch(View v) {
        startActivity(new Intent(activity, Search.class));
    }


}
