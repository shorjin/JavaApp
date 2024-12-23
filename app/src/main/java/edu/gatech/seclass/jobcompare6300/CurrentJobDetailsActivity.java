package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

// view import
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class CurrentJobDetailsActivity extends AppCompatActivity {

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
    private ComparisonSettingsViewModel settingsViewModel;

    private Job existingJob = null;
    private ComparisonSettings currentSettings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_job_details);

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

        // init jobviewmodel
        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(ComparisonSettingsViewModel.class);

        // cancel button
        Button buttonCancel = findViewById(R.id.buttonCancel);
        // save button
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setEnabled(false);

        // Observe settings and enable save button
        settingsViewModel.getSettings().observe(this, new Observer<ComparisonSettings>() {
            @Override
            public void onChanged(ComparisonSettings settings) {
                if (settings != null) {
                    currentSettings = settings;
                    buttonSave.setEnabled(true);
                }
            }
        });



        //edit button
        Button buttonEdit = findViewById(R.id.buttonEdit);

        buttonEdit.setOnClickListener(view -> {
            // Set fields to editable
            setFieldsEditable(true);

            // Hide the Edit button
            buttonEdit.setVisibility(View.GONE);

            // Optionally, show the Save button
            buttonSave.setVisibility(View.VISIBLE);
        });



        // if there is a current job already in db, show it
        jobViewModel.getCurrentJob().observe(this, job -> {
            existingJob = job;
            if (job != null) {
                // if current job exists in db, populate fields
                textEditJobTitle.setText(job.title);
                textEditCompanyName.setText(job.company);
                textEditSalary.setText(String.valueOf(job.yearlySalary));
                textEditBonus.setText(String.valueOf(job.yearlyBonus));
                textEditRestrictedStock.setText(String.valueOf(job.restrictedStock));
                textEditCostOfLiving.setText(String.valueOf(job.costOfLiving));
                textEditLearning.setText(String.valueOf(job.learningDevelopment));
                textEditAssistance.setText(String.valueOf(job.familyPlanningAssistance));
                seekBarRetirement.setProgress(job.retirementPercentage);
                retirementTextView.setText("Retirement:" + job.retirementPercentage + "%");
                textEditCity.setText(job.getLocationCity());
                textEditState.setText(job.getLocationState());

                //  Hide save button if current job exists
                buttonSave.setVisibility(View.GONE);
                // Make all fields non-editable
                setFieldsEditable(false);

            } else {
                // if no current job, set fields to default
                textEditJobTitle.setText("");
                textEditCompanyName.setText("");
                textEditSalary.setText("");
                textEditBonus.setText("");
                textEditRestrictedStock.setText("");
                textEditCostOfLiving.setText("");
                textEditLearning.setText("");
                textEditAssistance.setText("");
                seekBarRetirement.setProgress(0);
                retirementTextView.setText("Retirement: 0%");
                textEditCity.setText("");
                textEditState.setText("");

                // Show the Save button and hide edit button if there's no existing job
                buttonSave.setVisibility(View.VISIBLE);
                buttonEdit.setVisibility(View.GONE);


            }
        });


        // Set up the listener for the SeekBar to update the TextView dynamically
        seekBarRetirement.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView to reflect the current SeekBar value
                retirementTextView.setText("  Retirement: " + progress + "%"); // Use + "%" to append the percentage sign
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // validate family plan assist
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
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSettings != null) {
                    // save input data
                    String title = textEditJobTitle.getText().toString();
                    String company = textEditCompanyName.getText().toString();
                    double yearlySalary = textEditSalary.getText().toString().isEmpty() ? 0 : Double.parseDouble(textEditSalary.getText().toString());
                    double yearlyBonus = textEditBonus.getText().toString().isEmpty() ? 0 : Double.parseDouble(textEditBonus.getText().toString());
                    int costOfLivingIndex = textEditCostOfLiving.getText().toString().isEmpty() ? 0 : Integer.parseInt(textEditCostOfLiving.getText().toString());
                    int retirementPercentage = seekBarRetirement.getProgress();
                    double learningDevelopment = textEditLearning.getText().toString().isEmpty() ? 0 : Double.parseDouble(textEditLearning.getText().toString());
                    double familyPlanningAssistance = textEditAssistance.getText().toString().isEmpty() ? 0 : Double.parseDouble(textEditAssistance.getText().toString());
                    double restrictedStock = textEditRestrictedStock.getText().toString().isEmpty() ? 0 : Double.parseDouble(textEditRestrictedStock.getText().toString());
                    String city = textEditCity.getText().toString();
                    String state = textEditState.getText().toString();

                    // create new job object
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
                            false,
                            costOfLivingIndex
                    );

                    newJob.calculateFields(currentSettings);

                    // Insert or update the current job
                    if (existingJob == null) {
                        jobViewModel.insertJob(newJob);
                    } else {
                        newJob.id = existingJob.id;
                        jobViewModel.update(newJob);
                    }
                    Toast.makeText(CurrentJobDetailsActivity.this, "Current Job details saved successfully!", Toast.LENGTH_SHORT).show();

                    // Return to main menu
                    Intent intent = new Intent(CurrentJobDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Button cancel: Go to main menu
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentJobDetailsActivity.this, MainActivity.class);
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



    // Method to set fields editable or non-editable
    private void setFieldsEditable(boolean isEditable) {
        textEditJobTitle.setEnabled(isEditable);
        textEditCompanyName.setEnabled(isEditable);
        textEditSalary.setEnabled(isEditable);
        textEditBonus.setEnabled(isEditable);
        textEditRestrictedStock.setEnabled(isEditable);
        textEditCostOfLiving.setEnabled(isEditable);
        textEditLearning.setEnabled(isEditable);
        textEditAssistance.setEnabled(isEditable);
        seekBarRetirement.setEnabled(isEditable);
        textEditCity.setEnabled(isEditable);
        textEditState.setEnabled(isEditable);
    }
}