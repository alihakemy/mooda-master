package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import constants.Values;
import dataInLists.DataInUser;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.ProductHolder;
import helpers.UserHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {
    SharedPreferences Login;
    DataInUser UserData;
    String status, Userid, User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splach);
        // ***********************************************
       /* FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    String token = task.getResult().getToken();
                    Log.d("MyFirebaseMsgService", token + "");
                });*/

        FirebaseMessaging.getInstance().subscribeToTopic("USMART_Q8CARADS");
        FirebaseMessaging.getInstance().subscribeToTopic("moda");


        ProductHolder.getInstance().setData(Integer.toString(0));
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getBaseContext()));
        if (Locale.getDefault().getISO3Language().equals("ara")) {
            LangHolder.getInstance().setData("ar");
        } else {
            LangHolder.getInstance().setData("ar");
        }
        new Timer().schedule(new TimerTask() {
            public void run() {

                register();
                Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                status = Login.getString("isLogin", "logout");
                Userid = Login.getString("UserID", "0");
                Userid = Login.getString("UserID", "0");
                User = Login.getString("User", "");

                Gson g = new Gson();
                Type t = new TypeToken<DataInUser>() {
                }.getType();
                UserData = g.fromJson(User, t);

                checkAds();


            }
        }, 3000);
    }

    public void register() {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "visitors/create/en/v1";
        Log.i("TestApp_3_Error", Url + "");
        String json = new StringBuilder()
                .append("{")
                .append("\"unique_id\":\"" + Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"fcm_token\":\"" + FirebaseInstanceId.getInstance().getToken() + "\",")
                .append("\"type\":\"" + 2 + "\"")
                .append("}").toString();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Values.Authorization_User + "")
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
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInUser>() {
                    }.getType();

                    Log.i("TestApp_3_Error", response.body().string() + "");
                } catch (Exception e) {
                    Log.i("TestApp_3_Error", e.getMessage() + "");
                }
            }

        });
    }

    private void checkAds() {
      /*  AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "check_ad/" + LangHolder.getInstance().getData() + "/v1";
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + Values.Authorization_User);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);*/
        try {
                    /*Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);*/
            if (status.equals("logout")) {
                LoginHolder.getInstance().setData("logout");
                FaceIdHolder.getInstance().setData("0");
                UserHolder.getInstance().setData();
                SharedPreferences.Editor editor = Login.edit();
                editor.putString("isLogin", "logout");
                editor.putString("UserID", "");
                editor.putString("User", "");
                editor.commit();


                if (NetWork.isNetworkAvailable(getApplicationContext()) == true) {
                    Intent intent;
                           /* if (_Data.data.show_ad) {
                                intent = new Intent(MainActivity.this, Home.class);
                            } else {*/
                    intent = new Intent(MainActivity.this, Home.class);
                    //  }

                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                } else {
                    NetWork.gotoError(MainActivity.this);
                }

            } else {
                LoginHolder.getInstance().setData("login");
                FaceIdHolder.getInstance().setData(Userid);
                UserHolder.getInstance().setData(UserData.data);

                if (NetWork.isNetworkAvailable(getApplicationContext()) == true) {
                    Intent intent;
                           /* if (_Data.data.show_ad) {
                                intent = new Intent(MainActivity.this, Home.class);
                            } else {*/
                    intent = new Intent(MainActivity.this, Home.class);
                    //   }
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
                } else {
                    NetWork.gotoError(MainActivity.this);
                }
            }

        } catch (Exception e) {
            Log.i("TestApp", e.getMessage());
        }

           /* }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                super.onFailure(arg0);
                Log.i("TestApp", arg0.getMessage());
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                super.onFinish();
            }
        });*/

    }

}
