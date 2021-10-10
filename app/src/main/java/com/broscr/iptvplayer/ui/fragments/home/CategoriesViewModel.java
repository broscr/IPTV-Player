package com.broscr.iptvplayer.ui.fragments.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.broscr.iptvplayer.database.IPTvRealm;

import java.util.List;

public class CategoriesViewModel extends ViewModel {

    private final MutableLiveData<List<String>> categoriesLiveData;

    public CategoriesViewModel() {
        this.categoriesLiveData = new MutableLiveData<>();
        setCategoriesLiveData();
    }

    private void setCategoriesLiveData() {
        IPTvRealm ipTvRealm = new IPTvRealm();
        if (ipTvRealm.getCategories() != null && ipTvRealm.getCategories().size() > 0) {
            categoriesLiveData.setValue(ipTvRealm.getCategories());
        }
    }

    public MutableLiveData<List<String>> getCategoriesLiveData() {
        return categoriesLiveData;
    }
}
