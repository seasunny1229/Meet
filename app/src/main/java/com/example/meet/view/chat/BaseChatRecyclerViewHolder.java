package com.example.meet.view.chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.messaging.message.IMMessage;

public class BaseChatRecyclerViewHolder extends RecyclerView.ViewHolder {

    String name, portraitUrl;

    boolean isOnRightSide;

    public BaseChatRecyclerViewHolder(@NonNull View itemView, String name, String portraitUrl) {
        super(itemView);
        this.name = name;
        this. portraitUrl = portraitUrl;
    }

    public BaseChatRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void set(IMMessage imMessage){

    }

}
