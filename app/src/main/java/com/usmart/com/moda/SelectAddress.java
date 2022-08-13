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
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;

import com.ethanhua.skeleton.ViewSkeletonScreen;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListAddress;
import dataInLists.DataInAddress;
import dataInLists.DataInGlobal;
import helpers.AddressHolder;
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

public class SelectAddress extends Activity {

    Activity activity = SelectAddress.this;
    SharedPreferences Login;
    DataInAddress _Data = new DataInAddress();

    ListView lv;
    CustomListAddress adapter2;
    String AddressID, AddressName;
    TextView MainTitle;
    ViewSkeletonScreen skeletonScreen;
    ImageView HideAll;
    Button btn_Add, btn_Continue;
    TextView noData;
    ProgressBar prog;
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
        // ***********************************************
        setContentView(R.layout.activity_listview_address);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = "ar";
        } else {
            lang = "en";
        }

        AddressHolder.getInstance().setData(0);
        HideAll = findViewById(R.id.HideAll);
        prog = findViewById(R.id.progressBar1);

        MainTitle = findViewById(R.id.MainTitle);


        btn_Continue = findViewById(R.id.btn_Continue);
        noData = findViewById(R.id.noData);
        btn_Add = findViewById(R.id.btn_Add);
        findViewById(R.id.LeftSide).setVisibility(View.GONE);
        AddressID = Integer.toString(0);
        AddressName = "";
        lv = findViewById(R.id.listViewOrders);

        adapter2 = new CustomListAddress(activity, new ArrayList<>());
        lv.setAdapter(adapter2);


        MainTitle.setText(R.string.MyAddress);

        btn_Add.setVisibility(View.VISIBLE);
        btn_Add.setOnClickListener(v -> {
            Intent i = new Intent(activity, AddAddress.class);
            startActivityForResult(i, 11);
        });

        lv.setOnItemClickListener((parent, view, position, id) -> {

            selectType(position);
        });

        btn_Continue.setOnClickListener(v -> {
            if (_Data.data.size() > 0 && AddressHolder.getInstance().getData() > 0) {
                Intent i = new Intent(activity, Checkout.class);
                i.putExtra("AddressID", AddressHolder.getInstance().getData());
                startActivity(i);
            }
        });
        loadAddress();

    }

    private void loadAddress() {
        AsyncHttpClient client = new AsyncHttpClient();
        String Url;

        Url = Values.Link_service + "addresses/" + lang + "/v1";

        if (LoginHolder.getInstance().getData().equals("login")) {
            client.addHeader("Authorization", "" + UserHolder.getInstance().getData().token.token_type
                    + " " + UserHolder.getInstance().getData().token.access_token);
        } else {
            client.addHeader("Authorization", "" + Values.Authorization_User);
        }
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
                    Type t = new TypeToken<DataInAddress>() {
                    }.getType();
                    _Data = g.fromJson(arg1, t);
                    adapter2.clear();
                    adapter2.notifyDataSetChanged();
                    if (_Data.data.size() > 0) {
                        adapter2.addAll(_Data.data);
                        adapter2.notifyDataSetChanged();
                        lv.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }


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

    private void setDefAddress(int Address) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "addresses/setdefault/" + lang + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"address_id\":\"" + Address + "\"")
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
                    DataInGlobal Res;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInGlobal>() {
                    }.getType();

                    Res = g.fromJson(response.body().string(), t);


                    runOnUiThread(() -> {
                        if (!Res.success) {
                            if (Res.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(Res.message);
                            }
                        } else {


                        }
                    });

                } catch (Exception e) {
                }
            }

        });

        HideAll.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);

    }

    private void RemoveAddress(int Pos) {

        OkHttpClient client = new OkHttpClient();
        String Url = Values.Link_service + "addresses/" + LangHolder.getInstance().getData() + "/v1";
        String json = new StringBuilder()
                .append("{")
                .append("\"address_id\":\"" + _Data.data.get(Pos).id + "\"")
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
                .delete(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TestApp_5", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    DataInAddress Result;
                    Gson g = new Gson();
                    Type t = new TypeToken<DataInAddress>() {
                    }.getType();

                    Result = g.fromJson(response.body().string(), t);


                    runOnUiThread(() -> {
                        if (!Result.success) {
                            if (Result.code == 401) {
                                Intent i = new Intent(activity, Login.class);
                                startActivity(i);
                            } else {
                                loadMsg(Result.message);
                            }
                        } else {
                            adapter2.remove(_Data.data.get(Pos));
                            adapter2.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                }
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

    private void selectType(int Position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads_options);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView Yes = dialog.findViewById(R.id.btn_2);
        TextView No = dialog.findViewById(R.id.btn_1);
        TextView Cancel = dialog.findViewById(R.id.btn_3);

        Yes.setText(R.string.SetDefault);
        No.setText(R.string.DeleteAddress);


        Yes.setOnClickListener(v -> {
            AddressID = Integer.toString(_Data.data.get(Position).id);
            AddressName = _Data.data.get(Position).title;
            for (int i = 0; i < _Data.data.size(); i++) {
                _Data.data.get(i).is_default = false;
            }
            _Data.data.get(Position).is_default = true;
            adapter2.notifyDataSetChanged();

            setDefAddress(_Data.data.get(Position).id);
            //setOnBack(Position);

            dialog.dismiss();
        });
        No.setOnClickListener(v -> {
            RemoveAddress(Position);
            adapter2.notifyDataSetChanged();
            dialog.dismiss();
        });
        Cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11) {
            AddressID = data.getStringExtra("AddressID");
            AddressName = data.getStringExtra("AddressName");
            if (AddressName.equals("")) {
                AddressName = getResources().getString(R.string.SetAddressDef);
            }
            loadAddress();
        }
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(activity, Cart.class));
    }

    public void gotoBack(View v) {
        startActivity(new Intent(activity, Cart.class));
    }

    public void gotoSearch(View v) {
        startActivity(new Intent(activity, Search.class));
    }

    public void gotoCart(View v) {
        startActivity(new Intent(activity, Cart.class));
    }

    public void setOnBack(int Position) {
       /* Intent intent = new Intent();
        intent.putExtra("AddressID", AddressID);
        intent.putExtra("AddressName", AddressName);
        setResult(11, intent);
        super.onBackPressed();*/
        //overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);

        for (int i = 0; i < adapter2.getCount(); i++) {
            adapter2.getItem(i).is_default = false;
        }
        adapter2.getItem(Position).is_default = true;
        adapter2.notifyDataSetChanged();
    }


}
