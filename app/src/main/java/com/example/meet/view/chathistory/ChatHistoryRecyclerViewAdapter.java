package com.example.meet.view.chathistory;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.messaging.conversation.IMConversation;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class ChatHistoryRecyclerViewAdapter extends RecyclerView.Adapter<ChatHistoryRecyclerViewHolder> {

    private List<IMConversation> list = new ArrayList<>();


    @Override
    public int getItemViewType(int position) {
        return R.layout.layout_chat_history_item;
    }

    @NonNull
    @Override
    public ChatHistoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHistoryRecyclerViewHolder(View.inflate(parent.getContext(), viewType, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHistoryRecyclerViewHolder holder, int position) {
        holder.set(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clear(){
        list.clear();
    }

    public void add(IMConversation imConversation){
        list.add(imConversation);
    }
}
