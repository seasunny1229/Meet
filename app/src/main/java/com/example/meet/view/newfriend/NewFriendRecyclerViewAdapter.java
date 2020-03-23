package com.example.meet.view.newfriend;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.R;
import com.example.meet.persistent.litepal.bean.NewFriend;

import java.util.ArrayList;
import java.util.List;

public class NewFriendRecyclerViewAdapter extends RecyclerView.Adapter<NewFriendRecyclerViewHolder> {

    private List<NewFriend> list = new ArrayList<>();

    @NonNull
    @Override
    public NewFriendRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewFriendRecyclerViewHolder(View.inflate(parent.getContext(), viewType, null));
    }

    @Override
    public void onBindViewHolder(@NonNull NewFriendRecyclerViewHolder holder, int position) {
            holder.set(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.layout_new_friend_item;
    }

    public void clear(){
        list.clear();
    }

    public void add(NewFriend newFriend){
        list.add(newFriend);
    }

}
