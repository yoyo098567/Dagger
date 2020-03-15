package com.example.capturevideoandpictureandsaveandchoose.chose_device;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;

public class ChooseDeviceActivity extends BaseActivity implements View.OnClickListener {
    private TextView textDeviceNumberData;
    private Button btnAdd, btnDelete;
    private RecyclerView recyclerView;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        init();
    }

    @Override
    public void init() {
        textDeviceNumberData = findViewById(R.id.text_device_number_data);
        recyclerView = findViewById(R.id.recycler_view);
        btnAdd = findViewById(R.id.btn_add);
        btnDelete = findViewById(R.id.btn_delete);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                break;
            case R.id.btn_delete:
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
