package com.example.meet.view.chat;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerViewAdapter  extends RecyclerView.Adapter<BaseChatRecyclerViewHolder> {

    private String friendName, friendPortraitUrl, myName, myPortraitUrl;

    private List<IMMessage> imMessages = new ArrayList<>();

    public ChatRecyclerViewAdapter(String friendName, String friendPortraitUrl, String myName, String myPortraitUrl) {
        this.friendName = friendName;
        this.friendPortraitUrl = friendPortraitUrl;
        this.myName = myName;
        this.myPortraitUrl = myPortraitUrl;
    }

    @NonNull
    @Override
    public BaseChatRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), viewType, null);
        BaseChatRecyclerViewHolder holder = null;
        switch (viewType){
            case R.layout.layout_chat_time:
                holder = new TimeChatRecyclerViewHolder(itemView);
                break;
            case R.layout.layout_chat_left_text:
                holder = new TextChatRecyclerViewHolder(itemView, friendName, friendPortraitUrl);
                break;
            case R.layout.layout_chat_right_text:
                holder = new TextChatRecyclerViewHolder(itemView, myName, myPortraitUrl);
                holder.isOnRightSide = true;
                break;
            default:
                holder = new BaseChatRecyclerViewHolder(itemView, myName, myPortraitUrl);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseChatRecyclerViewHolder holder, int position) {
        holder.set(imMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return imMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage imMessage = imMessages.get(position);
        boolean isOnRightSide = UserManager.getInstance().getUser().getUid().equals(imMessage.getSourceId());
        int resId = 0;
        switch (imMessages.get(position).getImMessageType()){
            case TEXT:
                resId = isOnRightSide? R.layout.layout_chat_right_text : R.layout.layout_chat_left_text;
                break;
            case TIME:
                resId = R.layout.layout_chat_time;
                break;
            default:
                break;
        }
        return resId;
    }

    public void clear(){
        imMessages.clear();
    }

    public int size(){
        return imMessages.size();
    }

    public void add(IMMessage imMessage){
        imMessages.add(imMessage);
    }
}
