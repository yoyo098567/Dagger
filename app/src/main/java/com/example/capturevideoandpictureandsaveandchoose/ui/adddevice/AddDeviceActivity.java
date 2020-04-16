package com.example.capturevideoandpictureandsaveandchoose.ui.adddevice;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.capturevideoandpictureandsaveandchoose.Application;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.di.component.adddevice.AddDeviceComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.component.adddevice.DaggerAddDeviceComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.adddevice.AddDeviceModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNOResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class AddDeviceActivity extends BaseActivity implements View.OnClickListener, AddDeviceContract.View {
    @Inject
    AddDeviceContract.Presenter<AddDeviceContract.View> mPresenter;
    private TextView textMaintenancePlant,textCompany,textProductionPlant,textDeviceCategory,
           textDeviceNumber;
    private TextView textRecordDate, textFile, textUploadPerson;
    private EditText editKeynote;
    private AddDeviceData mAddDeviceData;
    private ChooseDeviceItemData mChooseDeviceItemData;
    private Button btnBrowse, btnUpload;
    private AddDeviceComponent mAddDeviceComponent;
    private ArrayList<String> dialogString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        init();
        mPresenter.onAttached(this);
    }

    @Override
    public void init() {
        Calendar mCal = Calendar.getInstance();
        CharSequence date = DateFormat.format("yyyy/MM/dd", mCal.getTime());
        textMaintenancePlant = findViewById(R.id.text_value_maintenance_plant);
        textCompany = findViewById(R.id.text_value_company);
        textProductionPlant = findViewById(R.id.text_value_production_plant);
        textDeviceCategory = findViewById(R.id.text_value_device_category);
        textDeviceNumber = findViewById(R.id.text_value_device_number);
        textRecordDate = findViewById(R.id.text_record_date_data);
        textFile = findViewById(R.id.text_file_data);
        textUploadPerson = findViewById(R.id.text_upload_person_data);
        btnBrowse = findViewById(R.id.btn_browse);
        btnUpload = findViewById(R.id.btn_upload);
        mAddDeviceComponent = DaggerAddDeviceComponent.builder()
                .addDeviceModule(new AddDeviceModule(this))
                .baseComponent(((Application) getApplication()).getApplicationComponent())
                .build();
        mAddDeviceComponent.inject(this);
        dialogString=new ArrayList<>();
        mAddDeviceData=new AddDeviceData();
        mChooseDeviceItemData=new ChooseDeviceItemData();
        textRecordDate.setText(date);
        btnUpload.setOnClickListener(this);
        btnBrowse.setOnClickListener(this);
        textMaintenancePlant.setOnClickListener(this);
        textCompany.setOnClickListener(this);
        textProductionPlant.setOnClickListener(this);
        textDeviceCategory.setOnClickListener(this);
        textDeviceNumber.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_browse:
                break;
            case R.id.btn_upload:
                uploadDevice();
                break;
            case R.id.text_value_maintenance_plant:
                mPresenter.onGetMNTFCTData();
                break;
            case R.id.text_value_production_plant:
                if (mChooseDeviceItemData.getMaintenancePlantCompanyId().equals("")||mChooseDeviceItemData.getMaintenancePlantId().equals("")){
                    showDialogCaveatMessage(getResourceString(R.string.add_device_no_maintenance_plant_error));
                }
                mPresenter.onGetPMFCTData(mChooseDeviceItemData.getMaintenancePlantCompanyId(),mChooseDeviceItemData.getMaintenancePlantId());
                break;
            case R.id.text_value_device_number:
                if (mChooseDeviceItemData.getCompanyId().equals("")){
                    showDialogCaveatMessage(getResourceString(R.string.add_device_no_company_error));
                }
                if (mChooseDeviceItemData.getProductionPlantId().equals("")){
                    showDialogCaveatMessage(getResourceString(R.string.add_device_no_production_plant_error));
                }
                if (mChooseDeviceItemData.getDeviceCategryId().equals("")){
                    showDialogCaveatMessage(getResourceString(R.string.add_device_no_device_categry_error));
                }
                mPresenter.onGetEQNOData(mChooseDeviceItemData.getCompanyId(),mChooseDeviceItemData.getProductionPlantId(),mChooseDeviceItemData.getDeviceCategryId());
                break;
            case R.id.text_value_device_category:
                if (mChooseDeviceItemData.getCompanyId().equals("")){
                    showDialogCaveatMessage(getResourceString(R.string.add_device_no_company_error));
                }
                if (mChooseDeviceItemData.getProductionPlantId().equals("")){
                    showDialogCaveatMessage(getResourceString(R.string.add_device_no_production_plant_error));
                }
                mPresenter.onGetEQKDData(mChooseDeviceItemData.getCompanyId(),mChooseDeviceItemData.getProductionPlantId());
                break;
            case R.id.text_value_company:
                mPresenter.onGetCOData();
                break;
        }
    }

    @Override
    public void setCOData(List<COResponse> adapterData) {
        dialogString.clear();
        mAddDeviceData.setmCODataList(adapterData
        );
        for(COResponse mCOResponse: adapterData){
            dialogString.add(mCOResponse.getcONM());
        }
        showItemDialog(dialogString,onCompanyDialogItemClick);
    }

    @Override
    public void setEQKDData(List<EQKDResponse> adapterData) {
        dialogString.clear();
        mAddDeviceData.setmEQKDDataList(adapterData);
        for(EQKDResponse mEQKDResponse: adapterData){
            dialogString.add(mEQKDResponse.getmEQKDNM());
        }
        showItemDialog(dialogString,onDeviceCategoryDialogItemClick);
    }

    @Override
    public void setEQNOData(List<EQNOResponse> adapterData) {
        dialogString.clear();
        mAddDeviceData.setmEQNODataList(adapterData);
        for(EQNOResponse mEQNOResponse: adapterData){
            dialogString.add(mEQNOResponse.getmEQNM());
        }
        showItemDialog(dialogString,onDeciceDialogItemClick);
    }


    @Override
    public void setMNTFCTData(List<MNTFCTResponse> adapterData) {
        dialogString.clear();
        mAddDeviceData.setmMNTFCTDataList(adapterData);
        for(MNTFCTResponse mMNTFCTResponse: adapterData){
            dialogString.add(mMNTFCTResponse.getmNTFCTNM());
        }
        showItemDialog(dialogString,onMaintenanceDialogItemClick);
    }

    @Override
    public void setPMFCTData(List<PMFCTResponse> adapterData) {
        dialogString.clear();
        mAddDeviceData.setmPMFCTDataList(adapterData);
        for(PMFCTResponse mPMFCTResponse: adapterData){
            dialogString.add(mPMFCTResponse.getmPMFCTNM());
        }
        showItemDialog(dialogString,onProductionDialogItemClick);
    }


    private DialogInterface.OnClickListener onCompanyDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            textCompany.setText(mAddDeviceData.getmCODataList().get(which).getcONM());
            mChooseDeviceItemData.setCompany(mAddDeviceData.getmCODataList().get(which).getcONM());
            mChooseDeviceItemData.setCompanyId(mAddDeviceData.getmCODataList().get(which).getcO());
        }
    };
    private DialogInterface.OnClickListener onMaintenanceDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            textMaintenancePlant.setText(mAddDeviceData.getmMNTFCTDataList().get(which).getmNTFCTNM());
            mChooseDeviceItemData.setMaintenancePlant(mAddDeviceData.getmMNTFCTDataList().get(which).getmNTFCTNM());
            mChooseDeviceItemData.setMaintenancePlantCompanyId(mAddDeviceData.getmMNTFCTDataList().get(which).getmNTCO());
            mChooseDeviceItemData.setMaintenancePlantId(mAddDeviceData.getmMNTFCTDataList().get(which).getmNTFCT());

        }
    };
    private DialogInterface.OnClickListener onProductionDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            textProductionPlant.setText(mAddDeviceData.getmPMFCTDataList().get(which).getmPMFCTNM());
            mChooseDeviceItemData.setProductionPlant(mAddDeviceData.getmPMFCTDataList().get(which).getmPMFCTNM());
            mChooseDeviceItemData.setProductionPlantId(mAddDeviceData.getmPMFCTDataList().get(which).getmPMFCT());
        }
    };
    private DialogInterface.OnClickListener onDeviceCategoryDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            textDeviceCategory.setText(mAddDeviceData.getmEQKDDataList().get(which).getmEQKDNM());
            mChooseDeviceItemData.setDeviceCategory(mAddDeviceData.getmEQKDDataList().get(which).getmEQKDNM());
            mChooseDeviceItemData.setDeviceCategryId(mAddDeviceData.getmEQKDDataList().get(which).getmEQKD());
        }
    };
    private DialogInterface.OnClickListener onDeciceDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            textDeviceNumber.setText(mAddDeviceData.getmEQNODataList().get(which).getmEQNM());
            mChooseDeviceItemData.setDeciceId(mAddDeviceData.getmEQNODataList().get(which).getmEQNO());
            mChooseDeviceItemData.setDeciceName(mAddDeviceData.getmEQNODataList().get(which).getmEQNM());
        }
    };

    private void uploadDevice(){
        if (mChooseDeviceItemData.getMaintenancePlantId().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_maintenance_plant));
        }else if(mChooseDeviceItemData.getCompanyId().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_company));
        }else if(mChooseDeviceItemData.getProductionPlantId().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_production_plant));
        }else if(mChooseDeviceItemData.getDeviceCategryId().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_device_categry));
        }else if(mChooseDeviceItemData.getDeciceId().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_device_id));
        }else if(mChooseDeviceItemData.getKeynote().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_keynote));
        }else if(mChooseDeviceItemData.getFilePath().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_file));
        }else{
            Intent intent = new Intent();
            intent.putExtra("device",mChooseDeviceItemData);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
