package com.example.adhd_wid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Random;

public class jam_interface extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private boolean lastButtonAddedBelow = false;  // Track if the last button was added below or to the right

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jam_board);

        flexboxLayout = findViewById(R.id.flexbox);

        // Set FlexboxLayout properties to ensure buttons align properly
        flexboxLayout.setFlexDirection(FlexDirection.ROW);  // Align buttons in a row
        flexboxLayout.setFlexWrap(FlexWrap.WRAP);  // Wrap buttons when space runs out

        // Get the existing small button from XML
        Button smallButton = findViewById(R.id.button);

        // Set an OnClickListener for the small button to add a new button
        smallButton.setOnClickListener(v -> {
            // Only add a new dynamic button when the small button is clicked
            addDynamicButton();
        });
    }

    private void addDynamicButton() {
        // Create the new button
        Button newButton = new Button(this);
        newButton.setText("");  // Set button text to empty for a clean look
        newButton.setBackgroundColor(Color.BLUE);  // Background color for the button
        newButton.setTextColor(Color.TRANSPARENT);  // Remove text color
        newButton.setPadding(0, 0, 0, 0);  // Remove padding for a clean button appearance

        // Get the available width and height of the FlexboxLayout
        int availableWidth = flexboxLayout.getWidth();
        int availableHeight = flexboxLayout.getHeight();

        // Determine the button size limits
        int minButtonSize = 50;  // Set a minimum button size
        int maxButtonSize = Math.min(availableWidth, availableHeight) / 2;  // Set a maximum button size

        // Randomly set button size within the available space
        Random random = new Random();
        int finalButtonSize = random.nextInt(maxButtonSize - minButtonSize + 1) + minButtonSize;

        // Check if the new button will fit within the available space
        if (!willFit(finalButtonSize, finalButtonSize, availableWidth, availableHeight)) {
            // Show overflow message if the button won't fit
            Toast.makeText(this, "Overflow: Not enough space", Toast.LENGTH_SHORT).show();
            return;  // Don't add the button if there is not enough space
        }

        // Set button size and margins
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(finalButtonSize, finalButtonSize);
        params.setMargins(10, 10, 10, 10);
        newButton.setLayoutParams(params);

        // Add the new button to the layout
        if (lastButtonAddedBelow) {
            // Add the button to the right of the last button
            flexboxLayout.addView(newButton);
        } else {
            // Add the button below the last button
            flexboxLayout.addView(newButton);
        }

        // Toggle the position for the next button
        lastButtonAddedBelow = !lastButtonAddedBelow;
    }

    // Helper method to check if the new button will fit within the available space
    private boolean willFit(int buttonWidth, int buttonHeight, int availableWidth, int availableHeight) {
        int totalWidth = 0;
        int totalHeight = 0;

        // Calculate total width and height of existing views in the layout
        for (int i = 0; i < flexboxLayout.getChildCount(); i++) {
            View child = flexboxLayout.getChildAt(i);
            FlexboxLayout.LayoutParams childParams = (FlexboxLayout.LayoutParams) child.getLayoutParams();
            totalWidth += child.getWidth() + childParams.getMarginStart() + childParams.getMarginEnd();
            totalHeight += child.getHeight() + childParams.topMargin + childParams.bottomMargin;
        }

        // Check if adding the new button will overflow the layout
        if (lastButtonAddedBelow) {
            // Check space to the right
            return totalWidth + buttonWidth <= availableWidth;
        } else {
            // Check space below
            return totalHeight + buttonHeight <= availableHeight;
        }
    }
}