package com.example.meet.view.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.glide.GlideUtil;
import com.example.framework.util.TimeUtil;
import com.example.meet.R;

public class TimeChatRecyclerViewHolder extends BaseChatRecyclerViewHolder {


    public TimeChatRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    void set(IMMessage imMessage) {
        super.set(imMessage);
        ((TextView) itemView.findViewById(R.id.tv_time)).setText(TimeUtil.formatTime(imMessage.getReceivedTime()));
    }
}
