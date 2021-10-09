package com.broscr.iptvplayer.ui.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.broscr.iptvplayer.database.IPTvRealm;
import com.broscr.iptvplayer.databinding.FragmentHomeBinding;
import com.broscr.iptvplayer.utils.CategoryOnClick;

import java.util.List;

import timber.log.Timber;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private FragmentHomeBinding binding;
    private CategoryOnClick categoryOnClick = category -> {

    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        initialize();

        return binding.getRoot();
    }

    private void initialize() {

        List<String> categories = new IPTvRealm().getCategories();

        Timber.d("CountCategories %s", categories.size());
    }
}