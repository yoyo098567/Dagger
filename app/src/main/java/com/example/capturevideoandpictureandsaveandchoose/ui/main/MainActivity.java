package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capturevideoandpictureandsaveandchoose.Application;
import com.example.capturevideoandpictureandsaveandchoose.MagicFileChooser;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.di.component.main.DaggerMainComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.component.main.MainComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.main.MainModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceActivity;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.ui.deviceinformation.DeviceInformationActivity;
import com.example.capturevideoandpictureandsaveandchoose.utils.service.NonInspectionService;
import com.example.capturevideoandpictureandsaveandchoose.utils.service.TeleportService;
import com.guoxiaoxing.phoenix.compress.video.VideoCompressor;
import com.guoxiaoxing.phoenix.compress.video.format.MediaFormatStrategyPresets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.capturevideoandpictureandsaveandchoose.ui.login.LoginActivity.loginStatus;

public class MainActivity extends BaseActivity implements MainContract.View, View.OnClickListener {
    @Inject
    MainContract.Presenter<MainContract.View> mPresenter;
    FilePartition filePartition;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private static final int REQUEST_VIDEO_CAPTURE = 200;
    private static final int PICK_IMAGE_FROM_GALLERY_REQUEST_CODE = 300;
    private static final int PICK_VIDEO_FROM_GALLERY_REQUEST_CODE = 400;
    private static final int PICK_FILE_REQUEST_CODE = 500;
    private static final int DEVICE_INFORMATION = 600;
    private static final int GET_DEVICE_DATA = 2021;
    private String AccessToken = "";
    private ArrayList<ChooseDeviceItemData> deviceDataList;


    Handler handler = new Handler();

    String imageFilePath;
    private Button btnFetchDevice, btnCapturePicture, btnRecordVideo, btnGetImageFromGallery, btnGetVideoFromGallery, btnDeviceEdit, btnCentralCloud, btnChoseDevice, btnBasicInformation;
    private TextView textDeviceNumber, textRouteCodeData;
    private File photoFile;
    private MainComponent mMainComponent;
    final String sn = android.os.Build.SERIAL;
    int countFile = 0;
    int deviceDataPosition = 0;
    int currentDataCount = 0;
    private IntentFilter mIntentFilter;
    String fetchDeviceMsg;
    String account;
    Intent NonInspectionServiceIntent;
    private Intent mTeleportServiceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mPresenter.onAttached(this);
        onStartTeleportService();
        mPresenter.onGetDisposableToken(sn);
        filePartition = new FilePartition();
    }

    @Override
    public void init() {
        btnCentralCloud = findViewById(R.id.btn_central_cloud);
        btnCapturePicture = findViewById(R.id.btnCaptureImage);
        textRouteCodeData = findViewById(R.id.text_route_code_data);
        textDeviceNumber = findViewById(R.id.text_device_number_data);
        btnRecordVideo = findViewById(R.id.btnRecordVideo);
        btnGetImageFromGallery = findViewById(R.id.btnGetImageFromGallery);
        btnGetVideoFromGallery = findViewById(R.id.btnGetVideoFromGallery);
        btnDeviceEdit = findViewById(R.id.btn_device_edit);
        btnChoseDevice = findViewById(R.id.btn_chose_device);
        btnBasicInformation = findViewById(R.id.btn_basic_information);
        btnFetchDevice = findViewById(R.id.btnFetchDevice);
        deviceDataList = new ArrayList<>();
        mMainComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .baseComponent(((Application) getApplication()).getApplicationComponent())
                .build();
        mMainComponent.inject(this);
        currentDataCount = 0;
        Log.v("LoginStatus", "" + loginStatus);
        if(loginStatus == 1){
            btnDeviceEdit.setEnabled(false);
            autoLogin();
        }
//        autoLogin();
        Intent intent = this.getIntent();
        AccessToken = intent.getStringExtra("AccessToken");
        account = intent.getStringExtra("account");
        btnCapturePicture.setOnClickListener(this);
        btnRecordVideo.setOnClickListener(this);
        btnGetImageFromGallery.setOnClickListener(this);
        btnGetVideoFromGallery.setOnClickListener(this);
        btnDeviceEdit.setOnClickListener(this);
        btnCentralCloud.setOnClickListener(this);
        btnChoseDevice.setOnClickListener(this);
        btnBasicInformation.setOnClickListener(this);
        btnFetchDevice.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("true".equals(intent.getStringExtra("time"))) {
                getCurrentDataList();
                if (deviceDataList.get(currentDataCount).getProgress() == 100) {
                    mPresenter.onAddChkInfo(deviceDataList.get(currentDataCount));
                }
            }else if("false".equals(intent.getStringExtra("time"))){
                //上傳end部分然後關掉service
                deviceDataList.get(currentDataCount).setEQNO("end");
                mPresenter.onAddChkInfo(deviceDataList.get(currentDataCount));
                stopService(mTeleportServiceIntent);
            }else{

            }
//            Log.e("ggggg7", "getExtra:" + intent.getStringExtra("Data"));
//            if(intent.getStringExtra("Data").equals("positionUpdate")){
//                int a = intent.getIntExtra("position",0);
//                Log.e("ggggg77", "" + a);
//                textDeviceNumber.setText(deviceDataList.get(a).getEQNO());
//                deviceDataPosition++;
//            }else if(intent.getStringExtra("Data").equals("end")){
//                Log.e("ggggg77", "END" );
//                deviceDataPosition = 0;
//            }
        }
    };

    private void fetchDevice() {
        showDialogMessage(fetchDeviceMsg);
    }

    private void pickImageFromGallery() {

        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST_CODE);
    }

    private void autoLogin() {
        String CONTENT_STRING = "content://tw.com.efpg.processe_equip.provider.ShareCloud/ShareCloud";
        Uri uri = Uri.parse(CONTENT_STRING);
        Cursor cursor = this.getContentResolver().query(
                uri,
                null,
                "CurrentJob",
                null, null
        );
        int a = 0;

        while (cursor.moveToNext()) {
            a++;
            Log.v("aaa", "" + a);
            String OPCO = cursor.getString(cursor.getColumnIndexOrThrow("OPCO"));
            String OPPLD = cursor.getString(cursor.getColumnIndexOrThrow("OPPLD"));
            String PMFCT = cursor.getString(cursor.getColumnIndexOrThrow("PMFCT"));
            String WAYID = cursor.getString(cursor.getColumnIndexOrThrow("WAYID"));
            String WAYNM = cursor.getString(cursor.getColumnIndexOrThrow("WAYNM"));
            String CTLPTID = cursor.getString(cursor.getColumnIndexOrThrow("CTLPTID"));
            String IT = cursor.getString(cursor.getColumnIndexOrThrow("IT"));
            String EQNO = cursor.getString(cursor.getColumnIndexOrThrow("EQNO"));
            String EQNM = cursor.getString(cursor.getColumnIndexOrThrow("EQNM"));
            String EQKD = cursor.getString(cursor.getColumnIndexOrThrow("EQKD"));
            int progress = cursor.getInt(cursor.getColumnIndexOrThrow("Progress"));
            String CO = cursor.getString(cursor.getColumnIndexOrThrow("CO"));
            String CONM = cursor.getString(cursor.getColumnIndexOrThrow("CONM"));
            String PMFCTNM = cursor.getString(cursor.getColumnIndexOrThrow("PMFCTNM"));
            Log.v("autoLogin", "OPCO:" + OPCO);
            Log.v("autoLogin", "OPPLD:" + OPPLD);
            Log.v("autoLogin", "PMFCT:" + PMFCT);
            Log.v("autoLogin", "WAYID:" + WAYID);
            Log.v("autoLogin", "WAYNM:" + WAYNM);
            Log.v("autoLogin", "CTLPTID:" + CTLPTID);
            Log.v("autoLogin", "IT:" + IT);
            Log.v("autoLogin", "EQNO:" + EQNO);
            Log.v("autoLogin", "EQNM:" + EQNM);
            Log.v("autoLogin", "EQKD:" + EQKD);
            Log.v("autoLogin", "progress:" + progress);
            Log.v("autoLogin", "CO:" + CO);
            Log.v("autoLogin", "CONM:" + CONM);
            Log.v("autoLogin", "PMFCTNM:" + PMFCTNM);


            ChooseDeviceItemData mChooseDeviceItemData = new ChooseDeviceItemData();
            mChooseDeviceItemData.setOPCO(OPCO);
            mChooseDeviceItemData.setOPPLD(OPPLD);
            mChooseDeviceItemData.setPMFCT(PMFCT);
            mChooseDeviceItemData.setWAYID(WAYID);
            mChooseDeviceItemData.setWAYNM(WAYNM);
            mChooseDeviceItemData.setEQNO(EQNO);
            mChooseDeviceItemData.setEQNM(EQNM);
            mChooseDeviceItemData.setEQKD(EQKD);
            mChooseDeviceItemData.setProgress(progress);
            mChooseDeviceItemData.setCO(CO);
            mChooseDeviceItemData.setCONM(CONM);
            mChooseDeviceItemData.setPMFCTNM(PMFCTNM);
            mChooseDeviceItemData.setUploadNM("王小明");
            mChooseDeviceItemData.setUploadEMP(account);
            mChooseDeviceItemData.setChcekDataFromAPP(true);
            deviceDataList.add(mChooseDeviceItemData);

            textRouteCodeData.setText(WAYID);
            textDeviceNumber.setText(EQNO);
        }
        fetchDeviceMsg = "筆數:" + deviceDataList.size() + "首筆資料:{CO:" + deviceDataList.get(0).getCO() +
                ",CONM:" + deviceDataList.get(0).getCONM() +
                ",EQKD:" + deviceDataList.get(0).getEQKD() +
                ",EQNM:" + deviceDataList.get(0).getEQNM() +
                ",EQNO:" + deviceDataList.get(0).getEQNO() +
                ",OPCO:" + deviceDataList.get(0).getOPCO() +
                ",OPPLD:" + deviceDataList.get(0).getOPPLD() +
                ",PMFCT:" + deviceDataList.get(0).getPMFCT() +
                ",PMFCTNM:" + deviceDataList.get(0).getPMFCTNM() +
                ",Progress:" + deviceDataList.get(0).getProgress() +
                ",WAYID:" + deviceDataList.get(0).getWAYID() +
                ",WAYNM:" + deviceDataList.get(0).getWAYNM() + "}";
    }

    private void pickVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        String[] mimeTypes = {"video/mp4", "video/mov"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_VIDEO_FROM_GALLERY_REQUEST_CODE);
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//        String[] mimeTypes = {"application/pdf"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        settingSystemCamera(pictureIntent);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.capturevideoandpictureandsaveandchoose.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private void openRecordVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(takeVideoIntent, 0);
        takeVideoIntent.setPackage(listCam.get(0).activityInfo.packageName);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void deviceInformation() {
        Intent it = new Intent(this, DeviceInformationActivity.class);
        if (deviceDataList.size() != 0 && !textDeviceNumber.getText().equals("")) {
            it.putExtra("NonInspectionWorkDevice", deviceDataList);
            it.putExtra("device", deviceDataList.get(deviceDataPosition));
        }
        startActivityForResult(it, DEVICE_INFORMATION);
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void settingSystemCamera(Intent intent) {
        PackageManager packageManager = MainActivity.this.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(intent, 0);
        intent.setPackage(listCam.get(0).activityInfo.packageName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            File imgFile = new File(imageFilePath);
            //照片的檔案
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                try {
                    FileOutputStream out = new FileOutputStream(imgFile);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            //影片的uri
            Uri videoUri = data.getData();
        }

        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getClipData().getItemAt(0).getUri();
            ArrayList<String> uriList = new ArrayList<String>();
            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
                Log.e("gggg", "" + uriList.get(i));
            }
            Log.e("gggg", "1:" + data.getClipData().getItemCount());
            //照片的uri
            onUploadFile(uriList, getResourceString(R.string.on_upload_image));
        }

        if (requestCode == PICK_VIDEO_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedVideo = data.getData();
            ArrayList<String> uriList = new ArrayList<String>();
            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
                Log.e("gggg555", "" + uriList.get(i));
                filePartition.partition(uriList.get(i), 50 * 1024 * 1024);

                try {
                    File file = new File(uriList.get(i));
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(file.getPath());
                    mediaPlayer.prepare();
                    Log.e("ggggLONG", "" + mediaPlayer.getDuration());
                    Log.e("ggggCurren", "" + mediaPlayer.getCurrentPosition());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            // TODO here====================
            ArrayList<String> compressList = new ArrayList<>();
            compressList.addAll(compressVideo(uriList));
            countFile = 0;


            //先註解測試影片壓縮功能
            onUploadFile(compressList, getResourceString(R.string.on_upload_vedio));
            //影片的uri
        }

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData();
            Log.e("ggggg", "" + selectedFile);
            String filePath = MagicFileChooser.getAbsolutePathFromUri(this, selectedFile);
            Log.e("filePath", filePath + "");

            checkFileTypeAndOpen(filePath, selectedFile);

        }

        if (requestCode == GET_DEVICE_DATA && resultCode == RESULT_OK) {
            deviceDataList = (ArrayList<ChooseDeviceItemData>) data.getSerializableExtra("NonInspectionWorkDevice");
        }

        if (requestCode == DEVICE_INFORMATION && resultCode == RESULT_OK) {
            deviceDataList = (ArrayList<ChooseDeviceItemData>) data.getSerializableExtra("NonInspectionWorkDevice");
        }
    }

    //根據副檔名判斷用什麼應用程式開啟
    private void checkFileTypeAndOpen(String filePath, Uri selectedFile) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (filePath.contains(".doc")) {
            intent.setDataAndType(selectedFile, "application/msword");
        } else if (filePath.contains(".ppt")) {
            intent.setDataAndType(selectedFile, "application/vnd.ms-powerpoint");
        } else if ((filePath.contains(".csv")) || (filePath.contains(".xls"))) {
            intent.setDataAndType(selectedFile, "application/vnd.ms-excel");
        } else if (filePath.contains(".pdf")) {
            intent.setDataAndType(selectedFile, "application/pdf");
        } else if (filePath.contains(".txt")) {
            intent.setDataAndType(selectedFile, "text/plain");
        } else {
            intent.setDataAndType(selectedFile, "application/*");
        }

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
        if (list.size() > 0)
            startActivity(intent);
    }

    private ArrayList compressVideo(ArrayList<String> uriList) {
        final ArrayList<String> returnList = new ArrayList<>();
        final ArrayList<File> inputFileList = new ArrayList<>();
        final ArrayList<File> compressFileList = new ArrayList<>();
        for (int i = 0; i < uriList.size(); i++) {
            inputFileList.add(i, new File(uriList.get(i)));
        }
        int countNeedToCompress = 0;
        for (int i = 0; i < uriList.size(); i++) {
            try {
                File compressCachePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "phoenix");
                compressCachePath.mkdir();
                Log.e("input", inputFileList.get(i).length() + "");
                if (inputFileList.get(i).length() / 1048576 >= 50) {
                    compressFileList.add(countNeedToCompress, File.createTempFile("compress" + i, ".mp4", compressCachePath));
                    countNeedToCompress++;

                } else {
                    returnList.add(uriList.get(i));
                }
            } catch (IOException e) {
                Toast.makeText(this, "Failed to create temporary file.", Toast.LENGTH_LONG).show();
                return null;
            }
        }


        VideoCompressor.Listener listener = new VideoCompressor.Listener() {
            @Override
            public void onTranscodeProgress(double progress) {
            }

            @Override
            public void onTranscodeCompleted() {
                String compressPath = compressFileList.get(countFile).getAbsolutePath();
                returnList.add(compressPath);
                Log.e("afterCompress", "" + compressFileList.get(countFile).length());
                Log.e("compressPath", "" + compressPath);
                countFile++;

            }

            @Override
            public void onTranscodeCanceled() {

            }

            @Override
            public void onTranscodeFailed(Exception exception) {

            }
        };


        for (int i = 0; i < compressFileList.size(); i++) {
            try {
                VideoCompressor.with().asyncTranscodeVideo(uriList.get(i), compressFileList.get(i).getAbsolutePath(),
                        MediaFormatStrategyPresets.createAndroid480pFormatStrategy(), listener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnList;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_basic_information:
                deviceInformation();
                break;
            case R.id.btnFetchDevice:
                fetchDevice();
                break;
            case R.id.btnCaptureImage:
                openCameraIntent();
                break;
            case R.id.btnRecordVideo:
                openRecordVideoIntent();
                break;
            case R.id.btnGetImageFromGallery:
                pickImageFromGallery();
                break;
            case R.id.btnGetVideoFromGallery:
                pickVideoFromGallery();
                break;
            case R.id.btn_central_cloud:
                if (textDeviceNumber.getText().equals("")) {
                    showDialogMessage("無設備");
                } else {
                    Intent intentCentralCloud = new Intent();
                    intentCentralCloud.setAction(Intent.ACTION_VIEW);
                    if (deviceDataList.get(deviceDataPosition).isChcekDataFromAPP()){
                        deviceDataList.get(deviceDataPosition).setMNTCO("1");
                    }
                    Log.e("gggg","CO=" + deviceDataList.get(deviceDataPosition).getCO() + "&" +
                            "PMFCT=" + deviceDataList.get(deviceDataPosition).getPMFCT() + "&" +
                            "MNTCO=" + deviceDataList.get(deviceDataPosition).getMNTCO() + "&" +
                            "MNTFCT=" + deviceDataList.get(deviceDataPosition).getMNTFCT() + "&" +
                            "EQNO=" + deviceDataList.get(deviceDataPosition).getEQNO() + "&" +
                            "token=" + mPresenter.getDisposableToken());
                    intentCentralCloud.setData(Uri.parse("https://cloud.fpcetg.com.tw/FPC/WEB/MTN/MTN_EQPT/Default.aspx?" +
                            "CO=" + deviceDataList.get(deviceDataPosition).getCO() + "&" +
                            "PMFCT=" + deviceDataList.get(deviceDataPosition).getPMFCT() + "&" +
                            "MNTCO=" + deviceDataList.get(deviceDataPosition).getMNTCO() + "&" +
                            "MNTFCT=" + deviceDataList.get(deviceDataPosition).getMNTFCT() + "&" +
                            "EQNO=" + deviceDataList.get(deviceDataPosition).getEQNO() + "&" +
                            "token=" + mPresenter.getDisposableToken()));
                    startActivity(intentCentralCloud);
                }
                break;
            case R.id.btn_device_edit:
                Intent intent = new Intent(this, ChooseDeviceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("deviceDataList", deviceDataList);
                bundle.putString("account", "N000054949");
                intent.putExtras(bundle);
                startActivityForResult(intent, GET_DEVICE_DATA);
                break;
            case R.id.btn_chose_device:
                ArrayList<String> dialogDeviceIDString = new ArrayList<>();
                for (ChooseDeviceItemData deviceData : deviceDataList) {
                    dialogDeviceIDString.add(deviceData.getEQNO());
                }
                showItemDialog(dialogDeviceIDString, onNonInspectionSelectDevice);
                break;
        }
    }
    private void onSendTeleportService(String msg){
        Bundle serviceBundle = new Bundle();
        serviceBundle.putString("msg",msg);
        mTeleportServiceIntent.putExtras(serviceBundle);
        this.startService(mTeleportServiceIntent);
    }
    //停止取資料和上傳
    private void onstopTeleportService() {
        this.stopService(mTeleportServiceIntent);
    }
    private void onStartTeleportService() {
        mTeleportServiceIntent=new Intent(this, TeleportService.class);
        Bundle serviceBundle = new Bundle();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("datatest");
        mTeleportServiceIntent.putExtras(serviceBundle);
        this.startService(mTeleportServiceIntent);
    }

    private void onStartNonInspectionService(int position) {
        //非巡檢時用 用來
        NonInspectionServiceIntent = new Intent(this, NonInspectionService.class);
        Bundle serviceBundle = new Bundle();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("NonInspection");
        serviceBundle.putSerializable("chooseDeviceData", deviceDataList);
        serviceBundle.putInt("position", position);
        NonInspectionServiceIntent.putExtras(serviceBundle);
        startService(NonInspectionServiceIntent);
    }

    private DialogInterface.OnClickListener onNonInspectionSelectDevice = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.v("CCCCC", "deviceDataPosition:" + deviceDataPosition);
            Log.v("CCCCC", "which:" + which);
            if (deviceDataPosition == 0) {
                onStartNonInspectionService(which);
            } else if (which == deviceDataPosition) {
                onStartNonInspectionService(which);
            } else {
                textDeviceNumber.setText(deviceDataList.get(which).getEQNO());
                stopService(NonInspectionServiceIntent);
            }
        }
    };

    //如果能改成用retrofit加rxjava最好，已經嘗試過三天的，可能有缺什麼，不過緊急所以先求功能
    private void onUploadFile(final ArrayList<String> uriList, String type) {
        showProgressDialog(type);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                MultipartBody.Builder buildernew = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("AuthorizedId", "1179cf63-9f4c-4060-a0f3-201f108b20c1")
                        .addFormDataPart("CO", "1")
                        .addFormDataPart("CONM", "台塑")
                        .addFormDataPart("PMFCT", "A3")
                        .addFormDataPart("PMFCTNM", "麥寮AN廠")
                        .addFormDataPart("EQKD", "PU")
                        .addFormDataPart("EQKDNM", "泵浦")
                        .addFormDataPart("EQNO", "P-166")
                        .addFormDataPart("EQNM", "工業用水泵浦")
                        .addFormDataPart("RecordDate", "2020/04/06")
                        .addFormDataPart("RecordSubject", "測試")
                        .addFormDataPart("UploadEMP", "1")
                        .addFormDataPart("UploadNM", "新人")
                        .addFormDataPart("UploadDATETM", "");
                for (String path : uriList) {
                    File uploadFile = new File(path);
                    buildernew.addFormDataPart("", uploadFile.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    uploadFile));
                }
                RequestBody body = buildernew.build();

                Request request = new Request.Builder()
                        .url("https://cloud.fpcetg.com.tw/FPC/API/MTN/API_MTN/MTN/Upload")
                        .method("POST", body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.e("response", response.body().string());
                    dismissProgressDialog();
//                        Log.e("isSuccess",json.get("IsSuccess").toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    dismissProgressDialog();
                    showDialogCaveatMessage("上傳失敗");
                    Log.e("error", "" + e.getMessage());
                }
            }
        }).start();
    }

    private void getCurrentDataList() {
        String CONTENT_STRING = "content://tw.com.efpg.processe_equip.provider.ShareCloud/ShareCloud";
        Uri uri = Uri.parse(CONTENT_STRING);
        Cursor cursor = this.getContentResolver().query(
                uri,
                null,
                "CurrentJob",
                null, null
        );
        ArrayList<ChooseDeviceItemData> tempDataList =new ArrayList<ChooseDeviceItemData>();
        while (cursor.moveToNext()) {
            String WAYID = cursor.getString(cursor.getColumnIndexOrThrow("WAYID"));
            String EQNO = cursor.getString(cursor.getColumnIndexOrThrow("EQNO"));
            ChooseDeviceItemData mChooseDeviceItemData = new ChooseDeviceItemData();
            mChooseDeviceItemData.setOPCO(cursor.getString(cursor.getColumnIndexOrThrow("OPCO")));
            mChooseDeviceItemData.setOPPLD(cursor.getString(cursor.getColumnIndexOrThrow("OPPLD")));
            mChooseDeviceItemData.setPMFCT(cursor.getString(cursor.getColumnIndexOrThrow("PMFCT")));
            //MNTFCT=OPPLD
            mChooseDeviceItemData.setMNTFCT(cursor.getString(cursor.getColumnIndexOrThrow("OPPLD")));
            mChooseDeviceItemData.setWAYID(WAYID);
            mChooseDeviceItemData.setWAYNM(cursor.getString(cursor.getColumnIndexOrThrow("WAYNM")));
            mChooseDeviceItemData.setEQNO(EQNO);
            mChooseDeviceItemData.setEQNM(cursor.getString(cursor.getColumnIndexOrThrow("EQNM")));
            mChooseDeviceItemData.setEQKD(cursor.getString(cursor.getColumnIndexOrThrow("EQKD")));
            mChooseDeviceItemData.setProgress(cursor.getInt(cursor.getColumnIndexOrThrow("Progress")));
            mChooseDeviceItemData.setCO(cursor.getString(cursor.getColumnIndexOrThrow("CO")));
            mChooseDeviceItemData.setCONM(cursor.getString(cursor.getColumnIndexOrThrow("CONM")));
            mChooseDeviceItemData.setPMFCTNM(cursor.getString(cursor.getColumnIndexOrThrow("PMFCTNM")));
            mChooseDeviceItemData.setUploadNM("王小明");
            mChooseDeviceItemData.setUploadEMP(account);
            mChooseDeviceItemData.setChcekDataFromAPP(true);
            tempDataList.add(mChooseDeviceItemData);

            textRouteCodeData.setText(WAYID);
            textDeviceNumber.setText(EQNO);
        }
        deviceDataList=tempDataList;
        textRouteCodeData.setText(deviceDataList.get(currentDataCount).getWAYID());
        textDeviceNumber.setText(deviceDataList.get(currentDataCount).getEQNO());
    }

    @Override
    public void onCompletebAddCurrentDevice() {
        currentDataCount++;
        deviceDataPosition=currentDataCount;
        if (currentDataCount>deviceDataList.size()-1){
            //這裡做end動作
            onSendTeleportService("end");
        }
    }
}
