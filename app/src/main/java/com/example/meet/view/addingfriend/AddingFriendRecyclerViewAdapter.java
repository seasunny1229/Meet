package com.example.meet.view.addingfriend;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.marker.IUser;
import com.example.framework.backend.marker.Info;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class AddingFriendRecyclerViewAdapter extends RecyclerView.Adapter<AddingFriendRecyclerViewHolder> {

    private List<Info> data = new ArrayList<>();

    @NonNull
    @Override
    public AddingFriendRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddingFriendRecyclerViewHolder(View.inflate(parent.getContext(), viewType, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AddingFriendRecyclerViewHolder holder, int position) {
        holder.set(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) instanceof IUser ? R.layout.layout_search_user_item : R.layout.layout_search_title_item;
    }

    public void clear(){
        data.clear();
    }

    public void add(Info info){
        data.add(info);
    }

}
