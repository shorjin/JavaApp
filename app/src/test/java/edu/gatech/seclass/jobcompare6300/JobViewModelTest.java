package edu.gatech.seclass.jobcompare6300;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
public class JobViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private JobRepository jobRepository;

    @Mock
    private Application application;

    private JobViewModel jobViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up the repository to return non-null LiveData
        MutableLiveData<List<Job>> jobOffersLiveData = new MutableLiveData<>();
        when(jobRepository.getJobOffers()).thenReturn(jobOffersLiveData);

        MutableLiveData<Job> currentJobLiveData = new MutableLiveData<>();
        when(jobRepository.getCurrentJob()).thenReturn(currentJobLiveData);

        MutableLiveData<List<Job>> allJobsLiveData = new MutableLiveData<>();
        when(jobRepository.getAllJobs()).thenReturn(allJobsLiveData);

        // Instantiate the ViewModel with the mocked repository
        jobViewModel = new JobViewModel(application, jobRepository);
    }

    @Test
    public void testGetJobOffers() {
        // Arrange
        Job mockJob = new Job(
                "Im a test",
                "Testing LLC",
                new Location("New York", "NY"),
                120000.0,
                15000.0,
                5,
                50000.0,
                2000.0,
                1600.0,
                true,
                130
        );
        MutableLiveData<List<Job>> jobOffersLiveData = new MutableLiveData<>();
        jobOffersLiveData.setValue(List.of(mockJob));
        when(jobRepository.getJobOffers()).thenReturn(jobOffersLiveData);

        // Re-instantiate ViewModel to pick up the new LiveData
        jobViewModel = new JobViewModel(application, jobRepository);

        // Act
        LiveData<List<Job>> result = jobViewModel.getJobOffers();

        // Assert
        assertNotNull("LiveData should not be null", result);
        assertEquals("LiveData should contain the mock job", List.of(mockJob), result.getValue());
    }
    @Test
    public void testInsertJob() {
        Job job = new Job(
                "Testing Dude",
                "The Test People",
                new Location("San Francisco", "CA"),
                110000.0,
                12000.0,
                7,
                40000.0,
                1800.0,
                1400.0,
                true,
                110
        );

        jobViewModel.insertJob(job);
        verify(jobRepository, times(1)).insertJob(job);
    }

    @Test
    public void testUpdateJob() {
        Job job = new Job(
                "Testing Update",
                "The Updated Company",
                new Location("Los Angeles", "CA"),
                130000.0,
                14000.0,
                8,
                45000.0,
                1900.0,
                1500.0,
                true,
                135
        );

        jobViewModel.update(job);

        verify(jobRepository, times(1)).updateJob(job);
    }

    @Test
    public void testGetCurrentJob() {
        Job mockCurrentJob = new Job(
                "Grand Wizard",
                "WizardsWithCode",
                new Location("San Francisco", "CA"),
                150000.0,
                20000.0,
                7,
                60000.0,
                2500.0,
                1800.0,
                true,
                140
        );
        MutableLiveData<Job> currentJobLiveData = new MutableLiveData<>();
        currentJobLiveData.setValue(mockCurrentJob);
        when(jobRepository.getCurrentJob()).thenReturn(currentJobLiveData);

        jobViewModel = new JobViewModel(application, jobRepository);

        LiveData<Job> result = jobViewModel.getCurrentJob();

        assertNotNull("LiveData should not be null", result);
        assertEquals("LiveData should contain the mock current job", mockCurrentJob, result.getValue());
    }

    @Test
    public void testInsertIfNotDuplicate() {
        Job job = new Job(
                "Testing Dude",
                "The Test People",
                new Location("San Francisco", "CA"),
                110000.0,
                12000.0,
                7,
                40000.0,
                1800.0,
                1400.0,
                true,
                110
        );

        jobViewModel.insertIfNotDuplicate(job);
        verify(jobRepository, times(1)).insertIfNotDuplicate(job);
    }

    @Test
    public void testUpdateJob_WithInvalidData() {
        Job invalidJob = new Job(
                "", // Empty title, assuming it's invalid
                "Invalid Company",
                new Location("Nowhere", "ZZ"),
                -50000.0, // Negative salary, invalid
                0.0,
                -1, // Negative retirement percentage, invalid
                -1000.0, // Negative restricted stock, invalid
                0.0,
                0.0,
                true,
                -10 // Negative cost of living, invalid
        );

        jobViewModel.update(invalidJob);

        verify(jobRepository, times(1)).updateJob(invalidJob);
    }
}
