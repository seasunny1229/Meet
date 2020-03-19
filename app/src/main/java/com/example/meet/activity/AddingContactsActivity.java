package com.example.meet.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseCommonStyleActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.provider.ContentProviderUtil;
import com.example.meet.R;
import com.example.meet.view.addingcontacts.AddingContactsRecyclerViewAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 导入手机通讯录朋友信息
 *
 *
 */

public class AddingContactsActivity extends BaseCommonStyleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_contacts);

        // recycler view
        RecyclerView contactView = findViewById(R.id.mContactView);
        AddingContactsRecyclerViewAdapter addingContactsRecyclerViewAdapter = new AddingContactsRecyclerViewAdapter();
        contactView.setLayoutManager(new LinearLayoutManager(this));
        contactView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        contactView.setAdapter(addingContactsRecyclerViewAdapter);


        // add data in recycler view
        addDataInRecyclerView(addingContactsRecyclerViewAdapter);

    }

    private void addDataInRecyclerView(final AddingContactsRecyclerViewAdapter adapter){
        adapter.clear();

        // contacts
        HashMap<String, String> contactData = ContentProviderUtil.getContactData(this);

        if(contactData == null){
            return;
        }

        // service
        IUserQueryService userQueryService = getBackendService(IUserQueryService.class);

        // iterate contacts
        for(final Map.Entry<String,String> entry: contactData.entrySet()){
            userQueryService.findUsersByPhoneNumber(entry.getValue(), new BackendServiceCallback<List<User>>() {
                @Override
                public void success(List<User> iUsers) {
                    if(!iUsers.isEmpty()){
                        adapter.add(iUsers.get(0), entry.getKey());
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void fail(BackendServiceException e) {

                }
            });
        }

    }


}
