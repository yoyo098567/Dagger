package com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capturevideoandpictureandsaveandchoose.R;

public class ChooseDeviceAdapterViewHolder extends RecyclerView.ViewHolder{
    private TextView number;
    private TextView deviceId;

    public ChooseDeviceAdapterViewHolder(View itemView) {
        super(itemView);
        number = (TextView) itemView.findViewById(R.id.text_number);

        deviceId = (TextView) itemView.findViewById(R.id.text_device_id);
    }
    public TextView getNumber(){
        return number;
    }
    public TextView getItemDevice() {
        return deviceId;
    }
}
