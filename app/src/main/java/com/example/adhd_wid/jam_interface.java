package com.example.adhd_wid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.Manifest;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexWrap;

import java.util.Random;

public class jam_interface extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private int buttonCount = 0; // Track the number of buttons added

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for picking an image
    private ImageView currentImageView; // Reference to the currently clicked ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jam_board);

        Button smallButton = findViewById(R.id.button);

        // Get reference to the FlexboxLayout
        flexboxLayout = findViewById(R.id.flexbox);

        // Set FlexboxLayout properties
        flexboxLayout.setFlexWrap(FlexWrap.WRAP); // Wrap buttons when space runs out

        // Set OnClickListener for the small button to add new buttons
        smallButton.setOnClickListener(v -> showPopup());
    }

    private void showPopup() {
        // Show dialog to choose between adding a button or an image
        new AlertDialog.Builder(this)
                .setTitle("Choose Action")
                .setMessage("Do you want to add a Button or an Image?")
                .setPositiveButton("Button", (dialog, which) -> addDynamicButton())
                .setNegativeButton("Image", (dialog, which) -> openGallery())
                .show();
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Set the type to image
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // Start the gallery activity
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData(); // Get the image URI
            if (imageUri != null) {
                addImageToFlexbox(imageUri); // Add the selected image to the FlexboxLayout
            }
        }
    }
    private void addImageToFlexbox(Uri imageUri) {
        // Create a new RelativeLayout to hold the image
        RelativeLayout imageLayout = new RelativeLayout(this);
        imageLayout.setBackgroundColor(Color.DKGRAY); // Set dark grey background

        // Create a new ImageView
        ImageView newImageView = new ImageView(this);
        newImageView.setImageURI(imageUri); // Set the selected image
        newImageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // Crop and center the image

        // Set a unique ID for the ImageView
        newImageView.setId(View.generateViewId());

        // Measure image size (similar logic to button sizing)
        Random random = new Random();
        int minButtonSize = 150; // Minimum size for the button (adjustable)
        int maxButtonSize = minButtonSize * 4;
        int imageSize = random.nextInt(maxButtonSize - minButtonSize + 1) + minButtonSize;

        // Set LayoutParams for the ImageView
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(imageSize, imageSize);
        imageParams.setMargins(10, 10, 10, 10); // Add margins for spacing
        newImageView.setLayoutParams(imageParams);

        // Add the ImageView to the RelativeLayout
        imageLayout.addView(newImageView);

        // Set LayoutParams for the RelativeLayout
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Positioning logic (similar to buttons)
        if (buttonCount == 0) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (buttonCount % 2 == 0) {
            layoutParams.addRule(RelativeLayout.RIGHT_OF, flexboxLayout.getChildAt(buttonCount - 1).getId());
        } else {
            layoutParams.addRule(RelativeLayout.BELOW, flexboxLayout.getChildAt(buttonCount - 1).getId());
        }

        // Set the layout parameters for the image layout
        imageLayout.setLayoutParams(layoutParams);

        // Add the RelativeLayout (containing the ImageView) to the FlexboxLayout
        flexboxLayout.addView(imageLayout);

        // Set OnClickListener for the new ImageView to handle scaling
        newImageView.setOnClickListener(v -> {
            // Increment the counter for this ImageView
            int currentCount = (int) (newImageView.getTag() == null ? 0 : newImageView.getTag());
            currentCount++;
            newImageView.setTag(currentCount); // Store the updated count as a tag

            // Calculate the new scale factor
            float scaleFactor = 1 + (currentCount * 0.05f); // Scale by 5% for each tap

            // Apply the scaling transformation to the ImageView
            newImageView.setScaleX(scaleFactor);
            newImageView.setScaleY(scaleFactor);
        });

        buttonCount++;
    }







    private void addDynamicButton() {
        // Create a new RelativeLayout to hold the button
        RelativeLayout buttonLayout = new RelativeLayout(this);
        buttonLayout.setBackgroundColor(Color.DKGRAY); // Set dark grey background

        // Create a new button
        Button newButton = new Button(this);
        newButton.setText("Cue");                          // Set button text
        newButton.setBackgroundColor(Color.BLUE);          // Set button background color
        newButton.setTextColor(Color.WHITE);               // Set text color
        newButton.setPadding(0, 0, 0, 0);                  // Remove padding for clean appearance

        // Set a unique ID for the new button
        newButton.setId(View.generateViewId());

        // Measure button size
        newButton.measure(0, 0);
        int minButtonSize = (newButton.getMeasuredWidth() + newButton.getMeasuredHeight()) / 4;
        int maxButtonSize = minButtonSize * 4;

        // Randomize button size within the range
        Random random = new Random();
        int buttonSize = random.nextInt(maxButtonSize - minButtonSize +  1) + minButtonSize;

        // Set LayoutParams for the button
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(buttonSize, buttonSize);
        buttonParams.setMargins(10, 10, 10, 10); // Add margins for spacing
        newButton.setLayoutParams(buttonParams);

        // Add the button to the RelativeLayout
        buttonLayout.addView(newButton);

        // Set LayoutParams for the RelativeLayout
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Positioning logic
        if (buttonCount == 0) {
            // First button, no specific positioning needed
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (buttonCount % 2 == 0) {
            // Even button count: Add to the right of the last button
            layoutParams.addRule(RelativeLayout.RIGHT_OF, flexboxLayout.getChildAt(buttonCount - 1).getId());
        } else {
            // Odd button count: Add below the last button
            layoutParams.addRule(RelativeLayout.BELOW, flexboxLayout.getChildAt(buttonCount - 1).getId());
        }

        // Set the layout parameters for the button layout
        buttonLayout.setLayoutParams(layoutParams);

        // Add the RelativeLayout (containing the button) to the FlexboxLayout
        flexboxLayout.addView(buttonLayout);

        // Set OnClickListener for the new button to increment counter and resize
        newButton.setOnClickListener(v -> {
            // Increment the counter for this button
            int currentCount = (int) (newButton.getTag() == null ? 0 : newButton.getTag());
            currentCount++;
            newButton.setTag(currentCount); // Store the updated count as a tag

            // Calculate the new scale factor
            float scaleFactor = 1 + (currentCount * 0.05f); // Scale by 5% for each tap

            // Apply the scaling transformation to the button
            newButton.setScaleX(scaleFactor);
            newButton.setScaleY(scaleFactor);
        });

        buttonCount++;
    }
}