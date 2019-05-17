package com.aier.ardemo.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.AdjustBitmap;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
//https://blog.csdn.net/u010296640/article/details/72731324  拍照代码参照
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.my_photo)
    ImageView my_photo;
    Person person;

    private static String path = "/sdcard/myHead/";// sd路径
    private Uri imageUri;//相机拍照图片保存地址
    private Uri outputUri;//裁剪万照片保存地址
    private String imagePath;//打开相册选择照片的路径
    private boolean isClickCamera;//是否是拍照裁剪
    @Override
    protected void initViews() {
        tv_title.setText("个人信息");
        my_photo.setOnClickListener(this);

        Bitmap bt = BitmapFactory.decodeFile(path + "crop_image.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            Bitmap pic = AdjustBitmap.getCircleBitmap(bt);
            my_photo.setImageBitmap(pic);
        } else {
            // TODO: 2019/4/26  如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
        }
    }

    @Override
    protected void initDate(Bundle savedInstanceState) {
       person = GloData.getPerson();
      if(TextUtils.isEmpty(person.getUsername())){
          et_name.setText("");
      }else {
          et_name.setText(person.getUsername());
      }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_person_info;
    }

    @OnClick({R.id.iv_back,R.id.ok_name,R.id.delete_name,R.id.my_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
              finish();
                break;
            case R.id.ok_name:
                String name = et_name.getText().toString().trim();
                person.setUsername(name);
                SharedPreferencesUtil.putString(this, "usersData", "usersData", new Gson().toJson(person));
                break;
            case R.id.delete_name:
                et_name.setText("");
                break;
            case R.id.my_photo:// 更换头像
                showTypeDialog();
                break;
        }

    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery =  view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera =  view.findViewById(R.id.tv_select_camera);
        // 在相册中选取
        tv_select_gallery.setOnClickListener(v -> {
            openAlbum();
            dialog.dismiss();
        });
        // 调用照相机
        tv_select_camera.setOnClickListener(v -> {
            openCamera();
            dialog.dismiss();
        });
        dialog.setView(view);
        dialog.show();
    }

    // 打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 5);
     }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                 String selection = MediaStore.Images.Media._ID + "=" + id;
                 imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
             imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
             imagePath = uri.getPath();
        }
        cropPhoto(uri);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
         Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
         if (cursor != null) {
             if (cursor.moveToFirst()) {
                 path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
             }
             cursor.close();
         }
         return path;
    }

    /**
     * 拍照
     */
    private void openCamera() {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
            // 参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名
             imageUri = FileProvider.getUriForFile(this, "com.aier.ardemo.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 2);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 5:
                if (Build.VERSION.SDK_INT >= 19) { // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(data);
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(data);
                }
                break;
            case 2:
                cropPhoto(imageUri);//裁剪图片
                break;

            case 6:
                //显示最后的照片
                isClickCamera = true;
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    try {
                        if (isClickCamera) {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));
                        } else {
                            bitmap = BitmapFactory.decodeFile(imagePath);
                        }
                        Bitmap pic = AdjustBitmap.getCircleBitmap(bitmap);
                        my_photo.setImageBitmap(pic);// 用ImageView显示出来
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
         // 创建File对象，用于存储裁剪后的图片，避免更改原图
         File file = new File(path, "crop_image.jpg");
         try {
             if (file.exists()) {
                 file.delete();
             }
            file.createNewFile();
         } catch (IOException e) {
             e.printStackTrace();
         }
     outputUri = Uri.fromFile(file);
     Intent intent = new Intent("com.android.camera.action.CROP");
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
         intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
     }
     intent.setDataAndType(uri, "image/*");
     //裁剪图片的宽高比例
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    intent.putExtra("crop", "true");//可裁剪
    // 裁剪后输出图片的尺寸大小
   //  intent.putExtra("outputX", 400);
   //  intent.putExtra("outputY", 200);
     intent.putExtra("scale", true);//支持缩放
     intent.putExtra("return-data", false);
     intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
     intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
     intent.putExtra("noFaceDetection", true);//取消人脸识别
     startActivityForResult(intent, 6);
}
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        cropPhoto(uri);
    }
}
