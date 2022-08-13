package helpers;

import android.app.Application;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

public class MyApp extends Application implements LifecycleObserver {
    private ForceUpgradeManager forceUpgradeManager;
    private static MyApp application;
    public MyApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            application = this;
            initForceUpgradeManager();
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        } catch (Exception e) {

        }

    }
    public void initForceUpgradeManager() {
        if (forceUpgradeManager == null) {
            forceUpgradeManager = new ForceUpgradeManager(application);
        }
    }
}