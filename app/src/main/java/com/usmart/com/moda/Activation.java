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
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Random;

import constants.Values;
import dataInLists.DataInUser;
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
import utils.OtpEditText;

public class Activation extends Activity {
    Activity activity = Activation.this;
    SharedPreferences Login;

    Button btn_Login;

    OtpEditText Mobile;
    ImageView HideAll;
    ProgressBar prog;
    TextView MobileNumer, ChaneNumer, ReSend, Timer;
    int ID, ProdID;
    boolean IsHome;
    String Phone, Password, Email, Name;
    DataInUser LoginData;
    String verificationId;
    int Second = 60;
    String lang;
    String CodeX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        super.onCreate(savedInstanceState);
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
        getWindow().setStatusBarColor(Color.parseColor("#CA0000"));
        getWindow().getDecorView().setSystemUiVisibility(0);
        //*******************************************************
        setContentView(R.layout.activity_sms_code);
        init();

        //************* Start Send Code **************
        //   sendVerificationCode(Phone);
        //*******************************************

        MobileNumer.setText(Phone + "");

        sendVerificationCode(Phone.replace("+", ""));


        ChaneNumer.setOnClickListener(v -> {
            Intent i = new Intent(activity, SetRegister.class);
            startActivity(i);

        });

        btn_Login.setOnClickListener(v -> {
            String str = Mobile.getText().toString();
            if (str.length() == 6) {
                verifyCode(Mobile.getText().toString());
            }
        });
        Timer.setOnClickListener(view -> {
            if (Second == 0) {
                sendVerificationCode(Phone);
            }
        });

        //******************** auto Check Code ***************
        Mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = Mobile.getText().toString();
                if (str.length() == 6) {
                    verifyCode(Mobile.getText().toString());

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        //**********************************************

    }

    private void init() {
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        // ******************** Receive data from previous activity ***************************
        ProdID = getIntent().getExtras().getInt("ProdID");
        ID = getIntent().getExtras().getInt("ID");
        Name = getIntent().getExtras().getString("Name");
        Email = getIntent().getExtras().getString("Email");
        Password = getIntent().getExtras().getString("Password");
        Phone = getIntent().getExtras().getString("Mobile");
        IsHome = getIntent().getExtras().getBoolean("IsHome");

        // ********************* Call Views  *****************
        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        btn_Login = findViewById(R.id.btn_Set_Login);
        Mobile = findViewById(R.id.edit_Code);
        MobileNumer = findViewById(R.id.MobileNumer);
        ChaneNumer = findViewById(R.id.ChaneNumer);
        ReSend = findViewById(R.id.ReSend);
        Timer = findViewById(R.id.Timer);
    }

    public String convertToArabic(String value) {
        String newValue = (((((((((((value + "")
                .replaceAll("1", "١")).replaceAll("2", "٢"))
                .replaceAll("3", "٣")).replaceAll("4", "٤"))
                .replaceAll("5", "٥")).replaceAll("6", "٦"))
                .replaceAll("7", "٧")).replaceAll("8", "٨"))
                .replaceAll("9", "٩")).replaceAll("0", "٠"));
        return newValue;
    }

    private void verifyCode(String code) {
        Log.d("TestApp_1", code + "");
        Log.d("TestApp_2", convertToArabic(CodeX) + "");
        if (code.equals(CodeX)) {
            if (IsHome) {
                register();
            } else {
                Intent i = new Intent(activity, NewPass.class);
                i.putExtra("ProdID", ProdID);
                i.putExtra("Mobile", Phone);
                startActivity(i);
            }
        }
    }

    private void sendVerificationCode(String number) {
        Log.e("PhoneNumberTest",number);
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        CodeX = getRandomNumberString();

//        Url="http://smsbox.com/smsgateway/services/messaging.asmx/Http_SendSMS?username=InstadealApp" +
//                "                &password=instadeal@2021&customerid=2195&sendertext=Masharia" +
//                "                &messagebody=Code%20Is%20:"+ CodeX +"&recipientnumbers="+ number  +
//                "                &defdate=&isblink=false&isflash=false";


        Url = "http://smsbox.com/smsgateway/services/messaging.asmx/Http_SendSMS?username=InstadealApp" +
                "&password=instadeal@2021&customerid=2195&sendertext=Masharia" +
                "&messagebody=Code%20Is%20:" + CodeX + "&recipientnumbers=" + number +
                "&defdate=&isblink=false&isflash=false";

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
                Log.i("TestAppMODA", arg1);
                try {
                    reSendTimer();

                } catch (Exception e) {
                    Log.i("TestApp_3_Error", e.getMessage() + "");
                }

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                super.onFailure(arg0);
                NetWork.gotoError(activity);
                Log.i("Android Code Error", arg0.getMessage() + "");

            }

            @Override
            public void onFinish() {
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                super.onFinish();

            }
        });

    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format(java.util.Locale.US, "%06d", number);
    }


    private void reSendTimer() {

        Handler handler = new Handler();
        int delay = 1000; //milliseconds
        Second = 60;
        handler.postDelayed(new Runnable() {
            public void run() {

                Timer.setText(Second + " " + getResources().getString(R.string.Second));
                Second--;
                if (Second > 0) {
                    handler.postDelayed(this, delay);
                } else {
                    Timer.setText(R.string.ResendClick);
                }
            }
        }, delay);

    }

    public void register() {
        HideAll.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "auth/register/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"name\":\"" + Name + "\",")
                .append("\"phone\":\"" + Phone + "\",")
                .append("\"email\":\"" + Email + "\",")
                .append("\"password\":\"" + Password + "\",")
                .append("\"unique_id\":\"" + Settings.Secure.getString(activity.getContentResolver(),
                        Settings.Secure.ANDROID_ID) + "\",")
                .append("\"type\":\"" + 2 + "\",")
                .append("\"fcm_token\":\"" + FirebaseInstanceId.getInstance().getToken() + "\"")
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
            public void onResponse(Call call, Response response){
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInUser>() {
                    }.getType();

                  //  LoginData = g.fromJson(response.body().string(), t);

                    String Res = response.body().string();
                    Log.i("TestApp_5", Res);
                    LoginData = g.fromJson(Res, t);

                    activity.runOnUiThread(() -> {
                        if (!LoginData.success) {
                            loadMsg(LoginData.message);
                        } else {
                            LoginHolder.getInstance().setData("login");
                            FaceIdHolder.getInstance().setData(LoginData.data.id + "");
                            UserHolder.getInstance().setData(LoginData.data);

                            Login = getSharedPreferences(Values.SharedPreferences_FileName, 0);
                            SharedPreferences.Editor editor = Login.edit();
                            editor.putString("isLogin", "login");
                            editor.putString("UserID", FaceIdHolder.getInstance().getData());
                            editor.putString("User", Res + "");
                            editor.commit();



                            if (ProdID > 0) {
                                Intent i = new Intent(activity, Product.class);
                                i.putExtra("ID", ProdID);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(activity, Home.class);
                                startActivity(i);
                            }

                        }
                    });

                } catch (Exception e) {
                    Log.i("TestApp_3_Error", e.getMessage() + "");
                }
            }

        });
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

        No.setText(R.string.OK);
        Text.setText(MSG + "");
        Yes.setVisibility(View.GONE);


        No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        try {
            if (OnlineHolder.getInstance().getData().equals("1")) {
                OnlineHolder.getInstance().setData("0");
                finish();
                startActivity(getIntent());

            } else if (NetWork.isNetworkAvailable(activity) == false) {
                NetWork.gotoError(activity);
            }
            if (LoginHolder.getInstance().getData().equals("login")) {

                startActivity(new Intent(activity, Login.class));
            }
        } catch (Exception e) {

        }
        try {
        } catch (Exception e) {
            NetWork.gotoError(activity);
        }
        super.onResume();

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }


}
