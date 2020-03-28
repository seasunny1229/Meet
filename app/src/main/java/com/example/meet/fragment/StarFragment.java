package com.example.meet.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.lifecycle.ServiceDisposable;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.service.IUserMatchingService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.fragment.BaseFragment;
import com.example.framework.util.ToastUtil;
import com.example.framework.view.controller.LoadingDialogController;
import com.example.meet.R;
import com.example.meet.activity.AddingFriendActivity;
import com.example.meet.activity.QrCodeActivity;
import com.example.meet.activity.UserInfoActivity;
import com.example.meet.view.cloud.CloudTagAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.List;

public class StarFragment extends BaseFragment {
    private static final int REQUEST_CODE = 2000;
    private static final int NUM_CLOUD_TAG_COUNT = 100;

    private IUserMatchingService userMatchingService;

    private View view;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        this.view = view;

        final BaseActivity activity = (BaseActivity) view.getContext();

        // 星球
        TagCloudView tagCloudView = view.findViewById(R.id.mCloudView);
        final CloudTagAdapter cloudTagAdapter = new CloudTagAdapter();
        tagCloudView.setAdapter(cloudTagAdapter);
        tagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                startUserInfoActivity(cloudTagAdapter.getUser(position));
            }
        });

        // 查询星球所有好友
        final LoadingDialogController loadingDialogController = LoadingDialogController.getLoadingDialog(activity).hideLoadingText();
        loadingDialogController.show();
        userMatchingService = activity.getBackendService(IUserMatchingService.class);
        userMatchingService.randomFindUsers(NUM_CLOUD_TAG_COUNT, new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {
                cloudTagAdapter.addAll(users);
                cloudTagAdapter.notifyDataSetChanged();
                loadingDialogController.dismiss();
            }

            @Override
            public void fail(BackendServiceException e) {
                loadingDialogController.dismiss();
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

        // 扫描二维码功能按键
        view.findViewById(R.id.iv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), QrCodeActivity.class), REQUEST_CODE);
            }
        });

        // 设置匹配按键
        int[] buttonResId = new int[]{R.id.ll_random, R.id.ll_soul, R.id.ll_fate, R.id.ll_love};
        for(int i=0;i<buttonResId.length;i++){
            final int index = i;
            view.findViewById(buttonResId[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findUser(index);
                }
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ServiceDisposable)userMatchingService).dispose();
    }

    private void findUser(int index){
        final LoadingDialogController loadingDialogController = LoadingDialogController.getLoadingDialog(view.getContext()).setLoadingText("匹配中...");
        loadingDialogController.show();
        BackendServiceCallback<User> backendServiceCallback = new BackendServiceCallback<User>() {
            @Override
            public void success(User user) {
                loadingDialogController.dismiss();
                startUserInfoActivity(user);
            }

            @Override
            public void fail(BackendServiceException e) {
                loadingDialogController.dismiss();
                ExceptionHandler.handleBackendServiceException(view.getContext(), e);
            }
        };
        switch (index){
            case 0:
                userMatchingService.matchUserByRandom(UserManager.getInstance().getUser(), backendServiceCallback);
                break;
            case 1:
                userMatchingService.matchUserBySoul(UserManager.getInstance().getUser(), backendServiceCallback);
                break;
            case 2:
                userMatchingService.matchUserByFate(UserManager.getInstance().getUser(), backendServiceCallback);
                break;
            case 3:
                userMatchingService.matchUserByLove(UserManager.getInstance().getUser(), backendServiceCallback);
                break;
            default:
                break;
        }

    }

    private void startUserInfoActivity(User user){
        if(user == null){
            ToastUtil.toastInDebugMode(view.getContext(), "匹配失败");
            return;
        }
        Intent intent = new Intent(view.getContext(), UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.INTENT_EXTRA_FRIEND_USER_ID, user.getUid());
        startActivity(intent);
    }


}
