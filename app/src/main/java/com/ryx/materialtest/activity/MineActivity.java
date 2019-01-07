package com.ryx.materialtest.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ryx.materialtest.R;
import com.ryx.materialtest.bean.ChangeUserBean;
import com.ryx.materialtest.dialog.CarmeraDialog;
import com.ryx.materialtest.utils.Constans;
import com.ryx.materialtest.utils.InfoPrefs;
import com.ryx.materialtest.utils.ItemsAlertDialogUtil;
import com.ryx.materialtest.utils.PictureUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ryx on 2019/1/5.
 */
public class MineActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView circleImageView;
    private CarmeraDialog carmeraDialog;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;
    private static final int REQUEST_CHANGE_USER_NICK_NAME = 10;
//    private static final int REQUEST_CHANGE_USER_MAIL = 15;
    private static final String IMAGE_FILE_NAME = "user_head.jpg";


    private RelativeLayout userHead;
    private RelativeLayout userName;
    private RelativeLayout userGender;
//    private RelativeLayout userMail;

    private ChangeUserBean bean;
    private TextView textView_user_nick_name;
    private TextView textView_user_gender;
//    private TextView textView_user_mail;
    private ImageView mineBackImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("我的");
//        setSupportActionBar(toolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        textView_user_nick_name = findViewById(R.id.user_nick_name_TV);
        textView_user_gender = findViewById(R.id.user_gender_TV);
//        textView_user_mail = findViewById(R.id.user_mail_text);
        circleImageView = (CircleImageView) findViewById(R.id.user_head_iv);
        InfoPrefs.init("user_info");
        refresh();
        userHead = (RelativeLayout) findViewById(R.id.user_head);
        userName = (RelativeLayout) findViewById(R.id.user_name);
        userGender = (RelativeLayout) findViewById(R.id.user_gender);
//        userMail = (RelativeLayout) findViewById(R.id.user_mail);
        mineBackImg = (ImageView) findViewById(R.id.mine_back);

        userHead.setOnClickListener(this);
        userName.setOnClickListener(this);
        userGender.setOnClickListener(this);
//        userMail.setOnClickListener(this);
        mineBackImg.setOnClickListener(this);

    }

    public void refresh(){
        String name = InfoPrefs.getData(Constans.UserInfo.NAME);
//        String mail = InfoPrefs.getData(Constans.UserInfo.MAIL);
        textView_user_nick_name.setText(name);
        textView_user_gender.setText(InfoPrefs.getData(Constans.UserInfo.GENDER));
//        textView_user_mail.setText(mail);
        showHeadImage();
        //circleImageView_user_head.setImageURI();
    }
    private void showDialog() {
        PictureUtil.mkdirMyPetRootDirectory();

        carmeraDialog = new CarmeraDialog(MineActivity.this, new CarmeraDialog.CarmeraListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.do_button:

                        if (ContextCompat.checkSelfPermission(MineActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MineActivity.this,Manifest
                                .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MineActivity.this,new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},300);
                        } else {
                            carmeraDialog.dismiss();
                            imageCapture();
                        }
                        break;
                    case R.id.select_img_button:
                        // 文件权限申请
                        if (ContextCompat.checkSelfPermission(MineActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // 权限还没有授予，进行申请
                            ActivityCompat.requestPermissions(MineActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200); // 申请的 requestCode 为 200
                        } else {
                            // 如果权限已经申请过，直接进行图片选择
                            carmeraDialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            // 判断系统中是否有处理该 Intent 的 Activity
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, REQUEST_IMAGE_GET);
                            } else {
                                Toast.makeText(MineActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case R.id.cancle_button:
                        carmeraDialog.dismiss();
                        break;
                }

            }

        });
        carmeraDialog.show();
    }

    public void imageCapture() {
        Intent intent;
        Uri pictureUri;
        //getMyPetRootDirectory()得到的是Environment.getExternalStorageDirectory() + File.separator+"MyPet"
        //也就是我之前创建的存放头像的文件夹（目录）
        File pictureFile = new File(PictureUtil.getMyPetRootDirectory(), IMAGE_FILE_NAME);
        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //这一句非常重要
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //""中的内容是随意的，但最好用package名.provider名的形式，清晰明了
            pictureUri = FileProvider.getUriForFile(this,
                    "com.ryx.materialtest.fileprovider", pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(pictureFile);
        }
        // 去拍照,拍照的结果存到oictureUri对应的路径中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        Log.e(TAG,"before take photo"+pictureUri.toString());
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void setPicToView(Uri uri)  {
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            // 创建 smallIcon 文件夹
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //String storage = Environment.getExternalStorageDirectory().getPath();
                File dirFile = new File(PictureUtil.getMyPetRootDirectory(),  "Icon");
                if (!dirFile.exists()) {
                    if (!dirFile.mkdirs()) {
                        Log.d(TAG, "in setPicToView->文件夹创建失败");
                    } else {
                        Log.d(TAG, "in setPicToView->文件夹创建成功");
                    }
                }
                File file = new File(dirFile, IMAGE_FILE_NAME);
                InfoPrefs.setData(Constans.UserInfo.HEAD_IMAGE,file.getPath());
                //Log.d("result",file.getPath());
                // Log.d("result",file.getAbsolutePath());
                // 保存图片
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 在视图中显示图片
            showHeadImage();
            //circleImageView_user_head.setImageBitmap(InfoPrefs.getData(Constants.UserInfo.GEAD_IMAGE));
        }
    }


    private void showHeadImage() {
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sdcard是否存在
        if (isSdCardExist) {

            String path = InfoPrefs.getData(Constans.UserInfo.HEAD_IMAGE);// 获取图片路径

            File file = new File(path);
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(path);
                // 将图片显示到ImageView中
                circleImageView.setImageBitmap(bm);
            }else{
                Log.e(TAG,"no file");
                circleImageView.setImageResource(R.drawable.th);
            }
        } else {
            Log.e(TAG,"no SD card");
            circleImageView.setImageResource(R.drawable.th);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                // 切割
                case REQUEST_SMALL_IMAGE_CUTTING:
                    Log.e(TAG,"before show");
                    File cropFile=new File(PictureUtil.getMyPetRootDirectory(),"crop.jpg");
                    Uri cropUri = Uri.fromFile(cropFile);
                    setPicToView(cropUri);
                    break;

                // 相册选取
                case REQUEST_IMAGE_GET:
                    Uri uri= PictureUtil.getImageUri(this,data);
                    startPhotoZoom(uri);
                    break;

                // 拍照
                case REQUEST_IMAGE_CAPTURE:
                    File pictureFile = new File(PictureUtil.getMyPetRootDirectory(), IMAGE_FILE_NAME);
                    Uri pictureUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pictureUri = FileProvider.getUriForFile(this,
                                "com.ryx.materialtest.fileprovider", pictureFile);
                        Log.e(TAG,"picURI="+pictureUri.toString());
                    } else {
                        pictureUri = Uri.fromFile(pictureFile);
                    }
                    startPhotoZoom(pictureUri);
                    break;
                // 获取changeinfo销毁 后 回传的数据
                case REQUEST_CHANGE_USER_NICK_NAME:
                    String returnData = data.getStringExtra("data_return");
                    InfoPrefs.setData(Constans.UserInfo.NAME,returnData);
                    textView_user_nick_name.setText(InfoPrefs.getData(Constans.UserInfo.NAME));
                    break;
            }
        }else{
            Log.e(TAG,"result = "+resultCode+",request = "+requestCode);
        }
    }

    private void startPhotoZoom(Uri uri) {
        Log.d(TAG,"Uri = "+uri.toString());
        //保存裁剪后的图片
        File cropFile=new File(PictureUtil.getMyPetRootDirectory(),"crop.jpg");
        try{
            if(cropFile.exists()){
                cropFile.delete();
                Log.e(TAG,"delete");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Uri cropUri;
        cropUri = Uri.fromFile(cropFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);

        Log.e(TAG,"cropUri = "+cropUri.toString());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    carmeraDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                    } else {
                        Toast.makeText(MineActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    carmeraDialog.dismiss();
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    carmeraDialog.dismiss();
                    imageCapture();
                } else {
                    carmeraDialog.dismiss();
                }
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head:
                showDialog();
                break;
            case R.id.mine_back:
                finish();
                break;
            case R.id.user_name:
                bean = new ChangeUserBean();
                bean.setNameinfo(InfoPrefs.getData(Constans.UserInfo.NAME));
                Intent intent = new Intent();
                intent.setClass(MineActivity.this,EditUserNameActivity.class);
                intent.putExtra("data",bean);
                startActivityForResult(intent,REQUEST_CHANGE_USER_NICK_NAME);
                break;
            case R.id.user_gender:
                new ItemsAlertDialogUtil(MineActivity.this).setItems(Constans.GENDER_ITEMS)
                        .setListener(new ItemsAlertDialogUtil.OnSelectFinishedListener() {
                            @Override
                            public void SelectFinished(int which) {
                                InfoPrefs.setData(Constans.UserInfo.GENDER,Constans.GENDER_ITEMS[which]);
                                textView_user_gender.setText(InfoPrefs.getData(Constans.UserInfo.GENDER));
                            }
                        }).showDialog();

        }
    }
}
