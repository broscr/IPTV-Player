package com.broscr.iptvplayer.ui.activitys.player;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.broscr.iptvplayer.database.IPTvRealm;
import com.broscr.iptvplayer.databinding.ActivityPlayerBinding;
import com.broscr.iptvplayer.models.Channel;
import com.broscr.iptvplayer.utils.Helper;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    protected StyledPlayerView playerView;
    protected @Nullable
    SimpleExoPlayer player;
    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;
    private ActivityPlayerBinding binding;
    private Channel channel;
    private List<Channel> channelList;
    private List<MediaItem> mediaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        playerView = binding.playerView;

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

        player.setMediaItems(mediaItems, !haveStartPosition);
        player.prepare();
        player.setPlayWhenReady(true);
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
    }

    private List<MediaItem> createMediaItems(Intent intent) {
        channel = intent.getParcelableExtra(Helper.CHANNEL);
        channelList = new IPTvRealm().getCategoriesChannel(channel.getChannelGroup());
        List<MediaItem> mediaItems = new ArrayList<>();

//        mediaItems.add(setMediaItem(channel));

        for (Channel ch : channelList) {
            mediaItems.add(setMediaItem(ch));
        }
        return mediaItems;
    }

    private MediaItem setMediaItem(Channel ch) {
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(ch.getChannelUrl())
                .setTag(ch.getChannelName())
                .setMediaMetadata(new MediaMetadata.Builder()
                        .setTitle(ch.getChannelName())
                        .setMediaUri(Uri.parse(ch.getChannelUrl()))
                        .setArtworkUri(Uri.parse(ch.getChannelImg()))
                        .setArtist(ch.getChannelGroup()).build())
                .build();
        return mediaItem;
    }

}