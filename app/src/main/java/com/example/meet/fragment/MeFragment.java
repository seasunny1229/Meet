package com.example.meet.fragment;

import android.view.View;
import android.widget.TextView;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.fragment.BaseFragment;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        // 加载个人信息，头像，昵称
        loadMyUserInfo(view);

    }

    private void loadMyUserInfo(View view){

        User user = (User) ((BaseActivity) view.getContext()).getBackendService(IUserInfoService.class).getUser();

        TextView nicknameView = view.findViewById(R.id.tv_nickname);
        nicknameView.setText(user.getNickName());

        CircleImageView portraitView = view.findViewById(R.id.iv_me_photo);
        GlideUtil.load(view.getContext(), user.getPhoto(), 100,  100, portraitView);

    }
}
