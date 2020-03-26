package com.example.meet.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserMatchingService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.fragment.BaseFragment;
import com.example.framework.util.ToastUtil;
import com.example.framework.view.controller.LoadingDialogController;
import com.example.meet.R;
import com.example.meet.activity.AddingFriendActivity;
import com.example.meet.activity.UserInfoActivity;
import com.example.meet.view.cloud.CloudTagAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.List;

public class StarFragment extends BaseFragment {
    private static final int NUM_CLOUD_TAG_COUNT = 100;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    protected void initView(final View view) {
        super.initView(view);

        final BaseActivity activity = (BaseActivity) view.getContext();

        // 星球
        TagCloudView tagCloudView = view.findViewById(R.id.mCloudView);
        final CloudTagAdapter cloudTagAdapter = new CloudTagAdapter();
        tagCloudView.setAdapter(cloudTagAdapter);
        tagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                Intent intent = new Intent(activity, UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.INTENT_EXTRA_FRIEND_USER_ID, cloudTagAdapter.getUser(position).getUid());
                startActivity(intent);
            }
        });

        // 查询星球所有好友
        final LoadingDialogController loadingDialogController = LoadingDialogController.getLoadingDialog(activity).hideLoadingText();
        loadingDialogController.show();
        IUserMatchingService userMatchingService = activity.getBackendService(IUserMatchingService.class);
        userMatchingService.randomFindUsers(NUM_CLOUD_TAG_COUNT, new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {
                cloudTagAdapter.addAll(users);
                cloudTagAdapter.notifyDataSetChanged();
                loadingDialogController.hide();
            }

            @Override
            public void fail(BackendServiceException e) {
                loadingDialogController.hide();
                ExceptionHandler.handleBackendServiceException(activity, e);
            }
        });

        // 设置添加好友功能
        ImageView addingFriendView = view.findViewById(R.id.iv_add);
        addingFriendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddingFriendActivity.class));
            }
        });

    }
}
