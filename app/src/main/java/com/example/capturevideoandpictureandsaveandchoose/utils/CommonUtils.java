package com.example.capturevideoandpictureandsaveandchoose.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonUtils {
    private static Toast toast;

    private CommonUtils() {
    }

    public static <T> String getRequestJson(List<T> request) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(request);
    }

    public static <T> List<T> getResponseJsonArray(String response, Class<T> mPojoClass) {
        List<T> list = new ArrayList<>();
        try {
            Gson gson = new GsonBuilder().create();
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                T item = gson.fromJson(array.getString(i), mPojoClass);
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T getResponseJson(String response, Class<T> mPojoClass) {
        Gson gson = new GsonBuilder().create();
        T item = gson.fromJson(response, mPojoClass);
        return item;
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return sdf.format(date);
    }

    public static String getYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    public static String getNotTransferReceipt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmss");
        Date date = new Date();
        return "TR" + sdf.format(date);
    }

    public static String getStringResourceFormat(Context context, int resId, Object... args) {
        String content = context.getResources().getString(resId);
        return String.format(content, args);
    }

    public static List<String> getNoneRepeatItemList(List<String> list) {
        Set<String> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }
}
