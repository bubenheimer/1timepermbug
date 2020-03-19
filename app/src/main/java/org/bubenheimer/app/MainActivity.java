package org.bubenheimer.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

public final class MainActivity extends AppCompatActivity {
    private static final int PRC = 1234;
    private static final int IRC = 4321;

    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);
        layout = findViewById(R.id.main_layout);
    }

    public void fetchLocation(View button) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            reallyFetchLocation();
        } else {
            requestPermission();
        }
    }

    public void reallyFetchLocation() {
        Snackbar.make(layout, "Getting location...", Snackbar.LENGTH_SHORT).show();
        //TODO actually get location
    }

    private void requestPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(layout, "This app needs location access to work. Please grant location permission.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reallyRequestPermission();
                }
            }).show();
        } else {
            reallyRequestPermission();
        }
    }

    private void reallyRequestPermission() {
        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PRC);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PRC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                reallyFetchLocation();
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermission();
            } else {
                Snackbar.make(layout, "Please grant location permission in app settings. The app will not work otherwise.",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                                Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(intent, IRC);
                    }
                }).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IRC) {
            fetchLocation(null);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
