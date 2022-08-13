
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
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListCheckout;
import dataInLists.DataInCartCheckout;
import dataInLists.DataInLogin;
import dataInLists.DataInReservation;
import helpers.FaceIdHolder;
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

public class Checkout extends Activity {
    Activity activity = Checkout.this;
    SharedPreferences Login;
    DataInCartCheckout _Data = new DataInCartCheckout();
    DataInReservation _DataRes = new DataInReservation();
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    TextView MainTitle, tv_PriceDetails, tv_ProductCount, tv_Products,
            tv_TotalOrdersPrice, tv_Price_Delivery,
            tv_Total, tv_Address, tv_City, tv_Mobile,tv_Area,
            tv_KNet, tv_Cash;
    CheckBox Chk_Cash, Chk_KNet;
    LinearLayout Cash, KNet;
    Button btn_Set_Checkout;
    Switch Chk;
    int AddressID, PaymentMethod;
    ExpandableHeightListView lvCart;
    CustomListCheckout adapterCarts;
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
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        // getWindow().getDecorView().setSystemUiVisibility(0);
        // ***********************************************
        setContentView(R.layout.activity_checkout);

        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        PaymentMethod = 3;
        AddressID = getIntent().getExtras().getInt("AddressID");

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);

        tv_PriceDetails = findViewById(R.id.tv_PriceDetails);
        tv_ProductCount = findViewById(R.id.tv_ProductCount);
        tv_Products = findViewById(R.id.tv_Products);

        tv_TotalOrdersPrice = findViewById(R.id.tv_TotalOrdersPrice);
        tv_Price_Delivery = findViewById(R.id.tv_Price_Delivery);
        tv_Total = findViewById(R.id.tv_Total);
        tv_Mobile = findViewById(R.id.tv_Mobile);
        tv_Address = findViewById(R.id.tv_Address);
        tv_City = findViewById(R.id.tv_City);
        tv_Area = findViewById(R.id.tv_Area);
        btn_Set_Checkout = findViewById(R.id.btn_Set_Checkout);
        tv_KNet = findViewById(R.id.tv_KNet);
        tv_Cash = findViewById(R.id.tv_Cash);
        Chk_Cash = findViewById(R.id.Chk_Cash);
        Chk_KNet = findViewById(R.id.Chk_KNet);
        Cash = findViewById(R.id.Cash);
        KNet = findViewById(R.id.KNet);
        Chk = findViewById(R.id.Chk);
        findViewById(R.id.LeftSide).setVisibility(View.GONE);


        MainTitle.setText(R.string.PaymentMethod);
        lvCart = findViewById(R.id.lvItems);
        adapterCarts = new CustomListCheckout(activity, new ArrayList<>());

        lvCart.setExpanded(true);

        lvCart.setAdapter(adapterCarts);

        //*********************************************************
        tv_KNet.setOnClickListener(v -> {
            PaymentMethod = 1;
            Chk_Cash.setChecked(false);
            Chk_KNet.setChecked(true);
        });


        tv_Cash.setOnClickListener(v -> {
            PaymentMethod = 3;
            Chk_Cash.setChecked(true);
            Chk_KNet.setChecked(false);
        });
        //*************************
        Chk_KNet.setOnClickListener(v -> {
            PaymentMethod = 1;
            Chk_Cash.setChecked(false);
            Chk_KNet.setChecked(true);
        });


        Chk_Cash.setOnClickListener(v -> {
            PaymentMethod = 3;
            Chk_Cash.setChecked(true);
            Chk_KNet.setChecked(false);
        });

        //*************************
        KNet.setOnClickListener(v -> {
            PaymentMethod = 1;
            Chk_Cash.setChecked(false);
            Chk_KNet.setChecked(true);
        });


        Cash.setOnClickListener(v -> {
            PaymentMethod = 3;
            Chk_Cash.setChecked(true);
            Chk_KNet.setChecked(false);
        });

        //******************************************************
        btn_Set_Checkout.setOnClickListener(v -> {
            if (Chk.isChecked()) {
                if (PaymentMethod == 0) {
                    loadMsg(getResources().getString(R.string.ValidPayment));
                } else {
                    createOrder();
                }
            }
            else{
                loadMsg(getResources().getString(R.string.ConditionField));
            }
        });

        loadData();
    }

    private void loadData() {

        HideAll.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/cart/getbeforeorder/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"address_id\":\"" + AddressID + "\",")
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
                    Type t = new TypeToken<DataInCartCheckout>() {
                    }.getType();
                    String XX = response.body().string();
                    Log.i("TestApp_5", XX);
                    _Data = g.fromJson(XX, t);


                    runOnUiThread(() -> {
                        if (!_Data.success) {
                            if (_Data.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(_Data.message);
                            }
                        } else {

                            tv_ProductCount.setText("( " + _Data.data.count + " )");
                            tv_TotalOrdersPrice.setText(_Data.data.subtotal_price + " " + getResources().getString(R.string.DK));
                            tv_Price_Delivery.setText(_Data.data.delivery_cost + " " + getResources().getString(R.string.DK));
                            tv_Total.setText(_Data.data.total_cost + " " + getResources().getString(R.string.DK));
                           /* btn_Set_Checkout.setText(getResources().getString(R.string.Pay) + " " +
                                    String.format(java.util.Locale.US, "%.3f", _Data.data.total_price) + " " + getResources().getString(R.string.DK));*/

                            tv_Mobile.setText(_Data.data.address.phone + "");
                            tv_Address.setText(_Data.data.address.title);
                            tv_Area.setText(_Data.data.address.area);
                            tv_City.setText(_Data.data.address.street + " , " + _Data.data.address.building + " , " + _Data.data.address.gaddah);


                            adapterCarts.addAll(_Data.data.cart);
                            adapterCarts.notifyDataSetChanged();
                            //getDeliveryCost(AddressID);

                        }
                    });

                } catch (Exception e) {
                }
            }

        });

        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);

    }

    private void createOrder() {

        HideAll.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);
        btn_Set_Checkout.setEnabled(false);

        OkHttpClient client = new OkHttpClient();

        String Url = Values.Link_service + "orders/create/" + lang + "/v1";
        Log.i("TestApp_12", Url);
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"address_id\":\"" + AddressID + "\",")
                .append("\"payment_method\":\"" + PaymentMethod + "\"")
                .append("}").toString();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Log.i("TestApp_12", json);
        String Auth;
        Auth = UserHolder.getInstance().getData().token.token_type
                + " " + UserHolder.getInstance().getData().token.access_token;

        Log.i("TestApp_12", Auth);
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
                    Type t = new TypeToken<DataInReservation>() {
                    }.getType();

                    String XX = response.body().string();
                    Log.i("TestApp_5", XX);
                    _DataRes = g.fromJson(XX, t);
                    // Log.i("TestApp55666",response.body().string()+"");
                    // _DataRes = g.fromJson(response.body().string(), t);


                    runOnUiThread(() -> {
                        if (!_Data.success) {
                            if (_Data.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(_Data.message);
                            }
                        } else {
                            if (PaymentMethod == 1) {
                                Intent i = new Intent(activity, Payment.class);
                                i.putExtra("ResData", _DataRes.data.url);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(activity, FinishOrder.class);
                                i.putExtra("ResData", XX);
                                startActivity(i);
                            }

                        }
                    });

                } catch (Exception e) {
                }
            }

        });
        //btn_Set_Checkout.setEnabled(true);

        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);

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

    public void gotoBack(View v) {
        super.onBackPressed();
    }

    public void gotoMenu(View v) {

    }

    public void gotoCart(View v) {

    }

    public void gotoSearch(View v) {

    }
}
