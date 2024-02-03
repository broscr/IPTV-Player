package com.broscr.iptvplayer.ui.fragments.about;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.broscr.iptvplayer.databinding.AboutFragmentBinding;
import com.broscr.iptvplayer.ui.activitys.fileselect.FileSelectActivity;

public class AboutFragment extends Fragment {

    private AboutViewModel mViewModel;
    private AboutFragmentBinding binding;
    private Activity activity;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        binding = AboutFragmentBinding.inflate(inflater, container, false);

        activity = getActivity();

        initialize();

        return binding.getRoot();
    }

    private void initialize() {
        binding.deleteAllBtn.setOnClickListener(v -> {
            mViewModel.deleteAllFile();
            startActivity(new Intent(activity, FileSelectActivity.class));
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