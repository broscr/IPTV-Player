package com.broscr.iptvplayer.ui.activitys;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.broscr.iptvplayer.R;
import com.broscr.iptvplayer.database.IPTvRealm;
import com.broscr.iptvplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize() {
        IPTvRealm ipTvRealm = new IPTvRealm();

        binding.countTest.setText(String.format(getString(R.string.count_save_list), ipTvRealm.allChannelCount()));
    }
}