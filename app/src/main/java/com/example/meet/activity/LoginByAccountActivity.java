package com.example.meet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.framework.activity.BaseCommonStyleActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserLoginService;
import com.example.framework.util.ToastUtil;
import com.example.framework.view.controller.LoadingDialogController;
import com.example.meet.R;
import com.example.meet.persistent.SharedPreferenceConstant;

/**
 *
 * 已经有账号和密码，通过账号和密码登录
 *
 *
 */

public class LoginByAccountActivity extends BaseCommonStyleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_account);

        Button button = findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginByAccount();
            }
        });
    }

    private void loginByAccount(){
        EditText accountEditText = findViewById(R.id.edit_account);
        final String account = accountEditText.getText().toString();

        EditText passwordEditText = findViewById(R.id.edit_password);
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(account)){
            return;
        }

        if(TextUtils.isEmpty(password)){
            return;
        }

        // loading
        final LoadingDialogController loadingDialogController = new LoadingDialogController(this);
        loadingDialogController.show();


        IUserLoginService<User> userLoginService = getBackendService(IUserLoginService.class);
        userLoginService.loginByAccount(account, password, new BackendServiceCallback<User>() {
            @Override
            public void success(User user) {

                loadingDialogController.hide();

                // 登录成功，跳转到主页
                startActivity(new Intent(LoginByAccountActivity.this, MainActivity.class));

                // 保存手机号到本地
                putString(SharedPreferenceConstant.PHONE, account);
            }

            @Override
            public void fail(BackendServiceException e) {
                loadingDialogController.hide();
                ToastUtil.toast(LoginByAccountActivity.this, R.string.text_login_account_login_failure);
            }
        });

    }
}
