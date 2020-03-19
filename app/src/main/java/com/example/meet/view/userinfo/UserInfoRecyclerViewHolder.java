package com.example.meet.view.userinfo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.R;

class UserInfoRecyclerViewHolder extends RecyclerView.ViewHolder {


    UserInfoRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void set(UserItemInfo userItemInfo){
        itemView.findViewById(R.id.ll_bg).setBackgroundColor(userItemInfo.getBackgroundColor());
        ((TextView)itemView.findViewById(R.id.tv_type)).setText(userItemInfo.getType());
        ((TextView)itemView.findViewById(R.id.tv_content)).setText(userItemInfo.getValue());
    }

}
