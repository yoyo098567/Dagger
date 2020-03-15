package com.example.capturevideoandpictureandsaveandchoose.route_code;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;

public class RouteCodeActivity extends BaseActivity implements View.OnClickListener  {
    private Button btnGetMessage, btnChoseDevice, btnEditDevice, btnCentralCloud, btnBasicInformation,
    btnTakePhoto, btnRecordVideo, btnUploadPhoto, btnUploadVideo;
    private TextView textDeviceNumberData, textRouteCodeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_code);
        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_message:
                break;
            case R.id.btn_chose_device:
                break;
            case R.id.btn_edit_device:
                break;
            case R.id.btn_central_cloud:
                break;
            case R.id.btn_basic_information:
                break;
            case R.id.btn_take_photo:
                break;
            case R.id.btn_record_video:
                break;
            case R.id.btn_upload_photo:
                break;
            case R.id.btn_upload_video:
                break;
        }

    }

    @Override
    public void init() {
        textDeviceNumberData = findViewById(R.id.text_device_number_data);
        textRouteCodeData = findViewById(R.id.text_route_code_data);
        btnGetMessage = findViewById(R.id.btn_get_message);
        btnChoseDevice = findViewById(R.id.btn_chose_device);
        btnEditDevice = findViewById(R.id.btn_edit_device);
        btnCentralCloud = findViewById(R.id.btn_central_cloud);
        btnBasicInformation = findViewById(R.id.btn_basic_information);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnRecordVideo = findViewById(R.id.btn_record_video);
        btnUploadPhoto = findViewById(R.id.btn_upload_photo);
        btnUploadVideo = findViewById(R.id.btn_upload_video);

        btnGetMessage.setOnClickListener(this);
        btnChoseDevice.setOnClickListener(this);
        btnEditDevice.setOnClickListener(this);
        btnCentralCloud.setOnClickListener(this);
        btnBasicInformation.setOnClickListener(this);
        btnUploadPhoto.setOnClickListener(this);
        btnRecordVideo.setOnClickListener(this);
        btnUploadPhoto.setOnClickListener(this);
        btnUploadVideo.setOnClickListener(this);
    }
}
