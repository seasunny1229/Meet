package com.example.meet.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.framework.fragment.BaseFragment;
import com.example.framework.util.ToastUtil;
import com.example.meet.R;
import com.example.meet.activity.AddingFriendActivity;
import com.example.meet.view.cloud.CloudTagAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

public class StarFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        // 星球
        TagCloudView tagCloudView = view.findViewById(R.id.mCloudView);
        tagCloudView.setAdapter(new CloudTagAdapter());
        tagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                ToastUtil.toast(getActivity(), "click");
            }
        });

        // 添加好友
        ImageView addingFriendView = view.findViewById(R.id.iv_add);
        addingFriendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddingFriendActivity.class));
            }
        });

    }
}
