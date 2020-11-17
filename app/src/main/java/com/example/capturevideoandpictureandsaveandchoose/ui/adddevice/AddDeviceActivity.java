package com.example.capturevideoandpictureandsaveandchoose.ui.adddevice;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.capturevideoandpictureandsaveandchoose.Application;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.databinding.ActivityAddDeviceBinding;
import com.example.capturevideoandpictureandsaveandchoose.di.component.adddevice.AddDeviceComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.component.adddevice.DaggerAddDeviceComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.adddevice.AddDeviceModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchco.COResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqkd.EQKDResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searcheqno.EQNOResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchmntfct.MNTFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.api.apidata.searchpmfct.PMFCTResponse;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferences;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferencesProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
// implements View.OnClickListener
public class AddDeviceActivity extends BaseActivity implements AddDeviceContract.View {
    @Inject
    AddDeviceContract.Presenter<AddDeviceContract.View> mPresenter;

    @Inject
    LoginPreferencesProvider loginPreferencesProvider;
    private AddDeviceData mAddDeviceData;
    private ChooseDeviceItemData mChooseDeviceItemData;
    private AddDeviceComponent mAddDeviceComponent;
    private ArrayList<String> dialogString;
    String account;
    CharSequence date;
    ActivityAddDeviceBinding activityAddDeviceBinding;
    private AddActivityViewData addActivityViewData;
    private Integer isWhereAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        Intent intent = this.getIntent();
        account = intent.getStringExtra("account");
        init();
        mPresenter.onAttached(this);
        mPresenter.setUserId(account);
    }

    @Override
    public void init() {

        mAddDeviceComponent = DaggerAddDeviceComponent.builder()
                .addDeviceModule(new AddDeviceModule(this))
                .baseComponent(((Application) getApplication()).getApplicationComponent())
                .build();
        mAddDeviceComponent.inject(this);

        Calendar mCal = Calendar.getInstance();
        date = DateFormat.format("yyyy/MM/dd", mCal.getTime());

        isWhereAccount=loginPreferencesProvider.getPersonId();

        addActivityViewData=new AddActivityViewData();
        activityAddDeviceBinding= DataBindingUtil.setContentView(this,R.layout.activity_add_device);
        activityAddDeviceBinding.setView(this);
        addActivityViewData.setDate(date.toString());
        activityAddDeviceBinding.setData(addActivityViewData);

        dialogString=new ArrayList<>();
        mAddDeviceData=new AddDeviceData();
        mChooseDeviceItemData=new ChooseDeviceItemData();
    }


    @Override
    public void OnUploadClick() {
        uploadDevice();
    }

    @Override
    public void OnMaintenancePlantClick() {
        if (isWhereAccount==1){
            mPresenter.onGetMNTFCTData();
        }else if (isWhereAccount==0){
            mPresenter.onCNGetMNTFCTData();
        }

    }

    @Override
    public void OnCompanyClick() {

        if (isWhereAccount==1){
            mPresenter.onGetCOData();
        }else if (isWhereAccount==0){
            mPresenter.onCNGetCOData();
        }

    }

    @Override
    public void OnProductionPlantClick() {

        if (mChooseDeviceItemData.getMNTFCTNM().equals("")|| mChooseDeviceItemData.getMNTFCT().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_maintenance_plant_error));
        }
        if (isWhereAccount==1){
            mPresenter.onGetPMFCTData(mChooseDeviceItemData.getMNTCO(), mChooseDeviceItemData.getMNTFCT());
        }else if (isWhereAccount==0){
            mPresenter.onCNGetPMFCTData(mChooseDeviceItemData.getMNTCO(), mChooseDeviceItemData.getMNTFCT());
        }

    }

    @Override
    public void OnDeviceCategoryClick() {
        if (mChooseDeviceItemData.getCO().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_company_error));
        }
        if (mChooseDeviceItemData.getPMFCT().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_production_plant_error));
        }

        if (isWhereAccount==1){
            mPresenter.onGetEQKDData(mChooseDeviceItemData.getCO(), mChooseDeviceItemData.getPMFCT());
        }else if (isWhereAccount==0){
            mPresenter.onCNGetEQKDData(mChooseDeviceItemData.getCO(), mChooseDeviceItemData.getPMFCT());
        }

    }

    @Override
    public void OnDeviceNumberClick() {
        if (mChooseDeviceItemData.getCO().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_company_error));
        }
        if (mChooseDeviceItemData.getPMFCT().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_production_plant_error));
        }
        if (mChooseDeviceItemData.getEQKD().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_device_categry_error));
        }

        if (isWhereAccount==1){
            mPresenter.onGetEQNOData(mChooseDeviceItemData.getCO(), mChooseDeviceItemData.getPMFCT(), mChooseDeviceItemData.getEQKD());
        }else if (isWhereAccount==0){
            mPresenter.onCNGetEQNOData(mChooseDeviceItemData.getCO(), mChooseDeviceItemData.getPMFCT(), mChooseDeviceItemData.getEQKD());
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
            dialogString.add(mEQKDResponse.getmEQKD()+"  "+mEQKDResponse.getmEQKDNM());
        }
        showItemDialog(dialogString,onDeviceCategoryDialogItemClick);
    }

    @Override
    public void setEQNOData(List<EQNOResponse> adapterData) {
        dialogString.clear();
        mAddDeviceData.setmEQNODataList(adapterData);
        for(EQNOResponse mEQNOResponse: adapterData){
            dialogString.add(mEQNOResponse.getmEQNO()+" "+mEQNOResponse.getmEQNM());
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
            addActivityViewData.setCOData(mAddDeviceData.getmCODataList().get(which).getcONM());
            activityAddDeviceBinding.setData(addActivityViewData);
           // textCompany.setText(mAddDeviceData.getmCODataList().get(which).getcONM());
            mChooseDeviceItemData.setCONM(mAddDeviceData.getmCODataList().get(which).getcONM());
            mChooseDeviceItemData.setCO(mAddDeviceData.getmCODataList().get(which).getcO());
        }
    };
    private DialogInterface.OnClickListener onMaintenanceDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            addActivityViewData.setMNTFCTData(mAddDeviceData.getmMNTFCTDataList().get(which).getmNTFCTNM());
            activityAddDeviceBinding.setData(addActivityViewData);
            mChooseDeviceItemData.setMNTFCTNM(mAddDeviceData.getmMNTFCTDataList().get(which).getmNTFCTNM());
            mChooseDeviceItemData.setMNTCO(mAddDeviceData.getmMNTFCTDataList().get(which).getmNTCO());
            mChooseDeviceItemData.setMNTFCT(mAddDeviceData.getmMNTFCTDataList().get(which).getmNTFCT());

        }
    };
    private DialogInterface.OnClickListener onProductionDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            addActivityViewData.setPMFCTData(mAddDeviceData.getmPMFCTDataList().get(which).getmPMFCTNM());
            activityAddDeviceBinding.setData(addActivityViewData);
            ///textProductionPlant.setText(mAddDeviceData.getmPMFCTDataList().get(which).getmPMFCTNM());
            mChooseDeviceItemData.setPMFCTNM(mAddDeviceData.getmPMFCTDataList().get(which).getmPMFCTNM());
            mChooseDeviceItemData.setPMFCT(mAddDeviceData.getmPMFCTDataList().get(which).getmPMFCT());
        }
    };
    private DialogInterface.OnClickListener onDeviceCategoryDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            addActivityViewData.setEQKDData(mAddDeviceData.getmEQKDDataList().get(which).getmEQKD());
            addActivityViewData.setCategoryNameValue(mAddDeviceData.getmEQKDDataList().get(which).getmEQKDNM());
            activityAddDeviceBinding.setData(addActivityViewData);
//            textDeviceCategory.setText(mAddDeviceData.getmEQKDDataList().get(which).getmEQKD());
//            textDeviceCategoryNameValue.setText(mAddDeviceData.getmEQKDDataList().get(which).getmEQKDNM());
            mChooseDeviceItemData.setEQKDNM(mAddDeviceData.getmEQKDDataList().get(which).getmEQKDNM());
            mChooseDeviceItemData.setEQKD(mAddDeviceData.getmEQKDDataList().get(which).getmEQKD());
        }
    };
    private DialogInterface.OnClickListener onDeciceDialogItemClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            addActivityViewData.setEQNOData(mAddDeviceData.getmEQNODataList().get(which).getmEQNO());
            addActivityViewData.setDeviceNameValue(mAddDeviceData.getmEQNODataList().get(which).getmEQNO());
            activityAddDeviceBinding.setData(addActivityViewData);
//            textDeviceNumber.setText(mAddDeviceData.getmEQNODataList().get(which).getmEQNO());
//            textDeviceNameValue.setText(mAddDeviceData.getmEQNODataList().get(which).getmEQNM());
            mChooseDeviceItemData.setEQNO(mAddDeviceData.getmEQNODataList().get(which).getmEQNO());
            mChooseDeviceItemData.setEQNM(mAddDeviceData.getmEQNODataList().get(which).getmEQNM());
        }
    };

    private void uploadDevice(){
        mChooseDeviceItemData.setRecordSubject(mChooseDeviceItemData.getEQNO());
        if (mChooseDeviceItemData.getMNTFCT().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_maintenance_plant));
        }else if(mChooseDeviceItemData.getCO().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_company));
        }else if(mChooseDeviceItemData.getPMFCT().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_production_plant));
        }else if(mChooseDeviceItemData.getEQKD().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_device_categry));
        }else if(mChooseDeviceItemData.getEQNO().equals("")){
            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_device_id));
        }
//        else if(mChooseDeviceItemData.getRecordSubject().equals("")){
//            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_keynote));
//        }
//        else if(mChooseDeviceItemData.getFilePath().equals("")){
//            showDialogCaveatMessage(getResourceString(R.string.add_device_no_data_file));
//        }
        else{
            mChooseDeviceItemData.setUploadEMP(account);
            mChooseDeviceItemData.setUploadNM("");
            mChooseDeviceItemData.setUploadDATETM(date.toString());
            Intent intent = new Intent();
            intent.putExtra("device", mChooseDeviceItemData);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
