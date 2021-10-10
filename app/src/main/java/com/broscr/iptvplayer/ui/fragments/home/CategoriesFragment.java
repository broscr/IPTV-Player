package com.broscr.iptvplayer.ui.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.broscr.iptvplayer.adapters.CategoryAdapter;
import com.broscr.iptvplayer.databinding.FragmentCategoriesBinding;
import com.broscr.iptvplayer.ui.activitys.channels.ChannelActivity;
import com.broscr.iptvplayer.utils.CategoryOnClick;
import com.broscr.iptvplayer.utils.Helper;

public class CategoriesFragment extends Fragment {

    private CategoriesViewModel mViewModel;
    private FragmentCategoriesBinding binding;
    private Context context;
    private final CategoryOnClick categoryOnClick = category -> {
        Intent intent = new Intent(context, ChannelActivity.class);
        intent.putExtra(Helper.CATEGORY, category);
        context.startActivity(intent);
    };

    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        initialize();

        return binding.getRoot();
    }

    private void initialize() {

        mViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            binding.recyclerCategories.setAdapter(new CategoryAdapter(context, categories, categoryOnClick));
            binding.recyclerCategories
                    .setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding = null;
        }
    }
}