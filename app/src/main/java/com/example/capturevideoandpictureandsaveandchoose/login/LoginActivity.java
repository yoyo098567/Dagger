package com.example.capturevideoandpictureandsaveandchoose.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capturevideoandpictureandsaveandchoose.MainActivity;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText editAccount,editPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    public void init() {
        editAccount=findViewById(R.id.edit_account);
        editAccount=findViewById(R.id.edit_password);
        btnLogin=findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Log.e("gggg","www");
                onLogin();
                break;
        }
    }
    private void onLogin(){
        if("".equals(editAccount.getText().toString())){
            showDialogMessage(getResourceString(R.string.login_accoutn_hint));
        }
        if ("".equals(editPassword.getText().toString())){
            showDialogMessage(getResourceString(R.string.login_password_hint));
        }
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
