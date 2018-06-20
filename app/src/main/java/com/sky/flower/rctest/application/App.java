package com.sky.flower.rctest.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.sky.flower.rctest.RongEvent;

import io.rong.imkit.RongIM;

public class App extends Application {
    private static App myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
        RongIM.init(this);
        RongEvent.getInstance().setMyExtensionModule();
        RongEvent.getInstance().insertMyMessageType();
        RongEvent.getInstance().setHaveRead();
    }


    public static App getApplication() {
        return myApplication;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
}
