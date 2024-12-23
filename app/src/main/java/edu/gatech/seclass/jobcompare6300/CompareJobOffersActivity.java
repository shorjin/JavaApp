package edu.gatech.seclass.jobcompare6300;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CompareJobOffersActivity extends AppCompatActivity {

    private JobViewModel jobViewModel;
    private LinearLayout jobListLayout;
    private List<Job> selectedJobs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_job_offers);

        // init viewmodel
        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);

        // init linearlayout
        jobListLayout = findViewById(R.id.jobListLayout);

        // get all jobs, rank based on score
        jobViewModel.getAllJobs().observe(this, jobs -> {
            if (jobs != null) {
                // sort existing jobs by score
                jobs.sort((job1, job2) -> Double.compare(job2.jobScore, job1.jobScore));

                jobListLayout.removeAllViews();

                // put jobs in layout
                for (Job job : jobs) {

                    // setup layout
                    LinearLayout jobItemLayout = new LinearLayout(this);
                    jobItemLayout.setOrientation(LinearLayout.HORIZONTAL);

                    CheckBox jobCheckBox = new CheckBox(this);
                    TextView jobTextView = new TextView(this);

//                    View jobView = getLayoutInflater().inflate(R.layout.job_item, null)
//                    TextView jobTextView = new TextView (this);
                    // limit jobscore to 2 decimal places
                    String formattedScore = String.format("%.2f", job.jobScore);
                    String jobInfo = "Title: " + job.title + "\n" + "Company: " + job.company + "\n" + "Score: " + formattedScore;

                    // indicate current job clearly
                    if (!job.isJobOffer) {
                        jobInfo = "----Current Job----\n" + jobInfo;
                    }

                    jobTextView.setText(jobInfo);
                    jobTextView.setPadding(16,16,16,16);
                    jobItemLayout.addView(jobCheckBox);
                    jobItemLayout.addView(jobTextView);
                    jobListLayout.addView(jobItemLayout);

                    // now select jobs
                    jobCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            // can't check more than two jobs
                            if (selectedJobs.size() < 2) {
                                selectedJobs.add(job);
                            } else {
                                buttonView.setChecked(false);
                                Toast.makeText(this, "Only two jobs can be selected for comparison", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            selectedJobs.remove(job);
                        }
                    });

                }

            }
        });

//        Save button
        // Initialize save Button
        Button buttonSave = findViewById(R.id.buttonSaveCompareJobs);
        buttonSave.setOnClickListener(v -> {
            if (selectedJobs.size () == 2) {
                // send jobs to ComparisonResultsActivity
                Intent intent = new Intent(CompareJobOffersActivity.this, ComparisonResultsActivity.class);
                intent.putExtra("job1", selectedJobs.get(0));
                intent.putExtra("job2", selectedJobs.get(1));
                startActivity(intent);
            } else {
                // if more or less than two jobs, send error message
                Toast.makeText(this, "Please select two jobs for comparison.", Toast.LENGTH_SHORT).show();
            }
        });

//        cancel button
        // Initialize Cancel Button
        Button buttonCancel = findViewById(R.id.buttonCancelCompareJobs);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous one (main activity)
                Intent intent = new Intent(CompareJobOffersActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}