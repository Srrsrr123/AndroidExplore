package com.example.apiexcute2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.example.apiexcute2.ViewManager.FloatViewManager;
import com.example.apiexcute2.serve.MyServe;
import com.example.apiexcute2.serve.ServeReceiver;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import static com.example.apiexcute2.receive.LocalActivityReceiver.ON_RESUME;
import static com.example.apiexcute2.serve.ServeReceiver.API_RESPONSE;
import static com.example.apiexcute2.serve.ServeReceiver.OPEN_API;

public class MainActivity extends AppCompatActivity {
    private int time;
    private String FLAG = "flag";
    private View view = null;
    private View view2;
    private MyTextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        view = findViewById(R.id.linearLayout);
        view2 = findViewById(R.id.textView);
        myTextView = findViewById(R.id.myTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init(){
    }

    public void click(View v){
        Log.i("CYR","view mLeft "+view.getLeft()+" layerType: "+view.getLayerType());
        Log.i("CYR","view2 mLeft "+view2.getTop()+" x "+view2.getY()+" tranlationX: "+view2.getTranslationX());
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    public void click2(View view){
        Log.i("CYR","click2");
    }

    private void test(){
        Class viewClass = View.class;
        try {
            Field attachField = viewClass.getDeclaredField("mAttachInfo");
            attachField.setAccessible(true);
            Object attachObject = attachField.get(view);
            if(attachObject==null){
                Log.i("CYR","attach is null");
            }else{
                Log.i("CYR","attach is not  null");
            }
            Class attachClass = attachObject.getClass();
            Field isAccelerate = attachClass.getDeclaredField("mHardwareAccelerated");
            isAccelerate.setAccessible(true);
            boolean  isAccObject = (boolean) isAccelerate.get(attachObject);
            Log.i("CYR",""+isAccObject);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
