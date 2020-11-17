package com.example.capturevideoandpictureandsaveandchoose;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateLog {

    public void generateLogTxt(String sBody) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM_dd");
            Date date = new Date();
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/capturevideoandpictureandsaveandchoose/");
            if (!root.exists()) {
                root.mkdirs();
            }
            String sFileName="log"+sdf.format(date)+".txt";
            File logttt = new File(root, sFileName);
            FileWriter writer = new FileWriter(logttt,true);
            writer.append(sBody);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
