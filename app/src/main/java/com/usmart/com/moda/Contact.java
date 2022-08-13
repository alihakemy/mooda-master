package com.usmart.com.moda;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Locale;

import constants.Values;
import dataInLists.DataInGlobal;
import dataInLists.DataInSetting;
import helpers.LangHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Contact extends Activity {
    Activity activity = Contact.this;
    EditText ed_Mobile, ed_Msg;
    Button btn_Send;
    ImageView HideAll;
    ProgressBar prog;
    TextView Enter;
    String lang;
    TextView MainTitle;
    LinearLayout Instagram, WhatsApp, Call;
    DataInSetting DataSent = new DataInSetting();
    DataInGlobal Data = new DataInGlobal();
    private static final int INITIAL_REQUEST = 1337;
    private static final int CALL_REQUEST = INITIAL_REQUEST + 5;
    private static final String[] CALL_PERMS = {
            Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        getWindow().setStatusBarColor(Color.parseColor("#ca0000"));
        getWindow().getDecorView().setSystemUiVisibility(0);
        //*******************************************************
        setContentView(R.layout.activity_contact);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }
        // ***********************************************

        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);
        Enter = findViewById(R.id.Enter);

        ed_Msg = findViewById(R.id.edit_Msg);
        ed_Mobile = findViewById(R.id.edit_Mobile);
        btn_Send = findViewById(R.id.btn_Send);
        Call = findViewById(R.id.Call);
        WhatsApp = findViewById(R.id.WhatsApp);
        Instagram = findViewById(R.id.Instagram);

        MainTitle.setText(R.string.Contact);


        btn_Send.setOnClickListener(v -> {
            if (ed_Mobile.getText().toString().matches("") || ed_Msg.getText().toString().matches("")) {
                Toast.makeText(activity, R.string.EmptyField, Toast.LENGTH_SHORT).show();
                return;
            }

            if (Patterns.PHONE.matcher(ed_Mobile.getText().toString()).matches() == false) {
                Toast.makeText(activity, R.string.ValidMobile, Toast.LENGTH_SHORT).show();
                return;
            }

            HideAll.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient();
            String Url = Values.Link_service + "contactus/" + lang + "/v1";
            String json = new StringBuilder()
                    .append("{")
                    .append("\"message\":\"" + ed_Msg.getText().toString() + "\",")
                    .append("\"phone\":\"" + ed_Mobile.getText().toString() + "\"")
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
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.i("TestApp_5", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {

                        Gson g = new Gson();
                        Type t = new TypeToken<DataInSetting>() {
                        }.getType();

                        DataSent = g.fromJson(response.body().string(), t);

                        activity.runOnUiThread(() -> {
                            if (!DataSent.success) {
                                loadMsg(DataSent.message);
                            } else {
                                loadMsg(DataSent.data.message + "");
                                ed_Mobile.setText("");
                                ed_Msg.setText("");

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


        loadData();
    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "getappnumber/" + lang + "/v1";
        Log.i("TestApp", Url);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "" + Values.Authorization_User);

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
                HideAll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                Log.i("TestApp", arg1 + "");
                try {
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();
                    Data = g.fromJson(arg1, t);

                    Call.setOnClickListener(v -> {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Data.data.phone));
                        startActivity(callIntent);
                    });
                    WhatsApp.setOnClickListener(v -> {
                        PackageManager packageManager = getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);

                        try {
                            String url = "https://api.whatsapp.com/send?phone=" + Data.data.watsapp + "&text=" + URLEncoder.encode("", "UTF-8");
                            i.setPackage("com.whatsapp");
                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                startActivity(i);
                            }
                        } catch (Exception e) {

                        }
                    });

                    Instagram.setOnClickListener(v -> {
                        try {
                            startActivity(new Intent(activity, WebView.class).putExtra("Link", Data.data.instegram + ""));

                        }catch ( Exception e ) {

                        }
                    });


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

        No.setText(R.string.OK);
        Text.setText(MSG + "");
        Yes.setVisibility(View.GONE);


        No.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private boolean canCall() {
        return (hasPermission(Manifest.permission.CALL_PHONE));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return false;
    }

    @Override
    protected void onResume() {
        if (!canCall()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(CALL_PERMS, CALL_REQUEST);
            }
        }
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
        } catch (Exception e) {
            NetWork.gotoError(activity);
        }
        super.onResume();

    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }


}
