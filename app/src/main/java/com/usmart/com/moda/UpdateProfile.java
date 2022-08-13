package com.usmart.com.moda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;

import constants.Values;
import dataInLists.DataInLogin;
import dataInLists.DataInUserProfile;
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


public class UpdateProfile extends Activity {
    Activity activity = UpdateProfile.this;
    SharedPreferences Login;
    EditText ed_DOB, ed_Email, ed_Name, ed_Mobile;
    Button btn_Save;
    TextView ChangePass, MainTitle;
    ImageView HideAll;
    ProgressBar prog;
    DataInLogin LoginData;
    DataInUserProfile _Data = new DataInUserProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(activity, 2));
        //*******************************************************
        setContentView(R.layout.activity_profile);
        // ***********************************************

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        ed_Name = findViewById(R.id.edit_Name);
        ed_DOB = findViewById(R.id.edit_DOB);
        ed_Email = findViewById(R.id.edit_Email);
        ed_Mobile = findViewById(R.id.edit_Mobile);
        btn_Save = findViewById(R.id.btn_Set_Save);
        MainTitle = findViewById(R.id.MainTitle);

        ChangePass = findViewById(R.id.ChangePassword);

        MainTitle.setText(R.string.EditProfile);


        ChangePass.setOnClickListener(view -> {
            Intent i = new Intent(activity, NewPassProfile.class);
            startActivity(i);
        });

        btn_Save.setOnClickListener(v -> {
            if (ed_Name.getText().toString().matches("") || ed_Email.getText().toString().matches("")
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
            String Url = Values.Link_service + "user/profile/"+ LangHolder.getInstance().getData() +"/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"name\":\"" + ed_Name.getText().toString() + "\",")
                    .append("\"phone\":\"" + ed_Mobile.getText().toString() + "\",")
                    .append("\"email\":\"" + ed_Email.getText().toString() + "\",")
                    .append("\"gender\":\"\"")
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
                public void onResponse(Call call, Response response) throws IOException {
                    try {

                        Gson g = new Gson();
                        Type t = new TypeToken<DataInLogin>() {
                        }.getType();
                        String Res = response.body().string();
                        Log.i("TestApp_3_Error", Res + "");
                        LoginData = g.fromJson(Res, t);

                        activity.runOnUiThread(() -> {
                            if (!LoginData.success) {
                                loadMsg(LoginData.message_ar);
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

    private void loadProfile() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "user/profile/en/v1";
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                + " " + UserHolder.getInstance().getData().token.access_token);
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                HideAll.setVisibility(View.VISIBLE);
                prog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO Auto-generated method stub
                super.onSuccess(arg0, arg1);
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInUserProfile>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);

                    ed_Name.setText(_Data.data.user_name + "");
                    ed_Mobile.setText(_Data.data.phone + "");
                    ed_Email.setText(_Data.data.email + "");
                    // ed_DOB.setText(_Data.data.dob + "");

                } catch (Exception e) {
                    Log.i("TestApp", e.getMessage());
                }

            }

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
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
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
        loadProfile();
        super.onResume();

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }
}

