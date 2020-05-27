package com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capturevideoandpictureandsaveandchoose.Application;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.di.component.choosedevice.ChooseDeviceComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.component.choosedevice.DaggerChooseDeviceComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.choosedevice.ChooseDeviceModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.adddevice.AddDeviceActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class ChooseDeviceActivity extends BaseActivity implements ChooseDeviceContract.View,View.OnClickListener {
    @Inject
    ChooseDeviceContract.Presenter<ChooseDeviceContract.View> mPresenter;

    private TextView textDeviceNumberData;
    private Button btnAdd, btnDelete;
    private RecyclerView recyclerView;
    private Button btnBack;
    private ChooseDeviceComponent mChooseDeviceComponent;
    private ArrayList<ChooseDeviceItemData> chooseDeviceItemDataList;
    private ChooseDeviceAdapter chooseDeviceAdapter;
    public static final int ADD_DEVICE_NUMBER=2020;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        init();
        mPresenter.onAttached(this);

    }

    @Override
    public void init() {
        textDeviceNumberData = findViewById(R.id.text_device_number_data);
        recyclerView = findViewById(R.id.recycler_view);
        btnAdd = findViewById(R.id.btn_add);
        btnDelete = findViewById(R.id.btn_delete);
        btnBack = findViewById(R.id.btn_back);
        mChooseDeviceComponent = DaggerChooseDeviceComponent.builder()
                .chooseDeviceModule(new ChooseDeviceModule(this))
                .baseComponent(((Application) getApplication()).getApplicationComponent())
                .build();
        mChooseDeviceComponent.inject(this);
        btnBack.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        Bundle bundle=getIntent().getExtras();

        chooseDeviceItemDataList= (ArrayList<ChooseDeviceItemData>) bundle.getSerializable("deviceDataList");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); // 必須設置 LayoutManager
        chooseDeviceAdapter = new ChooseDeviceAdapter();
        chooseDeviceAdapter.setDataList(chooseDeviceItemDataList);
        recyclerView.setAdapter(chooseDeviceAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(this, AddDeviceActivity.class);
                startActivityForResult(intent, ADD_DEVICE_NUMBER);
                break;
            case R.id.btn_delete:
                if (chooseDeviceItemDataList.size()>0){
                    chooseDeviceItemDataList.remove(chooseDeviceAdapter.getCurrentPosition());
                    chooseDeviceAdapter.setDataList(chooseDeviceItemDataList);
                }
                break;
            case R.id.btn_back:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("NonInspectionWorkDevice",chooseDeviceItemDataList);
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent resultIntent = new Intent();
        //if(chooseDeviceItemDataList.size()>0){
        //    if(chooseDeviceItemDataList.size()>1){
        //        chooseDeviceItemDataList.get(chooseDeviceItemDataList.size()-2).setCheckEndItem(false);
        //        chooseDeviceItemDataList.get(chooseDeviceItemDataList.size()-1).setCheckEndItem(true);
        //        resultIntent.putExtra("NonInspectionWorkDevice",chooseDeviceItemDataList);
        //    }else{
        //        chooseDeviceItemDataList.get(chooseDeviceItemDataList.size()-1).setCheckEndItem(true);
        //        resultIntent.putExtra("NonInspectionWorkDevice",chooseDeviceItemDataList);
        //    }
        //    setResult(RESULT_OK, resultIntent);
        //    finish();
        //}else {
        //    fileList();
        //}
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NonInspectionWorkDevice",chooseDeviceItemDataList);
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ChooseDeviceItemData mChooseDeviceItemData;
        if(requestCode==ADD_DEVICE_NUMBER && resultCode==RESULT_OK){
            mChooseDeviceItemData= (ChooseDeviceItemData) data.getSerializableExtra("device");
            Log.e("wwww1",""+mChooseDeviceItemData.getCompany());
            chooseDeviceAdapter.addDataToDataList(mChooseDeviceItemData);
        }else {
            showDialogCaveatMessage(getResourceString(R.string.add_device_error_on_choose_device_activity));
        }
    }

    @Override
    public void setCurrentItem(int position) {

    }
}
