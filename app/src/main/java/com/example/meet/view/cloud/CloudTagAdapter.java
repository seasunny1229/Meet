package com.example.meet.view.cloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meet.R;
import com.example.meet.info.StarInfo;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

public class CloudTagAdapter extends TagsAdapter {
    private static final int DEFAULT_COUNT = 100;
    private static final int DEFAULT_POPULARITY = 7;

    private List<StarInfo> starInfos;

    public CloudTagAdapter() {
    }

    public CloudTagAdapter(List<StarInfo> starInfos){
        this.starInfos = starInfos;
    }

    @Override
    public int getCount() {
        return starInfos != null ? starInfos.size() : DEFAULT_COUNT;
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_star_view_item, null);
        return view;
    }

    @Override
    public Object getItem(int position) {
        return starInfos != null ? starInfos.get(position) : null;
    }

    @Override
    public int getPopularity(int position) {
        return DEFAULT_POPULARITY;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
