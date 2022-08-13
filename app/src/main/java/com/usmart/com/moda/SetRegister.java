package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;

import dataInLists.DataInCheckUser;

import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import okhttp3.*;


public class SetRegister extends Activity {
    Activity activity = SetRegister.this;
    EditText ed_Pass, ed_Email, ed_Name, ed_Mobile;
    Button btn_register;
    TextView ForgetPassword, HasNewAccount, Login, Conditions, MainTitle;
    CountryCodePicker ed_Key;
    ImageView HideAll;
    ProgressBar prog;
    int ProdID;
    Switch Chk;
    DataInCheckUser LoginData;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
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
        setContentView(R.layout.activity_register);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        // ProdID = getIntent().getExtras().getInt("ProdID");
        Intent intent = getIntent();
        if (intent.hasExtra("ProdID")) {
            ProdID = intent.getExtras().getInt("ProdID");
        } else {
            ProdID = 0;
        }
        // ***********************************************

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        ed_Name = findViewById(R.id.edit_Name);
        ed_Pass = findViewById(R.id.edit_Password);
        ed_Email = findViewById(R.id.edit_Email);
        ed_Mobile = findViewById(R.id.edit_Mobile);
        ed_Key = findViewById(R.id.ed_Key);
        Chk = findViewById(R.id.Chk);
        btn_register = findViewById(R.id.btn_Set_Register);

        MainTitle = findViewById(R.id.MainTitle);
        Login = findViewById(R.id.Login);
        ForgetPassword = findViewById(R.id.ForgetPassword);
        HasNewAccount = findViewById(R.id.HasNewAccount);
        Conditions = findViewById(R.id.Conditions);

        MainTitle.setText(R.string.Register);

        Conditions.setOnClickListener(v -> {
            Intent i = new Intent(activity, Conditions.class);
            startActivity(i);
        });

        ForgetPassword.setOnClickListener(v -> {
            Intent i = new Intent(activity, ForgetPass.class);
            i.putExtra("ProdID", ProdID);
            startActivity(i);
        });

        Login.setOnClickListener(v -> {
            Intent i = new Intent(activity, Login.class);
            i.putExtra("ProdID", ProdID);
            startActivity(i);
            //overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
        });

        btn_register.setOnClickListener(v -> {
            Log.i("TestApp", ed_Key.getSelectedCountryCode());
            Log.i("TestApp", ed_Key.getSelectedCountryCodeWithPlus());
            if (!Chk.isChecked()) {
                loadMsg(getResources().getString(R.string.ConditionField));
                return;
            }
            if (ed_Name.getText().toString().matches("")
                    || ed_Pass.getText().toString().matches("") || ed_Email.getText().toString().matches("")
                    || ed_Mobile.getText().toString().matches("")) {
                loadMsg(getResources().getString(R.string.EmptyField));
                return;
            }
            if (Patterns.EMAIL_ADDRESS.matcher(ed_Email.getText().toString()).matches() == false) {
                loadMsg(getResources().getString(R.string.ValidMail));
                return;
            }
            if (Patterns.PHONE.matcher(ed_Mobile.getText().toString()).matches() == false) {
                loadMsg(getResources().getString(R.string.ValidMobile));
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);
            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "user/checkphoneexistanceandroid/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"phone\":\"" + ed_Key.getSelectedCountryCodeWithPlus() + ed_Mobile.getText().toString() + "\",")
                    .append("\"email\":\"" + ed_Email.getText().toString() + "\"")
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
                    Log.i("RegisterPhoneNumber", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        Gson g = new Gson();
                        Type t = new TypeToken<DataInCheckUser>() {
                        }.getType();
                        String Res = response.body().string();
                        Log.i("TestApp_5", Res);
                        LoginData = g.fromJson(Res, t);
                        //  LoginData = g.fromJson(response.body().string(), t);

                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message);
                            } else {
                                if (!LoginData.phone) {
                                    if (!LoginData.email) {
                                        Intent i = new Intent(activity, Activation.class);
                                        i.putExtra("ID", 0);
                                        i.putExtra("Mobile", ed_Key.getSelectedCountryCodeWithPlus() + ed_Mobile.getText().toString());
                                        i.putExtra("Name", ed_Name.getText().toString());
                                        i.putExtra("Email", ed_Email.getText().toString());
                                        i.putExtra("Password", ed_Pass.getText().toString());
                                        i.putExtra("ProdID", ProdID);
                                        i.putExtra("IsHome", true);

                                        startActivity(i);
                                    } else {
                                        loadMsg(getResources().getString(R.string.ExistEmail));
                                    }

                                } else {
                                    loadMsg(getResources().getString(R.string.ExistPhone));
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
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            if (LoginHolder.getInstance().getData().equals("login")) {

                // startActivity(new Intent(activity, Home.class));

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

