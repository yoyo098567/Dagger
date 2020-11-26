package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
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
import android.media.MediaScannerConnection;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capturevideoandpictureandsaveandchoose.Application;
import com.example.capturevideoandpictureandsaveandchoose.GenerateLog;
import com.example.capturevideoandpictureandsaveandchoose.MagicFileChooser;
import com.example.capturevideoandpictureandsaveandchoose.R;
import com.example.capturevideoandpictureandsaveandchoose.base.BaseActivity;
import com.example.capturevideoandpictureandsaveandchoose.databinding.ActivityMainBinding;
import com.example.capturevideoandpictureandsaveandchoose.di.component.main.DaggerMainComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.component.main.MainComponent;
import com.example.capturevideoandpictureandsaveandchoose.di.module.main.MainModule;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceActivity;
import com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice.ChooseDeviceItemData;
import com.example.capturevideoandpictureandsaveandchoose.ui.deviceinformation.DeviceInformationActivity;
import com.example.capturevideoandpictureandsaveandchoose.utils.service.TeleportService;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferences;
import com.example.capturevideoandpictureandsaveandchoose.utils.sharepreferences.LoginPreferencesProvider;
import com.guoxiaoxing.phoenix.compress.video.VideoCompressor;
import com.guoxiaoxing.phoenix.compress.video.format.MediaFormatStrategyPresets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.example.capturevideoandpictureandsaveandchoose.ui.login.LoginActivity.loginStatus;

public class MainActivity extends BaseActivity implements MainContract.View {
    @Inject
    MainContract.Presenter<MainContract.View> mPresenter;
    @Inject
    LoginPreferencesProvider loginPreferencesProvider;
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
    private int count=0;
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
    private String  recordSubjectValue="";
    private int allPhoto=0,nowPhoto=0;
    private Boolean haveNow=false;
    private ChooseDeviceItemData intoData;
    private  ProgressDialog pd;
    private SimpleDateFormat dateFormat;
    private ActivityMainBinding activityMainBinding;
    private GenerateLog generateLogger=new GenerateLog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        init();
        mPresenter.onAttached(this);
        if (loginPreferencesProvider.getPersonId()==1){
            mPresenter.onGetDisposableToken(sn);
        }else if (loginPreferencesProvider.getPersonId()==0){
            mPresenter.onCNGetDisposableToken(sn);
        }
        filePartition = new FilePartition();
    }

    @Override
    public void init() {

        mMainComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .baseComponent(((Application) getApplication()).getApplicationComponent())
                .build();
        mMainComponent.inject(this);

        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        activityMainBinding.setView(this);

        deviceDataList = new ArrayList<>();
        mIntentFilter = new IntentFilter();
        Intent intent = this.getIntent();
        AccessToken = intent.getStringExtra("AccessToken");
        account = intent.getStringExtra("account");
        Log.d("123ssssssssss", "init: "+account);
        currentDataCount = 0;
        deviceonLeaveTheRoute = 0;
        Log.v("LoginStatus", "" + loginStatus);
        if (loginStatus == 1) {
            activityMainBinding.btnDeviceEdit.setEnabled(false);

            autoLogin();
            generateLogger.generateLogTxt("onCreate 打開Service : "+dateFormat.format(Calendar.getInstance().getTime())+"\n");
            onStartTeleportService();
        } else {
            NonHandler = new Handler();
            activityMainBinding.btnFetchDevice.setEnabled(false);
        }

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
                activityMainBinding.textDeviceNumberData.setText(deviceDataList.get(currentDataCount).getEQNO() + " " + deviceDataList.get(currentDataCount).getEQNM());
            }
        }
    };

    //訊息擷取
    private void fetchDevice() {
        generateLogger.generateLogTxt("設備擷取 停止Service"+"\n");
        onstopTeleportService();
        deviceonLeaveTheRoute=0;
        autoLogin();

        if (fetchDeviceMsg.equals("")) {
            showDialogMessage("無資料");
        } else {
            showDialogMessage(fetchDeviceMsg);
        }
        generateLogger.generateLogTxt("設備擷取 打開Service"+"\n");
        onStartTeleportService();
        currentDataCount = 0;
    }

    private void autoLogin() {
        generateLogger.generateLogTxt("autoLogin start "+"\n");
        deviceDataList.clear();
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
            generateLogger.generateLogTxt("showDialogCaveatMessage"+"\n");
        } else {
            while (cursor.moveToNext()) {
                try {
                    String WAYID = cursor.getString(cursor.getColumnIndexOrThrow("WAYID"));
                    generateLogger.generateLogTxt("WAYID"+WAYID+"\n");
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
                    //

                    //
                    mChooseDeviceItemData.setProgress(cursor.getInt(cursor.getColumnIndexOrThrow("Progress")));
                    mChooseDeviceItemData.setCO(cursor.getString(cursor.getColumnIndexOrThrow("CO")));
                    mChooseDeviceItemData.setCONM(cursor.getString(cursor.getColumnIndexOrThrow("CONM")));
                    mChooseDeviceItemData.setPMFCTNM(cursor.getString(cursor.getColumnIndexOrThrow("PMFCTNM")));
                    mChooseDeviceItemData.setUploadNM("");
                    mChooseDeviceItemData.setUploadEMP(account);
                    mChooseDeviceItemData.setChcekDataFromAPP(true);
                    deviceDataList.add(mChooseDeviceItemData);
                    generateLogger.generateLogTxt("deviceDataList : "+mChooseDeviceItemData.getCO()+"\n");
                    if (mChooseDeviceItemData.getProgress() == 100) {
                        deviceonLeaveTheRoute++;
                    }
                    if (loginStatus == 1) {
                        activityMainBinding.textRouteCodeData.setText(WAYID);
                        activityMainBinding.textDeviceNumberData.setText(deviceDataList.get(0).getEQNO() + " " + deviceDataList.get(0).getEQNM());
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    generateLogger.generateLogTxt("IndexOutOfBoundsException exception :"+e);
                }

            }
            Log.e("rrrrrr","deviceonLeaveTheRoute:"+deviceonLeaveTheRoute);
            // Log.e("rrrrrr",deviceDataList.get(6).getEQNO()+" "+deviceDataList.get(6).getEQNM());
            generateLogger.generateLogTxt("deviceDataList.size()"+deviceDataList.size()+"\n");
            try {
                if (deviceDataList.size()>0){
                    if (deviceDataList.get(deviceDataList.size() - 1).getProgress() == 100) {
                        deviceonLeaveTheRoute = deviceDataList.size() - 1;
                    }
                }
            } catch(IndexOutOfBoundsException e) {
                generateLogger.generateLogTxt("deviceDataList IndexOutOfBoundsException exception :"+e+deviceDataList.size());
            }
            catch (Exception e){
                generateLogger.generateLogTxt("deviceDataList Exception exception :"+e+deviceDataList.size());
            }


            try {
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
            }catch (Exception e){
                generateLogger.generateLogTxt("autologin IndexOutOfBoundsException exception :"+e);
            }

        }
        if (loginStatus == 0) {
            deviceDataList = new ArrayList<>();
        }
    }

    private void pickImageFromGallery() {
        //Create an Intent with action as ACTION_PICK
        generateLogger.generateLogTxt("pickImageFromGallery"+"\n");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST_CODE);
    }

    private void pickVideoFromGallery() {
        generateLogger.generateLogTxt("pickVideoFromGallery"+"\n");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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

    //拍照按鈕
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

    //錄影按鈕
    private void openRecordVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(takeVideoIntent, 0);
        takeVideoIntent.setPackage(listCam.get(0).activityInfo.packageName);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    //設備資訊
    private void deviceInformation() {
        Intent it = new Intent(this, DeviceInformationActivity.class);
        if (loginStatus == 0) {
            if (deviceDataList.size() != 0 && !activityMainBinding.textDeviceNumberData.getText().equals("")) {
                it.putExtra("device", deviceDataList.get(currentDataCount));
            }
        } else {
            if (deviceDataList.size() != 0 && !activityMainBinding.textDeviceNumberData.getText().equals("")) {
                it.putExtra("device", deviceDataList.get(currentDataCount));
            }
        }
        startActivity(it);
    }

    /*
     * 將照相機旋轉回正確位置
     * */
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

    //創造照片
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
            //儲存照片的檔案
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = rotateBitmapByDegree(myBitmap, getBitmapDegree(imageFilePath));
                try {
                    FileOutputStream out = new FileOutputStream(imgFile);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    Log.d("123", "onActivityResult: "+imageFilePath);

                    //刷新媒體庫
                   updateFileFromDatabase(this,imgFile,imageFilePath, new onCompleteListener() {
                       @Override
                       public void onComplete() {
                           sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+imageFilePath)));
                           Intent intent = new Intent();
                           intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                           intent.setData(Uri.fromFile(imgFile));
                           getApplicationContext().sendBroadcast(intent);
                       }
                   });


//                    Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri uri = Uri.fromFile(imgFile);
//                    it.setData(uri);
//                    this.sendBroadcast(it);
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
            String originName = file.getName();
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss",
                            Locale.getDefault()).format(new Date());
            String changeFileName = deviceDataList.get(currentDataCount).getEQNO() + "_" + timeStamp + "_" + ".mp4";
            File newFile = new File(videoFilePath.replace(originName, changeFileName));
            final File originFile = new File(videoFilePath);
            originFile.renameTo(newFile);
            updateMedia(newFile.getAbsolutePath(), this);
            showProgressDialog(R.string.saving);
            showProgressDialog(R.string.saving);
            showProgressDialog(R.string.saving);
            showProgressDialog(R.string.saving);

            showProgressDialog(R.string.saving);
            showProgressDialog(R.string.saving);
            showProgressDialog(R.string.saving);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        updateMedia(new File(videoFilePath).getAbsolutePath(), MainActivity.this); //把舊檔案更新掉，不然媒體庫會有一個額外且不能用的檔案
                        dismissProgressDialog();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                    }
                }
            }).start();

        }

        //選擇照片按完成
        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            generateLogger.generateLogTxt("選擇照片按完成"+"\n");
            Context context = this;

            AlertDialog dialog= new AlertDialog.Builder(this)
                    .setMessage(getResourceString(R.string.chosse_camera_question))
                    .setPositiveButton(getResourceString(R.string.check), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generateLogger.generateLogTxt("選擇照片按確定"+"\n");
                            SpannableString ss=  new SpannableString(getResourceString(R.string.loading));
                            ss.setSpan(new RelativeSizeSpan(3f), 0, ss.length(), 0);
                            pd = new ProgressDialog(context);
                            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            pd.setMessage(ss);
                            pd.setIndeterminate(true);
                            pd.setCancelable(false);
                            pd.show();
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    Integer now = 0, urlnow = 0;
                                    // Uri selectedImage = data.getClipData().getItemAt(0).getUri();
                                    ArrayList<String> uriList = new ArrayList<String>();
                                    if (data.getClipData() != null) {
                                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                            uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
                                            //                    Log.e("gggg", "" + uriList.get(i));
                                        }
                                    } else if (Build.VERSION.SDK_INT >= 16 && data.getClipData() == null) {
                                        uriList.add(getPath(data.getData()));
                                    }
                                    allPhoto = uriList.size();

                                    nowPhoto = 0;
                                    for (int i = 0; i < uriList.size(); i++) {
//                                        uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
                                        intoData = new ChooseDeviceItemData();
                                        haveNow = false;
                                        String a = uriList.get(i);
                                        generateLogger.generateLogTxt("選擇的照片Uri: " + a+"\n");
                                        Log.d("dialogMessage", "選擇的照片Uri: " + a);
                                        for (int j = 0; j < deviceDataList.size(); j++) {
                                            if (uriList.get(i).split("/")[5].split("_")[0].equals(deviceDataList.get(j).getEQNO())) {

                                                intoData = deviceDataList.get(j);
                                                now = j;
                                                haveNow = true;
                                                urlnow = i;
                                                generateLogger.generateLogTxt("目前的EQNO + urlNow : + haveNow: + Now :" + deviceDataList.get(j).getEQNO() +urlnow.toString()+ haveNow.toString() + now.toString()+"\n");
                                                Log.d("dialogMessage", "目前的EQNO + urlNow : + haveNow: + Now :" + deviceDataList.get(j).getEQNO() +urlnow.toString()+ haveNow.toString() + now.toString());
                                                break;
                                            }
                                        }

                                        if (!haveNow) {
                                            allPhoto--;
                                            setDialogMessage(nowPhoto, false, uriList.get(i).split("/")[5].split("_")[0] + getResourceString(R.string.no_camera_data), "");

                                        } else {

                                            if ("".equals(recordSubjectValue)) {
                                                generateLogger.generateLogTxt("選擇的照片沒主旨: " +"\n");
                                                deviceDataList.get(now).setRecordSubject(getResourceString(R.string.on_no_set_recordSubject));
                                            } else {
                                                generateLogger.generateLogTxt("選擇的照片主旨: " +recordSubjectValue+"\n");
                                                deviceDataList.get(now).setRecordSubject(recordSubjectValue);
                                            }
                                            generateLogger.generateLogTxt("打第一支API前目前在第幾個 " + now + " 主旨" + deviceDataList.get(now).getRecordSubject()+"\n");
                                            Log.d("dialogMessage", "打第一支API前目前在第幾個 " + now + " 主旨" + deviceDataList.get(now).getRecordSubject());
                                            count++;
                                            if (loginPreferencesProvider.getPersonId()==0){
                                                mPresenter.onCNGetEQKDDataNoImg(account, deviceDataList.get(now).getCO(),
                                                        deviceDataList.get(now).getPMFCT(),
                                                        deviceDataList.get(now).getEQKD(),
                                                        data,
                                                        now,
                                                        urlnow,
                                                        //1是圖片
                                                        1
                                                );
                                            }
                                            else if (loginPreferencesProvider.getPersonId()==1){
                                                mPresenter.onGetEQKDDataNoImg(account, deviceDataList.get(now).getCO(),
                                                        deviceDataList.get(now).getPMFCT(),
                                                        deviceDataList.get(now).getEQKD(),
                                                        data,
                                                        now,
                                                        urlnow,
                                                        //1是圖片
                                                        1
                                                );
                                            }

                                            haveNow = false;
                                            now=0;
                                        }


                                    }

                                }
                            },100);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generateLogger.generateLogTxt("選擇的照片案取消: " +"\n");
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            try {
                Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
                mAlert.setAccessible(true);
                Object mAlertController = mAlert.get(dialog);
                Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
                mMessage.setAccessible(true);
                TextView mMessageView = (TextView) mMessage.get(mAlertController);
                mMessageView.setTextSize(40);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(30);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(30);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }


        //照片的uri
        //選擇影片按完成
        if (requestCode == PICK_VIDEO_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            generateLogger.generateLogTxt("選擇影片按完成: " +"\n");
            Context context=this;
            AlertDialog dialog= new AlertDialog.Builder(this)
                    .setMessage(getResourceString(R.string.chosse_video_question))
                    .setPositiveButton(getResourceString(R.string.check), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generateLogger.generateLogTxt("選擇影片按確定: " +"\n");
                            SpannableString ss=  new SpannableString(getResourceString(R.string.loading));
                            ss.setSpan(new RelativeSizeSpan(3f), 0, ss.length(), 0);
                            pd = new ProgressDialog(context);
                            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            pd.setMessage(ss);
                            pd.setIndeterminate(true);
                            pd.setCancelable(false);
                            pd.show();
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    Uri selectedVideo = data.getData();
                                    Integer now = 0,urlnow=0;
                                    ArrayList<String> uriList = new ArrayList<String>();
                                    if (data.getClipData() != null) {
                                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                            uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
                                            //                    Log.e("gggg", "" + uriList.get(i));
                                        }
                                    } else if (Build.VERSION.SDK_INT >= 16 && data.getClipData() == null) {
                                        uriList.add(getPath(data.getData()));
                                    }

                                    ChooseDeviceItemData intoData=new ChooseDeviceItemData();

//                                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
//                                        uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
//                                    }

                                    // TODO here====================
                                    ArrayList<String> compressList = new ArrayList<>();
                                    compressList.addAll(compressVideo(uriList));
                                    countFile = 0;
                                    allPhoto=uriList.size();
                                    nowPhoto=0;
                                    for (int i = 0; i < uriList.size(); i++) {
//                                        uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
//                filePartition.partition(uriList.get(i), 50 * 1024 * 1024);
                                        for (int j=0;j<deviceDataList.size();j++){
                                            Log.d("videodialogMessage", "onActivityResult: "+uriList.get(i)  );

                                            if (uriList.get(i).split("/")[6].split("_")[0].equals(deviceDataList.get(j).getEQNO())){
                                                now=j;haveNow=true;urlnow=i;
                                                intoData=deviceDataList.get(j);
                                                Log.d("videodialogMessage", "EQNO + haveNow: + Now :"+deviceDataList.get(j).getEQNO()+haveNow.toString()+now.toString());
                                                break;
                                            }
                                        }
                                        try {
                                            File file = new File(uriList.get(i));
                                            MediaPlayer mediaPlayer = new MediaPlayer();
                                            mediaPlayer.setDataSource(file.getPath());
                                            mediaPlayer.prepare();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if (!haveNow){
                                            allPhoto--;
                                            setDialogMessage(nowPhoto,false,uriList.get(i).split("/")[6].split("_")[0]+getResourceString(R.string.no_video_data),"");

                                        }else {
                                            if ("".equals(recordSubjectValue)) {
                                                generateLogger.generateLogTxt("選擇影片沒輸入主旨: " +"\n");
                                                deviceDataList.get(now).setRecordSubject(getResourceString(R.string.on_no_set_recordSubject));
                                            } else {
                                                generateLogger.generateLogTxt("選擇影片主旨為: " +recordSubjectValue+"\n");
                                                deviceDataList.get(now).setRecordSubject(recordSubjectValue);
                                            }
                                            Log.d("videodialogMessage", "api: "+now+" 主旨"+deviceDataList.get(now).getRecordSubject());
                                            if (loginPreferencesProvider.getPersonId()==0){
                                                mPresenter.onCNGetEQKDDataNoImg(account, deviceDataList.get(now).getCO(),
                                                        deviceDataList.get(now).getPMFCT(),
                                                        deviceDataList.get(now).getEQKD(),
                                                        data,
                                                        now,
                                                        urlnow,
                                                        //2是影片
                                                        2
                                                );
                                            }
                                            else if (loginPreferencesProvider.getPersonId()==1){
                                                mPresenter.onGetEQKDDataNoImg(account, deviceDataList.get(now).getCO(),
                                                        deviceDataList.get(now).getPMFCT(),
                                                        deviceDataList.get(now).getEQKD(),
                                                        data,
                                                        now,
                                                        urlnow,
                                                        //2是影片
                                                        2
                                                );
                                            }

                                            haveNow=false;
                                        }
                                    }
                                }
                            },100);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generateLogger.generateLogTxt("選擇影片按cancel: " +"\n");
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            try {
                Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
                mAlert.setAccessible(true);
                Object mAlertController = mAlert.get(dialog);
                Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
                mMessage.setAccessible(true);
                TextView mMessageView = (TextView) mMessage.get(mAlertController);
                mMessageView.setTextSize(40);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(30);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(30);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        //先註解測試影片壓縮功能
        //    onUploadFile(compressList, getResourceString(R.string.on_upload_vedio));
        //影片的uri


        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData();
            String filePath = MagicFileChooser.getAbsolutePathFromUri(this, selectedFile);
            checkFileTypeAndOpen(filePath, selectedFile);

        }

        if (requestCode == GET_DEVICE_DATA && resultCode == RESULT_OK) {
            deviceDataList = (ArrayList<ChooseDeviceItemData>) data.getSerializableExtra("NonInspectionWorkDevice");
        }

    }



    //打EQKDAPI回傳值
    @Override
    public void onSetEQKDdataNoTalk(String EQKDM,Intent data,Integer nowInList,Integer urlNow,Integer pickWhat) {
        generateLogger.generateLogTxt("打EQKDAPI回傳值: " +"打完API回傳在第幾個: "+nowInList + "打完API回傳的EQNO" +deviceDataList.get(nowInList).getEQNO()+"\n");
        Log.d("dialogMessage", "打完API回傳在第幾個: "+nowInList + "打完API回傳的EQNO" +deviceDataList.get(nowInList).getEQNO());
        Log.d("videodialogMessage", "onSetEQKDdataNoTalk: "+nowInList + "data" +deviceDataList.get(nowInList).getEQNO());
        ArrayList<String> uuList = new ArrayList<String>();
        if (data.getClipData() != null) {
            if (data.getClipData().getItemCount()>0) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    uuList.add(getPath(data.getClipData().getItemAt(i).getUri()));
                }
            }
        } else if (Build.VERSION.SDK_INT >= 16 && data.getClipData() == null) {
            uuList.add(getPath(data.getData()));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (!(EQKDM.length()>0)){
            allPhoto--;
            generateLogger.generateLogTxt("EQNO :"+uuList.get(urlNow).split("/")[5].split("_")[0]+getResourceString(R.string.device_nodata)+dateFormat.format(Calendar.getInstance().getTime())+"\n");
            setDialogMessage(nowPhoto, false, uuList.get(urlNow).split("/")[5].split("_")[0] + getResourceString(R.string.device_no), "");
        }else if (EQKDM.equals("Internnet")){
            allPhoto--;
            generateLogger.generateLogTxt("EQNO :"+uuList.get(urlNow).split("/")[5].split("_")[0]+getResourceString(R.string.search_not_ok)+dateFormat.format(Calendar.getInstance().getTime())+"\n");
            setDialogMessage(nowPhoto, false, uuList.get(urlNow).split("/")[5].split("_")[0] + getResourceString(R.string.search_not_ok_nt), "");
        } else {
            generateLogger.generateLogTxt("EQNO成功             deviceDataList.get(nowInList).setEQKDNM(EQKDM) "+"\n");
            deviceDataList.get(nowInList).setEQKDNM(EQKDM);
            intoData=deviceDataList.get(nowInList);
            ArrayList<String> uriList = new ArrayList<String>();
            if (data.getClipData() != null) {
                if (data.getClipData().getItemCount()>0){
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        uriList.add(getPath(data.getClipData().getItemAt(i).getUri()));
//                    Log.e("gggg", "" + uriList.get(i));
                    }
                }else{
                    allPhoto--;
                    generateLogger.generateLogTxt("data.getClipData().getItemCount()<0 無資料 :"+"\n");
                    setDialogMessage(nowPhoto, false, getResourceString(R.string.g_nodata),"");
                }

            } else if (Build.VERSION.SDK_INT >= 16 && data.getClipData() == null) {
                uriList.add(getPath(data.getData()));
            }

            // TODO here====================
            ArrayList<String> compressList = new ArrayList<>();
            compressList.addAll(compressVideo(uriList));
            Log.d("dialogMessage", "傳入第二支API的EQNO: "+intoData.getEQNO()+" urlnow:"+urlNow + " 照片url"+uriList.get(urlNow));
            Log.d("videodialogMessage", "onActivityResult: "+intoData.getEQNO()+" urlnow:"+urlNow+ " 照片url"+uriList.get(urlNow));
            if (pickWhat == 1){
                generateLogger.generateLogTxt("照片上傳中 "+uriList.get(urlNow)+"\n");
                onUploadFile(uriList.get(urlNow), getResourceString(R.string.on_upload_image),intoData);
            }else if(pickWhat ==2){
                generateLogger.generateLogTxt("影片上傳中 "+compressList.get(urlNow)+"\n");
                onUploadFile(compressList.get(urlNow), getResourceString(R.string.on_upload_vedio),intoData);
            }


        }

        //還沒寫 要防呆
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

    //壓縮影片
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
        builder.setMessage(getResourceString(R.string.choose_device));
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void OnFetchDeviceClick() {
        fetchDevice();
    }

    @Override
    public void OnChooseDeviceClick() {
        ArrayList<String> dialogDeviceIDString = new ArrayList<>();
        for (ChooseDeviceItemData deviceData : deviceDataList) {
            dialogDeviceIDString.add(deviceData.getEQNO() + "  " + deviceData.getEQNM());
        }
        showItemDialog(dialogDeviceIDString, onNonInspectionSelectDevice);

//                ArrayList<String> dialogDeviceIDString = new ArrayList<>();
//
//                sortList.clear();
//
//                for (ChooseDeviceItemData deviceData : deviceDataList) {
//                  //  dialogDeviceIDString.add(deviceData.getEQNO() + "  " + deviceData.getEQNM());
//                    sortList.add(deviceData);
//                }
//                //設備以EQNO排序
//                sortList.sort(
//                        Comparator.<ChooseDeviceItemData, String>comparing(p -> p.getEQNO())
//                );
////                Collections.sort(sortList);.thenComparing(p -> p.firstName)
////                                .thenComparing(p -> p.zipCode)
//                for (ChooseDeviceItemData data : sortList){
//                    Log.d("sortList", "sort: "+data.getEQNO());
//                    dialogDeviceIDString.add(data.getEQNO() + "  " + data.getEQNM());
//                }
//
//                showItemDialog(dialogDeviceIDString, onNonInspectionSelectDevice);
//                break;
    }

    @Override
    public void OnDeviceEditClick() {
        Intent intent = new Intent(this, ChooseDeviceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("deviceDataList", deviceDataList);
        bundle.putString("account", account);
        intent.putExtras(bundle);
        startActivityForResult(intent, GET_DEVICE_DATA);

    }

    @Override
    public void OnCentralCloudClick() {
        if (activityMainBinding.textDeviceNumberData.getText().equals("")) {
            showDialogMessage(getResourceString(R.string.no_device));
        } else {

            Intent intentCentralCloud = new Intent();
            intentCentralCloud.setAction(Intent.ACTION_VIEW);
            if (deviceDataList.get(currentDataCount).isChcekDataFromAPP()) {
                deviceDataList.get(currentDataCount).setMNTCO("1");
            }
            if (loginPreferencesProvider.getPersonId()==0){
                intentCentralCloud.setData(Uri.parse("https://cloud.fpcetg.com.tw/FPC/WEB/MTN/MTN_EQPT/LB_Default.aspx?" +
                        "CO=" + deviceDataList.get(currentDataCount).getCO() + "&" +
                        "PMFCT=" + deviceDataList.get(currentDataCount).getPMFCT() + "&" +
                        "MNTCO=" + deviceDataList.get(currentDataCount).getMNTCO() + "&" +
                        "MNTFCT=" + deviceDataList.get(currentDataCount).getMNTFCT() + "&" +
                        "EQNO=" + deviceDataList.get(currentDataCount).getEQNO() + "&" +
                        "token=" + mPresenter.getDisposableToken()));

                Log.d("1233qqqqqqqqqqqqqqqqqq", "OnCentralCloudClick: "+"https://cloud.fpcetg.com.tw/FPC/WEB/MTN/MTN_EQPT/LB_Default.aspx?" +
                        "CO=" + deviceDataList.get(currentDataCount).getCO() + "&" +
                        "PMFCT=" + deviceDataList.get(currentDataCount).getPMFCT() + "&" +
                        "MNTCO=" + deviceDataList.get(currentDataCount).getMNTCO() + "&" +
                        "MNTFCT=" + deviceDataList.get(currentDataCount).getMNTFCT() + "&" +
                        "EQNO=" + deviceDataList.get(currentDataCount).getEQNO() + "&" +
                        "token=" + mPresenter.getDisposableToken());
            }
            else if (loginPreferencesProvider.getPersonId()==1){
                intentCentralCloud.setData(Uri.parse("https://cloud.fpcetg.com.tw/FPC/WEB/MTN/MTN_EQPT/Default.aspx?" +
                        "CO=" + deviceDataList.get(currentDataCount).getCO() + "&" +
                        "PMFCT=" + deviceDataList.get(currentDataCount).getPMFCT() + "&" +
                        "MNTCO=" + deviceDataList.get(currentDataCount).getMNTCO() + "&" +
                        "MNTFCT=" + deviceDataList.get(currentDataCount).getMNTFCT() + "&" +
                        "EQNO=" + deviceDataList.get(currentDataCount).getEQNO() + "&" +
                        "token=" + mPresenter.getDisposableToken()));

                Log.d("1233qqqqqqqqqqqqqqqqqq", "OnCentralCloudClick: "+"https://cloud.fpcetg.com.tw/FPC/WEB/MTN/MTN_EQPT/Default.aspx?" +
                        "CO=" + deviceDataList.get(currentDataCount).getCO() + "&" +
                        "PMFCT=" + deviceDataList.get(currentDataCount).getPMFCT() + "&" +
                        "MNTCO=" + deviceDataList.get(currentDataCount).getMNTCO() + "&" +
                        "MNTFCT=" + deviceDataList.get(currentDataCount).getMNTFCT() + "&" +
                        "EQNO=" + deviceDataList.get(currentDataCount).getEQNO() + "&" +
                        "token=" + mPresenter.getDisposableToken());
            }

            startActivity(intentCentralCloud);
        }
    }

    @Override
    public void OnBasicInformationClick() {
        if (activityMainBinding.textDeviceNumberData.getText().equals("")) {
            showDialogMessage(getResourceString(R.string.no_device));
        } else {
            deviceInformation();
        }
    }

    @Override
    public void OnCaptureImageClick() {
        onNonService();
        if (deviceDataList.size()!=0){
            openCameraIntent();
        }else{
            showItemDialog();
        }
    }

    @Override
    public void OnRecordVideoClick() {
        onNonService();
        if (deviceDataList.size()!=0){
            openRecordVideoIntent();
        }else{
            showItemDialog();
        }
    }

    @Override
    public void OnGetImageFromGalleryClick() {
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
    }

    @Override
    public void OnGetVideoFromGalleryClick() {
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
    }


    //輸入主旨 主旨Dialog
    private void onShowRecordSubjectDialog(final int ispickImage) {

        // final EditText edittext = new EditText(this);
        generateLogger.generateLogTxt("輸入主旨 主旨Dialog "+"\n");
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.record_subject_dialog,null);
        final EditText edittext=view.findViewById(R.id.edittext);
        Button positiveButton=view.findViewById(R.id.positiveButton);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("主旨輸入");
        alert.setView(view);
        recordSubjectValue="";
        final AlertDialog ad=alert.show();
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                ad.dismiss();
                recordSubjectValue = edittext.getText().toString();
                if (deviceDataList.size() > 0) {
                    if (ispickImage == 1) {
                        final Observer<String> observer = new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(String value) {

                                Log.v("123",""+value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                generateLogger.generateLogTxt("輸入主旨 主旨Dialog error"+e+"\n");
                            }

                            @Override
                            public void onComplete() {
                                generateLogger.generateLogTxt("輸入主旨 主旨Dialog complete"+"\n");
                                pickImageFromGallery();
                            }

                        };

                        Observable.create(new ObservableOnSubscribe<String>(){
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                           updateFileFromDatabase(getApplicationContext(),getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"", new onCompleteListener(){
                               @Override
                               public void onComplete() {
                                   e.onComplete();
                               }
                           });
                           }
                        }).subscribe(observer);//订阅


                    } else if (ispickImage == 2) {
                        pickVideoFromGallery();
                    } else {
                        generateLogger.generateLogTxt("輸入主旨 沒有設備類別名稱 "+"\n");
                        showDialogCaveatMessage("沒有設備類別名稱，無法使用上傳功能");
                    }
//                    }
                } else {
                    generateLogger.generateLogTxt("輸入主旨 沒有設備 "+"\n");
                    showDialogMessage(getResourceString(R.string.no_device));
                }
            }
        });
    }

    //停止取資料和上傳
    private void onstopTeleportService() {
        this.stopService(mTeleportServiceIntent);
    }

    //打開service
    private void onStartTeleportService() {
        generateLogger.generateLogTxt("打開Service Intent"+"\n");
        mTeleportServiceIntent = new Intent(this, TeleportService.class);
        Bundle serviceBundle = new Bundle();
        serviceBundle.putString("account", account);
        serviceBundle.putString("currentDataCount", "" + deviceonLeaveTheRoute);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("datatest");
        mTeleportServiceIntent.putExtras(serviceBundle);
        this.startService(mTeleportServiceIntent);
    }

    //設備選擇 dialog listener
    private DialogInterface.OnClickListener onNonInspectionSelectDevice = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (loginStatus == 1) {   //自動登入
                if (which != deviceonLeaveTheRoute) {
                    activityMainBinding.textDeviceNumberData.setText(deviceDataList.get(which).getEQNO() + " " + deviceDataList.get(which).getEQNM());
                    activityMainBinding.textDeviceNumberData.setTextColor(getResources().getColor(R.color.crimson));
                    onstopTeleportService();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                    Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                    String date = sdf.format(curDate);
                    deviceDataList.get(which).setRecordDate(date);
                    generateLogger.generateLogTxt("Activity 準備打目前 currentDataCount:"+currentDataCount+" + "+deviceDataList.get(currentDataCount).getEQNO()+deviceDataList.get(currentDataCount).getEQNM()+"API，時間為"+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                    if (loginPreferencesProvider.getPersonId()==0) {
                        mPresenter.onCNAddChkInfo(deviceDataList.get(which));
                    }
                    else if (loginPreferencesProvider.getPersonId()==1){
                        mPresenter.onAddChkInfo(deviceDataList.get(which));
                    }

                    generateLogger.generateLogTxt("設備選擇 停止Service"+"\n");

                } else {
                    generateLogger.generateLogTxt("設備選擇打開 Service"+"\n");
                    onStartTeleportService();
                    activityMainBinding.textDeviceNumberData.setText(deviceDataList.get(which).getEQNO() + " " + deviceDataList.get(which).getEQNM());
                    activityMainBinding.textDeviceNumberData.setTextColor(getResources().getColor(R.color.black));
                }
                currentDataCount = which;
                Log.v("CCCCC", "currentDataCount:" + currentDataCount);
                Log.v("CCCCC", "which:" + which);
            } else {      //非自動登入
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                String date = sdf.format(curDate);
                NonServiceStatus = true;
                activityMainBinding.textDeviceNumberData.setText(deviceDataList.get(which).getEQNO() + " " + deviceDataList.get(which).getEQNM());
                deviceDataList.get(which).setWAYNM("非巡檢路線");
                deviceDataList.get(which).setWAYID("Non-Ins");
                deviceDataList.get(which).setRecordDate(date);
                //注意
                if (loginPreferencesProvider.getPersonId()==0){
                    mPresenter.onCNAddChkInfo(deviceDataList.get(which));
                }else if (loginPreferencesProvider.getPersonId()==1){
                    mPresenter.onAddChkInfo(deviceDataList.get(which));
                }

                currentDataCount = which;
                onNonService();
            }
        }
    };


    interface onCompleteListener {
        void onComplete();
    }

    //刷新媒體庫
    public  void updateFileFromDatabase(Context context, File file,String imageFilePath, onCompleteListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
            if (file!=null){
                MediaScannerConnection.scanFile(context, paths, null, null);
                MediaScannerConnection.scanFile(context, new String[]{
                                file.getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d("uppppppppp", "onScanCompleted: "+file.getName());
                                generateLogger.generateLogTxt("刷新媒體庫"+file.getName());
                                listener.onComplete();
                            }
                        });
            }else {
                if (imageFilePath.length()>0){
                    MediaScannerConnection.scanFile(context, new String[]{imageFilePath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("*******", "Scanned " + path + ":");
                                    Log.i("*******", "-> uri=" + uri);
                                    listener.onComplete();
                                }
                            });
                }

            }

        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            listener.onComplete();
        }
    }
    //上傳照片 / 圖片
    //如果能改成用retrofit加rxjava最好，已經嘗試過三天的，可能有缺什麼，不過緊急所以先求功能
    private void onUploadFile(final String url, final String type,final ChooseDeviceItemData data) {
        generateLogger.generateLogTxt("上傳照片 / 圖片中 "+"\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
        final String currentTime = curTime.format(curDate);
        final String currentDate = formatter.format(curDate);
        if (deviceDataList.size() < 1) {
            generateLogger.generateLogTxt("無設備資料無法上傳檔案 "+"\n");
            dismissProgressDialog();
            showDialogCaveatMessage(getResourceString(R.string.upload_device_data_is_null));
        } else {
            showProgressDialog(type);
            if (loginPreferencesProvider.getPersonId()==1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                    if (nowPhoto>=allPhoto){
//                        nowPhoto=0;
//                    }
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .writeTimeout(30, TimeUnit.SECONDS)
                                .build();
                        generateLogger.generateLogTxt("上傳照片 / 圖片中 EQNO"+data.getEQNO()+"\n");
                        Log.d("第二支API內的EQNO", "postData: "+data.getEQNO());
                        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("AuthorizedId", "1179cf63-9f4c-4060-a0f3-201f108b20c1")
                                .addFormDataPart("CO", data.getCO())
                                .addFormDataPart("CONM", data.getCONM())
                                .addFormDataPart("PMFCT", data.getPMFCT())
                                .addFormDataPart("PMFCTNM", data.getPMFCTNM())
                                .addFormDataPart("EQKD", data.getEQKD())
                                .addFormDataPart("EQKDNM", data.getEQKDNM())
                                .addFormDataPart("EQNO", data.getEQNO())
                                .addFormDataPart("EQNM", data.getEQNM())
                                .addFormDataPart("RecordDate", currentDate)
                                .addFormDataPart("RecordSubject", data.getRecordSubject())
                                .addFormDataPart("UploadEMP", account)
                                .addFormDataPart("UploadNM", "")
                                .addFormDataPart("UploadDATETM", currentTime);
                        File uploadFile = new File(url);
                        buildernew.addFormDataPart("", uploadFile.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        uploadFile));
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
                                        // dismissProgressDialog();
                                        //  showDialogMessage("上傳完成");
                                        generateLogger.generateLogTxt(url+"照片影片EQNO : "+data.getEQNO()+"上傳完成，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,true,getResourceString(R.string.upload_success),data.getEQNO());
                                    }
                                });
                            } else if (responseBody.length() <= 23) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //  dismissProgressDialog();

                                        //showDialogMessage("上傳失敗，此檔案無法上傳，請重新錄影並在上傳");
                                        generateLogger.generateLogTxt(url+"照片影片EQNO : "+data.getEQNO()+"上傳失敗，此檔案無法上傳，請重新錄影並在上傳，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+
                                                "ResponseMsg: "+response.message()+"ResponseBody :"+responseBody+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false_plz_upload_again),data.getEQNO());
                                    }
                                });
                            } else {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        generateLogger.generateLogTxt(url+"照片影片EQNO : "+data.getEQNO()+"上傳失敗，請重新上傳，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"ResponseBody: "+responseBody.toString()
                                                +"ResponseMsg: "+response.message()+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false),data.getEQNO());
                                    }
                                });
                            }
                        } catch (final Exception e) {
                            e.printStackTrace();
                            generateLogger.generateLogTxt("上傳照片 / 圖片中error "+e+"\n");
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("connect timed out".equals(e.toString())) {
                                        //showDialogCaveatMessage("上傳失敗，連結超時");
                                        generateLogger.generateLogTxt(url+"照片 / 圖片EQNO : "+data.getEQNO()+"上傳失敗，連結超時，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"Error: "+e+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false_linkfailed),data.getEQNO());
                                    } else if (" closed".equals(e.toString())) {
                                        //showDialogCaveatMessage("上傳失敗，檔案過大");
                                        generateLogger.generateLogTxt(url+"照片 / 圖片EQNO : "+data.getEQNO()+"上傳失敗，，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"Error: "+e+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false_bigdata),data.getEQNO());
                                    } else {
                                        //showDialogCaveatMessage("上傳失敗請確認網路問題");
                                        generateLogger.generateLogTxt(url+"照片 / 圖片EQNO : "+data.getEQNO()+"上傳失敗請確認網路問題，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"Error: "+e+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false_internetfailed),data.getEQNO());
                                    }
                                }
                            });
                        }


                    }
                }).start();
            }
            else if (loginPreferencesProvider.getPersonId()==0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                    if (nowPhoto>=allPhoto){
//                        nowPhoto=0;
//                    }
                        generateLogger.generateLogTxt("上傳照片 / 圖片中 EQNO"+data.getEQNO()+"\n");
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .writeTimeout(30, TimeUnit.SECONDS)
                                .build();
                        Log.d("第二支API內的EQNO", "postData: "+data.getEQNO());
                        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("AuthorizedId", "72d06a9b-e7f2-4eab-b7a0-a43f0f915fc4")
                                .addFormDataPart("CO", data.getCO())
                                .addFormDataPart("CONM", data.getCONM())
                                .addFormDataPart("PMFCT", data.getPMFCT())
                                .addFormDataPart("PMFCTNM", data.getPMFCTNM())
                                .addFormDataPart("EQKD", data.getEQKD())
                                .addFormDataPart("EQKDNM", data.getEQKDNM())
                                .addFormDataPart("EQNO", data.getEQNO())
                                .addFormDataPart("EQNM", data.getEQNM())
                                .addFormDataPart("RecordDate", currentDate)
                                .addFormDataPart("RecordSubject", data.getRecordSubject())
                                .addFormDataPart("UploadEMP", account)
                                .addFormDataPart("UploadNM", "")
                                .addFormDataPart("UploadDATETM", currentTime);
                        File uploadFile = new File(url);
                        buildernew.addFormDataPart("", uploadFile.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        uploadFile));
                        RequestBody body = buildernew.build();
                        Request request = new Request.Builder()
                                .url("https://cloud.fpcetg.com.tw/FPC/API/MTN/API_MTN/LB_MTN/Upload")
                                .method("POST", body)
                                .build();
                        try {
                            final Response response = client.newCall(request).execute();
                            String responseBody = response.body().string();

                            if ("OK".equals(response.message()) && responseBody.length() > 23) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // dismissProgressDialog();
                                        //  showDialogMessage("上傳完成");
                                        generateLogger.generateLogTxt(url+"上傳照片 / 圖片EQNO : "+data.getEQNO()+"上傳完成，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,true,getResourceString(R.string.upload_success),data.getEQNO());
                                    }
                                });
                            } else if (responseBody.length() <= 23) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //  dismissProgressDialog();

                                        //showDialogMessage("上傳失敗，此檔案無法上傳，請重新錄影並在上傳");
                                        generateLogger.generateLogTxt(url+"上傳照片 / 圖片EQNO : "+data.getEQNO()+"上傳失敗，此檔案無法上傳，請重新錄影並在上傳，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+
                                                "ResponseMsg: "+response.message()+"ResponseBody :"+responseBody+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false_plz_upload_again),data.getEQNO());
                                    }
                                });
                            } else {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        generateLogger.generateLogTxt(url+"上傳照片 / 圖片EQNO : "+data.getEQNO()+"上傳失敗，請重新上傳，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"ResponseBody: "+responseBody.toString()
                                                +"ResponseMsg: "+response.message()+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false),data.getEQNO());
                                    }
                                });
                            }
                        } catch (final Exception e) {
                            e.printStackTrace();
                            generateLogger.generateLogTxt("上傳照片 / 圖片中 error"+e+"\n");
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("connect timed out".equals(e.toString())) {
                                        //showDialogCaveatMessage("上傳失敗，連結超時");
                                        generateLogger.generateLogTxt(url+"上傳照片 / 圖片EQNO : "+data.getEQNO()+"上傳失敗，連結超時，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"Error: "+e+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false_linkfailed),data.getEQNO());
                                    } else if (" closed".equals(e.toString())) {
                                        //showDialogCaveatMessage("上傳失敗，檔案過大");
                                        generateLogger.generateLogTxt(url+"上傳照片 / 圖片EQNO : "+data.getEQNO()+"上傳失敗，，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"Error: "+e+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false_bigdata),data.getEQNO());
                                    } else {
                                        //showDialogCaveatMessage("上傳失敗請確認網路問題");
                                        generateLogger.generateLogTxt(url+"上傳照片 / 圖片EQNO : "+data.getEQNO()+"上傳失敗請確認網路問題，時間為 : "+dateFormat.format(Calendar.getInstance().getTime())+"Error: "+e+"\n");
                                        nowPhoto++;
                                        setDialogMessage(nowPhoto,false,getResourceString(R.string.upload_false_internetfailed),data.getEQNO());
                                    }
                                }
                            });
                        }


                    }
                }).start();
            }

        }
    }

    private String success_message="";
    private String false_message="";
    private String dialog="";
    private int success=0;

    //設定上傳後的message
    private void setDialogMessage(int nowPhoto,boolean successOrfalse,String successOrFalseMessage,String EQNO){
        Log.d("dialogMessage", " update: "+nowPhoto+" allPhotoSize:"+allPhoto);
        generateLogger.generateLogTxt("設定上傳後的message successOrfalse"+successOrfalse+"successOrFalseMessage "+successOrFalseMessage+"EQNO"+ EQNO+"\n");
        if (successOrfalse){
            success++;
        }else {
            false_message+=EQNO+" "+successOrFalseMessage+"\n";
        }
        if (nowPhoto>=allPhoto){
            pd.dismiss();
            dismissProgressDialog();
            dialog="共"+success+getResourceString(R.string.g_upload_ok)+"\n"+false_message;
            showLongErroeDialogMessage(dialog);
            dialog="";success=0;false_message="";allPhoto=0;

        }
    }
//    //如果能改成用retrofit加rxjava最好，已經嘗試過三天的，可能有缺什麼，不過緊急所以先求功能
//    private void onUploadFile(final ArrayList<String> uriList, final String type,ChooseDeviceItemData data) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
//        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
//        Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
//        final String currentTime = curTime.format(curDate);
//        final String currentDate = formatter.format(curDate);
//        if (deviceDataList.size() < 1) {
//            showDialogCaveatMessage(getResourceString(R.string.
//            _device_data_is_null));
//        } else {
//            showProgressDialog(type);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    OkHttpClient client = new OkHttpClient().newBuilder()
//                            .connectTimeout(10, TimeUnit.SECONDS)
//                            .readTimeout(30, TimeUnit.SECONDS)
//                            .writeTimeout(30, TimeUnit.SECONDS)
//                            .build();
//                    MultipartBody.Builder buildernew = new MultipartBody.Builder()
//                            .setType(MultipartBody.FORM)
//                            .addFormDataPart("AuthorizedId", "1179cf63-9f4c-4060-a0f3-201f108b20c1")
//                            .addFormDataPart("CO", deviceDataList.get(currentDataCount).getCO())
//                            .addFormDataPart("CONM", deviceDataList.get(currentDataCount).getCONM())
//                            .addFormDataPart("PMFCT", deviceDataList.get(currentDataCount).getPMFCT())
//                            .addFormDataPart("PMFCTNM", deviceDataList.get(currentDataCount).getPMFCTNM())
//                            .addFormDataPart("EQKD", deviceDataList.get(currentDataCount).getEQKD())
//                            .addFormDataPart("EQKDNM", deviceDataList.get(currentDataCount).getEQKDNM())
//                            .addFormDataPart("EQNO", deviceDataList.get(currentDataCount).getEQNO())
//                            .addFormDataPart("EQNM", deviceDataList.get(currentDataCount).getEQNM())
//                            .addFormDataPart("RecordDate", currentDate)
//                            .addFormDataPart("RecordSubject", deviceDataList.get(currentDataCount).getRecordSubject())
//                            .addFormDataPart("UploadEMP", account)
//                            .addFormDataPart("UploadNM", "")
//                            .addFormDataPart("UploadDATETM", currentTime);
//                    for (String path : uriList) {
//                        File uploadFile = new File(path);
//                        buildernew.addFormDataPart("", uploadFile.getName(),
//                                RequestBody.create(MediaType.parse("application/octet-stream"),
//                                        uploadFile));
//                        Log.d("uploadFile.getName", "run: "+uploadFile.getName());
//                    }
//                    RequestBody body = buildernew.build();
//
//                    Request request = new Request.Builder()
//                            .url("https://cloud.fpcetg.com.tw/FPC/API/MTN/API_MTN/MTN/Upload")
//                            .method("POST", body)
//                            .build();
//                    try {
//                        final Response response = client.newCall(request).execute();
//                        String responseBody = response.body().string();
//                        if ("OK".equals(response.message()) && responseBody.length() > 23) {
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    dismissProgressDialog();
//                                    showDialogMessage("上傳完成");
//                                }
//                            });
//                        } else if (responseBody.length() <= 23) {
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    dismissProgressDialog();
//                                    showDialogMessage("上傳失敗，此檔案無法上傳，請重新錄影並在上傳");
//                                }
//                            });
//                        } else {
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    dismissProgressDialog();
//                                    showDialogMessage("上傳失敗，請重新上傳");
//                                }
//                            });
//                        }
//                    } catch (final Exception e) {
//                        e.printStackTrace();
//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                dismissProgressDialog();
//                                if ("connect timed out".equals(e.toString())) {
//                                    showDialogCaveatMessage("上傳失敗，連結超時");
//                                } else if (" closed".equals(e.toString())) {
//                                    showDialogCaveatMessage("上傳失敗，檔案過大");
//                                } else {
//                                    showDialogCaveatMessage("上傳失敗請確認網路問題");
//                                }
//                            }
//                        });
//                    }
//                }
//            }).start();
//        }
//    }




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

    //EQKDNM
    @Override
    public void onSetEQKDNMData(String mEQKDNM, int ispickImage) {
        generateLogger.generateLogTxt("onSetEQKDNMData  "+mEQKDNM+"ispickImg"+ispickImage+"\n");
        if (ispickImage == 1) {
            deviceDataList.get(currentDataCount).setEQKDNM(mEQKDNM);
            updateFileFromDatabase(this,getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"", new onCompleteListener() {
                @Override
                public void onComplete() {

                }
            });
            pickImageFromGallery();
        } else if (ispickImage == 2) {
            deviceDataList.get(currentDataCount).setEQKDNM(mEQKDNM);
            pickVideoFromGallery();
        } else {
            generateLogger.generateLogTxt("沒有設備類別名稱，無法使用上傳功能"+"\n");
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
            if (loginPreferencesProvider.getPersonId()==0){
                mPresenter.onCNAddEndChkInfo(deviceDataList.get(currentDataCount));
            }
            else if (loginPreferencesProvider.getPersonId()==1){
                mPresenter.onAddEndChkInfo(deviceDataList.get(currentDataCount));
            }

            NonHandler.removeCallbacks(periodicUpdate);
        }
    };

    @Override
    protected void onDestroy() {
        if (loginStatus == 1) {
            generateLogger.generateLogTxt("Activity Destroy 停止Service"+"\n");
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
