package com.usmart.com.moda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import constants.Values;
import customLists.CustomListCatOptions;
import customLists.CustomListMultiOptions;
import customLists.CustomListMultiOptionsFilter;
import dataInLists.DataInCatOptions;
import dataInLists.DataInFilterData;
import dataInLists.DataInGlobal;
import dataInLists.DataInOptions;
import dataInLists.DataInProducts;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.UserHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.ExpandableHeightGridView;
import utils.ImagesUtils;


public class Filter extends FragmentActivity {
    Activity activity = Filter.this;
    SharedPreferences Login;
    DataInFilterData _DataPrice = new DataInFilterData();
    CustomListMultiOptionsFilter adapterMultiOptions;
    ExpandableHeightGridView lvMultiOptions;
    Button btn_Add;
    TextView MainTitle, Max, Min;
    float MaxVal, MinVal;
    int Size, Type, CatId;
    CrystalRangeSeekbar Price;
    ImageView HideAll;
    ProgressBar prog;
    int StoreID;
    String lang;
    TabLayout MainTap;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        getWindow().setStatusBarColor(Color.parseColor("#ca0000"));
        getWindow().getDecorView().setSystemUiVisibility(0);
        // ***********************************************
        //*******************************************************
        setContentView(R.layout.activity_filter);
        // ***********************************************
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        StoreID = getIntent().getExtras().getInt("StoreID");
        CatId = 0;
        Type = 0;
        Size = 0;
        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        btn_Add = findViewById(R.id.btn_Add);
        Price = findViewById(R.id.Price);
        Max = findViewById(R.id.Max);
        Min = findViewById(R.id.Min);
        MainTap = findViewById(R.id.Tab);
        lvMultiOptions = findViewById(R.id.lvMultiOptions);
        lvMultiOptions.setExpanded(true);
        MainTitle = findViewById(R.id.MainTitle);

        Price.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            Max.setText(String.format(Locale.US, "%.2f", maxValue) + " " + activity.getResources().getString(R.string.DK3));
            Min.setText(String.format(Locale.US, "%.2f", minValue) + " " + activity.getResources().getString(R.string.DK3));
            MaxVal = (float) maxValue;
            MinVal = (float) minValue;
        });

        MainTap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                LinearLayout List = tab.getCustomView().findViewById(R.id.List);
                TextView CatTitle = tab.getCustomView().findViewById(R.id.tv_Title);
                CatTitle.setTextColor(Color.parseColor("#FFFFFF"));

                List.setBackgroundResource(R.drawable.btn_red_radius50);
                CatId = _DataPrice.data.categories.get(tab.getPosition()).id;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                LinearLayout List = tab.getCustomView().findViewById(R.id.List);
                TextView CatTitle = tab.getCustomView().findViewById(R.id.tv_Title);
                CatTitle.setTextColor(Color.parseColor("#000000"));

                List.setBackgroundResource(0);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       /* edit_Cat.setOnClickListener(view -> {
            Intent i = new Intent(activity, SelectMainCat.class);
            startActivityForResult(i, Values.PICK_ADS_CATEGORY);
            overridePendingTransition(R.anim.right_slide_in, R.anim.fade_out);
        });*/

        btn_Add.setOnClickListener(v -> {

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);
            btn_Add.setEnabled(false);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "store/category/" + StoreID + "/" + lang + "/v1?category_id=" + CatId +
                    "&type=" + Type + "&size_id=" + Size + "&from=" + MinVal + "&to=" + MaxVal;
            Log.i("TestApp_33", Url);
            MultipartBody.Builder body1 = new MultipartBody.Builder().setType(MultipartBody.FORM);
            body1.addFormDataPart("from_price", MinVal + "");
            body1.addFormDataPart("to_price", MaxVal + "");
            // if (AreaID > 0)


            RequestBody body = body1.build();
            Log.i("TestApp_33", body.toString());
            Request request = new Request.Builder()
                    .url(Url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                            + " " + UserHolder.getInstance().getData().token.access_token)
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("TestApp_5", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {


                    try {
                        String Res = response.body().string();
                        Log.i("TestApp_33", Res);
                        startActivity(new Intent(activity, FilterResult.class)
                                .putExtra("Results", Res)
                                .putExtra("StoreID", StoreID)
                                .putExtra("Type", Type)
                                .putExtra("Size", Size)
                                .putExtra("From", MinVal)
                                .putExtra("To", MaxVal)
                        );
                      /*  Gson g = new Gson();
                        Type t = new TypeToken<DataInEmptyArray>() {
                        }.getType();
                        String Res = response.body().string();
                        Log.i("TestApp_33", Res);
                        _DataRes = g.fromJson(Res, t);


                        activity.runOnUiThread(() -> {
                            if (!_DataRes.success) {
                                btn_Add.setEnabled(true);
                                HideAll.setVisibility(View.GONE);
                                prog.setVisibility(View.GONE);
                                loadMsg(_DataRes.message);
                            } else {
                                // loadMsg(_DataRes.data.title);
                                btn_Add.setEnabled(true);
                                HideAll.setVisibility(View.GONE);
                                prog.setVisibility(View.GONE);
                                startActivity(new Intent(activity, FinishOrder.class));
                            }
                        });*/

                    } catch (Exception e) {
                        Log.i("TestApp_3_Error", e.getMessage() + "");
                    }
                }

            });


        });
    }


    private void loadMinMaxPrice() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url = "";
        Url = Values.Link_service + "filter/" + StoreID + "/" + lang + "/v1";
        Log.i("TestApp", Url + "");
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
                // TODO Auto-generated method stub
                super.onStart();

            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                Log.i("TestApp_1", arg1 + "");

                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInFilterData>() {
                    }.getType();
                    _DataPrice = g.fromJson(arg1, t);

                    adapterMultiOptions = new CustomListMultiOptionsFilter(activity, _DataPrice.data.sizes);
                    lvMultiOptions.setAdapter(adapterMultiOptions);

                    Price.setMaxValue(_DataPrice.data.max_price);
                    Price.setMinValue(_DataPrice.data.min_price);
                    MinVal = _DataPrice.data.min_price;
                    MaxVal = _DataPrice.data.max_price;
                    Max.setText(String.format(Locale.US, "%.2f", _DataPrice.data.max_price) + " " + activity.getResources().getString(R.string.DK3));
                    Min.setText(String.format(Locale.US, "%.2f", _DataPrice.data.min_price) + " " + activity.getResources().getString(R.string.DK3));

                    lvMultiOptions.setOnItemClickListener((parent, view, position, id) -> {
                        for (int i = 0; i < adapterMultiOptions.getCount(); i++) {
                            adapterMultiOptions.getItem(i).selected = false;
                        }
                        adapterMultiOptions.getItem(position).selected = true;
                        Size = adapterMultiOptions.getItem(position).id;

                        adapterMultiOptions.notifyDataSetChanged();
                    });
                    TabLayout.Tab fTab;
                    int SubIndex = 0;
                    for (DataInFilterData.Cats _data : _DataPrice.data.categories) {
                        fTab = MainTap.newTab();
                        fTab.setCustomView(R.layout.singel_cat_tap);
                        LinearLayout List = fTab.getCustomView().findViewById(R.id.List);
                        TextView CatTitle = fTab.getCustomView().findViewById(R.id.tv_Title);
                        ImageView CatImg = fTab.getCustomView().findViewById(R.id.iv_Feeds);
                        CatImg.setVisibility(View.GONE);
                        CatTitle.setTextColor(Color.parseColor("#000000"));
                        List.setBackgroundResource(0);
                        CatTitle.setText(_data.title + "");

                        MainTap.addTab(fTab, SubIndex);
                        SubIndex++;
                    }
                    new Handler().postDelayed(() -> {
                        TextView CatTitle = MainTap.getTabAt(0).getCustomView().findViewById(R.id.tv_Title);
                        LinearLayout List = MainTap.getTabAt(0).getCustomView().findViewById(R.id.List);
                        CatTitle.setTextColor(Color.parseColor("#FFFFFF"));
                        List.setBackgroundResource(R.drawable.btn_red_radius50);
                        MainTap.getTabAt(0).select();


                    }, 100);
                } catch (Exception e) {
                    Log.i("TestApp_2", e.getMessage());

                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                super.onFailure(arg0);
                Log.i("TestApp", arg0.getMessage() + "");
                NetWork.gotoError(activity);
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                super.onFinish();
            }
        });

    }


    private void loadMsg(String MSG) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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

    public void setOnBack() {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        loadMinMaxPrice();
        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);
        btn_Add.setEnabled(true);

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
        super.onResume();

    }


    @Override
    public void onBackPressed() {
        setOnBack();
    }

    public void gotoBack(View v) {
        setOnBack();
    }
}

