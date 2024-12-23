package edu.gatech.seclass.jobcompare6300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CompareWithCurrentJobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_with_current_job);

        //cancel button
        // Initialize Cancel Button
        Button buttonCancel = findViewById(R.id.cancelCompareCurrentJobs);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous one (main activity)
                Intent intent = new Intent(CompareWithCurrentJobActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}