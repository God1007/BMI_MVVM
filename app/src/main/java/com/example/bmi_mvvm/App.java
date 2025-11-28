package com.example.bmi_mvvm;

import android.app.Application;
import android.content.Context;

/**
 * 全局 Application 类，用于在应用启动时初始化并保存一个全局可用的 {@link Context}。
 * 这样 ViewModel 等不直接持有 Activity 的组件也能安全地获取到应用级上下文。
 */
public class App extends Application {

    /**
     * 缓存应用级上下文，避免在需要时还要从 Activity 传递。
     */
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // 保存全局 Context，生命周期与应用一致，不会造成 Activity 泄漏。
        context = getApplicationContext();
    }

    /**
     * 对外暴露的静态方法，供模型层或仓库层获取 Context 使用。
     */
    public static Context getContext() {
        return context;
    }
}
