package com.broscr.iptvplayer.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.broscr.iptvplayer.R;
import com.broscr.iptvplayer.databinding.ActivityFirstBinding;
import com.broscr.iptvplayer.filereader.FileReader;
import com.broscr.iptvplayer.utils.Helper;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

public class FirstActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks, View.OnClickListener {

    private static final int IPTV_READ_DOC_PERM = 123;
    private ActivityFirstBinding binding;
    private ActivityResultLauncher<String> selectFileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize() {
        binding.selectBtn.setOnClickListener(this);
        selectFileLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        Timber.e("Result %s", result);
                        //Result uri file
                        new FileReader(FirstActivity.this, result).readFile();
                    } else {
                        Timber.e("Nothing Select");
                    }
                });
    }


    private boolean hasReadFilePermission() {
        return EasyPermissions.hasPermissions(FirstActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @AfterPermissionGranted(IPTV_READ_DOC_PERM)
    public void readFileTask() {
        if (hasReadFilePermission()) {
            //File read permission success
            selectFileLauncher.launch(Helper.FILE_MIME_TYPE);
        } else {
            EasyPermissions.requestPermissions(
                    FirstActivity.this,
                    getString(R.string.rationale_read_file),
                    IPTV_READ_DOC_PERM,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

            Timber.e(getString(R.string.returned_from_app_settings_to_activity,
                    hasReadFilePermission() ? getString(R.string.yes) : getString(R.string.no)));
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Timber.d("onPermissionsDenied:" + requestCode + ":" + perms.size());

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Timber.d("onRationaleAccepted:%s", requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Timber.d("onRationaleDenied:%s", requestCode);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.selectBtn.getId()) {
            readFileTask();
        }
    }
}