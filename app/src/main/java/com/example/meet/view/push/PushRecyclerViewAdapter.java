package com.example.meet.view.push;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.pushing.bean.PushInfo;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class PushRecyclerViewAdapter extends RecyclerView.Adapter<PushRecyclerViewHolder> {

    private List<PushInfo> list = new ArrayList<>();

    @NonNull
    @Override
    public PushRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PushRecyclerViewHolder(View.inflate(parent.getContext(), viewType, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PushRecyclerViewHolder holder, int position) {
        holder.set(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.layout_square_item;
    }

    public void clear(){
        list.clear();
    }

    public void add(PushInfo pushInfo){
        list.add(pushInfo);
    }

}
