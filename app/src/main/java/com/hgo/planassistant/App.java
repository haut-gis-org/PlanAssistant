package com.hgo.planassistant;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;


import com.avos.avoscloud.AVOSCloud;

import java.util.concurrent.Executor;

/**
 * Created by zhangxiao on 2019/4/2
 */
public class App extends Application {

    private Handler mHandler;
    private Executor mExecutor;
    private static Context context;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mHandler = new Handler();
        mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"eR1JFxB61gInL1GhmaURGdAx-gzGzoHsz","1nddY6z37rpVV2OzxXuWPdSI");
        // 正式发布前去除
        AVOSCloud.setDebugLogEnabled(false);
    }

    public void runOnUi(Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        } else {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(runnable);
        }
    }

    public void runOnBackground(Runnable runnable) {
        if (mExecutor != null) {
            mExecutor.execute(runnable);
        } else {
            mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
            mExecutor.execute(runnable);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = base;
    }

    public static Context getContext() {
        return context;
    }

    public static App getApplication() {
        return instance;
    }
}
