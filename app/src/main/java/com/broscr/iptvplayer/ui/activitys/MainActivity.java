package com.broscr.iptvplayer.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.broscr.iptvplayer.R;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    private static final int IPTV_READ_DOC_PERM = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private boolean hasReadFilePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @AfterPermissionGranted(IPTV_READ_DOC_PERM)
    public void readFileTask() {
        if (hasReadFilePermission()) {
            Toast.makeText(this, getString(R.string.file_permission_success), Toast.LENGTH_LONG).show();
        } else {
            EasyPermissions.requestPermissions(
                    this,
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
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasReadFilePermission() ? yes : no),
                    Toast.LENGTH_LONG).show();
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
}