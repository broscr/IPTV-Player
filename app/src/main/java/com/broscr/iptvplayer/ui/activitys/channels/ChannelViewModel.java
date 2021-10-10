package com.broscr.iptvplayer.ui.activitys.channels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.broscr.iptvplayer.database.IPTvRealm;
import com.broscr.iptvplayer.models.Channel;

import java.util.List;

public class ChannelViewModel extends ViewModel {

    private final String category;
    private final MutableLiveData<List<Channel>> channelLiveData;

    public ChannelViewModel(String category) {
        this.category = category;
        channelLiveData = new MutableLiveData<>();
        setChannelLiveData();
    }

    private void setChannelLiveData() {
        List<Channel> channelList = new IPTvRealm().getCategoriesChannel(category);
        if (channelList != null && channelList.size() > 0) {
            channelLiveData.setValue(new IPTvRealm().getCategoriesChannel(category));
        }
    }

    public MutableLiveData<List<Channel>> getChannelLiveData() {
        return channelLiveData;
    }
}
