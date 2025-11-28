package com.example.bmi_mvvm; // 指定包名，声明应用所属的命名空间

import android.app.Application; // 导入 Application 以创建全局应用类
import android.content.Context; // 导入 Context 以便存储全局上下文

/**
 * 全局 Application 类，用于在应用启动时初始化并保存一个全局可用的 {@link Context}。
 * 这样 ViewModel 等不直接持有 Activity 的组件也能安全地获取到应用级上下文。
 */
public class App extends Application { // 继承 Application 以参与应用生命周期

    private static Context context; // 静态字段保存应用级 Context

    @Override
    public void onCreate() { // 应用创建时回调
        super.onCreate(); // 调用父类保证基础初始化
        context = getApplicationContext(); // 保存全局 Context，供全局访问
    } // onCreate 结束

    public static Context getContext() { // 提供静态方法获取应用级 Context
        return context; // 返回之前缓存的 Context
    } // getContext 结束
} // App 类结束
