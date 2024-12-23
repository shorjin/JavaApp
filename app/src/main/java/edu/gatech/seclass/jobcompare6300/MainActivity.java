package edu.gatech.seclass.jobcompare6300;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// for view model
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ComparisonSettingsViewModel comparisonSettingsViewModel = new ViewModelProvider(this).get(ComparisonSettingsViewModel.class);

//      Button 1 Go to current job details

        Button move = findViewById(R.id.currentJobDetails);
        // Set an OnClickListener on the button
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start CurrentJobDetails
                Intent intent = new Intent(MainActivity.this, CurrentJobDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Button 2: Go to New Job offers
        Button buttonJobOffers= findViewById(R.id.jobOffers);
        buttonJobOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JobOffersActivity.class);
                startActivity(intent);
            }
        });

        // Button 3: Go to Comparison Settings
        Button buttonComparisonSettings= findViewById(R.id.settings);
        buttonComparisonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComparisonSettingsActivity.class);
                startActivity(intent);
            }
        });

        // Button 4: Go to Compare Jobs
        Button buttonCompareOffers= findViewById(R.id.compareJob);
        buttonCompareOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompareJobOffersActivity.class);
                startActivity(intent);
            }
        });

        // insert default comparisonsettings when app starts
        // if settings already in database, initialize seekbars and textviews with those values
        AppDatabase db = AppDatabase.getDatabase(this);
        comparisonSettingsViewModel.getSettings().observe(this, settings -> {
            if (settings == null) {
                comparisonSettingsViewModel.insertDefaultSettings();
            }
        });

    }
}