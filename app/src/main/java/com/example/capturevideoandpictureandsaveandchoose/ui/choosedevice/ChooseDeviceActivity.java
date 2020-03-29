package com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capturevideoandpictureandsaveandchoose.Application;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.di.component.choosedevice.ChooseDeviceComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.component.choosedevice.DaggerChooseDeviceComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.choosedevice.ChooseDeviceModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.adddevice.AddDeviceActivity;

import javax.inject.Inject;

public class ChooseDeviceActivity extends BaseActivity implements ChooseDeviceContract.View,View.OnClickListener {
    @Inject
    ChooseDeviceContract.Presenter<ChooseDeviceContract.View> mPresenter;

    private TextView textDeviceNumberData;
    private Button btnAdd, btnDelete;
    private RecyclerView recyclerView;
    private Button btnBack;
    private ChooseDeviceComponent mChooseDeviceComponent;

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
        btnBack.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(this, AddDeviceActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_delete:
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
