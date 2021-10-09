package com.broscr.iptvplayer.ui.activitys;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.broscr.iptvplayer.R;
import com.broscr.iptvplayer.databinding.ActivityMainBinding;
import com.broscr.iptvplayer.ui.fragments.about.AboutFragment;
import com.broscr.iptvplayer.ui.fragments.favorite.FavoriteFragment;
import com.broscr.iptvplayer.ui.fragments.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize() {
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        openFragment(HomeFragment.newInstance());
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.bottomFrameLayout.getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mainMenu) {
            openFragment(HomeFragment.newInstance());
            return true;
        } else if (item.getItemId() == R.id.favoriteMenu) {
            openFragment(FavoriteFragment.newInstance());
            return true;
        } else if (item.getItemId() == R.id.aboutMenu) {
            openFragment(AboutFragment.newInstance());
            return true;
        }

        return false;
    }
}