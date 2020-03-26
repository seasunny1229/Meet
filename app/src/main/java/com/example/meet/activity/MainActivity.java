package com.example.meet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.framework.activity.BaseMainActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IConnectionService;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.util.LogUtil;
import com.example.framework.view.custom.ModifiedDialog;
import com.example.meet.R;
import com.example.meet.persistent.preference.SharedPreferenceConstant;
import com.example.meet.service.CloudService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * 主页
 *
 *
 */

public class MainActivity extends BaseMainActivity {

    // tag index
    private static final int TAG_INDEX = 0;

    public static final int UPDATE_TOKEN_REQUEST_CODE = 1010;


    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS
    };

    private ModifiedDialog uploadTokenGuideDialog;

    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide dialog
        if(uploadTokenGuideDialog != null){
            uploadTokenGuideDialog.hide();
        }

        // request permissions
        requestPermissions();

        // update token
        StartIMCloudServer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // update token
        if(requestCode == UPDATE_TOKEN_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            StartIMCloudServer();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    // region fragments and tabs config

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getAttachedFragmentLayoutId() {
        return R.id.mMainLayout;
    }

    @Override
    protected TabInfo[] getTabs() {
        return new TabInfo[]{
            new TabInfo(R.id.ll_star, R.id.iv_star, R.id.tv_star,R.drawable.img_star_p, R.drawable.img_star,"StarFragment"),
            new TabInfo(R.id.ll_square, R.id.iv_square, R.id.tv_square,R.drawable.img_square_p, R.drawable.img_square,"SquareFragment"),
            new TabInfo(R.id.ll_chat, R.id.iv_chat, R.id.tv_chat,R.drawable.img_chat_p, R.drawable.img_chat,"ChatFragment"),
            new TabInfo(R.id.ll_me, R.id.iv_me, R.id.tv_me,R.drawable.img_me_p, R.drawable.img_me,"MeFragment")
        };
    }

    @Override
    protected int getFirstTabIndex() {
        return TAG_INDEX;
    }

    @Override
    protected StyleInfo getClickedStyle() {
        return new StyleInfo(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected StyleInfo getNormalStyle() {
        return new StyleInfo(Color.BLACK);
    }

    @Override
    protected String getFragmentPackage() {
        return "com.example.meet.fragment";
    }


    // endregion

    // region user token
    private void StartIMCloudServer(){

        // 定义过头像，个人昵称信息
        if(hasSetTokenUsedPersonalData()){

            // 得到token，启动IM云服务
            getTokenDataAndStartIMCloudServer();
        }
        else {

            // show guide dialog
            showSettingTokenUsedDataGuideDialog();
        }
    }

    private boolean hasLocalizedTokenData(){

        //获取TOKEN 需要三个参数 1.用户ID 2.头像地址 3.昵称
        String token = getString(SharedPreferenceConstant.TOKEN, "");
        return !TextUtils.isEmpty(token);
    }

    private void getTokenDataAndStartIMCloudServer(){

        // 去融云服务器获取token，连接融云
        // 解析token
        // 保存到本地
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                // 从服务器请求token
                IConnectionService userConnectionService = getBackendService(IConnectionService.class);
                userConnectionService.getUserToken( new BackendServiceCallback<String>() {
                    @Override
                    public void success(String s) {
                        emitter.onComplete();
                    }

                    @Override
                    public void fail(BackendServiceException e) {
                        emitter.onError(e);
                    }
                });

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionHandler.handleBackendServiceException(MainActivity.this, (BackendServiceException) e);
                    }

                    @Override
                    public void onComplete() {
                        startCloudService();
                    }
                });
    }

    private boolean hasSetTokenUsedPersonalData(){

        // 获取本地用户对象
        IUserInfoService userInfoService = getBackendService(IUserInfoService.class);
        User user = (User) userInfoService.getUser();

        // 获取token数据
        String tokenNickName = user.getNickName();
        String tokenPhoto = user.getPhoto();

        return !TextUtils.isEmpty(tokenNickName) && !TextUtils.isEmpty(tokenPhoto);
    }

    // endregion

    // region permission
    private void requestPermissions(){

        // 动态权限申请
        requestPermissions(PERMISSIONS);

        // 窗口权限申请
        requestWindowPermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> stillDeniedPermissions = new ArrayList<>();
        if(isAllPermissionsGranted(requestCode, permissions, grantResults, stillDeniedPermissions)){
            LogUtil.i("all permissions are granted");
        }
        else {
            for(String permission : stillDeniedPermissions){
                LogUtil.e(permission);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // endregion

    // region cloud service
    private void startCloudService(){
        startService(new Intent(this, CloudService.class));
    }

    // endregion

    // region guide dialog
    private void showSettingTokenUsedDataGuideDialog(){
        uploadTokenGuideDialog = ModifiedDialog.createDefaultDialog(this, R.layout.dialog_first_upload);
        uploadTokenGuideDialog.show();
        ImageView imageView = uploadTokenGuideDialog.findViewById(R.id.iv_go_upload);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // hide dialog
                uploadTokenGuideDialog.hide();

                // next
                Intent intent = new Intent(MainActivity.this, UpdateTokenActivity.class);
                startActivityForResult(intent,UPDATE_TOKEN_REQUEST_CODE);
            }
        });
    }

    // endregion
}

