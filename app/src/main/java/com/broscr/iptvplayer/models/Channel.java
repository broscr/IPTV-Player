package com.broscr.iptvplayer.models;

import androidx.annotation.NonNull;

public class Channel {

    private String channelName;
    private String channelUrl;
    private String channelImg;


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

    @NonNull
    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelUrl='" + channelUrl + '\'' +
                ", channelImg='" + channelImg + '\'' +
                '}';
    }
}
