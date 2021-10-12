package com.broscr.iptvplayer.ui.fragments.favorite;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.broscr.iptvplayer.database.IPTvRealm;
import com.broscr.iptvplayer.models.Channel;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private final MutableLiveData<List<Channel>> listFavoriteLiveData;
    private final IPTvRealm ipTvRealm;

    public FavoriteViewModel() {
        this.listFavoriteLiveData = new MutableLiveData<>();
        ipTvRealm = new IPTvRealm();
        setLiveData();
    }

    private void setLiveData() {
        List<Channel> channelList = ipTvRealm.getFavoriteList();
        if (channelList != null) {
            listFavoriteLiveData.setValue(channelList);
        }
    }

    public MutableLiveData<List<Channel>> getFavoriteLiveData() {
        return listFavoriteLiveData;
    }

    public void updateFavorite() {
        List<Channel> channelList = ipTvRealm.getFavoriteList();
        if (channelList != null) {
            listFavoriteLiveData.setValue(channelList);
        }
    }

}