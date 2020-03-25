package com.example.meet.view.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;

public class TextChatRecyclerViewHolder extends BaseChatRecyclerViewHolder {


    public TextChatRecyclerViewHolder(@NonNull View itemView, String name, String portraitUrl) {
        super(itemView, name, portraitUrl);
    }

    @Override
    void set(IMMessage imMessage) {
        super.set(imMessage);
        ImageView photoView = itemView.findViewById(isOnRightSide ? R.id.iv_right_photo : R.id.iv_left_photo);
        GlideUtil.load(itemView.getContext(), portraitUrl, 100, 100, photoView);
        ((TextView) itemView.findViewById(isOnRightSide ? R.id.tv_right_text : R.id.tv_left_text)).setText(((IMTextMessage)imMessage).getMessageContent());
    }
}
