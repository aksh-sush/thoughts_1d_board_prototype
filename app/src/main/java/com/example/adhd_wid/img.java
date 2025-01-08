package com.example.adhd_wid;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

public class img {

    private final Context context;
    private final ActivityResultLauncher<String[]> requestPermissions;

    public img(Context context) {
        this.context = context;

        // Initialize the requestPermissions launcher
        requestPermissions = ((AppCompatActivity) context).registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    if (result != null && result.containsValue(true)) {
                        accessGallery();
                    } else {
                        Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Request gallery permissions based on Android version
    public void requestGalleryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(new String[]{
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO,
                    android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(new String[]{
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO
            });
        } else {
            requestPermissions.launch(new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            });
        }
    }

    // Access the gallery after permissions are granted
    private void accessGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_IMAGES) == PermissionChecker.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_VIDEO) == PermissionChecker.PERMISSION_GRANTED) {
                loadGallery();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PermissionChecker.PERMISSION_GRANTED) {
                loadGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
                loadGallery();
            }
        }
    }

    // Load the gallery media (both images and videos)
    private void loadGallery() {
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
        };

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"; // Sort by most recent
        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    long dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));

                    // Do something with the media item, e.g., add it to an ImageView or list
                }
            }
        }
    }
}
