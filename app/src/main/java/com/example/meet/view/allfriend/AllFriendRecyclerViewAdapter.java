package com.example.meet.view.allfriend;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.bean.Friend;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class AllFriendRecyclerViewAdapter extends RecyclerView.Adapter<AllFriendRecyclerViewHolder> {

    private List<Friend> friends = new ArrayList<>();

    @NonNull
    @Override
    public AllFriendRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllFriendRecyclerViewHolder(View.inflate(parent.getContext(), viewType, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AllFriendRecyclerViewHolder holder, int position) {
        holder.set(friends.get(position));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.layout_all_friend_item;
    }

    public void add(Friend friend){
        friends.add(friend);
    }

    public void clear(){
        friends.clear();
    }
}
