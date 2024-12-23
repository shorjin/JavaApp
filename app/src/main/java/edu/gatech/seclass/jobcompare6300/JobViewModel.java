package edu.gatech.seclass.jobcompare6300;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


// will interact with JobRepository to get and update Job Data
public class JobViewModel extends AndroidViewModel{


    // set up vars to use

    // use repository for database operations
    private final JobRepository jobRepository;
    private final LiveData<List<Job>> jobOffers;
    private final LiveData<Job> currentJob;
    private final LiveData<List<Job>> allJobs;

    // init jobviewmodel with jobs info from repository
    public JobViewModel(@NonNull Application application) {
        super(application);
        jobRepository = new JobRepository(application);
        jobOffers = jobRepository.getJobOffers();
        currentJob = jobRepository.getCurrentJob();
        allJobs = jobRepository.getAllJobs();
    }
    // secondary constructor for testing (needed for mockito)
    public JobViewModel(@NonNull Application application, JobRepository jobRepository) {
        super(application);
        this.jobRepository = jobRepository;
        jobOffers = jobRepository.getJobOffers();
        currentJob = jobRepository.getCurrentJob();
        allJobs = jobRepository.getAllJobs();
    }

    // get all job offers
    public LiveData<List<Job>> getJobOffers() {
        return jobOffers;
    }

    // get the current job
    public LiveData<Job> getCurrentJob() {
        return currentJob;
    }

    // get all jobs
    public LiveData<List<Job>> getAllJobs() {
        return allJobs;
    }

    // insert functionality
    public void insertJob(Job job) {
        jobRepository.insertJob(job);
    }

    // update functionality
    public void update(Job job) {
        jobRepository.updateJob(job);
    }
//
//     delete method that we don't use right now but might use later
    public void delete(Job job) {
        jobRepository.deleteJob(job);
    }


    // delete currentjob
    public void deleteCurrentJob() {
        jobRepository.deleteCurrentJob();
    }


    // check if duplicate, then insert if not
    public void insertIfNotDuplicate(Job job) {
        jobRepository.insertIfNotDuplicate(job);
    }
}
