package com.example.framework.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.application.BaseApplication;
import com.example.framework.backend.application.IBackendService;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.eventbus.MessageEvent;
import com.example.framework.persistent.BaseSharedPreferenceConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    //申请运行时权限的Code
    private static final int PERMISSION_REQUEST_CODE = 1000;

    //申请窗口权限的Code
    private static final int PERMISSION_WINDOW_REQUEST_CODE = 1001;

    // region system UI
    protected void fixSystemUI(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            // 获取最顶层的view
            // 全屏+显示状态栏
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );

            // 设置顶部状态栏透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected void showBackButton(){

        //显示返回键
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //清除阴影
            getSupportActionBar().setElevation(0);
        }
    }

    // endregion

    // region permissions

    // Android6.0 以后版本的动态权限申请
    protected final void requestPermissions(String[] permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            // 检查当前权限
            List<String> deniedPermissions = new ArrayList<>();
            checkPermissions(permissions, deniedPermissions);

            // 申请权限
            if(!deniedPermissions.isEmpty()){
                requestPermissions(deniedPermissions.toArray(new String[]{}), PERMISSION_REQUEST_CODE);
            }
        }
    }

    // 申请权限后的结果
    protected final boolean isAllPermissionsGranted(int requestCode, String[] permissions, int[] grantResults, List<String> stillDeniedPermissions){
        boolean isAllPermissionsGranted = true;
        if(requestCode == PERMISSION_REQUEST_CODE){
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    isAllPermissionsGranted = false;
                    if(stillDeniedPermissions != null) {
                        stillDeniedPermissions.add(permissions[i]);
                    }
                }
            }
        }
        return isAllPermissionsGranted;
    }


    // 检查权限状态
    private void checkPermissions(String[] permissions, List<String> deniedPermissions){
        for(String permission : permissions){
            if(!isPermissionGranted(permission)){
                deniedPermissions.add(permission);
            }
        }
    }

    // 检查某个权限
    protected final boolean isPermissionGranted(String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int permissionState = checkSelfPermission(permission);
            return permissionState == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    // 请求窗口权限
    protected final void requestWindowPermission(){
        if(!isWindowPermissionGranted()){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, PERMISSION_WINDOW_REQUEST_CODE);
        }
    }

    protected final boolean isWindowPermissionGrantedAfterRequest(int requestCode){
        if(requestCode == PERMISSION_WINDOW_REQUEST_CODE){
            return isWindowPermissionGranted();
        }
        return false;
    }


    // 检查窗口权限
    private boolean isWindowPermissionGranted(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return Settings.canDrawOverlays(this);
        }
        return true;
    }


    // endregion

    // region shared preference
    public void putInt(String key, int value){
        SharedPreferences sharedPreferences = getDefaultSharedPreference();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue){
        SharedPreferences sharedPreferences = getDefaultSharedPreference();
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putString(String key, String value){
        SharedPreferences sharedPreferences = getDefaultSharedPreference();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue){
        SharedPreferences sharedPreferences = getDefaultSharedPreference();
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putBoolean(String key, boolean value){
        SharedPreferences sharedPreferences = getDefaultSharedPreference();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue){
        SharedPreferences sharedPreferences = getDefaultSharedPreference();
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void deleteKey(String key){
        SharedPreferences sharedPreferences = getDefaultSharedPreference();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    protected SharedPreferences getDefaultSharedPreference(){
        return  ((BaseApplication)getApplication()).getDefaultSharedPreference();
    }
    // endregion

    // region backend service
    public final <T> T getBackendService(Class<T> clazz){
        if(getApplication() instanceof IBackendService){
            return ((IBackendService) getApplication()).getBackendService(clazz);
        }
        throw new BackendServiceException("Backend service is not accessible");
    }

    // endregion
}
