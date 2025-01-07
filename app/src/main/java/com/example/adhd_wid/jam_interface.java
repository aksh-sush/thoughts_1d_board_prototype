package com.example.adhd_wid;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Random;

public class jam_interface extends AppCompatActivity {


    private FlexboxLayout flexboxLayout;
    private boolean lastButtonAddedBelow = false; // Track alternating placement
    private int buttonCount = 0; // Track the number of buttons added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jam_board);

        flexboxLayout = findViewById(R.id.flexbox);

        // Set FlexboxLayout properties to ensure buttons align properly
        flexboxLayout.setFlexDirection(FlexDirection.COLUMN);  // Align buttons in a row
        flexboxLayout.setFlexWrap(FlexWrap.WRAP);           // Wrap buttons when space runs out

        // Get the existing small button from XML
        Button smallButton = findViewById(R.id.button);

        // Set an OnClickListener for the small button to add a new button
        smallButton.setOnClickListener(v -> addDynamicButton());
    }

    private void addDynamicButton() {
        // Create the new button
        Button newButton = new Button(this);
        newButton.setText("");  // Set button text to empty for a clean look
        newButton.setBackgroundColor(Color.BLUE);  // Background color for the button
        newButton.setPadding(0, 0, 0, 0);  // Remove padding for a clean button appearance

        int availableWidth = flexboxLayout.getWidth();
        int availableHeight = flexboxLayout.getHeight();

        // Calculate button size
        int minButtonSize = 50;
        int maxButtonSize;
        Random random = new Random();

        if (buttonCount == 0) {
            // First button is added directly
            maxButtonSize = Math.min(availableWidth, availableHeight) / 4;
            int finalButtonSize = random.nextInt(maxButtonSize - minButtonSize + 1) + minButtonSize;
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(finalButtonSize, finalButtonSize);
            params.setMargins(10, 10, 10, 10);
            newButton.setLayoutParams(params);
            flexboxLayout.addView(newButton);
        } else if (buttonCount == 1) {
            // Second button is added below the first
            maxButtonSize = availableHeight / 4;
            int finalButtonSize = random.nextInt(maxButtonSize - minButtonSize + 1) + minButtonSize;
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(finalButtonSize, finalButtonSize);
            params.setMargins(10, 10, 10, 10);
            newButton.setLayoutParams(params);
            flexboxLayout.addView(newButton);
        } else {
            // For other buttons, alternate placement
            maxButtonSize = lastButtonAddedBelow ? availableWidth / 4 : availableHeight / 4;
            int finalButtonSize = random.nextInt(maxButtonSize - minButtonSize + 1) + minButtonSize;

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(finalButtonSize, finalButtonSize);
            params.setMargins(10, 10, 10, 10);
            newButton.setLayoutParams(params);

            if (lastButtonAddedBelow) {
                // Add to the right
                flexboxLayout.addView(newButton);
            } else {
                // Add below
                flexboxLayout.addView(newButton);
            }
            lastButtonAddedBelow = !lastButtonAddedBelow; // Toggle position
        }

        buttonCount++;
    }
}
