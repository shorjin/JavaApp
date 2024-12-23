package edu.gatech.seclass.jobcompare6300;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class JobTest {

    private Job job;
    private ComparisonSettings settings;

    @Before
    public void setUp() {
        Location location = new Location("Atlanta", "GA");
        job = new Job(
                "Testing Job",
                "The Testing Corp",
                location,
                100000.0,
                10000.0,
                5,
                30000.0,
                1500.0,
                1200.0,
                true,
                120
        );

        settings = new ComparisonSettings();
        settings.salaryWeight = 1;
        settings.bonusWeight = 1;
        settings.retirementWeight = 1;
        settings.stockWeight = 1;
        settings.learningDevelopmentWeight = 1;
        settings.familyPlanningWeight = 1;
    }

    @Test
    public void testCalculateFields() {
        job.calculateFields(settings);
        assertEquals(100000.0 / (120 / 100.0), job.adjustedYearlySalary, 0.001);
        assertEquals(10000.0 / (120 / 100.0), job.adjustedYearlyBonus, 0.001);

        double expectedScore = (100000.0 / 1.2) * 1 +
                (10000.0 / 1.2) * 1 +
                (100000.0 * 0.05) * 1 +
                (30000.0 / 3.0) * 1 +
                1500.0 * 1 +
                1200.0 * 1;
        assertEquals(expectedScore, job.jobScore, 0.001);
    }

    @Test
    public void testParcelable() {
        Location location = new Location("Atlanta", "GA");
        Job originalJob = new Job(
                "Testing Engineer",
                "Test Inc.",
                location,
                100000.0,
                10000.0,
                5,
                30000.0,
                1500.0,
                1200.0,
                true,
                120
        );
        originalJob.adjustedYearlySalary = 83333.33;
        originalJob.adjustedYearlyBonus = 8333.33;
        originalJob.jobScore = 95000.0;

        Parcel parcel = Parcel.obtain();
        originalJob.writeToParcel(parcel, originalJob.describeContents());
        parcel.setDataPosition(0);

        Job createdFromParcel = Job.CREATOR.createFromParcel(parcel);
        assertEquals(originalJob.id, createdFromParcel.id);
        assertEquals(originalJob.title, createdFromParcel.title);
        assertEquals(originalJob.company, createdFromParcel.company);
        assertEquals(originalJob.costOfLiving, createdFromParcel.costOfLiving);
        assertEquals(originalJob.yearlySalary, createdFromParcel.yearlySalary, 0.001);
        assertEquals(originalJob.yearlyBonus, createdFromParcel.yearlyBonus, 0.001);
        assertEquals(originalJob.retirementPercentage, createdFromParcel.retirementPercentage);
        assertEquals(originalJob.restrictedStock, createdFromParcel.restrictedStock, 0.001);
        assertEquals(originalJob.learningDevelopment, createdFromParcel.learningDevelopment, 0.001);
        assertEquals(originalJob.familyPlanningAssistance, createdFromParcel.familyPlanningAssistance, 0.001);
        assertEquals(originalJob.isJobOffer, createdFromParcel.isJobOffer);
        assertNotNull(createdFromParcel.location);
        assertEquals(originalJob.location.getCity(), createdFromParcel.location.getCity());
        assertEquals(originalJob.location.getState(), createdFromParcel.location.getState());
        assertEquals(originalJob.adjustedYearlySalary, createdFromParcel.adjustedYearlySalary, 0.001);
        assertEquals(originalJob.adjustedYearlyBonus, createdFromParcel.adjustedYearlyBonus, 0.001);
        assertEquals(originalJob.jobScore, createdFromParcel.jobScore, 0.001);

        parcel.recycle();
    }
}
