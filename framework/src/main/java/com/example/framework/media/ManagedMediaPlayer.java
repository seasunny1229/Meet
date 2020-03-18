package com.example.framework.media;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import com.example.framework.util.LogUtil;

import java.io.IOException;

public class ManagedMediaPlayer {


    // media player's playing status
    private static final int MEDIA_STATUS_PLAY = 0;
    private static final int MEDIA_STATUS_PAUSE = 1;
    private static final int MEDIA_STATUS_STOP = 2;

    // handler message what
    private static final int HANDLER_MESSAGE_PROGRESS = 1000;

    // android media player
    private MediaPlayer mMediaPlayer;

    // media status
    private int mMediaStatus = MEDIA_STATUS_STOP;

    // progress listener
    private OnMusicProgressListener progressListener;

    // handler
    private Handler mHandler;


    public ManagedMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case HANDLER_MESSAGE_PROGRESS:
                        if(progressListener != null) {

                            // get current position
                            int currentPosition = mMediaPlayer.getCurrentPosition();
                            int pos = (int) ((float) (currentPosition / getDuration())) * 100;

                            // music progress listener
                            progressListener.onProgress(currentPosition, pos);

                            // 在1000ms之后发送空的消息
                            mHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_PROGRESS, 1000);
                        }
                        break;
                }

                return false;
            }
        });
    }

    // region lifecycle
    public void start(AssetFileDescriptor assetFileDescriptor){

        try {

            // create media player
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            // update status
            mMediaStatus = MEDIA_STATUS_PLAY;

            // send empty message
            mHandler.sendEmptyMessage(HANDLER_MESSAGE_PROGRESS);


        }catch (IOException e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
        }
    }

    public void start(String path){

        try {

            // create media player
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            // update status
            mMediaStatus = MEDIA_STATUS_PLAY;

            // send empty message
            mHandler.sendEmptyMessage(HANDLER_MESSAGE_PROGRESS);

        }catch (IOException e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
        }
    }

    public void pause(){
        if(isPlaying()){
            mMediaPlayer.pause();

            // update status
            mMediaStatus = MEDIA_STATUS_PAUSE;

            // remove message from handler
            removeMessageFromHandler();
        }
    }

    public void resume(){
        mMediaPlayer.start();

        // update status
        mMediaStatus = MEDIA_STATUS_PLAY;

        // send empty message into handler
        mHandler.sendEmptyMessage(HANDLER_MESSAGE_PROGRESS);

    }

    public void stop(){
        mMediaPlayer.stop();

        // update status
        mMediaStatus = MEDIA_STATUS_PLAY;

        // remove message from handler
        removeMessageFromHandler();
    }
    // endregion

    // region flag
    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    // endregion

    // region parameter
    public int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }

    public int getDuration(){
        return mMediaPlayer.getDuration();
    }

    public void setLooping(boolean isLooping){
         mMediaPlayer.setLooping(isLooping);
    }

    public void seekTo(int ms){
        mMediaPlayer.seekTo(ms);
    }
    // endregion

    // region listener
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        mMediaPlayer.setOnCompletionListener(listener);
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener listener){
        mMediaPlayer.setOnErrorListener(listener);
    }

    public void setOnProgressListener(OnMusicProgressListener listener){
        this.progressListener = listener;
    }
    // endregion

    // region handler
    private void removeMessageFromHandler(){
        if(mHandler != null){
            mHandler.removeMessages(HANDLER_MESSAGE_PROGRESS);
        }
    }

    // endregion


    public interface OnMusicProgressListener{
        void onProgress(int progress, int pos);
    }

}
