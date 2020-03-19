package com.example.meet.view.userinfo;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class UserInfoRecyclerViewAdapter extends RecyclerView.Adapter<UserInfoRecyclerViewHolder> {

    private List<UserItemInfo> list = new ArrayList<>();


    @NonNull
    @Override
    public UserInfoRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserInfoRecyclerViewHolder(View.inflate(parent.getContext(), viewType, null));
    }

    @Override
    public void onBindViewHolder(@NonNull UserInfoRecyclerViewHolder holder, int position) {
        holder.set(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.layout_user_info_item;
    }

    public void add(UserItemInfo userItemInfo){
        list.add(userItemInfo);
    }
}
