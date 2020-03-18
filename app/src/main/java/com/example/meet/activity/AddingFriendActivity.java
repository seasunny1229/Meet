package com.example.meet.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseCommonStyleActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.marker.IUser;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.util.ToastUtil;
import com.example.meet.R;
import com.example.meet.view.addingfriend.AddingFriendRecyclerViewAdapter;
import com.example.meet.view.addingfriend.AddingFriendTitleInfo;

import java.util.List;

public class AddingFriendActivity extends BaseCommonStyleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_friend);

        // 列表的实现
        RecyclerView searchRecyclerView = findViewById(R.id.mSearchResultView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searchRecyclerView.setAdapter(new AddingFriendRecyclerViewAdapter());

        // 查询输入的手机号码
        ImageView queryView = findViewById(R.id.iv_search);
        queryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPhoneNumber();
            }
        });


        // 从通讯录中导入
        LinearLayout importView = findViewById(R.id.ll_to_contact);
        importView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importPhoneNumbersFromContacts();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(isPermissionGranted(Manifest.permission.READ_CONTACTS)){
            startActivity(new Intent(this, AddingContactsActivity.class));
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void queryPhoneNumber(){

        // 获取电话号码
        EditText editText = findViewById(R.id.et_phone);
        String phone = editText.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.toast(this, R.string.text_login_phone_null);
            return;
        }

        // 过滤掉自己的电话号码
        String myPhone = ((User) getBackendService(IUserInfoService.class).getUser()).getMobilePhoneNumber();
        if(phone.equals(myPhone)){
            ToastUtil.toast(this, R.string.text_add_friend_no_me);
            return;
        }

        // 查询用户信息
        final IUserQueryService<User> userQueryService = getBackendService(IUserQueryService.class);
        userQueryService.findUsersByPhoneNumber(phone, new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> iUsers) {
                RecyclerView searchRecyclerView = findViewById(R.id.mSearchResultView);
                View emptyView = findViewById(R.id.include_empty_view);
                if(!iUsers.isEmpty()){
                    searchRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    String title = getResources().getString(R.string.text_add_friend_title);
                    ((AddingFriendRecyclerViewAdapter)searchRecyclerView.getAdapter()).clear();
                    ((AddingFriendRecyclerViewAdapter)searchRecyclerView.getAdapter()).add(new AddingFriendTitleInfo(title));
                    ((AddingFriendRecyclerViewAdapter)searchRecyclerView.getAdapter()).add(iUsers.get(0));
                    searchRecyclerView.getAdapter().notifyDataSetChanged();
                }
                else {
                    searchRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void fail(BackendServiceException e) {

            }
        });

    }


    private void importPhoneNumbersFromContacts(){

        // 访问通讯录需要检查权限
        if(isPermissionGranted(Manifest.permission.READ_CONTACTS)){
            startActivity(new Intent(this, AddingContactsActivity.class));
        }
        else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS});
        }
    }

}
