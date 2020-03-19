package com.example.meet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.framework.activity.BaseFullScreenStyleActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserLoginService;
import com.example.framework.util.StringUtil;
import com.example.framework.util.ToastUtil;
import com.example.framework.view.controller.ImageVerificationDialogController;
import com.example.framework.view.controller.LoadingDialogController;
import com.example.framework.view.listener.OnViewResultListener;
import com.example.meet.R;
import com.example.meet.persistent.preference.SharedPreferenceConstant;

/**
 *
 * 通过手机号码和短信验证码注册和登录
 *
 *
 */

public class LoginActivity extends BaseFullScreenStyleActivity {

    // retry sms, unit second
    private static final int SMS_RETRY_TIME_INTERVAL = 60;

    // count down interval, unit millisecond
    private static final int SMS_COUNT_DOWN_INTERVAL = 1000;

    // handler message what
    private static final int HANDLER_MESSAGE_WHAT_SMS_RETRY = 1000;

    // handler
    private Handler handler;

    // count down time remained
    private int timeRemained = SMS_RETRY_TIME_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 读取本地cookie数据
        String savedPhone = getString(SharedPreferenceConstant.PHONE, "");
        if(savedPhone != null){
            EditText phoneText = findViewById(R.id.edit_phone);
            phoneText.setText(savedPhone);
        }

        // 登录按键
        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // 请求验证码的按键
        final Button requestSms = findViewById(R.id.send_code);
        requestSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 显示图片验证
                ImageVerificationDialogController imageVerificationDialogController = new ImageVerificationDialogController(LoginActivity.this);
                imageVerificationDialogController.setOnViewResultListener(new OnViewResultListener() {
                    @Override
                    public void onResult(Object object) {

                        // 请求短信验证
                        requestSms();
                    }
                });
                imageVerificationDialogController.show();
            }
        });

        // handler to update text on UI thread
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case HANDLER_MESSAGE_WHAT_SMS_RETRY:
                        if(timeRemained > 0){
                            requestSms.setText(timeRemained + "s");
                            handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_WHAT_SMS_RETRY, SMS_COUNT_DOWN_INTERVAL);
                            timeRemained --;
                        }
                        else {
                            requestSms.setEnabled(true);
                            requestSms.setText(R.string.text_login_send);
                            timeRemained = SMS_RETRY_TIME_INTERVAL;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        // 通过账号密码登录
        Button loginByAccountButton = findViewById(R.id.btn_login_by_account);
        loginByAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginByAccount();
            }
        });
    }


    private void requestSms(){

        // get phone info
        EditText phoneText = findViewById(R.id.edit_phone);
        String phone = phoneText.getText().toString().trim();

        // text verification
        if(TextUtils.isEmpty(phone)){
            ToastUtil.toast(this, R.string.text_login_phone_null);
            return;
        }
        if(!StringUtil.isTelephoneValid(phone)){
            ToastUtil.toast(this, R.string.text_login_phone_not_valid);
            return;
        }

        // update sms button
        final Button requestSms = findViewById(R.id.send_code);
        requestSms.setEnabled(false);
        handler.sendEmptyMessage(HANDLER_MESSAGE_WHAT_SMS_RETRY);


        // request sms
        IUserLoginService userLoginService = getBackendService(IUserLoginService.class);
        userLoginService.requestSms(phone, new BackendServiceCallback<String>() {
            @Override
            public void success(String string) {
                ToastUtil.toast(LoginActivity.this, R.string.text_user_request_succeed);
            }

            @Override
            public void fail(BackendServiceException e) {
                ToastUtil.toast(LoginActivity.this, R.string.text_user_request_fail);
            }
        });
    }


    private void login(){

        // get edit texts
        EditText phoneText = findViewById(R.id.edit_phone);
        EditText verificationCodeText = findViewById(R.id.edit_code);

        // text verification
        final String phone = phoneText.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.toast(this, R.string.text_login_phone_null);
            return;
        }

        String verificationCode = verificationCodeText.getText().toString().trim();
        if(TextUtils.isEmpty(verificationCode)){
            ToastUtil.toast(this, R.string.text_login_code_null);
            return;
        }

        // loading
        final LoadingDialogController loadingDialogController = new LoadingDialogController(this);
        loadingDialogController.show();

        // login verification
        final IUserLoginService<User> userLoginService = getBackendService(IUserLoginService.class);
        userLoginService.loginByPhone(phone, verificationCode, new BackendServiceCallback<User>() {
            @Override
            public void success(User user) {

                loadingDialogController.hide();

                // 登录成功，跳转到主页
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                // 保存手机号到本地
                putString(SharedPreferenceConstant.PHONE, phone);

                finish();
            }

            @Override
            public void fail(BackendServiceException e) {

                loadingDialogController.hide();

                 if(userLoginService.isLoginFailedByWrongVerificationCode(LoginActivity.this, e)) {
                     ToastUtil.toast(LoginActivity.this, R.string.text_login_code_error);
                 }
                 else {
                     ToastUtil.toast(LoginActivity.this, R.string.text_login_failure);
                 }
            }
        });

    }

    private void loginByAccount(){
        startActivity(new Intent(this, LoginByAccountActivity.class));
    }

}
