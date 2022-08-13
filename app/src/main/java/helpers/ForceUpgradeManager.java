package helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import constants.Values;

public class ForceUpgradeManager implements LifecycleObserver {

    private static final String KEY_UPDATE_REQUIRED = "force_update_required";
    private static final String KEY_CURRENT_VERSION = "force_update_current_version";

    private static final String TAG = "TestApp_Code";

    private final Context context;

    @Nullable
    private WeakReference<Activity> activityWeakReference;

    public ForceUpgradeManager(MyApp application) {
        this.context = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(callbacks);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public ForceUpgradeManager(Activity activity) {
        this.context = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.registerActivityLifecycleCallbacks(callbacks);
        }
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void appStarted() {
        checkForceUpdateNeeded();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void appStopped() {
        if (activityWeakReference != null) {
            activityWeakReference.clear();
        }
    }

    @Nullable
    private Activity getCurrentActivity() {
        return activityWeakReference != null && activityWeakReference.get() != null
                ? activityWeakReference.get() : null;
    }

    private final Application.ActivityLifecycleCallbacks callbacks =
            new Application.ActivityLifecycleCallbacks() {

                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                }

                @Override
                public void onActivityStarted(@NonNull Activity activity) {
                    ForceUpgradeManager.this.activityWeakReference = new WeakReference<>(activity);
                }

                @Override
                public void onActivityResumed(@NonNull Activity activity) {

                }

                @Override
                public void onActivityPaused(@NonNull Activity activity) {

                }

                @Override
                public void onActivityStopped(@NonNull Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(@NonNull Activity activity,
                                                        @NonNull Bundle bundle) {

                }

                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {

                }
            };

    /**
     * Gets update alert.
     */
    private void onUpdateNeeded() {

        Activity temp = getCurrentActivity();
        if (temp != null) {
            AlertDialog dialog = new AlertDialog.Builder(temp)
                    .setTitle("New version available")
                    .setCancelable(false)
                    .setMessage("Please update app for seamless experience.")
                    .setPositiveButton("Continue",
                            (dialog1, which) -> redirectStore()).create();
            dialog.show();
        }
    }

    /**
     * Redirect to play store
     */
    private void redirectStore() {
        Uri updateUrl = Uri.parse("market://details?id=" + context.getPackageName());
        final Intent intent = new Intent(Intent.ACTION_VIEW, updateUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void checkForceUpdateNeeded() {
        UpdateHolder.getInstance().setData(false);
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        // long cacheExpiration = 12 * 60 * 60; // fetch every 12 hours
        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(KEY_UPDATE_REQUIRED, true);
        remoteConfigDefaults.put(KEY_CURRENT_VERSION, Values.App_Version);

        remoteConfig.setDefaultsAsync(remoteConfigDefaults);
        remoteConfig.fetch(0)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "remote config is fetched.");
                        remoteConfig.fetchAndActivate();
                    }
                    if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
                        String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
                        String appVersion = getAppVersion(context);
                        Log.i(TAG, "remote config is fetched. " + appVersion);

                        if (!TextUtils.equals(currentVersion, appVersion)) {
                            Log.i(TAG, "remote config is fetched Current. " + currentVersion);
                            UpdateHolder.getInstance().setData(true);
                            // onUpdateNeeded();
                        }else{
                            UpdateHolder.getInstance().setData(false);
                        }

                    }
                });
    }

    private String getAppVersion(Context context) {
        String result = "";
        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, e.getMessage());
        }

        return result;
    }
}
