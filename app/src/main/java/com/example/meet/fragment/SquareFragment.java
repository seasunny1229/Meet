package com.example.meet.fragment;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.framework.fragment.BaseFragment;
import com.example.meet.R;

public class SquareFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_square;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);

        // swipe refresh layout
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.mSquareSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });
    }

    private void updateData(){



    }

}
