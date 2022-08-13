package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListCart;
import dataInLists.DataInCart;
import dataInLists.DataInGlobal;
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

public class Cart extends Activity {
    Activity activity = Cart.this;
    DataInCart _Data = new DataInCart();
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll , Delete;
    ProgressBar prog;
    TextView MainTitle,
            tv_TotalOrdersTxt, tv_DeliveryTxt, tv_TotalTxt, tv_TotalOrdersPrice, tv_Price_Delivery,
            tv_Price_Delivery_Curr, tv_Total;
    Button btn_Set_Order;
    ExpandableHeightListView lvCart;
    CustomListCart adapterCarts;
    String lang;
    boolean edit = false ;


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
        setContentView(R.layout.activity_cart);

        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }


        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);


        MainTitle = findViewById(R.id.MainTitle);
        tv_TotalOrdersTxt = findViewById(R.id.tv_TotalOrdersTxt);
        tv_DeliveryTxt = findViewById(R.id.tv_DeliveryTxt);
        tv_TotalTxt = findViewById(R.id.tv_TotalTxt);
        tv_TotalOrdersPrice = findViewById(R.id.tv_TotalOrdersPrice);
        tv_Price_Delivery = findViewById(R.id.tv_Price_Delivery);
        tv_Price_Delivery_Curr = findViewById(R.id.tv_Price_Delivery_Curr);
        tv_Total = findViewById(R.id.tv_Total);
        Delete = findViewById(R.id.Delete);

        btn_Set_Order = findViewById(R.id.btn_Set_Order);


        lvCart = findViewById(R.id.lvItems);
        adapterCarts = new CustomListCart(activity, new ArrayList<>());

        lvCart.setExpanded(true);

        lvCart.setAdapter(adapterCarts);


        btn_Set_Order.setOnClickListener(v -> {

            Intent i = new Intent(activity, SelectAddress.class);
            // i.putExtra("AddressID", Integer.parseInt(AddressID));
            startActivity(i);
        });

        Delete.setOnClickListener(v -> {

            if (edit == false) {
                for (int i = 0; i < adapterCarts.getCount(); i++) {

                    adapterCarts.getItem(i).edit = true;

                }
                edit = true;
              //  Delete.setBackgroundResource(R.mipmap.delete);
            } else {
                for (int i = 0; i < adapterCarts.getCount(); i++) {

                    adapterCarts.getItem(i).edit = false;

                }
                edit = true;
              //  Delete.setBackgroundResource(R.mipmap.delete);
            }
            adapterCarts.notifyDataSetChanged();
        });

        loadData();
    }

    private void loadData() {
        skeletonScreen = Skeleton.bind(lvCart)
                .load(R.layout.singel_load_list)
                .show();

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/get/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\"")
                .append("}").toString();
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
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInCart>() {
                    }.getType();
                    String XX = response.body().string();
                    _Data = g.fromJson(XX, t);
                    Log.i("TestApp_5", XX);

                    runOnUiThread(() -> {
                        if (!_Data.success) {
                            if (_Data.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(_Data.message);
                            }
                        } else {

                            if (_Data.data.cart.size() > 0){
                                btn_Set_Order.setVisibility(View.VISIBLE);
                            }else {
                                btn_Set_Order.setVisibility(View.INVISIBLE);
                            }
                            adapterCarts.addAll(_Data.data.cart);
                            adapterCarts.notifyDataSetChanged();

                            tv_TotalOrdersPrice.setText("( " + _Data.data.subtotal_price + " " +
                                    getResources().getString(R.string.DK) + " )");
                            tv_Total.setText("( " + _Data.data.subtotal_price + " " +
                                    getResources().getString(R.string.DK) + " )");


                            skeletonScreen.hide();
                        }
                    });

                } catch (Exception e) {
                    Log.i("TestApp_5", e.getMessage());
                }
            }

        });


    }

    private void getDeliveryCost(int AdID) {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "delivery_price/" + lang + "/v1?address_id=" + AdID;
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + Values.Authorization_User);

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
                DataInGlobal Res;
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();
                    Res = g.fromJson(arg1, t);
                    tv_Price_Delivery.setText(Res.data.delivery_cost + "");

                    float Total = _Data.data.subtotal_price + Float.parseFloat(Res.data.delivery_cost);
                    tv_Total.setText(Total + "");
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
        startActivity(new Intent(activity, Home.class));
    }

    public void gotoBack(View v) {
        startActivity(new Intent(activity, Home.class));
    }

    public void gotoMenu(View v) {

    }

    public void gotoCart(View v) {

    }

    public void gotoSearch(View v) {

    }
}
