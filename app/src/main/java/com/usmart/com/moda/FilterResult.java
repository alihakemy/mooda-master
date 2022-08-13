package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import constants.Values;
import customLists.CustomListProducts;
import customLists.CustomSlider;
import customLists.SliderLayout;
import dataInLists.DataInProducts;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserHolder;


public class FilterResult extends Activity {
    Activity activity = FilterResult.this;
    SharedPreferences Login;
    DataInProducts _Data = new DataInProducts();
    GridView lv;
    SliderLayout mDemoSlider;
    HashMap<String, String> url_maps = new HashMap<>();
    HashMap<String, Integer> file_maps = new HashMap<>();
    CustomListProducts adapter2;
    TextView MainTitle;
    ImageView Logo2;
    ViewSkeletonScreen skeletonScreen;
    TabLayout MainTap;
    ImageView HideAll;
    ProgressBar prog;
    TextView tv_CartNum;
    boolean First = true;
    int SelectedIndex = 0;
    int SubIndex;
    int CatID, StoreID, Size;
    float From, To;
    int Type;
    String Results;
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
        getWindow().setStatusBarColor(Color.WHITE);
        // ***********************************************
        setContentView(R.layout.activity_gridview_products);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        Results = getIntent().getExtras().getString("Results");
        StoreID = getIntent().getExtras().getInt("StoreID");
        Type = getIntent().getExtras().getInt("Type");
        Size = getIntent().getExtras().getInt("Size");
        From = getIntent().getExtras().getFloat("From");
        To = getIntent().getExtras().getFloat("To");


        Type = 0;
        Size = 0;
        From = 0;
        To = 0;

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        mDemoSlider = findViewById(R.id.slider);
        Logo2 = findViewById(R.id.Logo2);

        tv_CartNum = findViewById(R.id.tv_CartNum);

        MainTitle = findViewById(R.id.MainTitle);

        lv = findViewById(R.id.listViewOrders);

        adapter2 = new CustomListProducts(activity, new ArrayList<>());
        lv.setAdapter(adapter2);

        //  MainTitle.setText(CatName + "");
        MainTap = findViewById(R.id.Tab);


        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent i;
            if (_Data.data.products.data.get(position).type == 1) {
                i = new Intent(activity, Product.class);
            } else {
                i = new Intent(activity, Product2.class);
            }

            i.putExtra("ID", _Data.data.products.data.get(position).id);
            startActivity(i);
        });

        MainTap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                LinearLayout List = tab.getCustomView().findViewById(R.id.List);
                TextView CatTitle = tab.getCustomView().findViewById(R.id.tv_Title);
                CatTitle.setTextColor(Color.parseColor("#FFFFFF"));

                List.setBackgroundResource(R.drawable.btn_red_radius50);

                if (First == false) {
                    CatID = _Data.data.categories.get(tab.getPosition()).id;
                    loadProducts(_Data.data.categories.get(tab.getPosition()).id);
                }
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

        loadMainCats();

    }


    private void loadMainCats() {
        try {
            Gson g = new Gson();
            Type t = new TypeToken<DataInProducts>() {
            }.getType();
            _Data = g.fromJson(Results, t);

            loadAds(_Data.data.slider);
            TabLayout.Tab fTab;
            DisplayImageOptions options2;

                  /*  options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.def_icon)
                            .showImageForEmptyUri(R.mipmap.def_icon)
                            .showImageOnFail(R.mipmap.def_icon)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();*/

            options2 = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.def_icon)
                    .showImageForEmptyUri(R.mipmap.def_icon)
                    .showImageOnFail(R.mipmap.def_icon)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            MainTitle.setText(_Data.data.store.name + "");
            ImageLoader.getInstance().displayImage(Values.Link_ImageHomeCats + _Data.data.store.logo, Logo2, options2);
            // _Data.data.sub_category_array.add(0, new DataInCatsLevels().new Category(0, getResources().getString(R.string.All)));
            for (DataInProducts.SubCats _data : _Data.data.categories) {
                fTab = MainTap.newTab();
                fTab.setCustomView(R.layout.singel_cat_tap);
                LinearLayout List = fTab.getCustomView().findViewById(R.id.List);
                TextView CatTitle = fTab.getCustomView().findViewById(R.id.tv_Title);
                ImageView CatImg = fTab.getCustomView().findViewById(R.id.iv_Feeds);
                CatImg.setVisibility(View.GONE);
                CatTitle.setTextColor(Color.parseColor("#000000"));
                List.setBackgroundResource(0);
                //  ImageLoader.getInstance().displayImage(Values.Link_Image + _data.image, CatImg, options);
                CatTitle.setText(_data.title + "");
                if (_data.id == CatID) {
                    SelectedIndex = SubIndex;
                }
                MainTap.addTab(fTab, SubIndex);
                SubIndex++;
            }


            new Handler().postDelayed(() -> {
                TextView CatTitle = MainTap.getTabAt(SelectedIndex).getCustomView().findViewById(R.id.tv_Title);
                LinearLayout List = MainTap.getTabAt(SelectedIndex).getCustomView().findViewById(R.id.List);
                CatTitle.setTextColor(Color.parseColor("#FFFFFF"));
                MainTap.getTabAt(SelectedIndex).select();

                //  loadProducts(MainID, 0);
                adapter2.clear();
                adapter2.notifyDataSetChanged();

                adapter2.addAll(_Data.data.products.data);
                adapter2.notifyDataSetChanged();

            }, 100);


        } catch (Exception e) {
            Log.i("TestApp0000", e.getMessage());
        }

    }

    private void loadProducts(int SubCat) {
        // SubCat = 0;

        AsyncHttpClient client = new AsyncHttpClient();
        String Url = "";

        Url = Values.Link_service + "store/category/" + StoreID + "/" + lang + "/v1?category_id=" + SubCat +
                "&type=" + Type + "&size_id=" + Size + "&from=" + From + "&to=" + To;
        Log.i("TestApp", Url);

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
                skeletonScreen = Skeleton.bind(lv)
                        .load(R.layout.singel_load_list)
                        .show();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                Log.i("TestApp_556", arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInProducts>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);
                    adapter2.clear();
                    adapter2.notifyDataSetChanged();

                    // _Data.data.sub_category_array.add(0, new DataInCatsLevels().new Category(0, getResources().getString(R.string.All)));
                    adapter2.addAll(_Data.data.products.data);
                    adapter2.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.i("TestApp_error", e.getMessage());
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
                super.onFinish();

            }
        });

    }

    private void loadAds(ArrayList<DataInProducts.Ads> Data) {

        try {
            int x = 0;
            for (DataInProducts.Ads _data : Data) {
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

        BottomNavigationViewEx navigation = findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.setTextSize(12);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //navigation.setSelectedItemId(R.id.navigation_cart);
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

    public void gotoAction(View v) {

    }

    public void gotoCart(View v) {
        //  startActivity(new Intent(activity, Home.class).putExtra("Pos", 2));
    }

    public void gotoSearch(View v) {
        startActivity(new Intent(activity, Filter.class).putExtra("StoreID", StoreID));
    }


}
