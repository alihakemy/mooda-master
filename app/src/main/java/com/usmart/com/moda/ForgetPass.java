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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInUser;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetPass extends Activity {
    Activity activity = ForgetPass.this;

    Button btn_Login;
    DataInUser LoginData;
    EditText Mobile;
    CountryCodePicker ed_Key;
    ImageView HideAll;
    ProgressBar prog;
    TextView MainTitle, Conditions, HasNewAccount, Register;
    int ProdID;
    String lang;

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
           /* Intent intent = new Intent(this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
        //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        //*******************************************************
        setContentView(R.layout.activity_forget_password);

        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        Intent intent = getIntent();
        if (intent.hasExtra("ProdID")) {
            ProdID = intent.getExtras().getInt("ProdID");
        } else {
            ProdID = 0;
        }

        // ***********************************************

        Conditions = findViewById(R.id.Conditions);
        btn_Login = findViewById(R.id.btn_Set_Login);
        MainTitle = findViewById(R.id.MainTitle);
        HasNewAccount = findViewById(R.id.HasNewAccount);
        Register = findViewById(R.id.Register);

        Mobile = findViewById(R.id.edit_Mobile);
        ed_Key = findViewById(R.id.ed_Key);

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);


        Mobile = findViewById(R.id.edit_Mobile);
        MainTitle.setText(R.string.ForgetPass2);

        Conditions.setOnClickListener(v -> {
            Intent i = new Intent(activity, Conditions.class);
            startActivity(i);
        });

        Register.setOnClickListener(v -> {
            Intent i = new Intent(activity, SetRegister.class);
            i.putExtra("ProdID", ProdID);
            startActivity(i);
        });

        btn_Login.setOnClickListener(v -> {
            if (Mobile.getText().toString().matches("")) {
                loadMsg(getResources().getString(R.string.EmptyField));
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "user/checkphoneexistance/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"phone\":\"" + ed_Key.getSelectedCountryCodeWithPlus() + Mobile.getText().toString() + "\"")
                    .append("}").toString();

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    json
            );
            Request request = new Request.Builder()
                    .url(Url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", Values.Authorization_User)
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
                      //  Log.i("TestApp_5", response.body().string());
                        LoginData = g.fromJson(response.body().string(), t);


                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message);
                            } else {
                                Intent i = new Intent(activity, Activation.class);
                                i.putExtra("ID", LoginData.data.id);
                                i.putExtra("Mobile", LoginData.data.phone);
                                i.putExtra("ProdID", ProdID);
                                i.putExtra("Name", "");
                                i.putExtra("Email", "");
                                i.putExtra("Password", "");
                                i.putExtra("IsHome", false);
                                startActivity(i);


                                // loadMsg(LoginData.data.name + "");
                       /* Intent i = new Intent(activity, Activation.class);
                        i.putExtra("ID", x.data.id);
                        i.putExtra("Mobile", x.data.phone);
                        // i.putExtra("isCart", isCart);
                        startActivity(i);*/
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


}
