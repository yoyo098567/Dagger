package com.example.capturevideoandpictureandsaveandchoose.ui.adddevice;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;

import java.util.ArrayList;

public class AddDeviceActivity extends BaseActivity implements View.OnClickListener {
    private Spinner spinnerMaintenancePlant, spinnerCompany, spinnerProductionPlant, spinnerDeviceCategory,
            spinnerDeviceNumber;
    private TextView textRecordDate, textFile, textUploadPerson;
    private EditText editKeynote;
    private ArrayList maintenancePlantList, companyList, productionPlantList, deviceCategoryList,
            deviceNumberList;
    private Button btnBrowse, btnUpload;
    private ArrayAdapter maintenancePlantAdapter, companyAdapter, productionPlantAdapter,
            deviceCategoryAdapter, deviceNumberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        init();
        spinnerMaintenancePlant.setAdapter(maintenancePlantAdapter);
        spinnerCompany.setAdapter(companyAdapter);
        spinnerProductionPlant.setAdapter(productionPlantAdapter);
        spinnerDeviceCategory.setAdapter(deviceCategoryAdapter);
        spinnerDeviceNumber.setAdapter(deviceNumberAdapter);

    }

    @Override
    public void init() {
        spinnerMaintenancePlant = findViewById(R.id.spinner_maintenance_plant);
        spinnerCompany = findViewById(R.id.spinner_company);
        spinnerProductionPlant = findViewById(R.id.spinner_production_plant);
        spinnerDeviceCategory = findViewById(R.id.spinner_device_category);
        spinnerDeviceNumber = findViewById(R.id.spinner_device_number);
        textRecordDate = findViewById(R.id.text_record_date_data);
        textFile = findViewById(R.id.text_file_data);
        textUploadPerson = findViewById(R.id.text_upload_person_data);
        btnBrowse = findViewById(R.id.btn_browse);
        btnUpload = findViewById(R.id.btn_upload);

        maintenancePlantList = new ArrayList<>();
        companyList = new ArrayList<>();
        productionPlantList = new ArrayList<>();
        deviceCategoryList = new ArrayList<>();
        deviceNumberList = new ArrayList<>();

        maintenancePlantAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, maintenancePlantList);
        companyAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, companyList);
        productionPlantAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, productionPlantList);
        deviceCategoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceCategoryList);
        deviceNumberAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceNumberList);
    }

    public void updateData(ArrayList list, ArrayAdapter arrayAdapter) {
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_browse:
                break;
            case R.id.btn_upload:
                break;

        }
    }
}
