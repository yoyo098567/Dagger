package com.example.capturevideoandpictureandsaveandchoose.base;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;

import androidx.annotation.StringRes;

import java.util.List;

/**
 * Created by 5*N on 2017/12/22
 */

public interface BaseView {
    void init();
    void showItemDialog(List<String> list, DialogInterface.OnClickListener onClickListener);

    void showProgressDialog(@StringRes int text);
    void showProgressDialog(String text);
    void dismissProgressDialog();

    void showDialogCaveatMessage(String message);
    void showDialogMessage(String message);

    void showLongErroeDialogMessage(String message);
    void showDialogMessage(String title,String message);
    void showDialogCaveatMessage(String title,String message);
    void showDatePickerDialog(DatePickerDialog.OnDateSetListener onDateSetListener);
    void showSelectDialog(String text, DialogInterface.OnClickListener onClickListener);

    String getResourceString(@StringRes int text);
    String getTodayTime();
    void showToast(String text);

    void showToast(@StringRes int text);
    String getPath(Uri uri);
}
