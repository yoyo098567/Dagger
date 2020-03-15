package com.example.capturevideoandpictureandsaveandchoose.base;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capturevideoandpictureandsaveandchoose.utils.ToastCreator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 5*N on 2017/12/22
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private ProgressDialog mProgressDialog;
    private Calendar mCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showProgressDialog(@StringRes int text) {
        dismissProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(this.getResources().getString(text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void showProgressDialog(String text) {
        dismissProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(text);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showDialogCaveatMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showDialogCaveatMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showDialogMessage(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton(android.R.string.yes, null)
                .show();
    }

    @Override
    public void showDialogMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showDatePickerDialog(DatePickerDialog.OnDateSetListener onDateSetListener) {
        DatePickerDialog dialog = new DatePickerDialog(this,
                onDateSetListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void showSelectDialog(String text, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton("確認", onClickListener)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public String getResourceString(@StringRes int text) {
        return getResources().getString(text);
    }


    @Override
    public String getTodayTime() {
        String dateformat = "yyyyMMdd";
        Calendar mCal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        String today = df.format(mCal.getTime());
        return today;
    }

    @Override
    public void showToast(@StringRes int text) {
        ToastCreator.makeText(this, text, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(String text) {
        ToastCreator.makeText(this, text, Toast.LENGTH_SHORT);
    }
}
