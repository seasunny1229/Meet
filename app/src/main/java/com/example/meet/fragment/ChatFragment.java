package com.example.meet.fragment;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.framework.fragment.BaseFragment;
import com.example.meet.R;
import com.google.android.material.tabs.TabLayout;

public class ChatFragment extends BaseFragment {


    private TabInfo[] tabInfos = new TabInfo[]{
            new TabInfo(R.string.text_chat_tab_title_1, new ChatHistoryFragment()),
            new TabInfo(R.string.text_chat_tab_title_3, new AllFriendFragment())
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);


        // view pager
        ViewPager viewPager = view.findViewById(R.id.mViewPager);
        viewPager.setOffscreenPageLimit(tabInfos.length);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return tabInfos[position].fragment;
            }

            @Override
            public int getCount() {
                return tabInfos.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return getString(tabInfos[position].tabResId);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                //super.destroyItem(container, position, object);
            }
        });


        // tab layout
        TabLayout tabLayout = view.findViewById(R.id.mTabLayout);

        // add tabs
        for(int i=0;i<tabInfos.length;i++){
            tabLayout.addTab(tabLayout.newTab().setText(getString(tabInfos[i].tabResId)));
        }
        tabLayout.setupWithViewPager(viewPager);
    }


    private class TabInfo{
        int tabResId;
        Fragment fragment;
        TabInfo(int tabResId, Fragment fragment){
            this.tabResId = tabResId;
            this.fragment = fragment;
        }
    }
}
