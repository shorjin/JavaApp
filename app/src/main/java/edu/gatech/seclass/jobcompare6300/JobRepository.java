package edu.gatech.seclass.jobcompare6300;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class JobRepository {
    private final JobDAO jobdao;
    private final LiveData<Job> currentJob;
    private final LiveData<List<Job>> jobOffers;
    private final LiveData<List<Job>> allJobs;

    JobRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        jobdao = db.jobdao();
        currentJob = jobdao.getCurrentJob();
        jobOffers = jobdao.getJobOffers();
        allJobs = jobdao.getAllJobs();
    }

    // Return the LiveData for the current job
    public LiveData<Job> getCurrentJob() {return currentJob;}

    // Return the LiveData for the job offers
    public LiveData<List<Job>> getJobOffers() {return jobOffers;}

    // Return the LiveData for the jobs
    public LiveData<List<Job>> getAllJobs() {return allJobs;}

    // Executor service is used to asynchronously execute operations in the background
    // Insert job
    public void insertJob(final Job job) {
        AppDatabase.databaseWriteExecutor.execute(() -> jobdao.insertJob(job));
    }

    // Update job
    public void updateJob(final Job job) {
        AppDatabase.databaseWriteExecutor.execute(() -> jobdao.updateJob(job));
    }

    // delete job
    public void deleteJob(final Job job) {
        AppDatabase.databaseWriteExecutor.execute(() -> jobdao.deleteJob(job));
    }

    // delete current job job
    public void deleteCurrentJob() {
        AppDatabase.databaseWriteExecutor.execute(() -> jobdao.deleteCurrentJob());
    }

    // check if duplicate, then insert if not
    public void insertIfNotDuplicate(Job job) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Job duplicateJob = jobdao.isJobDuplicate(
                        job.title,
                        job.company,
                        job.yearlySalary,
                        job.yearlyBonus,
                        job.retirementPercentage,
                        job.restrictedStock,
                        job.costOfLiving,
                        job.learningDevelopment,
                        job.familyPlanningAssistance,
                        job.isJobOffer,
                        job.location.getCity(),
                        job.location.getState()
                );

                if (duplicateJob == null) {
                    jobdao.insertJob(job);
                    Log.d("JobViewModel", "Job inserted successfully: " + job.title + ", " + job.company);
                } else {
                    Log.d("JobViewModel", "Duplicate job found. Not inserting: " + job.title + ", " + job.company);
                }
            } catch (Exception e) {
                Log.e("JobViewModel", "Error inserting job: " + e.getMessage(), e);
            }
        });
    }


}
