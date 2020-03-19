package com.example.meet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.framework.activity.BaseFullScreenStyleActivity;
import com.example.framework.backend.service.IUserInfoService;
import com.example.meet.persistent.SharedPreferenceConstant;
import com.example.meet.R;

/**
 *
 * 启动APP后的索引页
 *
 *
 */

public class IndexActivity extends BaseFullScreenStyleActivity {
    private static final int HANDLER_MESSAGE_WHAT_NEXT_ACTIVITY = 1000;
    private static final int INDEX_PAGE_SHOWING_DURATION = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        // init handler
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case HANDLER_MESSAGE_WHAT_NEXT_ACTIVITY:
                        startNextActivity();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        // delay 2 seconds to start main activity
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_WHAT_NEXT_ACTIVITY, INDEX_PAGE_SHOWING_DURATION);
    }


    private void startNextActivity(){
        boolean isFirstLaunchApp = getBoolean(SharedPreferenceConstant.IS_FIRST_LAUNCH_APP, true);

        Intent intent = new Intent();

        // first launch this APP
        if(isFirstLaunchApp){

            // jump to guide activity
            intent.setClass(this, GuideActivity.class);

            // update data
            putBoolean(SharedPreferenceConstant.IS_FIRST_LAUNCH_APP, false);
        }

        // not the first time to launch this APP
        else {

            IUserInfoService userInfoService = getBackendService(IUserInfoService.class);

            // jump to main activity
            if(userInfoService.isLogin()){
                intent.setClass(this, MainActivity.class);

            }

            // jump to login activity
            else {
                intent.setClass(this, LoginActivity.class);
            }
        }

        // start next activity
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        // 引导页无需退出
        //super.onBackPressed();
    }
}

