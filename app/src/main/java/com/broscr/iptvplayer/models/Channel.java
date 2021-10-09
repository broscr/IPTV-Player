package com.broscr.iptvplayer.models;

import androidx.annotation.NonNull;

import io.realm.RealmObject;

public class Channel extends RealmObject {

    private String channelName;
    private String channelUrl;
    private String channelImg;
    private String channelGroup;

    public Channel() {
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getChannelImg() {
        return channelImg;
    }

    public void setChannelImg(String channelImg) {
        this.channelImg = channelImg;
    }

    public String getChannelGroup() {
        return channelGroup;
    }

    public void setChannelGroup(String channelGroup) {
        this.channelGroup = channelGroup;
    }

    @NonNull
    @Override
    public String toString() {
        return "Channel{" +
                ", channelName='" + channelName + '\'' +
                ", channelUrl='" + channelUrl + '\'' +
                ", channelImg='" + channelImg + '\'' +
                ", channelGroup='" + channelGroup + '\'' +
                '}';
    }
}
