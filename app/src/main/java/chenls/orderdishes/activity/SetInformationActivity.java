package chenls.orderdishes.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.MyUser;
import chenls.orderdishes.utils.CommonUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

public class SetInformationActivity extends AppCompatActivity {

    private static final int GALLERY = 1;
    private static final int CAMERA = 2;
    private static final int PHOTO_ZOOM = 3;
    private static final String PICTURE_NAME = "picture.jpg";

    private EditText et_name;
    private EditText et_phone_num;
    private EditText et_location;
    private ImageView iv_face;
    private File picture_file;
    private boolean IS_CHANGE_FACE;
    private ProgressDialog progressDialog;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_information);
        ActionBar action = getSupportActionBar();
        if (action != null) {
            action.setDisplayHomeAsUpEnabled(true);
            action.setHomeAsUpIndicator(R.mipmap.ic_clear);
        }
        iv_face = (ImageView) findViewById(R.id.iv_face);
        MyUser myUser = MyUser.getCurrentUser(SetInformationActivity.this, MyUser.class);
        BmobFile pic = myUser.getPic();
        if (pic != null) {
            Glide.with(SetInformationActivity.this)
                    .load(pic.getFileUrl(SetInformationActivity.this))
                    .asBitmap()
                    .placeholder(R.mipmap.face)
                    .centerCrop()
                    .into(iv_face);
        }
        Button bt_change_face = (Button) findViewById(R.id.bt_change_face);
        bt_change_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SetInformationActivity.this);
                builder.setIcon(R.mipmap.select);
                builder.setTitle(SetInformationActivity.this.getString(R.string.choose));
                final String s[] = new String[]{getString(R.string.gallery),
                        getString(R.string.picture)};
                builder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        picture_file = new File(SetInformationActivity.this.getExternalCacheDir(), PICTURE_NAME);
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        "image/*");
                                startActivityForResult(intent, GALLERY);
                                break;
                            case 1:
                                if (CommonUtil.isExternalStorageWritable()) {
                                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture_file));
                                    startActivityForResult(intent2, CAMERA);
                                }
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        et_name = (EditText) findViewById(R.id.et_name);
        et_name.setText((String) MyUser.getObjectByKey(this, "username"));
        et_phone_num = (EditText) findViewById(R.id.et_phone_num);
        et_phone_num.setText((String) MyUser.getObjectByKey(this, "mobilePhoneNumber"));
        et_location = (EditText) findViewById(R.id.et_location);
        et_location.setText((String) MyUser.getObjectByKey(this, "address"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA:
                    startPhotoZoom(Uri.fromFile(picture_file));
                    break;
                case PHOTO_ZOOM:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            photo = extras.getParcelable("data");
                            IS_CHANGE_FACE = true;
                            saveMyBitmap(photo);
                            iv_face.setImageBitmap(photo);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri uri
     */

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_ZOOM);
    }

    /**
     * 保存裁剪后的图片
     *
     * @param mBitmap 位图
     */
    public void saveMyBitmap(Bitmap mBitmap) {
        try {
            FileOutputStream fOut = new FileOutputStream(picture_file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            if (!CommonUtil.checkNetState(SetInformationActivity.this)) {
                Toast.makeText(SetInformationActivity.this, "网络不可用！", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (IS_CHANGE_FACE) {
                uploadImage();//先上传图片
            } else {
                updateUser(null);
            }
            progressDialog = new ProgressDialog(SetInformationActivity.this);
            progressDialog.setMessage("上传中...");
            progressDialog.show();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 更新用户
     */
    private void updateUser(BmobFile file) {
        MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
        if (myUser != null) {
            MyUser newUser = new MyUser();
            if (file != null)
                newUser.setPic(file);
            newUser.setUsername(et_name.getText().toString().trim());
            newUser.setMobilePhoneNumber(et_phone_num.getText().toString().trim());
            newUser.setAddress(et_location.getText().toString().trim());
            newUser.update(this, myUser.getObjectId(), new UpdateListener() {

                @Override
                public void onSuccess() {
                    Toast.makeText(SetInformationActivity.this, "完成修改", Toast.LENGTH_SHORT).show();
                    returnData();
                }

                @Override
                public void onFailure(int code, String msg) {
                    progressDialog.dismiss();
                    Toast.makeText(SetInformationActivity.this, "更新失败" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(SetInformationActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        BmobProFile.getInstance(SetInformationActivity.this)
                .upload(picture_file.getAbsolutePath(), new UploadListener() {
                    @Override
                    public void onSuccess(String fileName, String url, BmobFile file) {
                        Log.i("smile", "新版文件服务的fileName = " + fileName + ",新版文件服务的url =" + url);
                        if (file != null) {
                            Log.i("smile", "兼容旧版文件服务的源文件名 = " + file.getFilename() + ",文件地址url = " + file.getUrl());
                        }
                        updateUser(file); //图片绑定到用户上
                    }

                    @Override
                    public void onProgress(int ratio) {
                        Log.i("smile", "MainActivity -onProgress :" + ratio);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        progressDialog.dismiss();
                        Toast.makeText(SetInformationActivity.this, "上传图片出错" + code + " " + msg,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void returnData() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.PIC, photo);
        setResult(RESULT_OK, intent);
        finish();
    }
}
