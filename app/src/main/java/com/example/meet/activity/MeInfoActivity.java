package com.example.meet.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.framework.activity.BaseCommonStyleActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.eventbus.EventBusUtil;
import com.example.framework.eventbus.MessageEvent;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.glide.GlideUtil;
import com.example.framework.util.ToastUtil;
import com.example.framework.view.controller.LoadingDialogController;
import com.example.meet.R;
import com.example.meet.eventbus.EventBusConstant;

/**
 *
 * 修改个人信息页面
 *
 *
 */

public class MeInfoActivity extends BaseCommonStyleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info);

        // load personal info
        loadPersonalInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.me_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_save){
            updatePersonalInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPersonalInfo(){

        User user = (User) getBackendService(IUserInfoService.class).getUser();

        // name
        String name = user.getNickName();
        ((EditText)findViewById(R.id.et_nickname)).setText(name);

        // photo
        String photoUrl = user.getPhoto();
        GlideUtil.load(this, photoUrl, (ImageView) findViewById(R.id.iv_user_photo));

        // gender
        boolean isBoy = user.isSex();
        ((TextView)findViewById(R.id.tv_user_sex)).setText(isBoy? getString(R.string.text_me_info_boy) : getString(R.string.text_me_info_girl));

        // age
        int age = user.getAge();
        ((TextView)findViewById(R.id.tv_user_age)).setText(String.valueOf(age));

        // birth
        String birth = user.getBirthday();
        ((TextView)findViewById(R.id.tv_user_birthday)).setText(birth);

        // constellation
        String constellation = user.getConstellation();
        ((TextView)findViewById(R.id.tv_user_constellation)).setText(constellation);

        // description
        String description = user.getDesc();
        ((EditText)findViewById(R.id.et_user_desc)).setText(description);

        // hobby
        String hobby = user.getHobby();
        ((TextView)findViewById(R.id.tv_user_hobby)).setText(hobby);

        // status
        String status = user.getStatus();
        ((TextView)findViewById(R.id.tv_user_status)).setText(status);

    }

    private void updatePersonalInfo(){

        User user = (User) getBackendService(IUserInfoService.class).getUser();

        // name
        user.setNickName(((EditText)findViewById(R.id.et_nickname)).getText().toString());

        // desc
        user.setDesc(((EditText)findViewById(R.id.et_user_desc)).getText().toString());

        // constellation

        // hobby

        // status

        // update
        final LoadingDialogController loadingDialogController = LoadingDialogController.getLoadingDialog(this);
        loadingDialogController.show();
        IUserInfoService<User> userInfoService = getBackendService(IUserInfoService.class);
        userInfoService.updateUserInfo(user, new BackendServiceCallback<User>() {
            @Override
            public void success(User user) {
                loadingDialogController.hide();
                EventBusUtil.post(MessageEvent.newMessage(EventBusConstant.UPDATE_PERSONAL_INFO));
                ToastUtil.toast(MeInfoActivity.this, getResources().getString(R.string.menu_info_save_success));
            }

            @Override
            public void fail(BackendServiceException e) {
                loadingDialogController.hide();
                ExceptionHandler.handleBackendServiceException(MeInfoActivity.this, e);
            }
        });


    }
}
