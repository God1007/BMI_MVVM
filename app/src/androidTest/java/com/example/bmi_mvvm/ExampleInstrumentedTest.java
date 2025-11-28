package com.example.bmi_mvvm;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * 示例仪器化测试，用于在真机/模拟器上验证应用上下文的包名是否符合预期。
 * 主要展示如何在 Android 环境下获取 Context 并做断言。
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.bmi_mvp", appContext.getPackageName());
    }
}