package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Environment.getExternalStoragePublicDirectory;
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
    private static final int GET_DEVICE_DATA = 2021;
    private String AccessToken = "";
    private ArrayList<ChooseDeviceItemData> deviceDataList;
    String imageFilePath;
    private Button btnFetchDevice, btnCapturePicture, btnRecordVideo, btnGetImageFromGallery, btnGetVideoFromGallery, btnDeviceEdit, btnCentralCloud, btnChoseDevice, btnBasicInformation;
    private TextView textDeviceNumber, textRouteCodeData;
    private File photoFile;
    private MainComponent mMainComponent;
    final String sn = android.os.Build.SERIAL;
    private int countFile = 0;
    private int currentDataCount = 0;
    private IntentFilter mIntentFilter;
    private String fetchDeviceMsg;
    private String account;
    private Intent mTeleportServiceIntent;
    private int deviceonLeaveTheRoute;
    private Handler NonHandler;
    private boolean NonServiceStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mPresenter.onAttached(this);
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
        mMainComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .baseComponent(((Application) getApplication()).getApplicationComponent())
                .build();
        mMainComponent.inject(this);
        deviceDataList = new ArrayList<>();
        mIntentFilter = new IntentFilter();
        Intent intent = this.getIntent();
        AccessToken = intent.getStringExtra("AccessToken");
        account = intent.getStringExtra("account");
        currentDataCount = 0;
        deviceonLeaveTheRoute = 0;
        Log.v("LoginStatus", "" + loginStatus);
        if (loginStatus == 1) {
            btnDeviceEdit.setEnabled(false);
            autoLogin();
            onStartTeleportService();
        } else {
            NonHandler = new Handler();
            btnFetchDevice.setEnabled(false);
        }
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
            if (loginStatus == 1) {   //自動登入
                currentDataCount = Integer.valueOf(intent.getStringExtra("refresh"));
                if (currentDataCount >= deviceDataList.size()) {
                    currentDataCount = deviceDataList.size() - 1;
                }
                deviceonLeaveTheRoute = currentDataCount;
                textDeviceNumber.setText(deviceDataList.get(currentDataCount).getEQNO() + " " + deviceDataList.get(currentDataCount).getEQNM());
            }
        }
    };

    private void fetchDevice() {
        onstopTeleportService();
        deviceonLeaveTheRoute=0;
        autoLogin();

        if (fetchDeviceMsg.equals("")) {
            showDialogMessage("無資料");
        } else {
            showDialogMessage(fetchDeviceMsg);
        }

        onStartTeleportService();
        currentDataCount = 0;
    }

    private void pickImageFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
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
        if (cursor == null) {
            showDialogCaveatMessage(getResourceString(R.string.get_inspection_data_error_message));
        } else {
            while (cursor.moveToNext()) {
//                String OPCO = cursor.getString(cursor.getColumnIndexOrThrow("OPCO"));
//                String OPPLD = cursor.getString(cursor.getColumnIndexOrThrow("OPPLD"));
//                String PMFCT = cursor.getString(cursor.getColumnIndexOrThrow("PMFCT"));
//                String WAYID = cursor.getString(cursor.getColumnIndexOrThrow("WAYID"));
//                String WAYNM = cursor.getString(cursor.getColumnIndexOrThrow("WAYNM"));
//                String CTLPTID = cursor.getString(cursor.getColumnIndexOrThrow("CTLPTID"));
//                String IT = cursor.getString(cursor.getColumnIndexOrThrow("IT"));
//                String EQNO = cursor.getString(cursor.getColumnIndexOrThrow("EQNO"));
//                String EQNM = cursor.getString(cursor.getColumnIndexOrThrow("EQNM"));
//                String EQKD = cursor.getString(cursor.getColumnIndexOrThrow("EQKD"));
//                int progress = cursor.getInt(cursor.getColumnIndexOrThrow("Progress"));
//                String CO = cursor.getString(cursor.getColumnIndexOrThrow("CO"));
//                String CONM = cursor.getString(cursor.getColumnIndexOrThrow("CONM"));
//                String PMFCTNM = cursor.getString(cursor.getColumnIndexOrThrow("PMFCTNM"));
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
                mChooseDeviceItemData.setUploadNM("");
                mChooseDeviceItemData.setUploadEMP(account);
                mChooseDeviceItemData.setChcekDataFromAPP(true);
                deviceDataList.add(mChooseDeviceItemData);
                if (mChooseDeviceItemData.getProgress() == 100) {
                    deviceonLeaveTheRoute++;
                }
                if (loginStatus == 1) {
                    textRouteCodeData.setText(WAYID);
                    textDeviceNumber.setText(deviceDataList.get(0).getEQNO() + " " + deviceDataList.get(0).getEQNM());
                }
            }
            Log.e("rrrrrr","deviceonLeaveTheRoute:"+deviceonLeaveTheRoute);
            Log.e("rrrrrr",deviceDataList.get(6).getEQNO()+" "+deviceDataList.get(6).getEQNM());

            if (deviceDataList.get(deviceDataList.size() - 1).getProgress() == 100) {
                deviceonLeaveTheRoute = deviceDataList.size() - 1;
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
        if (loginStatus == 0) {
            deviceDataList = new ArrayList<>();
        }
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
                Log.v("ERROR", "" + ex);
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
        if (loginStatus == 0) {
            if (deviceDataList.size() != 0 && !textDeviceNumber.getText().equals("")) {
                it.putExtra("device", deviceDataList.get(currentDataCount));
            }
        } else {
            if (deviceDataList.size() != 0 && !textDeviceNumber.getText().equals("")) {
                it.putExtra("device", deviceDataList.get(currentDataCount));
            }
        }
        startActivity(it);
    }

    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 從指定路徑下讀取圖片，並獲取其EXIF資訊
            ExifInterface exifInterface = new ExifInterface(path);
            // 獲取圖片的旋轉資訊
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根據旋轉角度，生成旋轉矩陣
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 將原始圖片按照旋轉矩陣進行旋轉，並得到新的圖片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    private File createImageFile() throws IOException {
        String imageFileName="";
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        imageFileName = deviceDataList.get(currentDataCount).getEQNO()+"_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        Log.v("77777777", "" + imageFilePath);

        return image;
    }

    private void settingSystemCamera(Intent intent) {
        PackageManager packageManager = MainActivity.this.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(intent, 0);
        intent.setPackage(listCam.get(0).activityInfo.packageName);
    }

    private static void updateMedia(String path , Context context){
        File file = new File(path);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            File imgFile = new File(imageFilePath);
            //照片的檔案
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap=rotateBitmapByDegree(myBitmap,getBitmapDegree(imageFilePath));
                try {
                    FileOutputStream out = new FileOutputStream(imgFile);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(imgFile);
                    it.setData(uri);
                    this.sendBroadcast(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            //影片的uri
            final String videoFilePath;
            Uri videoUri = data.getData();

            videoFilePath = MagicFileChooser.getAbsolutePathFromUri(this, videoUri);
            File file = new File(videoFilePath);
            String originName=file.getName();
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
            String changeFileName=deviceDataList.get(currentDataCount).getEQNO()+"_" + timeStamp + "_"+".mp4";
            File newFile = new File(videoFilePath.replace(originName,changeFileName));
            final File originFile = new File(videoFilePath);
            originFile.renameTo(newFile);
            updateMedia(newFile.getAbsolutePath(),this);
            showProgressDialog("檔案儲存中");
            showProgressDialog("檔案儲存中");
            showProgressDialog("檔案儲存中");
            showProgressDialog("檔案儲存中");

            showProgressDialog("檔案儲存中");
            showProgressDialog("檔案儲存中");
            showProgressDialog("檔案儲存中");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        updateMedia(new File(videoFilePath).getAbsolutePath(),MainActivity.this); //把舊檔案更新掉，不然媒體庫會有一個額外且不能用的檔案
                        dismissProgressDialog();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("error",e.getMessage());
                    }
                }
            }).start();

        }

        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getClipData().getItemAt(0).getUri();
            ArrayList<String> uriList = new ArrayList<String>();
            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
            }
            //照片的uri
            onUploadFile(uriList, getResourceString(R.string.on_upload_image));
        }

        if (requestCode == PICK_VIDEO_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedVideo = data.getData();
            ArrayList<String> uriList = new ArrayList<String>();
            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
//                filePartition.partition(uriList.get(i), 50 * 1024 * 1024);

                try {
                    File file = new File(uriList.get(i));
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(file.getPath());
                    mediaPlayer.prepare();
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
            String filePath = MagicFileChooser.getAbsolutePathFromUri(this, selectedFile);
            checkFileTypeAndOpen(filePath, selectedFile);

        }

        if (requestCode == GET_DEVICE_DATA && resultCode == RESULT_OK) {
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

    public void showItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("請選取設備");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_basic_information:
                if (textDeviceNumber.getText().equals("")) {
                    showDialogMessage("無設備");
                } else {
                    deviceInformation();
                }
                break;
            case R.id.btnFetchDevice:
                fetchDevice();
                break;
            case R.id.btnCaptureImage:
                onNonService();
                if (deviceDataList.size()!=0){
                    openCameraIntent();
                }else{
                    showItemDialog();
                }
                break;
            case R.id.btnRecordVideo:
                onNonService();
                if (deviceDataList.size()!=0){
                    openRecordVideoIntent();
                }else{
                    showItemDialog();
                }
                break;
            case R.id.btnGetImageFromGallery:
                onNonService();
                onShowRecordSubjectDialog(1);
//                if(deviceDataList.size()>0){
//                    if("".equals(deviceDataList.get(currentDataCount).getEQKDNM())){
//                        mPresenter.onGetEQKDData(account,deviceDataList.get(currentDataCount).getCO(),
//                                deviceDataList.get(currentDataCount).getPMFCT(),
//                                deviceDataList.get(currentDataCount).getEQKD(),
//                                1);
//                    }else{
//                        pickImageFromGallery();
//                    }
//                }else{
//                    showDialogMessage("無設備");
//                }
                break;
            case R.id.btnGetVideoFromGallery:
                //目前影片能用到1分02秒
                onNonService();
                onShowRecordSubjectDialog(2);
//                if(deviceDataList.size()>0){
//                    if("".equals(deviceDataList.get(currentDataCount).getEQKDNM())){
//                        mPresenter.onGetEQKDData(account,deviceDataList.get(currentDataCount).getCO(),
//                                deviceDataList.get(currentDataCount).getPMFCT(),
//                                deviceDataList.get(currentDataCount).getEQKD(),
//                                2);
//                    }else{
//                        pickVideoFromGallery();
//                    }
//                }else{
//                    showDialogMessage("無設備");
//                }
                break;
            case R.id.btn_central_cloud:
                if (textDeviceNumber.getText().equals("")) {
                    showDialogMessage("無設備");
                } else {
                    Intent intentCentralCloud = new Intent();
                    intentCentralCloud.setAction(Intent.ACTION_VIEW);
                    if (deviceDataList.get(currentDataCount).isChcekDataFromAPP()) {
                        deviceDataList.get(currentDataCount).setMNTCO("1");
                    }
                    intentCentralCloud.setData(Uri.parse("https://cloud.fpcetg.com.tw/FPC/WEB/MTN/MTN_EQPT/Default.aspx?" +
                            "CO=" + deviceDataList.get(currentDataCount).getCO() + "&" +
                            "PMFCT=" + deviceDataList.get(currentDataCount).getPMFCT() + "&" +
                            "MNTCO=" + deviceDataList.get(currentDataCount).getMNTCO() + "&" +
                            "MNTFCT=" + deviceDataList.get(currentDataCount).getMNTFCT() + "&" +
                            "EQNO=" + deviceDataList.get(currentDataCount).getEQNO() + "&" +
                            "token=" + mPresenter.getDisposableToken()));
                    startActivity(intentCentralCloud);
                }
                break;
            case R.id.btn_device_edit:
                Intent intent = new Intent(this, ChooseDeviceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("deviceDataList", deviceDataList);
                bundle.putString("account", account);
                intent.putExtras(bundle);
                startActivityForResult(intent, GET_DEVICE_DATA);
                break;
            case R.id.btn_chose_device:
                ArrayList<String> dialogDeviceIDString = new ArrayList<>();
                for (ChooseDeviceItemData deviceData : deviceDataList) {
                    dialogDeviceIDString.add(deviceData.getEQNO() + "  " + deviceData.getEQNM());
                }
                showItemDialog(dialogDeviceIDString, onNonInspectionSelectDevice);
                break;
        }
    }

    private void onShowRecordSubjectDialog(final int ispickImage) {
        final EditText edittext = new EditText(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("主旨輸入");
        alert.setView(edittext);

        alert.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String recordSubjectValue = edittext.getText().toString();
                if (deviceDataList.size() > 0) {
                    if ("".equals(recordSubjectValue)) {
                        deviceDataList.get(currentDataCount).setRecordSubject("沒有輸入主旨");
                    } else {
                        deviceDataList.get(currentDataCount).setRecordSubject(recordSubjectValue);
                    }
                    if ("".equals(deviceDataList.get(currentDataCount).getEQKDNM())) {
                        mPresenter.onGetEQKDData(account, deviceDataList.get(currentDataCount).getCO(),
                                deviceDataList.get(currentDataCount).getPMFCT(),
                                deviceDataList.get(currentDataCount).getEQKD(),
                                ispickImage);
                    } else {
                        if (ispickImage == 1) {
                            pickImageFromGallery();
                        } else if (ispickImage == 2) {
                            pickVideoFromGallery();
                        } else {
                            showDialogCaveatMessage("沒有設備類別名稱，無法使用上傳功能");
                        }
                    }
                } else {
                    showDialogMessage("無設備");
                }
            }
        });

        alert.show();
    }

    //停止取資料和上傳
    private void onstopTeleportService() {
        this.stopService(mTeleportServiceIntent);
    }

    private void onStartTeleportService() {
        mTeleportServiceIntent = new Intent(this, TeleportService.class);
        Bundle serviceBundle = new Bundle();
        serviceBundle.putString("account", account);
        serviceBundle.putString("currentDataCount", "" + deviceonLeaveTheRoute);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("datatest");
        mTeleportServiceIntent.putExtras(serviceBundle);
        this.startService(mTeleportServiceIntent);
    }

    private DialogInterface.OnClickListener onNonInspectionSelectDevice = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (loginStatus == 1) {   //自動登入
                if (which != deviceonLeaveTheRoute) {
                    textDeviceNumber.setText(deviceDataList.get(which).getEQNO() + " " + deviceDataList.get(which).getEQNM());
                    textDeviceNumber.setTextColor(getResources().getColor(R.color.crimson));
                    onstopTeleportService();
                } else {
                    onStartTeleportService();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                    Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                    String date = sdf.format(curDate);
                    deviceDataList.get(currentDataCount).setRecordDate(date);
                    mPresenter.onAddChkInfo(deviceDataList.get(currentDataCount));
                    textDeviceNumber.setText(deviceDataList.get(which).getEQNO() + " " + deviceDataList.get(which).getEQNM());
                    textDeviceNumber.setTextColor(getResources().getColor(R.color.black));
                }
                currentDataCount = which;
                Log.v("CCCCC", "currentDataCount:" + currentDataCount);
                Log.v("CCCCC", "which:" + which);
            } else {      //非自動登入
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                String date = sdf.format(curDate);
                NonServiceStatus = true;
                textDeviceNumber.setText(deviceDataList.get(which).getEQNO() + " " + deviceDataList.get(which).getEQNM());
                deviceDataList.get(which).setWAYNM("非巡檢路線");
                deviceDataList.get(which).setWAYID("Non-Ins");
                deviceDataList.get(which).setRecordDate(date);
                mPresenter.onAddChkInfo(deviceDataList.get(which));
                currentDataCount = which;
                onNonService();
            }
        }
    };

    //如果能改成用retrofit加rxjava最好，已經嘗試過三天的，可能有缺什麼，不過緊急所以先求功能
    private void onUploadFile(final ArrayList<String> uriList, final String type) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        final String currentTime = curTime.format(curDate);
        final String currentDate = formatter.format(curDate);
        if (deviceDataList.size() < 1) {
            showDialogCaveatMessage(getResourceString(R.string.upload_device_data_is_null));
        } else {
            showProgressDialog(type);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build();
                    MultipartBody.Builder buildernew = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("AuthorizedId", "1179cf63-9f4c-4060-a0f3-201f108b20c1")
                            .addFormDataPart("CO", deviceDataList.get(currentDataCount).getCO())
                            .addFormDataPart("CONM", deviceDataList.get(currentDataCount).getCONM())
                            .addFormDataPart("PMFCT", deviceDataList.get(currentDataCount).getPMFCT())
                            .addFormDataPart("PMFCTNM", deviceDataList.get(currentDataCount).getPMFCTNM())
                            .addFormDataPart("EQKD", deviceDataList.get(currentDataCount).getEQKD())
                            .addFormDataPart("EQKDNM", deviceDataList.get(currentDataCount).getEQKDNM())
                            .addFormDataPart("EQNO", deviceDataList.get(currentDataCount).getEQNO())
                            .addFormDataPart("EQNM", deviceDataList.get(currentDataCount).getEQNM())
                            .addFormDataPart("RecordDate", currentDate)
                            .addFormDataPart("RecordSubject", deviceDataList.get(currentDataCount).getRecordSubject())
                            .addFormDataPart("UploadEMP", account)
                            .addFormDataPart("UploadNM", "")
                            .addFormDataPart("UploadDATETM", currentTime);
                    for (String path : uriList) {
                        File uploadFile = new File(path);
                        buildernew.addFormDataPart("", uploadFile.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        uploadFile));
                        Log.d("uploadFile.getName", "run: "+uploadFile.getName());
                    }
                    RequestBody body = buildernew.build();

                    Request request = new Request.Builder()
                            .url("https://cloud.fpcetg.com.tw/FPC/API/MTN/API_MTN/MTN/Upload")
                            .method("POST", body)
                            .build();
                    try {
                        final Response response = client.newCall(request).execute();
                        String responseBody = response.body().string();
                        if ("OK".equals(response.message()) && responseBody.length() > 23) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissProgressDialog();
                                    showDialogMessage("上傳完成");
                                }
                            });
                        } else if (responseBody.length() <= 23) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissProgressDialog();
                                    showDialogMessage("上傳失敗，此檔案無法上傳，請重新錄影並在上傳");
                                }
                            });
                        } else {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissProgressDialog();
                                    showDialogMessage("上傳失敗，請重新上傳");
                                }
                            });
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissProgressDialog();
                                if ("connect timed out".equals(e.toString())) {
                                    showDialogCaveatMessage("上傳失敗，連結超時");
                                } else if (" closed".equals(e.toString())) {
                                    showDialogCaveatMessage("上傳失敗，檔案過大");
                                } else {
                                    showDialogCaveatMessage("上傳失敗請確認網路問題");
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }

    @Override
    public void onNonService() {
        if (currentDataCount == deviceDataList.size() - 1) {
            if (loginStatus == 0) {
                NonHandler.removeCallbacks(periodicUpdate);
                if (NonServiceStatus) {
//                    NonHandler.postDelayed(periodicUpdate, 10000);
                NonHandler.postDelayed(periodicUpdate, 600000);
                }
            }
        }
    }

    @Override
    public void onSetEQKDNMData(String mEQKDNM, int ispickImage) {
        if (ispickImage == 1) {
            deviceDataList.get(currentDataCount).setEQKDNM(mEQKDNM);
            pickImageFromGallery();
        } else if (ispickImage == 2) {
            deviceDataList.get(currentDataCount).setEQKDNM(mEQKDNM);
            pickVideoFromGallery();
        } else {
            showDialogCaveatMessage("沒有設備類別名稱，無法使用上傳功能");
        }
    }

    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
            String date = sdf.format(curDate);
            deviceDataList.get(currentDataCount).setRecordDate(date);
            mPresenter.onAddEndChkInfo(deviceDataList.get(currentDataCount));
            NonHandler.removeCallbacks(periodicUpdate);
        }
    };

    @Override
    protected void onDestroy() {
        if (loginStatus == 1) {
            onstopTeleportService();
        } else {
            if (NonServiceStatus) {
            }
        }
        dismissProgressDialog();
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }
}
