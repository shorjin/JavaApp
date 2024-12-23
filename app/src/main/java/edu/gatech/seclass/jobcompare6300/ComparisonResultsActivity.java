package edu.gatech.seclass.jobcompare6300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ComparisonResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison_results);

        // get jobs sent from comparejoboffersactivity
        Job job1 = (Job) getIntent().getParcelableExtra("job1", Job.class);
        Job job2 = (Job) getIntent().getParcelableExtra("job2", Job.class);

        if (job1 == null || job2 == null) {
            Toast.makeText(this, "Error retrieving job data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // Set job 1 details
        ((TextView) findViewById(R.id.job1Title)).setText(job1.title);
        ((TextView) findViewById(R.id.job1Company)).setText(job1.company);
        ((TextView) findViewById(R.id.job1City)).setText(job1.getLocationCity());
        ((TextView) findViewById(R.id.job1State)).setText(job1.getLocationState());
        ((TextView) findViewById(R.id.job1Salary)).setText(String.format("$%.2f", job1.adjustedYearlySalary));
        ((TextView) findViewById(R.id.job1Bonus)).setText(String.format("$%.2f", job1.adjustedYearlyBonus));
        ((TextView) findViewById(R.id.job1Retirement)).setText(String.format("%d%%", job1.retirementPercentage));
        ((TextView) findViewById(R.id.job1RSU)).setText(String.format("$%.2f", job1.restrictedStock));
        ((TextView) findViewById(R.id.job1PLD)).setText(String.format("$%.2f", job1.learningDevelopment));
        ((TextView) findViewById(R.id.job1FPA)).setText(String.format("$%.2f", job1.familyPlanningAssistance));

        // Set job 2 details
        ((TextView) findViewById(R.id.job2Title)).setText(job2.title);
        ((TextView) findViewById(R.id.job2Company)).setText(job2.company);
        ((TextView) findViewById(R.id.job2City)).setText(job2.getLocationCity());
        ((TextView) findViewById(R.id.job2State)).setText(job2.getLocationState());
        ((TextView) findViewById(R.id.job2Salary)).setText(String.format("$%.2f", job2.adjustedYearlySalary));
        ((TextView) findViewById(R.id.job2Bonus)).setText(String.format("$%.2f", job2.adjustedYearlyBonus));
        ((TextView) findViewById(R.id.job2Retirement)).setText(String.format("%d%%", job2.retirementPercentage));
        ((TextView) findViewById(R.id.job2RSU)).setText(String.format("$%.2f", job2.restrictedStock));
        ((TextView) findViewById(R.id.job2PLD)).setText(String.format("$%.2f", job2.learningDevelopment));
        ((TextView) findViewById(R.id.job2FPA)).setText(String.format("$%.2f", job2.familyPlanningAssistance));

        // Initialize buttons for navigation
        Button buttonAnotherComparison = findViewById(R.id.buttonAnotherComparison);
        buttonAnotherComparison.setOnClickListener(v -> {
            // Finish this activity and go back to CompareJobOffersActivity
            Intent intent = new Intent(ComparisonResultsActivity.this, CompareJobOffersActivity.class);
            startActivity(intent);
            finish();
        });

        // cancel button
        // Initialize Cancel Button
        Button buttonCancelComparison = findViewById(R.id.buttonCancelComparison);
        buttonCancelComparison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous one (main activity)
                Intent intent = new Intent(ComparisonResultsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}