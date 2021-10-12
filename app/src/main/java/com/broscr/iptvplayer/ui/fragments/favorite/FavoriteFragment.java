package com.broscr.iptvplayer.ui.fragments.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.broscr.iptvplayer.adapters.ChannelListAdapter;
import com.broscr.iptvplayer.databinding.FavoriteFragmentBinding;
import com.broscr.iptvplayer.ui.activitys.player.PlayerActivity;
import com.broscr.iptvplayer.utils.ChannelOnClick;
import com.broscr.iptvplayer.utils.Helper;

public class FavoriteFragment extends Fragment {

    private FavoriteViewModel mViewModel;
    private FavoriteFragmentBinding binding;
    private Context context;
    private ChannelListAdapter adapter;
    private final ChannelOnClick channelOnClick = channel -> {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(Helper.CHANNEL, channel);
        startActivity(intent);
    };

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        binding = FavoriteFragmentBinding.inflate(inflater, container, false);

        initialize();

        return binding.getRoot();
    }

    private void initialize() {
        mViewModel.getFavoriteLiveData().observe(getViewLifecycleOwner(), channels -> {
            adapter = new ChannelListAdapter(context, channels, channelOnClick);
            binding.favoriteRecycler.setAdapter(adapter);
            binding.favoriteRecycler.setLayoutManager(new LinearLayoutManager(context));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding = null;
        }
    }
}