package com.example.adhd_wid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Random;

public class jam_interface extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private boolean lastButtonAddedBelow = false;

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
        // Create a container (layer) for the button
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(Color.GRAY);  // Set a background color for the layer (optional)

        // Create the new button inside the container
        Button newButton = new Button(this);
        newButton.setText("");  // Set button text to empty for a clean look
        newButton.setBackgroundColor(Color.BLUE);  // Background color for the button
        newButton.setTextColor(Color.TRANSPARENT);  // Remove text color
        newButton.setPadding(0, 0, 0, 0);  // Remove padding for a clean button appearance

        // Measure button size based on its content
        newButton.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int minTextWidth = newButton.getMeasuredWidth();
        int minTextHeight = newButton.getMeasuredHeight();

        // Get layout dimensions
        int layoutWidth = flexboxLayout.getWidth();

        // Calculate minimum and maximum button sizes
        int minButtonSize = Math.max(minTextWidth, minTextHeight);
        int maxButtonSize = minButtonSize * 2;

        // Calculate the total width of existing buttons, including margins
        int totalWidth = 0;
        for (int i = 0; i < flexboxLayout.getChildCount(); i++) {
            View child = flexboxLayout.getChildAt(i);
            FlexboxLayout.LayoutParams childParams = (FlexboxLayout.LayoutParams) child.getLayoutParams();
            totalWidth += child.getWidth() + childParams.getMarginStart() + childParams.getMarginEnd();
        }

        // Remaining space in the layout
        int remainingSpace = layoutWidth - totalWidth;

        // Adjust button size based on remaining space
        if (remainingSpace > 0) {
            minButtonSize = Math.min(minButtonSize, remainingSpace / 2);
            maxButtonSize = Math.min(maxButtonSize, remainingSpace);
        } else {
            return; // No space to add a button
        }

        // Randomly set button size
        Random random = new Random();
        int buttonSize = random.nextInt(maxButtonSize - minButtonSize + 1) + minButtonSize;

        // Set button size and margins
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(buttonSize, buttonSize);
        params.setMargins(10, 10, 10, 10);
        newButton.setLayoutParams(params);

        // Add the new button to the container
        container.addView(newButton);

        // Add the container (with the button inside) to the FlexboxLayout
        if (lastButtonAddedBelow) {
            // Add the container to the left of the last button (this will add the new button next to the previous one)
            flexboxLayout.addView(container, flexboxLayout.getChildCount() - 1);
        } else {
            // Add the container below the last button (this will add the new button below the previous one)
            flexboxLayout.addView(container);
        }

        // Toggle the position for the next button
        lastButtonAddedBelow = !lastButtonAddedBelow;
    }
}
