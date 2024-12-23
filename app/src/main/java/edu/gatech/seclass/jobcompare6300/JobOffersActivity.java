package edu.gatech.seclass.jobcompare6300;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class JobOffersActivity extends AppCompatActivity {
    private TextInputLayout textLayoutLearning;
    private EditText textEditLearning;

    private TextInputLayout TextLayoutAssistance;
    private EditText textEditAssistance;
    private EditText textEditSalary;

    private TextView retirementTextView;
    private SeekBar seekBarRetirement;

    private EditText textEditJobTitle;
    private EditText textEditBonus;
    private EditText textEditCompanyName;
    private EditText textEditRestrictedStock;
    private EditText textEditCostOfLiving;

    private EditText textEditCity;
    private EditText textEditState;

    private JobViewModel jobViewModel;

    // view model to connect activity to database
    private ComparisonSettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_offers);

        textLayoutLearning = findViewById(R.id.TextLayoutLearning);
        textEditLearning = findViewById(R.id.textEditLearning);
        TextLayoutAssistance = findViewById(R.id.TextLayoutAssistance);
        textEditAssistance = findViewById(R.id.textEditAssistance);
        textEditSalary = findViewById(R.id.textEditSalary);
        textEditJobTitle = findViewById(R.id.textEditTitle);
        textEditBonus = findViewById(R.id.textEditBonus);
        textEditCompanyName = findViewById(R.id.textEditCompany);
        textEditCostOfLiving = findViewById(R.id.textEditIndex);
        textEditRestrictedStock = findViewById(R.id.textEditStock);
        textEditCity = findViewById(R.id.textEditCity);
        textEditState = findViewById(R.id.textEditState);

        // Link the TextView and SeekBar to their XML
        retirementTextView = findViewById(R.id.RetirementTextView);
        seekBarRetirement = findViewById(R.id.seekBarRetirement);

        // Init ViewModel
        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(ComparisonSettingsViewModel.class);

        // Cancel button
        Button buttonCancel = findViewById(R.id.buttonCancel);
        // Save button
        Button buttonSave = findViewById(R.id.buttonSave);

        // Seekbar
        // Initialize SeekBar value and TextView
        seekBarRetirement.setProgress(0);  // Set initial progress (optional)
        retirementTextView.setText("Retirement: 0%");

        // Set up the listener for the SeekBar to update the TextView dynamically
        seekBarRetirement.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView to reflect the current SeekBar value
                retirementTextView.setText("  Retirement: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Verify family assistance
        textEditAssistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputAssistance();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add a TextWatcher to validate input
        textEditLearning.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateLearningDev();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Button save: save job
        settingsViewModel.getSettings().observe(this, settings -> {
            if (settings != null) {
                // Save button functionality
                buttonSave.setOnClickListener(view -> {
                    // Save input data
                    String title = textEditJobTitle.getText().toString();
                    String company = textEditCompanyName.getText().toString();
                    String salaryString = textEditSalary.getText().toString();
                    String bonusString = textEditBonus.getText().toString();
                    String costOfLivingString = textEditCostOfLiving.getText().toString();
                    String learningString = textEditLearning.getText().toString();
                    String assistanceString = textEditAssistance.getText().toString();
                    String restrictedStockString = textEditRestrictedStock.getText().toString();
                    String city = textEditCity.getText().toString();
                    String state = textEditState.getText().toString();

                    // Make sure all fields are filled out
                    if (title.isEmpty() || company.isEmpty() || salaryString.isEmpty() || bonusString.isEmpty() ||
                            costOfLivingString.isEmpty() || learningString.isEmpty() || assistanceString.isEmpty() ||
                            restrictedStockString.isEmpty() || city.isEmpty() || state.isEmpty()) {
                        Toast.makeText(JobOffersActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double yearlySalary = Double.parseDouble(salaryString);
                    double yearlyBonus = Double.parseDouble(bonusString);
                    int costOfLivingIndex = Integer.parseInt(costOfLivingString);
                    int retirementPercentage = seekBarRetirement.getProgress();
                    double learningDevelopment = Double.parseDouble(learningString);
                    double familyPlanningAssistance = Double.parseDouble(assistanceString);
                    double restrictedStock = Double.parseDouble(restrictedStockString);
                    Location location = new Location(city, state);

                    Job newJob = new Job(
                            title,
                            company,
                            location,
                            yearlySalary,
                            yearlyBonus,
                            retirementPercentage,
                            restrictedStock,
                            learningDevelopment,
                            familyPlanningAssistance,
                            true,
                            costOfLivingIndex
                    );

                    newJob.calculateFields(settings);

                    // If job not duplicate, insert
                    jobViewModel.insertIfNotDuplicate(newJob);

                    // make interactive message pop up
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobOffersActivity.this);
                    builder.setTitle("Job offer saved successfully");
                    builder.setMessage("Do you want to compare the saved job to your current job?");
                    builder.setNegativeButton("Yes", (dialog, which) -> {
                            jobViewModel.getCurrentJob().observe(this, currentJob -> {
                                if (currentJob != null) {
                                // use currentjob and newjob and send to Comparisonresultsactivity
                                Intent intent = new Intent (JobOffersActivity.this, ComparisonResultsActivity.class);
                                intent.putExtra("job1", newJob);
                                intent.putExtra("job2", currentJob);
                                startActivity(intent);
                                } else {
                                    Toast.makeText(JobOffersActivity.this, "No current job available for comparison.\nPlease input a current job before choosing this option.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    });
                    builder.setPositiveButton("No", (dialog, which) -> {
                        // if no, do nothing
                        dialog.dismiss();
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


//                    // Display a Toast message to indicate success
//                    Toast.makeText(JobOffersActivity.this, "Job offer saved successfully!", Toast.LENGTH_SHORT).show();

                    // Clear all input fields after saving
                    clearInputFields();
                });
            }
        });

        // Button cancel: Go to main menu
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobOffersActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validateLearningDev() {
        String input = textEditLearning.getText().toString();
        if (!input.isEmpty()) {
            double value = Double.parseDouble(input);
            if (value < 0 || value > 18000) {
                textLayoutLearning.setError("Max amount is $18,000");
            } else {
                textLayoutLearning.setError(null); // Clear the error if valid
            }
        }
    }

    private void validateInputAssistance() {
        String input = textEditAssistance.getText().toString();
        String salary = textEditSalary.getText().toString();

        if (salary.isEmpty()) {
            TextLayoutAssistance.setError("Please input salary first");
            return;
        }

        if (!input.isEmpty()) {
            double value = Double.parseDouble(input);
            double value2 = Double.parseDouble(salary);
            double maxAmount = 0.12 * value2;

            if (value > maxAmount) {
                TextLayoutAssistance.setError("Max amount is 12% of Salary");
            } else {
                TextLayoutAssistance.setError(null); // Clear the error if valid
            }
        }
    }

    private void clearInputFields() {
        textEditJobTitle.setText("");
        textEditCompanyName.setText("");
        textEditSalary.setText("");
        textEditBonus.setText("");
        textEditCostOfLiving.setText("");
        textEditLearning.setText("");
        textEditAssistance.setText("");
        textEditRestrictedStock.setText("");
        textEditCity.setText("");
        textEditState.setText("");
        seekBarRetirement.setProgress(0);
        retirementTextView.setText("Retirement: 0%");
    }
}
