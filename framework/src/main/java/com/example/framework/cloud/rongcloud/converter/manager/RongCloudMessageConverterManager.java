package com.example.framework.cloud.rongcloud.converter.manager;

import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.converter.IMConversationInfoConverter;
import com.example.framework.backend.messaging.converter.IMInfoConverterManager;
import com.example.framework.backend.messaging.converter.IMMessageInfoConverter;
import com.example.framework.backend.messaging.handler.manager.MessageHandlerManager;
import com.example.framework.backend.messaging.info.ExtraInfo;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMMessageType;
import com.example.framework.cloud.rongcloud.converter.conversation.PrivateConversationInfoConverter;
import com.example.framework.cloud.rongcloud.converter.extra.IExtraInfoConverter;
import com.example.framework.cloud.rongcloud.converter.extra.TextMessageExtraInfoConverter;
import com.example.framework.cloud.rongcloud.converter.message.TextMessageInfoConverter;
import com.example.framework.gson.GsonUtil;

import java.util.HashMap;

import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class RongCloudMessageConverterManager extends IMInfoConverterManager<Message> {

    private HashMap<Class<?>, IExtraInfoConverter>  extraInfoConverterMap = new HashMap<>();

    public RongCloudMessageConverterManager(){
        addExtraInfoConverter(TextMessage.class, new TextMessageExtraInfoConverter());
    }

    @Override
    protected void addConversationConverters(HashMap<IMConversationType, IMConversationInfoConverter<Message>> conversationMap) {
        conversationMap.put(IMConversationType.PRIVATE, new PrivateConversationInfoConverter());
    }

    @Override
    protected void addMessageConverters(HashMap<IMMessageType, IMMessageInfoConverter<Message>> messageMap) {
        messageMap.put(IMMessageType.TEXT, new TextMessageInfoConverter());
    }

    @Override
    public void convertToRemoteMessage(IMMessage imMessage, Message message) {

        // conversation
        getConversationConverter(imMessage.getImConversationType()).convertToRemoteConversationInfo(imMessage, message);

        // content
        getMessageConverter(imMessage.getImMessageType()).convertToRemoteMessageInfo(imMessage, message);

        // target id
        message.setTargetId(imMessage.getTargetId());

        // extra
        ExtraInfo extraInfo = new ExtraInfo();
        extraInfo.setConversationTypeName(imMessage.getImConversationType().name());
        extraInfo.setMessageTypeName(imMessage.getImMessageType().name());
        extraInfo.setHandlerClassName(imMessage.getMessageReceivingHandler().getClass().getName());
        getExtraInfoConverter(message.getContent().getClass()).setExtraInfo(message.getContent(), GsonUtil.toJson(extraInfo));
    }

    @Override
    public void convertToLocalMessage(IMMessage imMessage, Message message) {


        // parse extra
        ExtraInfo extraInfo = GsonUtil.fromJson(getExtraInfoConverter(message.getContent().getClass()).getExtraInfo(message.getContent()), ExtraInfo.class);
        IMConversationType imConversationType = Enum.valueOf(IMConversationType.class, extraInfo.getConversationTypeName());
        IMMessageType imMessageType = Enum.valueOf(IMMessageType.class, extraInfo.getMessageTypeName());
        Class clazz = null;
        try {
             clazz = Class.forName(extraInfo.getHandlerClassName());
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        // conversation
        getConversationConverter(imConversationType).convertToLocalConversationInfo(imMessage, message);

        // content
        getMessageConverter(imMessageType).convertToLocalMessageInfo(imMessage, message);


        // target id
        imMessage.setTargetId(UserManager.getInstance().getUser().getUid());


        // source id
        imMessage.setSourceId(message.getSenderUserId());

        // handler
        imMessage.setMessageReceivingHandler(MessageHandlerManager.getInstance().get(clazz));
    }

    private void addExtraInfoConverter(Class<?> clazz, IExtraInfoConverter converter){
        if(extraInfoConverterMap.containsKey(clazz)){
            return;
        }

        extraInfoConverterMap.put(clazz, converter);
    }

    private IExtraInfoConverter getExtraInfoConverter(Class<?> clazz){
        if(!extraInfoConverterMap.containsKey(clazz)){
            throw new IllegalArgumentException("cannot find key in the extra info converter map");
        }
        return extraInfoConverterMap.get(clazz);
    }
}
