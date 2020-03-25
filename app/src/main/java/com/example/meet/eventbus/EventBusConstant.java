package com.example.meet.eventbus;

public class EventBusConstant {

    // post : MeInfoActivity when personal info is changed by user
    // subscribe: MeFragment to update and display latest changed personal info
    public static final int UPDATE_PERSONAL_INFO = 1001;

    // post: ChatHandler when new messages are received
    // subscribe: ChatActivity to update latest messages in private chat conversation
    // subscribe: ChatHistoryFragment to update latest messages in conversation list
    public static final int UPDATE_CHAT_INFO = 1002;

    // post: ChatActivity to clear unread messages
    // subscribe: ChatHistoryFragment to show empty unread messages
    public static final int CLEAR_UNREAD_MESSAGES = 1003;
}
