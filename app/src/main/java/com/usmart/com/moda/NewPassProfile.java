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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInUser;
import helpers.FaceIdHolder;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.UserHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewPassProfile extends Activity {
    Activity activity = NewPassProfile.this;
    SharedPreferences Login;
    Button btn_Login;
    DataInUser LoginData;
    EditText OldPassword , Password, ConfPassword;
    ImageView HideAll;
    ProgressBar prog;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
        setContentView(R.layout.activity_new_password_profile);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        // ***********************************************

        btn_Login = findViewById(R.id.btn_Set_Login);

        Password = findViewById(R.id.edit_Password);
        ConfPassword = findViewById(R.id.edit_ConfPassword);
        OldPassword = findViewById(R.id.edit_OldPassword);

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);


        btn_Login.setOnClickListener(v -> {
            if (Password.getText().toString().matches("") || ConfPassword.getText().toString().matches("")) {
                loadMsg(getResources().getString(R.string.EmptyField));
                return;
            }
            if (!Password.getText().toString().equals(ConfPassword.getText().toString())) {
                loadMsg(getResources().getString(R.string.PassValid));
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "user/resetpassword/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"old_password\":\"" + OldPassword.getText().toString() + "\",")
                    .append("\"password\":\"" + Password.getText().toString() + "\"")
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
                    .put(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("TestApp_5", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response)  {
                    try {

                        Gson g = new Gson();
                        Type t = new TypeToken<DataInUser>() {
                        }.getType();

                        LoginData = g.fromJson(response.body().string(), t);


                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message);
                            } else {
                                loadMsg(getResources().getString(R.string.UpdateDone));
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
            Intent i = new Intent(activity, UpdateProfile.class);
            startActivity(i);
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
        super.onResume();

    }


}
