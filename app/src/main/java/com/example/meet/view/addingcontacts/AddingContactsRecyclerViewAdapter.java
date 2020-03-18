package com.example.meet.view.addingcontacts;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.marker.Info;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class AddingContactsRecyclerViewAdapter extends RecyclerView.Adapter<AddingContactsRecyclerViewHolder> {

    private List<Info> data = new ArrayList<>();
    private List<String> contactNames = new ArrayList<>();

    @NonNull
    @Override
    public AddingContactsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddingContactsRecyclerViewHolder(View.inflate(parent.getContext(), viewType, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AddingContactsRecyclerViewHolder holder, int position) {
        holder.set(data.get(position),contactNames.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.layout_search_user_item;
    }

    public void clear(){
        data.clear();
        contactNames.clear();
    }

    public void add(Info info, String contactName){
        data.add(info);
        contactNames.add(contactName);
    }
}
