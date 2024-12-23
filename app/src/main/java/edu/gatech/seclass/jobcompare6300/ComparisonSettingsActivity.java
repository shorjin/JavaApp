package edu.gatech.seclass.jobcompare6300;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class ComparisonSettingsActivity extends AppCompatActivity {

    // vars for textviews
    private TextView SalarySettingsTextView;
    private TextView BonusSettingsTextView;
    private TextView RetirementSettingsTextView;
    private TextView StockSettingsTextView;
    private TextView LearningSettingsTextView;
    private TextView AssistanceSettingsTextView;

    // vars for seekbars
    private SeekBar seekBarSalary;
    private SeekBar seekBarBonus;
    private SeekBar seekBarRetirement;
    private SeekBar seekBarStock;
    private SeekBar seekBarLearning;
    private SeekBar seekBarAssistance;

    // view model to connect activity to database
    private ComparisonSettingsViewModel settingsViewModel;
    private JobViewModel jobViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison_settings);

        // Initialize SeekBars
        seekBarSalary = findViewById(R.id.seekBarSalarySettings);
        seekBarBonus = findViewById(R.id.seekBarBonusSettings);
        seekBarRetirement = findViewById(R.id.seekBarRetirement);
        seekBarStock = findViewById(R.id.seekBarStockSettings);
        seekBarLearning = findViewById(R.id.seekBarLearningSettings);
        seekBarAssistance = findViewById(R.id.seekBarAssistanceSettings);

//        initialize textview
        SalarySettingsTextView = findViewById(R.id.textViewSalarySettings);
        BonusSettingsTextView = findViewById(R.id.textViewBonusSettings);
        RetirementSettingsTextView = findViewById(R.id.textViewRetirementSettings);
        StockSettingsTextView = findViewById(R.id.textViewStockSettings);
        LearningSettingsTextView = findViewById(R.id.textViewLearningSettings);
        AssistanceSettingsTextView = findViewById(R.id.textViewAssistanceSettings);

        // init viewmodel
        settingsViewModel = new ViewModelProvider(this).get(ComparisonSettingsViewModel.class);
        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);

        // if settings already in database, initialize seekbars and textviews with those values
        settingsViewModel.getSettings().observe(this, settings -> {
            if (settings != null) {
                // init seekbar with existing values
                seekBarSalary.setProgress(settings.salaryWeight);
                seekBarBonus.setProgress(settings.bonusWeight);
                seekBarRetirement.setProgress(settings.retirementWeight);
                seekBarStock.setProgress(settings.stockWeight);
                seekBarLearning.setProgress(settings.learningDevelopmentWeight);
                seekBarAssistance.setProgress(settings.familyPlanningWeight);

                // init textviews with existing values
                // TODO: use string resources for this instead of literals
                SalarySettingsTextView.setText("Yearly Salary Weights: " + settings.salaryWeight);
                BonusSettingsTextView.setText("Yearly Bonus Weights: " + settings.bonusWeight);
                RetirementSettingsTextView.setText("Retirement Weights: " + settings.retirementWeight);
                StockSettingsTextView.setText("Restricted Stock Weights: " + settings.stockWeight);
                LearningSettingsTextView.setText("Personalized Learning Weights: " + settings.learningDevelopmentWeight);
                AssistanceSettingsTextView.setText("Family Planning Assistance Weights: " + settings.familyPlanningWeight);

            }
        });

        // Set up SeekBars using the helper function
        setupSeekBar(seekBarSalary, SalarySettingsTextView, "Yearly Salary Weights: ");
        setupSeekBar(seekBarBonus, BonusSettingsTextView, "Yearly Bonus Weights: ");
        setupSeekBar(seekBarRetirement, RetirementSettingsTextView, "Retirement Weights: ");
        setupSeekBar(seekBarStock, StockSettingsTextView, "Restricted Stock Weights: ");
        setupSeekBar(seekBarLearning, LearningSettingsTextView, "Personalized Learning Weights: ");
        setupSeekBar(seekBarAssistance, AssistanceSettingsTextView, "Family Planning Assistance Weights: ");
    }


    private void setupSeekBar(SeekBar seekBar, TextView textView, String label) {


        // Set up the listener for the SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView to reflect the current SeekBar value
                textView.setText(label + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: You can add any behavior when the user starts touching the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: You can add any behavior when the user stops touching the SeekBar
            }
        });

        // save button
        Button buttonSave = findViewById(R.id.buttonSaveSetting);
        // Button save: save job
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get comparison settings values
                int salaryWeight = seekBarSalary.getProgress();
                int bonusWeight = seekBarBonus.getProgress();
                int retirementWeight = seekBarRetirement.getProgress();
                int stockWeight = seekBarStock.getProgress();
                int learningWeight = seekBarLearning.getProgress();
                int assistanceWeight = seekBarAssistance.getProgress();

                // create new ComparisonSettings object
                ComparisonSettings newSettings = new ComparisonSettings(
                        salaryWeight,
                        bonusWeight,
                        retirementWeight,
                        stockWeight,
                        learningWeight,
                        assistanceWeight
                );

                // save settings to database
                settingsViewModel.insert(newSettings);

                // if comparisonsettings change, update job score for all jobs
                jobViewModel.getAllJobs().observe(ComparisonSettingsActivity.this, jobs -> {
                    for (Job job : jobs) {
                        job.calculateFields(newSettings);
                        jobViewModel.update(job);
                    }
                });

                // Return to main menu
                Intent intent = new Intent(ComparisonSettingsActivity.this, MainActivity.class);
                startActivity(intent);

                // TODO: make confirmation message pop up

            }
        });


//        cancel button
        // Initialize Cancel Button
        Button buttonCancel = findViewById(R.id.buttonCancelSetting);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous one (main activity)
                Intent intent = new Intent(ComparisonSettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}