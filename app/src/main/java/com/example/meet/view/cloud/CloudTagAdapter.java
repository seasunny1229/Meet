package com.example.meet.view.cloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framework.backend.bean.User;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;
import com.example.meet.info.StarInfo;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.ArrayList;
import java.util.List;

public class CloudTagAdapter extends TagsAdapter {
    private static final int DEFAULT_POPULARITY = 7;

    private List<User> users = new ArrayList<>();

    public CloudTagAdapter() {
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_star_view_item, null);
        User user = users.get(position);
        GlideUtil.load(context, user.getPhoto(), 50,50, (ImageView)view.findViewById(R.id.iv_star_icon),R.drawable.img_star_p, R.drawable.img_star_p);
        ((TextView)view.findViewById(R.id.tv_star_name)).setText(user.getNickName());
        return view;
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return DEFAULT_POPULARITY;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }

    public void addAll(List<User> users){
        this.users.addAll(users);
    }

    public User getUser(int position){
        return users.get(position);
    }
}
