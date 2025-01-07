package com.example.adhd_wid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexWrap;

import java.util.Random;

public class jam_interface extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private int buttonCount = 0; // Track the number of buttons added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jam_board);

        // Get reference to the small button
        Button smallButton = findViewById(R.id.button);

        // Get reference to the FlexboxLayout
        flexboxLayout = findViewById(R.id.flexbox);

        // Set FlexboxLayout properties
        flexboxLayout.setFlexWrap(FlexWrap.WRAP); // Wrap buttons when space runs out

        // Set OnClickListener for the small button to add new buttons
        smallButton.setOnClickListener(v -> addDynamicButton());
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
        int buttonSize = random.nextInt(maxButtonSize - minButtonSize + 1) + minButtonSize;

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
            newButton.setTag(currentCount ); // Store the updated count as a tag

            // Calculate the new scale factor
            float scaleFactor = 1 + (currentCount * 0.1f); // Scale by 10% for each tap

            // Apply the scaling transformation to the button
            newButton.setScaleX(scaleFactor);
            newButton.setScaleY(scaleFactor);
        });

        buttonCount++;
    }
}