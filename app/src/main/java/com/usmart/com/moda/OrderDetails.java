package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
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

import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListCheckout;
import customLists.CustomListOrderProducts;
import dataInLists.DataInLogin;
import dataInLists.DataInOrderItem;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserHolder;


public class OrderDetails extends Activity {

    Activity activity = OrderDetails.this;
    SharedPreferences Login;
    DataInOrderItem _Data = new DataInOrderItem();

    ExpandableHeightListView lvCart;
    CustomListOrderProducts adapterCarts;
    TextView MainTitle, tv_Order, tv_Date,
            tv_Address, tv_Payment, tv_SubTotalOrders,
            tv_Total, tv_TotalDelivery;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    ProgressBar prog;
    int ID;
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
        setContentView(R.layout.activity_order_details);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        ID = getIntent().getExtras().getInt("ID");

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);

        lvCart = findViewById(R.id.lvItems);
        lvCart.setExpanded(true);

        adapterCarts = new CustomListOrderProducts(activity, new ArrayList<>());
        lvCart.setAdapter(adapterCarts);

        MainTitle = findViewById(R.id.MainTitle);
        tv_Order = findViewById(R.id.tv_Order);
        tv_Date = findViewById(R.id.tv_Date);
        tv_Address = findViewById(R.id.tv_Address);
        tv_Payment = findViewById(R.id.tv_Payment);
        tv_SubTotalOrders = findViewById(R.id.tv_SubTotalOrders);
        tv_Total = findViewById(R.id.tv_Total);

        tv_TotalDelivery = findViewById(R.id.tv_TotalDelivery);


        MainTitle.setText(R.string.OrderDetails);


        loadOrder();

    }

    private void loadOrder() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "orders/" + ID + "/" + lang + "/v1";

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
                    Type t = new TypeToken<DataInOrderItem>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    tv_Order.setText(_Data.data.order.main_order_number + "");
                    tv_Date.setText(_Data.data.order.date + "");
                    tv_Address.setText(_Data.data.address.street + " " + _Data.data.address.building + " " +
                            _Data.data.address.gaddah + " "+_Data.data.address.area);
                    tv_SubTotalOrders.setText(_Data.data.order.subtotal_price + " " + getResources().getString(R.string.DK));
                    tv_TotalDelivery.setText(_Data.data.order.delivery_cost + " " + getResources().getString(R.string.DK));
                    tv_Total.setText(_Data.data.order.total_price + " " + getResources().getString(R.string.DK));

                  /*  if (_Data.data.status == 1) {
                        tv_Status.setText(R.string.OrderStatus1);
                    } else if (_Data.data.status == 2) {
                        tv_Status.setText(R.string.OrderStatus2);
                    } else if (_Data.data.status == 3) {
                        tv_Status.setText(R.string.OrderStatus3);
                    }*/

                    if (_Data.data.order.payment_method == 1) {
                        tv_Payment.setText(R.string.KNet);
                    } else if (_Data.data.order.payment_method == 3) {
                        tv_Payment.setText(R.string.Cash);
                    }


                    adapterCarts.addAll(_Data.data.stores);
                    adapterCarts.notifyDataSetChanged();

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
        onBackPressed();
    }


}
