package com.example.apiexcute2;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.apiexcute2.appData.DataCollection;
import com.example.apiexcute2.serve.MyServe;
import com.example.apiexcute2.util.MyLog;

import junit.framework.TestCase;

public class UiTest extends TestCase {

    public void testA() throws UiObjectNotFoundException, InterruptedException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);
        // 获取上下文
        Context context = instrumentation.getContext();

        // 启动测试App
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("org.isoron.uhabits");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        Thread.currentThread().sleep(2000);
        MyLog.CYR(DataCollection.getCurActivityName());
//        // 打开CollapsingToolbarLayout
//        String resourceId = "com.yang.designsupportdemo:id/CollapsingToolbarLayout";
//        UiObject collapsingToolbarLayout = uiDevice.findObject(new UiSelector().resourceId(resourceId));
//        collapsingToolbarLayout.click();
//
//        for (int i = 0; i < 5; i++) {
//            // 向上移动
//            uiDevice.swipe(uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayHeight(),
//                    uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayHeight() / 2, 10);
//
//            // 向下移动
//            uiDevice.swipe(uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayHeight() / 2,
//                    uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayHeight(), 10);
//        }
//
//        // 点击应用返回按钮
//        UiObject back = uiDevice.findObject(new UiSelector().description("Navigate up"));
//        back.click();
//
//        // 点击设备返回按钮
//        uiDevice.pressBack();
    }
}
