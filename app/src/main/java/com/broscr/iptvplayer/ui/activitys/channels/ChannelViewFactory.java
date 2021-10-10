package com.broscr.iptvplayer.ui.activitys.channels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChannelViewFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final String category;

    public ChannelViewFactory(@NonNull Application application, String category) {
        super(application);
        this.category = category;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChannelViewModel(category);
    }
}
