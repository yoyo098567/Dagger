package com.example.capturevideoandpictureandsaveandchoose.base;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.capturevideoandpictureandsaveandchoose.utils.CommonUtils;
import com.example.capturevideoandpictureandsaveandchoose.utils.ToastCreator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 5*N on 2018/1/12
 */

public abstract class BaseFragment extends Fragment implements BaseView {
    private ProgressDialog mProgressDialog;
    private Calendar mCalendar = Calendar.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showProgressDialog(@StringRes int text) {
        dismissProgressDialog();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getActivity().getResources().getString(text));
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
        ToastCreator.makeText(getActivity(), text, Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(String text) {
        ToastCreator.makeText(getActivity(), text, Toast.LENGTH_SHORT);
    }
}
