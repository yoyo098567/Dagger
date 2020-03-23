package com.example.capturevideoandpictureandsaveandchoose.ui.deviceinformation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;

public class DeviceInformationActivity extends BaseActivity implements View.OnClickListener {
    private TextView textCompanyData, textJobSiteData, textProductionPlantData, textRouteCodeData,
    textRouteNameData, textDeviceCategoryData, textDeviceNumberData, textDeviceNameData;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        init();
    }

    @Override
    public void init() {
        textCompanyData = findViewById(R.id.text_company_data);
        textJobSiteData = findViewById(R.id.text_job_site_data);
        textProductionPlantData = findViewById(R.id.text_production_plant_data);
        textRouteCodeData = findViewById(R.id.text_route_code_data);
        textRouteNameData = findViewById(R.id.text_route_name_data);
        textDeviceCategoryData = findViewById(R.id.text_device_category_data);
        textDeviceNumberData = findViewById(R.id.text_device_number_data);
        textDeviceNameData = findViewById(R.id.text_device_name_data);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
