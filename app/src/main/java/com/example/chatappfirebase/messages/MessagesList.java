package com.example.chatappfirebase.messages;

public class MessagesList {

    private String name,mobile,lastMessage,profilePic,chatKey;

    private int unseenMessage;

    public MessagesList(String name, String mobile, String lastMessage,String profilePic, int unseenMessage,String chatKey) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.unseenMessage = unseenMessage;
        this.profilePic = profilePic;
        this.chatKey = chatKey;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessage() {
        return unseenMessage;
    }

    public String getChatKey() {
        return chatKey;
    }
}
