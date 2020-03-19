package com.example.meet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.framework.activity.BaseFullScreenStyleActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.file.ApplicationFileNameManager;
import com.example.framework.file.FileUtil;
import com.example.framework.intent.CameraIntent;
import com.example.framework.intent.PhotoIntent;
import com.example.framework.provider.ContentProviderUtil;
import com.example.framework.view.controller.LoadingDialogController;
import com.example.framework.view.custom.ModifiedDialog;
import com.example.meet.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * 第一次启动后，添加用户名和上传头像
 *
 *
 */

public class UpdateTokenActivity extends BaseFullScreenStyleActivity {

    //相机
    public static final int CAMERA_REQUEST_CODE = 1004;

    //相册
    public static final int ALBUM_REQUEST_CODE = 1005;

    //裁剪结果
    public static final int PHOTO_CROP_REQUEST_CODE = 1008;

    // guide dialog
    private ModifiedDialog updateHeadPortraitGuideDialog;

    // temp file
    private File rawPhotoFile, croppedPhotoFile;

    // flag
    private boolean hasChangedNickname, hasUpdatedHeadPortrait;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_token);

        // layout touch events
        initSelectPhotoButton();
        initNicknameEditText();
        initCompleteButton();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(isAllPermissionsGranted(requestCode,permissions,grantResults, null)){
            //跳转到相机
            jumpToCamera();
        }
        else {

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case CAMERA_REQUEST_CODE:
                    cropPhoto();
                    break;
                case ALBUM_REQUEST_CODE:
                    cropPhotoSelectedFromAlbum(data);
                    break;
                case PHOTO_CROP_REQUEST_CODE:
                    updateHeadPortrait();
                    updateCompleteButtonState();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initSelectPhotoButton(){
        CircleImageView view = findViewById(R.id.iv_photo);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateHeadPortraitGuideDialog == null){
                    updateHeadPortraitGuideDialog = createUploadPhotoGuideDialog();
                }
                updateHeadPortraitGuideDialog.show();
            }
        });
    }

    private void initNicknameEditText(){
        final EditText editText = findViewById(R.id.et_nickname);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasChangedNickname = true;
                updateCompleteButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initCompleteButton(){
        Button button = findViewById(R.id.btn_upload);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    private ModifiedDialog createUploadPhotoGuideDialog(){

        // dialog
        final ModifiedDialog uploadPhotoGuideDialog = ModifiedDialog.createDefaultDialog(UpdateTokenActivity.this, R.layout.dialog_select_photo, Gravity.BOTTOM);
        uploadPhotoGuideDialog.setCancelable(true);

        // from camera
        TextView fromCamera = uploadPhotoGuideDialog.findViewById(R.id.tv_camera);
        fromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotoGuideDialog.hide();

                // 检查权限
                if(isPermissionGranted(Manifest.permission.CAMERA)){

                    // 跳转到相机
                    jumpToCamera();
                }
                else {

                    // 请求权限
                    requestPermissions(new String[]{Manifest.permission.CAMERA});
                }
            }
        });

        // from file
        TextView fromFile = uploadPhotoGuideDialog.findViewById(R.id.tv_ablum);
        fromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotoGuideDialog.hide();

                // 跳转到文件
                jumpToAlbum();
            }
        });

        // cancel
        TextView cancel = uploadPhotoGuideDialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotoGuideDialog.hide();
            }
        });

        return uploadPhotoGuideDialog;
    }

    private void jumpToCamera(){
        rawPhotoFile = FileUtil.newTempFile(ApplicationFileNameManager.RAW_PHOTO_FILE_NAME);
        Intent intent = CameraIntent.camera(this, rawPhotoFile);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void jumpToAlbum(){
        Intent intent = PhotoIntent.selectPhotoFromAlbum();
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    private void cropPhotoSelectedFromAlbum(Intent data){
        Uri uri = data.getData();
        if (uri != null) {
            String path = ContentProviderUtil.getRealPathFromURI(this, uri);
            if (!TextUtils.isEmpty(path)) {
                rawPhotoFile = new File(path);
                cropPhoto();
            }
        }
    }

    private void cropPhoto(){
        String uid = ((User)getBackendService(IUserInfoService.class).getUser()).getUid();
        croppedPhotoFile = FileUtil.newTempFile(ApplicationFileNameManager.getCroppedPhotoName(uid));
        Intent intent = PhotoIntent.cropPhoto(this, rawPhotoFile, croppedPhotoFile);
        startActivityForResult(intent, PHOTO_CROP_REQUEST_CODE);
    }

    private void updateHeadPortrait(){
        Bitmap mBitmap = BitmapFactory.decodeFile(croppedPhotoFile.getPath());
        CircleImageView view = findViewById(R.id.iv_photo);
        view.setImageBitmap(mBitmap);
        hasUpdatedHeadPortrait = true;
    }

    private void updateCompleteButtonState(){
        Button completeButton = findViewById(R.id.btn_upload);
        if(canCompleteButtonEnable()){
            completeButton.setEnabled(true);
        }
    }

    private boolean canCompleteButtonEnable(){
        return hasChangedNickname && hasUpdatedHeadPortrait;
    }

    @SuppressWarnings("unchecked")
    private void upload(){

        // loading dialog controller
        final LoadingDialogController controller = new LoadingDialogController(this);
        controller.show();

        // nickname
        EditText editText = findViewById(R.id.et_nickname);
        String nickName = editText.getText().toString().trim();

        // update nickname and upload head portrait
        IUserInfoService<User> userInfoService = getBackendService(IUserInfoService.class);
        userInfoService.updateNicknameAndHeadPortrait(nickName, croppedPhotoFile, new BackendServiceCallback<User>() {
            @Override
            public void success(User user) {
                controller.hide();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void fail(BackendServiceException e) {
                e.printStackTrace();
            }
        });



    }


}
