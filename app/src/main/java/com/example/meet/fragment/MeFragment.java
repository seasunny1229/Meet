package com.example.meet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.eventbus.EventBusUtil;
import com.example.framework.eventbus.MessageEvent;
import com.example.framework.fragment.BaseFragment;
import com.example.framework.glide.GlideUtil;
import com.example.framework.util.ToastUtil;
import com.example.meet.R;
import com.example.meet.activity.MeInfoActivity;
import com.example.meet.eventbus.EventBusConstant;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends BaseFragment {

    private View mView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // event bus
        EventBusUtil.registerEventBus(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        // 加载个人信息，头像，昵称
        loadMyUserInfo(mView = view);

        // 设置功能按键
        setFunctionalButtons();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // event bus
        EventBusUtil.unregisterEventBus(this);
    }

    private void loadMyUserInfo(View view){

        User user = (User) ((BaseActivity) view.getContext()).getBackendService(IUserInfoService.class).getUser();

        TextView nicknameView = view.findViewById(R.id.tv_nickname);
        nicknameView.setText(user.getNickName());

        CircleImageView portraitView = view.findViewById(R.id.iv_me_photo);
        GlideUtil.load(view.getContext(), user.getPhoto(), 100,  100, portraitView);

    }

    private void setFunctionalButtons(){

        // 跳转设置用户个人信息
        mView.findViewById(R.id.ll_me_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToSettingUserInfo();
            }
        });
    }

    private void jumpToSettingUserInfo(){
        startActivity(new Intent(getActivity(), MeInfoActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){
        if(messageEvent.getType() == EventBusConstant.UPDATE_PERSONAL_INFO){
             loadMyUserInfo(mView);
        }
    }
}
