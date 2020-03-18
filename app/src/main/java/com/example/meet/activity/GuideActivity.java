package com.example.meet.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.framework.activity.BaseFullScreenStyleActivity;
import com.example.framework.media.ManagedMediaPlayer;
import com.example.framework.view.animation.AnimationFactory;
import com.example.framework.view.adapter.BasePagerAdapter;
import com.example.meet.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseFullScreenStyleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 实现手滑动切换图片的效果 （view pager）
        ViewPager viewPager = (ViewPager) findViewById(R.id.mViewPager);
        List<View> pageViews = new ArrayList<>();
        pageViews.add(View.inflate(this, R.layout.layout_pager_guide_1, null));
        pageViews.add(View.inflate(this, R.layout.layout_pager_guide_2, null));
        pageViews.add(View.inflate(this, R.layout.layout_pager_guide_3, null));
        viewPager.setOffscreenPageLimit(pageViews.size());
        viewPager.setAdapter(new BasePagerAdapter(pageViews));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateGuidePoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 播放帧动画
        ((AnimationDrawable)pageViews.get(0).findViewById(R.id.iv_guide_star).getBackground()).start();
        ((AnimationDrawable)pageViews.get(1).findViewById(R.id.iv_guide_night).getBackground()).start();
        ((AnimationDrawable)pageViews.get(2).findViewById(R.id.iv_guide_smile).getBackground()).start();

        // 播放音乐（多媒体播放器）
        final ManagedMediaPlayer mediaPlayer = new ManagedMediaPlayer();
        mediaPlayer.setLooping(true);
        AssetFileDescriptor assetFileDescriptor = getResources().openRawResourceFd(R.raw.guide);
        mediaPlayer.start(assetFileDescriptor);

        // 播放音乐图标的旋转动画
        final ObjectAnimator buttonRotationAnimator = AnimationFactory.rotation(findViewById(R.id.iv_music_switch));
        buttonRotationAnimator.start();

        // 设置音乐播放控制按键
        final ImageView musicButtonView = (ImageView) findViewById(R.id.iv_music_switch);
        musicButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    buttonRotationAnimator.pause();
                    musicButtonView.setImageResource(R.drawable.img_guide_music_off);

                }
                else {
                    mediaPlayer.resume();
                    buttonRotationAnimator.resume();
                    musicButtonView.setImageResource(R.drawable.img_guide_music);
                }
            }
        });

        // 设置跳转到主页按键
        TextView skipButtonView = findViewById(R.id.tv_guide_skip);
        skipButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // change to login activity
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                mediaPlayer.stop();
                finish();
            }
        });

    }

    private void updateGuidePoint(int position){
        ImageView iv_guide_point_1 = (ImageView) findViewById(R.id.iv_guide_point_1);
        ImageView iv_guide_point_2 = (ImageView) findViewById(R.id.iv_guide_point_2);
        ImageView iv_guide_point_3 = (ImageView) findViewById(R.id.iv_guide_point_3);

        switch (position){
            case 0:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point_p);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point);
                break;
            case 1:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point_p);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point);
                break;
            case 2:
                iv_guide_point_1.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_2.setImageResource(R.drawable.img_guide_point);
                iv_guide_point_3.setImageResource(R.drawable.img_guide_point_p);
                break;
        }
    }


}
