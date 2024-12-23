package edu.gatech.seclass.jobcompare6300;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JobDAO {
    // Insert new job
    // If the same job exists in the database, replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJob(Job job);

    // Update existing job
    @Update
    void updateJob(Job job);

    // Delete existing job
    @Delete
    void deleteJob(Job job);

    // delete the current job
    @Query("DELETE FROM Job WHERE isJobOffer = 0")
    void deleteCurrentJob();

    // Get the current job where isJobOffer is false
    // Returns only one Job instance
    @Query("SELECT * FROM Job WHERE isJobOffer = 0 LIMIT 1")
    LiveData<Job> getCurrentJob();

    // Get all job offers where isJobOffer is true
    @Query("SELECT * FROM Job WHERE isJobOffer = 1")
    LiveData<List<Job>> getJobOffers();  // Multiple job offers

    // Get all jobs including current and offers
    @Query("SELECT * FROM Job")
    LiveData<List<Job>> getAllJobs();  // Multiple job offers

    // Check if a job already exists with the given attributes
    @Query("SELECT * FROM Job WHERE title = :title AND company = :company AND yearlySalary = :yearlySalary " +
            "AND yearlyBonus = :yearlyBonus AND retirementPercentage = :retirementPercentage AND restrictedStock = :restrictedStock " +
            "AND costOfLiving = :costOfLiving AND learningDevelopment = :learningDevelopment " +
            "AND familyPlanningAssistance = :familyPlanningAssistance AND isJobOffer = :isJobOffer " +
            "AND city = :city AND state = :state LIMIT 1")
    Job isJobDuplicate(String title, String company, double yearlySalary, double yearlyBonus,
                       int retirementPercentage, double restrictedStock, int costOfLiving,
                       double learningDevelopment, double familyPlanningAssistance, boolean isJobOffer,
                       String city, String state);
}
