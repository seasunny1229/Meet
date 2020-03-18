package com.example.framework.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 *
 *
 * base class to create fragments and tags
 *
 */

public abstract class BaseMainActivity extends BaseFullScreenStyleActivity {

    private static final int DEFAULT_FIRST_SHOW_TAB_INDEX = 0;

    private Fragment[] fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());

        initTouchEvents();
        initFragment();
        showTab(getFirstTabIndex());
    }

    /**
     * 防止重叠
     * 当应用的内存紧张的时候，系统会回收掉Fragment对象
     * 再一次进入的时候会重新创建Fragment
     * 非原来对象，我们无法控制，导致重叠
     *
     * @param fragment newly attached fragment
     */
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragments == null){
            fragments = new Fragment[getTabs().length];
        }
        for(int i=0;i<fragments.length;i++){
            Class clazz;
            try{
                clazz = Class.forName(getFragmentFullClassName(i));

            }catch (ClassNotFoundException e){
                throw new RuntimeException("fragment class from tab info cannot be found");
            }

            if(fragments[i] == null && fragment.getClass() == clazz){
                    fragments[i] = fragment;
            }

        }
    }

    protected abstract int getActivityLayoutId();

    protected abstract int getAttachedFragmentLayoutId();

    protected abstract TabInfo[] getTabs();

    protected abstract StyleInfo getClickedStyle();

    protected abstract StyleInfo getNormalStyle();

    protected abstract String getFragmentPackage();

    protected int getFirstTabIndex(){
        return DEFAULT_FIRST_SHOW_TAB_INDEX;
    };

    private void initTouchEvents(){
        for(int i=0;i<getTabs().length;i++){
            LinearLayout linearLayout = findViewById(getTabs()[i].layoutResId);
            linearLayout.setTag(i);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer tag = (Integer) v.getTag();
                     showTab(tag);
                }
            });
        }
    }

    private void showTab(int index){
        showFragment(fragments[index]);
        setAllTabsNormal();
        TabInfo tabInfo = getTabs()[index];
        ImageView imageView = findViewById(tabInfo.imageResId);
        imageView.setImageResource(tabInfo.clickedImageDrawableId);
        TextView textView = findViewById(tabInfo.textResId);
        textView.setTextColor(getClickedStyle().textColor);
    }

    private void setAllTabsNormal(){
        for(int i=0;i<getTabs().length;i++){
            TabInfo tabInfo = getTabs()[i];
            ImageView imageView = findViewById(tabInfo.imageResId);
            imageView.setImageResource(tabInfo.normalImageDrawableId);
            TextView textView = findViewById(tabInfo.textResId);
            textView.setTextColor(getNormalStyle().textColor);
        }
    }

    private void initFragment(){
        if(fragments == null){
            fragments = new Fragment[getTabs().length];
        }
        for(int i=0;i<fragments.length;i++){
            if(fragments[i] == null){
                try{
                    Class clazz = Class.forName(getFragmentFullClassName(i));
                    fragments[i] = (Fragment) clazz.newInstance();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(getAttachedFragmentLayoutId(), fragments[i]);
            transaction.commit();
        }
    }

    private void showFragment(Fragment fragment){
        if(fragment == null){
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragments(transaction);
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();

    }

    private void hideAllFragments(FragmentTransaction transaction){
        for(int i=0;i<fragments.length;i++){
            if(fragments[i] != null){
                transaction.hide(fragments[i]);
            }
        }
    }

    private String getFragmentFullClassName(int index){
        return getFragmentPackage() + "." + getTabs()[index].fragmentName;
    }

    protected class TabInfo{

        public int layoutResId, imageResId, textResId;
        public int clickedImageDrawableId, normalImageDrawableId;
        public String fragmentName;


        public TabInfo(int layoutResId, int imageResId, int textResId, int clickedImageDrawableId, int normalImageDrawableId, String fragmentName) {
            this.layoutResId = layoutResId;
            this.imageResId = imageResId;
            this.textResId = textResId;
            this.clickedImageDrawableId = clickedImageDrawableId;
            this.normalImageDrawableId = normalImageDrawableId;
            this.fragmentName = fragmentName;
        }
    }

    protected class StyleInfo{
        public int textColor;

        public StyleInfo(int textColor) {
            this.textColor = textColor;
        }
    }

}
