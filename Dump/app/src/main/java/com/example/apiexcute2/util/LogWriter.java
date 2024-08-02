package com.example.apiexcute2.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LogWriter {
    private static File file;
    private static BufferedWriter writer;
    private static volatile LogWriter logWriter;
    private static String fName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/methodLog.txt";
    private static boolean token = false;
    private static long preTime;
    private static List<String> list ;
    public LogWriter(){
        file = new File(fName);
        list = new LinkedList<>();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            writer = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static LogWriter getInstance(){
        if(logWriter==null){
            synchronized (LogWriter.class){
                if(logWriter==null){
                    logWriter = new LogWriter();
                }
            }
        }
        return logWriter;
    }

    public synchronized void writeLog(String log){
        if(!token){
            return;
        }
        list.add(log);
    }

    public static void turnWriteAble(){
        long curTime = System.currentTimeMillis();
        if(curTime-preTime<=300){
            preTime = curTime;
            return;
        }
        preTime = curTime;

        token = !token;
        MyLog.CYR(token+"");
        if(!token){
            if (writer==null){
                MyLog.CYR("writer is null");
                return;
            }
            writeLogList();
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void writeLogList(){
        try {
            for(String log:list){
                writer.write(log+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
