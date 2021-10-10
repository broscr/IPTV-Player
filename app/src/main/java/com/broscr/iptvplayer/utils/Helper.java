package com.broscr.iptvplayer.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.broscr.iptvplayer.R;

import java.util.Objects;

public class Helper {

    public static final String FILE_MIME_TYPE = "audio/x-mpegurl";
    public static final String CATEGORY = "category";

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void getToolbarStyle(AppCompatActivity activity, Toolbar toolbar, String title) {
        activity.setSupportActionBar(toolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(title);
        final Drawable upArrow = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_arrow_back, null);
        activity.getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }
}
