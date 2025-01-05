package com.example.adhd_wid;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class jam_interface extends AppCompatActivity {

    private int currentColumn = 0; // Track the current column for placing buttons
    private int currentRow = 0;    // Track the current row for placing buttons
    private int gridColumns;       // Number of columns in the grid
    private int gridRows = 4;      // Number of rows in the grid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jam_board);

        // Get reference to the small button
        Button smallButton = findViewById(R.id.button);

        // Get reference to the GridLayout
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Dynamically calculate grid size based on screen size
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;

        // Set the minimum button size to 1/4 of the screen width and max size to 1/2
        int minButtonWidthPx = screenWidth / 8;
        int maxButtonWidthPx = screenWidth / 4;

        // Calculate the number of columns that can fit in the grid based on min button size
        gridColumns = screenWidth / minButtonWidthPx;

        // Set the GridLayout's column count dynamically
        gridLayout.setColumnCount(gridColumns);

        // Set an OnClickListener on the small button
        smallButton.setOnClickListener(v -> addNewButton(gridLayout, minButtonWidthPx, maxButtonWidthPx));
    }

    private void addNewButton(GridLayout gridLayout, int minButtonWidthPx, int maxButtonWidthPx) {
        // Create a new button
        Button newButton = new Button(this);
        newButton.setText("Resize Me!");

        // Calculate random button size between min and max width
        int buttonWidth = minButtonWidthPx + (int) (Math.random() * (maxButtonWidthPx - minButtonWidthPx));
        int buttonHeight = buttonWidth; // Keep the button square

        // Set layout parameters for the button
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = buttonWidth;
        params.height = buttonHeight;
        params.rowSpec = GridLayout.spec(currentRow);
        params.columnSpec = GridLayout.spec(currentColumn);
        params.setMargins(10, 10, 10, 10);
        newButton.setLayoutParams(params);

        // Add functionality to resize the button

        // Add the new button to the grid
        gridLayout.addView(newButton);

        // Update position for the next button
        if (currentRow + 1 < gridRows) {
            currentRow++;  // Move to the next row
        } else {
            currentRow = 0;  // Reset to first row
            if (currentColumn + 1 < gridColumns) {
                currentColumn++;  // Move to the next column
            } else {
                currentColumn = 0;  // Reset to the first column
            }
        }
    }
}
