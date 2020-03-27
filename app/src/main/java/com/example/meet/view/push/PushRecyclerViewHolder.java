package com.example.meet.view.push;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.pushing.bean.PushInfo;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;
import com.example.meet.activity.UserInfoActivity;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PushRecyclerViewHolder extends RecyclerView.ViewHolder{


    public PushRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void set(PushInfo pushInfo){

        // personal info
        String uid = pushInfo.getUid();
        final BaseActivity activity = (BaseActivity) itemView.getContext();
        IUserQueryService<User> userQueryService = activity.getBackendService(IUserQueryService.class);
        userQueryService.findUsersById(uid, new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {
                setPersonalInfo(users.get(0));
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(itemView.getContext(), e);
            }
        });

        // push time
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ((TextView) itemView.findViewById(R.id.tv_time)).setText(dateFormat.format(pushInfo.getTime()));

        // content
        if(!TextUtils.isEmpty(pushInfo.getText())){
            TextView contentView = itemView.findViewById(R.id.tv_text);
            contentView.setVisibility(View.VISIBLE);
            contentView.setText(pushInfo.getText());
        }
    }

    private void setPersonalInfo(final User user){

        // display
        CircleImageView circleImageView = itemView.findViewById(R.id.iv_photo);
        GlideUtil.load(itemView.getContext(), user.getPhoto(), 50,50, circleImageView);
        ((TextView) itemView.findViewById(R.id.tv_nickname)).setText(user.getNickName());
        ((TextView) itemView.findViewById(R.id.tv_square_age)).setText(String.valueOf(user.getAge()));

        // button to personal page
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.INTENT_EXTRA_FRIEND_USER_ID, user.getUid());
                itemView.getContext().startActivity(intent);
            }
        });
    }


}
