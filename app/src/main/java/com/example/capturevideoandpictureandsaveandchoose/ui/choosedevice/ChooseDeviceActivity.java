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
        chooseDeviceItemDataList=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); // 必須設置 LayoutManager
        chooseDeviceAdapter = new ChooseDeviceAdapter();
        for (int i=0;i<3;i++){
            ChooseDeviceItemData chooseDeviceItemData=new ChooseDeviceItemData();
            chooseDeviceItemData.setDeciceId("aaaa"+i);
            chooseDeviceItemDataList.add(chooseDeviceItemData);
        }
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
                Log.e("gggg",""+chooseDeviceAdapter.getCurrentPosition());
                Log.e("gggg",""+chooseDeviceAdapter.getDataList().get(chooseDeviceAdapter.getCurrentPosition()).getBackgroundChange());

                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ADD_DEVICE_NUMBER && resultCode==RESULT_OK){
            Log.e("gggg",""+data.getExtras().getString("a"));
        }
        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
    }

    @Override
    public void setCurrentItem(int position) {

    }
}
