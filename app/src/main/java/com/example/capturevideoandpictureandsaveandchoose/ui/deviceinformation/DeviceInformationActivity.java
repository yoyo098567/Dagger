package com.example.capturevideoandpictureandsaveandchoose.ui.deviceinformation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.ui.main.MainActivity;

import java.util.ArrayList;

public class DeviceInformationActivity extends BaseActivity implements View.OnClickListener {
    private TextView textCompanyData, textJobSiteData, textProductionPlantData, textRouteCodeData,
    textRouteNameData, textDeviceCategoryData, textDeviceNumberData, textDeviceNameData;
    private Button btnBack;
    private ChooseDeviceItemData chooseDeviceItemData;
    private ArrayList<ChooseDeviceItemData> deviceDataList;

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


        if(getIntent().getExtras() != null){
            chooseDeviceItemData =(ChooseDeviceItemData) getIntent().getExtras().getSerializable("device");
            deviceDataList = (ArrayList<ChooseDeviceItemData>) getIntent().getExtras().getSerializable("NonInspectionWorkDevic");
            textCompanyData.setText(chooseDeviceItemData.getCompany());
            textJobSiteData.setText(chooseDeviceItemData.getMaintenancePlant());
            textProductionPlantData.setText(chooseDeviceItemData.getMaintenancePlant());
            textRouteCodeData.setText("123");
            textRouteNameData.setText("123");
            textDeviceCategoryData.setText(chooseDeviceItemData.getDeviceCategory());
            textDeviceNumberData.setText(chooseDeviceItemData.getProductionPlantId());
            textDeviceNameData.setText(chooseDeviceItemData.getProductionPlant());
        }


        btnBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                Log.v("77777777","back");
                Intent it = new Intent(this, MainActivity.class);
                it.putExtra("NonInspectionWorkDevice",deviceDataList);
                setResult(RESULT_OK, it);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("NonInspectionWorkDevice",deviceDataList);
        setResult(RESULT_OK, it);
    }
}
