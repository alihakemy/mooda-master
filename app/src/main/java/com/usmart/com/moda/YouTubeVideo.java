package com.usmart.com.moda;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Locale;

import constants.Values;
import helpers.LangHolder;

public class YouTubeVideo extends YouTubeBaseActivity {
    Activity activity = YouTubeVideo.this;
    YouTubePlayerView youTubeView;
    private static final int RECOVERY_REQUEST = 1;
    Li myLi;
    String Code;

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
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //*******************************************************
        setContentView(R.layout.activity_youtube);
        Code = getIntent().getExtras().getString("Code");
        Log.i("TestApp", Code);
        youTubeView = findViewById(R.id.youtube_view);
        myLi = new Li();
        youTubeView.initialize(Values.YOUTUBE_API_KEY, myLi);

    }

    public class Li implements YouTubePlayer.OnInitializedListener {

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
            if (!wasRestored) {
                youTubePlayer.cueVideo(Code);

            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            if (youTubeInitializationResult.isUserRecoverableError()) {
                youTubeInitializationResult.getErrorDialog(activity, RECOVERY_REQUEST).show();
            } else {
                // String error = String.format(getString("error"), youTubeInitializationResult.toString());
                Toast.makeText(activity, "error", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Values.YOUTUBE_API_KEY, myLi);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }


    public void gotoBack(View v) {
        super.onBackPressed();
    }

}
