package com.example.chatappfirebase.messages;

public class MessagesList {

    private String name,mobile,lastMessage,profilePic;

    private int unseenMessage;

    public MessagesList(String name, String mobile, String lastMessage,String profilePic, int unseenMessage) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.unseenMessage = unseenMessage;
        this.profilePic = profilePic;
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
}
