package com.broscr.iptvplayer.ui.activitys.player;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.broscr.iptvplayer.R;
import com.broscr.iptvplayer.database.IPTvRealm;
import com.broscr.iptvplayer.databinding.ActivityPlayerBinding;
import com.broscr.iptvplayer.models.Channel;
import com.broscr.iptvplayer.ui.fragments.favorite.FavoriteViewModel;
import com.broscr.iptvplayer.utils.Helper;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, Player.Listener {

    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    protected PlayerView playerView;
    protected @Nullable
    SimpleExoPlayer player;
    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;
    private ActivityPlayerBinding binding;
    private Channel channel;
    private List<Channel> channelList;
    private List<MediaItem> mediaItems;
    private FrameLayout favoriteLayout;
    private ImageView favoriteBtn;
    private IPTvRealm ipTvRealm;
    private TextView titleText;
    private FavoriteViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize(savedInstanceState);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        clearStartPosition();
        setIntent(intent);
    }

    private void initialize(Bundle savedInstanceState) {
        ipTvRealm = new IPTvRealm();
        playerView = binding.playerView;
        favoriteBtn = playerView.findViewById(R.id.exo_favorite_icon);
        favoriteLayout = playerView.findViewById(R.id.exo_favorite_button);
        titleText = playerView.findViewById(R.id.exo_item_title);
        favoriteLayout.setOnClickListener(this);

        if (savedInstanceState != null) {
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            clearStartPosition();
        }

    }

    private void initializePlayer() {

        if (player == null) {

            Intent intent = getIntent();

            mediaItems = createMediaItems(intent);
            player =
                    new SimpleExoPlayer.Builder(this)
                            .build();
            player.setPlayWhenReady(startAutoPlay);
            playerView.setPlayer(player);
        }

        boolean haveStartPosition = startWindow != C.INDEX_UNSET;

        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition);
        }

        player.addListener(this);
        player.setMediaItems(mediaItems, !haveStartPosition);
        player.seekTo(channelPosition(), C.INDEX_UNSET);
        player.prepare();
        player.setPlayWhenReady(true);

    }

    private void checkFavorite(String name) {
        if (ipTvRealm.isFavorite(name)) {
            favoriteBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite, null));
        } else {
            favoriteBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_null, null));
        }
    }

    protected void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            player.release();
            player = null;
            mediaItems = Collections.emptyList();
        }
    }

    private void updateStartPosition() {
        if (player != null) {
            startAutoPlay = player.getPlayWhenReady();
            startWindow = player.getCurrentWindowIndex();
            startPosition = Math.max(0, player.getContentPosition());
        }
    }

    protected void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        if (mediaItem != null) {
            assert mediaItem.mediaMetadata.title != null;
            titleText.setText(mediaItem.mediaMetadata.title.toString());
            checkFavorite(mediaItem.mediaMetadata.title.toString());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    private List<MediaItem> createMediaItems(Intent intent) {
        channel = intent.getParcelableExtra(Helper.CHANNEL);
        channelList = new IPTvRealm().getCategoriesChannel(channel.getChannelGroup());
        List<MediaItem> mediaItems = new ArrayList<>();

        for (Channel ch : channelList) {
            mediaItems.add(setMediaItem(ch));
        }
        return mediaItems;
    }

    private int channelPosition() {
        return IntStream.range(0, channelList.size()).filter(i -> channelList.get(i).getChannelName()
                .equals(channel.getChannelName())).findFirst().getAsInt();
    }

    private MediaItem setMediaItem(Channel ch) {
        return new MediaItem.Builder()
                .setUri(ch.getChannelUrl())
                .setTag(ch.getChannelName())
                .setMediaMetadata(new MediaMetadata.Builder()
                        .setTitle(ch.getChannelName())
                        .setMediaUri(Uri.parse(ch.getChannelUrl()))
                        .setArtworkUri(Uri.parse(ch.getChannelImg()))
                        .setArtist(ch.getChannelGroup()).build())
                .build();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUi();
        }
    }

    private void hideSystemUi() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == favoriteLayout.getId() && player != null && player.getCurrentMediaItem() != null) {
            Channel channel = channelList.stream().filter(i -> i.getChannelName()
                    .equals(player.getCurrentMediaItem().mediaMetadata.title.toString())).findFirst().get();
            if (ipTvRealm.isFavorite(channel.getChannelName())) {
                ipTvRealm.deleteFavorite(channel);
                favoriteBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_favorite_null, null));
            } else {
                ipTvRealm.setFavorite(channel);
                favoriteBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_favorite, null));
            }

            favoriteViewModel.updateFavorite();
        }
    }
}
