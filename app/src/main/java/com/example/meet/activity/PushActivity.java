package com.example.meet.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.framework.activity.BaseCommonStyleActivity;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.pushing.constant.PushType;
import com.example.framework.backend.service.IPushService;
import com.example.framework.exception.ExceptionHandler;
import com.example.meet.R;

public class PushActivity extends BaseCommonStyleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_square);

        // edit text
        ((EditText) findViewById(R.id.et_content)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((TextView) findViewById(R.id.tv_content_size)).setText( s.length() + "/140");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_input){
            publish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void publish(){

        // service
        IPushService pushService = getBackendService(IPushService.class);

        // backend callback
        BackendServiceCallback<Void> backendServiceCallback = new BackendServiceCallback<Void>() {
            @Override
            public void success(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(PushActivity.this, e);
            }
        };

        // text
        String text = ((EditText) findViewById(R.id.et_content)).getText().toString().trim();
        if(!TextUtils.isEmpty(text)){
            pushService.push(PushType.TEXT, text, null, backendServiceCallback);
        }
    }

}
