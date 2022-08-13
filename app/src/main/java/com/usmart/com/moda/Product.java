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

import androidx.fragment.app.FragmentActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constants.Values;
import customLists.CustomListMultiOptions;
import customLists.CustomListSliderThumbs;
import customLists.CustomSliderNoPadding;
import dataInLists.DataInGlobal;
import dataInLists.DataInProduct;
import dataInLists.ImageString;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.ProductHolder;
import helpers.UserHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.ExpandableHeightGridView;
import utils.RecyclerItemClickListener;

public class Product extends FragmentActivity {
    Activity activity = Product.this;
    DataInProduct Data = new DataInProduct();

    ArrayList<String> Photos = new ArrayList<>();
    HashMap<String, String> url_maps = new HashMap<>();
    SliderLayout mDemoSlider;
    //   CustomListOptions adapterOptions;
    CustomListMultiOptions adapterMultiOptions;
    ImageView Logo2;
    TextView MainTitle;
    ImageView HideAll, btn_Favorite, VidIcon;
    EditText edit_Desc;
    TextView iv_Add, iv_Remove;
    ProgressBar prog;
    RecyclerView lvOffers;
    Button btn_Buy;
    TextView tv_Title, tv_Desc, tv_Price, tv_Delivery, tv_Return,
            tv_CartNum, tv_discount, tv_amount, tv_OptionTitle, tv_MultiOptionTitle, tv_remain , tv_Sizes;
    int ID;
    int SelectedOptID = 0;
    ExpandableHeightGridView lvOptions, lvMultiOptions;
    RelativeLayout Related;
    FrameLayout Specifications;
    String lang;
    boolean IsFav = false;
    ScrollView Scroll;

    RelativeLayout Options;
    float XPrice = 0;
    int Remain = 0;
    RecyclerView lvThumbs;
    CustomListSliderThumbs adapterSliderThumbs;
    ArrayList<ImageString> Thumbs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        setContentView(R.layout.activity_product);

        // ************ Custom Action Bar *****************
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
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        ID = getIntent().getExtras().getInt("ID");
        // ******************** Sliding Menu *****************


        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);
        mDemoSlider = findViewById(R.id.slider);
        Specifications = findViewById(R.id.Specifications);
        Options = findViewById(R.id.Options);
        lvOptions = findViewById(R.id.lvOptions);
        lvMultiOptions = findViewById(R.id.lvMultiOptions);
        tv_OptionTitle = findViewById(R.id.tv_OptionTitle);
        tv_MultiOptionTitle = findViewById(R.id.tv_MultiOptionTitle);
        Scroll = findViewById(R.id.Scroll);
        tv_CartNum = findViewById(R.id.tv_CartNum);
        tv_discount = findViewById(R.id.tv_discount);
        tv_amount = findViewById(R.id.tv_amount);
        iv_Add = findViewById(R.id.iv_Add);
        iv_Remove = findViewById(R.id.iv_Remove);
        VidIcon = findViewById(R.id.VidIcon);
        edit_Desc = findViewById(R.id.edit_Desc);
        tv_Sizes = findViewById(R.id.tv_Sizes);

        MainTitle = findViewById(R.id.MainTitle);
        Logo2 = findViewById(R.id.Logo2);

        lvThumbs = findViewById(R.id.lvThumbs);

        lvMultiOptions.setExpanded(true);
        lvOptions.setExpanded(true);

        tv_Title = findViewById(R.id.tv_Title);
        tv_remain = findViewById(R.id.tv_remain);
        tv_Price = findViewById(R.id.tv_Price);
        tv_Return = findViewById(R.id.tv_Return);
        tv_Delivery = findViewById(R.id.tv_Delivery);
        tv_Desc = findViewById(R.id.tv_Desc);


        btn_Favorite = findViewById(R.id.img_Fav);
        btn_Buy = findViewById(R.id.btn_Buy);


        Scroll.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == 0) {
                // ActionBar.setBackgroundColor(Color.TRANSPARENT);
                // Log.i("TestApp_Pos", "Top");
                //  getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                //  Log.i("TestApp_Pos", "Not Top");
                //   getWindow().setStatusBarColor(Color.parseColor("#a9d44e"));

                // ActionBar.setBackgroundColor(Color.parseColor("#2699FB"));
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1);
        gridLayoutManager.setSpanCount(1);
        RecyclerView.LayoutManager layoutManager = new
                GridLayoutManager(activity, 1, LinearLayoutManager.VERTICAL, false);

        lvThumbs.setLayoutManager(layoutManager);


      /*  fav.setOnClickListener(v -> Fav());
        btn_Buy.setOnClickListener(v -> addCart((byte) 1));
        btn_Cart.setOnClickListener(v -> {
            addCart((byte) 0);
        });*/
        tv_Sizes.setOnClickListener(v -> {
            startActivity(new Intent(activity, Sizes.class));
        });

        tv_Delivery.setOnClickListener(v -> {
            startActivity(new Intent(activity, Delivery.class));
        });

        tv_Return.setOnClickListener(v -> {
            startActivity(new Intent(activity, ReturnPolicy.class));
        });

        btn_Buy.setOnClickListener(v -> {
            AddCart(ID);

        });
        Logo2.setOnClickListener(v -> {
            gotoStore();
        });
        btn_Favorite.setOnClickListener(view -> {
            if (IsFav) {
                RemoveFave(Data.data.product.id, btn_Favorite);
            } else {
                AddFave(Data.data.product.id, btn_Favorite);
            }
        });
        iv_Add.setOnClickListener(v -> {
            int n = Integer.parseInt(tv_amount.getText().toString());

            if (n <= Remain) {
                n++;
                tv_amount.setText(Integer.toString(n));
                float x = XPrice * n;
                tv_Price.setText(x + " " + getResources().getString(R.string.DK));
            } else {
                if (n > Remain) {
                    if (Remain > 0)
                        tv_amount.setText(Integer.toString(Remain));
                }
                loadMsg(getResources().getString(R.string.NotEnough), false);
            }

        });

        iv_Remove.setOnClickListener(v -> {
            int n = Integer.parseInt(tv_amount.getText().toString());
            if (n > 1) {
                n--;
                tv_amount.setText(Integer.toString(n));
                float x = XPrice * n;
                tv_Price.setText(x + " " + getResources().getString(R.string.DK));
            }

        });

        loadData();

    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "products/" + ID + "/" + lang + "/v1/";
        client.addHeader("Content-Type", "application/json");
        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                    + " " + UserHolder.getInstance().getData().token.access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }

        Log.i("TestApp", Url);
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
                    Type t = new TypeToken<DataInProduct>() {
                    }.getType();
                    Data = g.fromJson(arg1, t);
                    DisplayImageOptions options2;
                    options2 = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.def_icon)
                            .showImageForEmptyUri(R.mipmap.def_icon)
                            .showImageOnFail(R.mipmap.def_icon)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();

                    MainTitle.setText(Data.data.store.name + "");
                    ImageLoader.getInstance().displayImage(Values.Link_ImageHomeCats + Data.data.store.logo, Logo2, options2);


                    Remain = Data.data.product.remaining_quantity;
                    tv_Title.setText(Data.data.product.title);
                    tv_Price.setText(Data.data.product.final_price + " " + getResources().getString(R.string.DK));

                    tv_discount.setText(getResources().getString(R.string.Discount) + " - " + Data.data.product.offer_percentage + " %");

                    if (Data.data.product.multi_options == 1) {
                        tv_Price.setText(Data.data.product.multiple_option.option_values.get(0).option_data.final_price + " " + getResources().getString(R.string.DK));
                        tv_remain.setText(Data.data.product.multiple_option.option_values.get(0).option_data.remaining_quantity + " " + getResources().getString(R.string.RemainItems));
                        Remain = Data.data.product.multiple_option.option_values.get(0).option_data.remaining_quantity;
                        if (Data.data.product.offer == 0) {
                            //   txtDiscount.setVisibility(View.GONE);
                            tv_Price.setText(Data.data.product.multiple_option.option_values.get(0).option_data.final_price + " " + getResources().getString(R.string.DK));
                            XPrice = Float.parseFloat(Data.data.product.multiple_option.option_values.get(0).option_data.final_price);
                            tv_discount.setVisibility(View.GONE);

                        } else {
                            tv_discount.setVisibility(View.VISIBLE);
                            tv_Price.setText(Data.data.product.multiple_option.option_values.get(0).option_data.final_price + " " + getResources().getString(R.string.DK));
                            XPrice = Float.parseFloat(Data.data.product.multiple_option.option_values.get(0).option_data.final_price);
                        }
                    } else {
                        tv_Price.setText(Data.data.product.final_price + " " + getResources().getString(R.string.DK));
                        tv_remain.setText(Data.data.product.remaining_quantity + " " + getResources().getString(R.string.RemainItems));
                        Remain = Data.data.product.remaining_quantity;
                        if (Data.data.product.offer == 0) {
                            //   txtDiscount.setVisibility(View.GONE);
                            tv_Price.setText(Data.data.product.final_price + " " + getResources().getString(R.string.DK));
                            XPrice = Float.parseFloat(Data.data.product.final_price);
                            tv_discount.setVisibility(View.GONE);

                        } else {
                            tv_discount.setVisibility(View.VISIBLE);
                            tv_Price.setText(Data.data.product.final_price + " " + getResources().getString(R.string.DK));
                            XPrice = Float.parseFloat(Data.data.product.final_price);
                        }
                    }


                    int x = Data.data.product.description.length();
                    if (x >= 250) {
                        tv_Desc.setText(Html.fromHtml(Data.data.product.description.substring(0, 250)) + " ...");
                    } else {
                        tv_Desc.setText(Html.fromHtml(Data.data.product.description) + " ");
                    }
                    //  tv_Desc.setText(Html.fromHtml(Data.data.product.description));


                    tv_Desc.setOnClickListener(v -> {
                        Intent i = new Intent(activity, ProductDesc.class);
                        i.putExtra("Description", Data.data.product.description);
                        startActivity(i);
                    });
                    IsFav = Data.data.product.favorite;
                    if (Data.data.product.favorite) {

                        btn_Favorite.setImageResource(R.mipmap.like);
                    } else {
                        btn_Favorite.setImageResource(R.mipmap.non_like);
                    }


                    loadAds(Data.data.product.product_images);

                  /*  if (Data.data.product.properties.size() > 0) {
                       /* adapterOptions = new CustomListOptions(activity, Data.data.product.properties);
                        lvOptions.setAdapter(adapterOptions);* /
                        lvOptions.setOnItemClickListener((parent, view, position, id) -> {
                            Intent i = new Intent(activity, ProductOptions.class);
                            i.putExtra("Options", Data.data.product.properties);
                            startActivity(i);
                        });
                    } else {
                        Specifications.setVisibility(View.GONE);
                    }*/

                    if (Data.data.product.multi_options == 1) {
                        Data.data.product.multiple_option.option_values.get(0).selected = true;
                        SelectedOptID = Data.data.product.multiple_option.option_values.get(0).option_data.option_id;
                        adapterMultiOptions = new CustomListMultiOptions(activity, Data.data.product.multiple_option.option_values);
                        lvMultiOptions.setAdapter(adapterMultiOptions);
                        tv_MultiOptionTitle.setText(Data.data.product.multiple_option.option_name + "");
                        lvMultiOptions.setOnItemClickListener((parent, view, position, id) -> {
                            for (int i = 0; i < adapterMultiOptions.getCount(); i++) {
                                adapterMultiOptions.getItem(i).selected = false;
                            }
                            adapterMultiOptions.getItem(position).selected = true;
                            SelectedOptID = Data.data.product.multiple_option.option_values.get(position).option_data.option_id;
                            tv_Price.setText(Data.data.product.multiple_option.option_values.get(position).option_data.final_price + " " + getResources().getString(R.string.DK));
                            tv_remain.setText(Data.data.product.multiple_option.option_values.get(position).option_data.remaining_quantity + " " + getResources().getString(R.string.RemainItems));
                            Remain = Data.data.product.multiple_option.option_values.get(position).option_data.remaining_quantity;
                            //*******************************************
                            int n = Integer.parseInt(tv_amount.getText().toString());
                            if (n > Remain) {
                                tv_amount.setText(Integer.toString(Remain));
                            }
                            int nn = Integer.parseInt(tv_amount.getText().toString());
                            float xx = XPrice * nn;
                            tv_Price.setText(xx + " ");
                            //***************************
                            XPrice = Float.parseFloat(Data.data.product.multiple_option.option_values.get(position).option_data.final_price);
                            if (Data.data.product.offer == 0) {
                                //   txtDiscount.setVisibility(View.GONE);
                                tv_Price.setText(Data.data.product.multiple_option.option_values.get(position).option_data.final_price + " " + getResources().getString(R.string.DK));
                                XPrice = Float.parseFloat(Data.data.product.multiple_option.option_values.get(position).option_data.final_price);
                                tv_discount.setVisibility(View.GONE);

                            } else {
                                tv_discount.setVisibility(View.VISIBLE);
                                tv_Price.setText(Data.data.product.multiple_option.option_values.get(position).option_data.final_price + " " + getResources().getString(R.string.DK));
                                XPrice = Float.parseFloat(Data.data.product.multiple_option.option_values.get(position).option_data.final_price);
                            }
                            adapterMultiOptions.notifyDataSetChanged();
                        });
                    } else {
                        Options.setVisibility(View.GONE);
                    }


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

            }
        });

    }

    private void loadAds(ArrayList<DataInProduct.ProductImages> Data) {
        for (DataInProduct.ProductImages _data : Data) {
            if (_data.link.isEmpty())
                Thumbs.add(new ImageString(_data.image + "", false));
            else
                Thumbs.add(new ImageString(_data.image + "", false, (byte) 1));

            Photos.add(_data.image + "");
        }
        adapterSliderThumbs = new CustomListSliderThumbs(activity, Thumbs);
        lvThumbs.setAdapter(adapterSliderThumbs);

        lvThumbs.addOnItemTouchListener(
                new RecyclerItemClickListener(activity, lvThumbs, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        for (int i = 0; i < adapterSliderThumbs.getItemCount(); i++) {
                            Thumbs.get(i).selected = false;
                            adapterSliderThumbs.notifyItemChanged(i);
                        }
                        Thumbs.get(position).selected = true;
                        adapterSliderThumbs.notifyItemChanged(position);

                        int P = mDemoSlider.getCurrentPosition() - position;
                        if (P >= 1) {
                            mDemoSlider.movePrevPosition();
                        } else if (P <= -1) {
                            mDemoSlider.moveNextPosition();
                        }
                       /* if (P == 1 || P == -1)
                            if (mDemoSlider.getCurrentPosition() > position) {
                                //  mDemoSlider.setCurrentPosition( Data.size() - 1, false);
                                mDemoSlider.moveNextPosition();
                            } else {
                                mDemoSlider.movePrevPosition();
                            }*/
                        //   mDemoSlider.setCurrentPosition(position, false);
                        Log.i("TestAppClick", position + "");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        mDemoSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                lvThumbs.scrollToPosition(mDemoSlider.getCurrentPosition());
                for (int j = 0; j < adapterSliderThumbs.getItemCount(); j++) {
                    Thumbs.get(j).selected = false;
                    adapterSliderThumbs.notifyItemChanged(j);
                }
                Thumbs.get(mDemoSlider.getCurrentPosition()).selected = true;
                if (Thumbs.get(mDemoSlider.getCurrentPosition()).type == 1)
                    VidIcon.setVisibility(View.VISIBLE);
                else
                    VidIcon.setVisibility(View.GONE);
                adapterSliderThumbs.notifyItemChanged(mDemoSlider.getCurrentPosition());
            }

            @Override
            public void onPageSelected(int position) {
                lvThumbs.scrollToPosition(mDemoSlider.getCurrentPosition());
                for (int m = 0; m < adapterSliderThumbs.getItemCount(); m++) {
                    Thumbs.get(m).selected = false;
                    adapterSliderThumbs.notifyItemChanged(m);
                }
                Thumbs.get(mDemoSlider.getCurrentPosition()).selected = true;
                adapterSliderThumbs.notifyItemChanged(mDemoSlider.getCurrentPosition());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        try {
            /*int x = 0;
            for (String _data : Data) {
                Log.i("TestAppItem", _data + "");
                url_maps.put(" " + x + " - " + _data, Values.Link_Image + _data);
                file_maps.put(" " + x + " - " + _data, x);
                x++;
            }*/

            for (DataInProduct.ProductImages name : Data) {
                Log.i("TestAppItem2", name.image + "");
                CustomSliderNoPadding textSliderView = new CustomSliderNoPadding(activity);
                textSliderView.getView().findViewById(com.daimajia.slider.library.R.id.description_layout)
                        .setBackgroundColor(Color.TRANSPARENT);

                // textSliderView.image(url_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit)
                textSliderView.image(Values.Link_Image + name.image).setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(slider -> {
                            try {
                                lvThumbs.scrollToPosition(mDemoSlider.getCurrentPosition());
                                Log.i("TestAppSlider", mDemoSlider.getCurrentPosition() + "");
                                if (name.link.isEmpty()) {
                                    loadPhoto();
                                } else {
                                    startActivity(new Intent(activity, YouTubeVideo.class)
                                            .putExtra("Code", getYoutubeCode(name.link)));
                                }

                            } catch (Exception e) {
                                Log.i("TestAppSlider_Error", e.getMessage() + "");
                            }
                        });

                // add your extra information
                ///   textSliderView.getBundle().putString("extra", name + x);
                mDemoSlider.addSlider(textSliderView);

            }

            //    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
            mDemoSlider.setCustomIndicator(findViewById(R.id.custom_indicator));
            /// mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(6000);

        } catch (Exception e) {
            Log.i("TestApp", e.getMessage());
        }
    }

    private String getYoutubeCode(String Url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(Url);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }

    }

    private void AddFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(getResources().getString(R.string.LoginFirst));
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "favorites/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                // .append("\"type\":\"" + Type + "\",")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                        + " " + UserHolder.getInstance().getData().token.access_token)
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
                                i.putExtra("ProdID", ID);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message, false);
                            }
                        } else {
                            IsFav = true;
                            LikeIc.setImageResource(R.mipmap.like);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void RemoveFave(int ID, ImageView LikeIc) {
        if (LoginHolder.getInstance().getData().equals("logout")) {
            // ProductHolder.getInstance().setData(Integer.toString(ID));
            //  Activity.startActivity(new Intent(Activity, Login.class));
            loadMsgLogin(getResources().getString(R.string.LoginFirst));
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "favorites/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                //.append("\"type\":\"" + Type + "\",")
                .append("\"product_id\":\"" + ID + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                        + " " + UserHolder.getInstance().getData().token.access_token)
                .delete(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();
                    // Log.i("TestApp_555",response.body().string());
                    Result = g.fromJson(response.body().string(), t);


                    activity.runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                i.putExtra("ProdID", ID);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message, false);
                            }
                        } else {
                            Log.i("TestApp_555", "44444");
                            IsFav = false;
                            LikeIc.setImageResource(R.mipmap.non_like);
                        }
                    });

                } catch (Exception e) {
                    Log.i("TestApp_555", e.getMessage());
                }
            }

        });

    }

    private void AddCart(int ID) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/add/" + LangHolder.getInstance().getData() + "/v1";
        String json;
        if (SelectedOptID > 0) {
            json = new StringBuilder()
                    .append("{")
                    .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID) + "\",")
                    .append("\"product_id\":\"" + ID + "\",")
                    .append("\"option_id\":\"" + SelectedOptID + "\",")
                    .append("\"details\":\"" + edit_Desc.getText().toString() + "\",")
                    .append("\"product_number\":\"" + tv_amount.getText().toString() + "\"")
                    .append("}").toString();
        } else {
            json = new StringBuilder()
                    .append("{")
                    .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID) + "\",")
                    .append("\"product_id\":\"" + ID + "\",")
                    .append("\"details\":\"" + edit_Desc.getText().toString() + "\",")
                    .append("\"product_number\":\"" + tv_amount.getText().toString() + "\"")
                    .append("}").toString();
        }


        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        String Auth;
        if (LoginHolder.getInstance().getData().equals("login")) {
            Auth = UserHolder.getInstance().getData().token.token_type
                    + " " + UserHolder.getInstance().getData().token.access_token;
        } else {
            Auth = Values.Authorization_User;
        }
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "" + Auth)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    DataInGlobal Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    String Res = response.body().string();

                    Log.i("TestApp", Res);
                    Result = g.fromJson(Res, t);


                    runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message, false);
                            }
                        } else {
                            loadMsg(getResources().getString(R.string.AddedToCart), true);
                        }
                    });

                } catch (Exception e) {
                }
            }

        });

    }

    private void loadPhoto() {
        Intent i = new Intent(activity, PhotoGallery.class);
        i.putExtra("Photos", Photos);
        startActivity(i);
        //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

    private void loadMsgLogin(String MSG) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.OK);
        No.setText(R.string.Login);
        Text.setText(MSG + "");
        // No.setVisibility(View.GONE);

        Yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        No.setOnClickListener(v -> {
            dialog.dismiss();

            Intent i = new Intent(activity, Login.class);
            i.putExtra("ProdID", ID);
            startActivity(i);

        });
        dialog.show();
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

        CartCount();
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
        onBackPressed();
    }

    public void gotoBack() {
        onBackPressed();

    }

    public void gotoCart(View v) {
        startActivity(new Intent(activity, Cart.class));
    }

    public void gotoShare(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://onelink.to/mooda");
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.app_name));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share"));
    }

    public void gotoProperties(View v) {
        Intent i = new Intent(activity, ProductOptions.class);
        i.putExtra("Options", Data.data.product.properties);
        startActivity(i);
    }

    public void gotoStore() {
        startActivity(new Intent(activity, StoreCats.class)
                .putExtra("StoreID", Data.data.product.store_id));
    }

}
