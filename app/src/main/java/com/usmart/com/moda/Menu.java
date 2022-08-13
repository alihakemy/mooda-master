package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Locale;

import constants.Values;
import customLists.CustomListMenu;
import dataInLists.DataInListIcons;
import helpers.LangHolder;
import helpers.LoginHolder;
import helpers.NetWork;
import helpers.OnlineHolder;
import helpers.UserHolder;
import utils.Dialogs;


public class Menu extends Activity {
    Activity activity = Menu.this;
    ArrayList<DataInListIcons> _Data = new ArrayList<>();
    ExpandableHeightListView lv;
    CustomListMenu adapter2;
    ImageView User_Image;
    Button btn_Login;
    LinearLayout Visitor, User;
    TextView UserName, Mobile;
    byte lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setContentView(R.layout.activity_menu);
        if (LangHolder.getInstance().getData().equals("ar")) {
            lang = 2;
        } else {
            lang = 1;
        }

        UserName = findViewById(R.id.UserName);
        Mobile = findViewById(R.id.Mobile);
        User_Image = findViewById(R.id.User_Image);
        btn_Login = findViewById(R.id.btn_Login);
        Visitor = findViewById(R.id.Visitor);
        User = findViewById(R.id.User);

        lv = findViewById(R.id.listViewOrders);
        lv.setExpanded(true);
        adapter2 = new CustomListMenu(activity, _Data);

        lv.setAdapter(adapter2);
        lv.setOnItemClickListener((parent, view, position, id) -> {

            if (adapter2.getItem(position).id == 1) {
              /*  Intent i = new Intent(activity, FeatAds.class);
                startActivity(i);*/
            }
            if (adapter2.getItem(position).id == 2) {
                Intent i = new Intent(activity, Notis.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 3) {
               /* Intent i = new Intent(activity, MyCurrentAds.class);
                startActivity(i);*/
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 4) {
                Intent i = new Intent(activity, SelectPackage.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 5) {
               /* Intent i = new Intent(activity, MyCurrentAds.class);
                startActivity(i);*/
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 6) {
                Intent i = new Intent(activity, AccountMenu.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 7) {
                Intent i = new Intent(activity, Contact.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 8) {
                Intent i = new Intent(activity, About.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 9) {
                Intent i = new Intent(activity, Contact.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 10) {
                Intent i = new Intent(activity, Conditions.class);
                startActivity(i);
                //overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
            if (adapter2.getItem(position).id == 11) {
                Dialogs.selectLang(activity);
            }


        });
        btn_Login.setOnClickListener(v -> startActivity(new Intent(activity, Login.class)));
        loadData();

    }

    private void loadData() {
        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        adapter2.add(new DataInListIcons(navMenuTitles[0],
                "drawable://" + R.mipmap.more_01, 1));

        adapter2.add(new DataInListIcons(navMenuTitles[1],
                "drawable://" + R.mipmap.more_02, 2));

        adapter2.add(new DataInListIcons(navMenuTitles[2],
                "drawable://" + R.mipmap.menu_a_02, 3));

        adapter2.add(new DataInListIcons(navMenuTitles[3],
                "drawable://" + R.mipmap.menu_a_01, 4));

        adapter2.add(new DataInListIcons(navMenuTitles[4],
                "drawable://" + R.mipmap.more_05, 5));

        adapter2.add(new DataInListIcons(navMenuTitles[5],
                "drawable://" + R.mipmap.more_06, 6));

        adapter2.add(new DataInListIcons(navMenuTitles[6],
                "drawable://" + R.mipmap.menu_a_02, 7));

        adapter2.add(new DataInListIcons(navMenuTitles[7],
                "drawable://" + R.mipmap.menu_a_02, 8));

        adapter2.add(new DataInListIcons(navMenuTitles[8],
                "drawable://" + R.mipmap.menu_a_02, 9));

        adapter2.add(new DataInListIcons(navMenuTitles[9],
                "drawable://" + R.mipmap.menu_a_02, 10));

        adapter2.add(new DataInListIcons(navMenuTitles[10],
                "drawable://" + R.mipmap.more_11, 11));

        adapter2.notifyDataSetChanged();


    }


    @Override
    protected void onResume() {
        if (LoginHolder.getInstance().getData().equals("login")) {
            Visitor.setVisibility(View.GONE);
            User.setVisibility(View.VISIBLE);
            UserName.setText(UserHolder.getInstance().getData().name + "");
            Mobile.setText(UserHolder.getInstance().getData().email + "");
            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_avatar)
                    .showImageForEmptyUri(R.mipmap.ic_avatar)
                    .showImageOnFail(R.mipmap.ic_avatar)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Values.Link_ImageHomeCats + UserHolder.getInstance().getData().image, User_Image, options);
        } else {
            Visitor.setVisibility(View.VISIBLE);
            User.setVisibility(View.GONE);
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

    public void gotoUpload(View v) {
        Intent i = new Intent(activity, Menu.class);
        startActivity(i);
        //overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
    }

    public void gotoAction(View v) {

    }

}
