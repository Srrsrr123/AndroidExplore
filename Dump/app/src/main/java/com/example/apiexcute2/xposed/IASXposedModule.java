package com.example.apiexcute2.xposed;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;

import com.example.apiexcute2.appData.DataCollection;
import com.example.apiexcute2.serve.MyServe;
import com.example.apiexcute2.util.MyLog;
import com.example.apiexcute2.xposed.event.ActivityOnCreateHook;
import com.example.apiexcute2.xposed.event.ActivityOnResumeHook;
import com.example.apiexcute2.xposed.event.HookOnDraw;

import java.io.IOException;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @create data: 2022/2/23
 * @author: CYR
**/
public class IASXposedModule implements IXposedHookLoadPackage{
    public static String AUT = "com.netease.cloudmusic";
    private MyServe serve;

    /**
     * @create data: 2022/2/23
     * @author: CYR
     * @param:
     * @return:
    **/
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        MyLog.CYR("Loaded app: "+lpparam.packageName);
        if(lpparam.packageName.equals("com.netease.cloudmusic")
                || lpparam.packageName.equals("com.dzmf.zmfxsdq")
                || lpparam.packageName.equals("sina.mobile.tianqitong")
                || lpparam.packageName.equals("com.example.asd")
                || lpparam.packageName.equals("com.hui.tally")
                || lpparam.packageName.equals("com.example.notes")
                || lpparam.packageName.equals("com.demo.appmonitor")
                || lpparam.packageName.equals("com.xinyang.calendarview")
                || lpparam.packageName.equals("com.northenbank.pomodoro")
                || lpparam.packageName.equals("com.agmcs.countdown")
                || lpparam.packageName.equals("com.zhihaofans.texttext")
                || lpparam.packageName.equals("com.example.lrp.auto")
                || lpparam.packageName.equals("net.thebrennt.anycut")
                || lpparam.packageName.equals("com.example.weather")
                || lpparam.packageName.equals("top.ysccx.myfirstapp")
                || lpparam.packageName.equals("com.talengu.wordwarrior")
                || lpparam.packageName.equals("me.veryyoung.game2048")
                || lpparam.packageName.equals("com.example.rubbishrecog")
                || lpparam.packageName.equals("comjoshsibayan.github.alarm")
                || lpparam.packageName.equals("news.androidtv.quicksettingstv")
                || lpparam.packageName.equals("com.example.vasilis.myapplication")
                || lpparam.packageName.equals("in.shick.lockpatterngenerator")
                || lpparam.packageName.equals("de.meonwax.soundboard")
                || lpparam.packageName.equals("com.example.helloworld")
                || lpparam.packageName.equals("com.example.ben.tezfillup")
                || lpparam.packageName.equals("com.jeremy.passwordmaker")
                || lpparam.packageName.equals("be.ppareit.swiftp")
                || lpparam.packageName.equals("com.hectorone.multismssender")
                || lpparam.packageName.equals("edu.utep.cs.cs4330.dumbphone")
                || lpparam.packageName.equals("com.example.android.divideandconquer")
                || lpparam.packageName.equals("org.jtb.alogcat")
                || lpparam.packageName.equals("naman14.timber")
                || lpparam.packageName.equals("com.hanyuzhou.accountingapp")
                || lpparam.packageName.equals("org.isoron.uhabits")
                || lpparam.packageName.equals("org.secuso.privacyfriendlysketching")
                || lpparam.packageName.equals("de.k3b.android.androFotoFinder")
                || lpparam.packageName.equals("com.llw.goodweather")
                || lpparam.packageName.equals("cn.javayuan.diary")
                || lpparam.packageName.equals("com.android.keepass")){
            XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onCreate", Bundle.class, new ActivityOnCreateHook(lpparam));
            XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onResume", new ActivityOnResumeHook());
            XposedHelpers.findAndHookMethod("android.view.View", lpparam.classLoader, "draw",Canvas.class, new HookOnDraw());
            System.out.print("11111111111111111111111");
            serve = new MyServe(8888);
            System.out.print("22222222222222222222222");
            try {
                serve.start();
                MyLog.CYR("start serve");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(lpparam.packageName.equals("com.dragon.read")){
        }
    }
    private void initHook(XC_LoadPackage.LoadPackageParam lpparam){
        MyLog.CYR("hook init end");
    }
}
