package com.example.framework.cloud.rongcloud.converter.filter;

import com.example.framework.backend.messaging.message.IMMessage;

import java.util.ArrayList;
import java.util.List;

public class IMMessageFilterManager {


    private List<IMMessageFilter> filters = new ArrayList<>();


    public IMMessageFilterManager add(IMMessageFilter filter){
        filters.add(filter);
        return this;
    }

    public boolean isValid(IMMessage imMessage){
        for(IMMessageFilter filter : filters){
            if(!filter.isValid(imMessage)){
                return false;
            }
        }
        return true;
    }

}
