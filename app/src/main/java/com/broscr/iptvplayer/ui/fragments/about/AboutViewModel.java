package com.broscr.iptvplayer.ui.fragments.about;

import androidx.lifecycle.ViewModel;

import com.broscr.iptvplayer.database.IPTvRealm;

public class AboutViewModel extends ViewModel {

    private IPTvRealm ipTvRealm;

    public AboutViewModel() {
        ipTvRealm = new IPTvRealm();
    }

    public boolean deleteAllFile() {
        ipTvRealm.deleteAllList();
        return true;
    }
}