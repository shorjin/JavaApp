package edu.gatech.seclass.jobcompare6300;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import android.os.Parcelable;
import android.os.Parcel;

// The job class with its attributes
@Entity
public class Job implements Parcelable{
    @ColumnInfo(name="job_id")
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String company;
    public int costOfLiving;
    public double yearlySalary;
    public double yearlyBonus;
    public int retirementPercentage;
    public double restrictedStock;
    public double learningDevelopment;
    public double familyPlanningAssistance;
    public boolean isJobOffer;
    @Embedded
    public Location location;

    // calculated fields
    @ColumnInfo(name = "adjusted_yearly_salary")
    public double adjustedYearlySalary;

    @ColumnInfo(name = "adjusted_yearly_bonus")
    public double adjustedYearlyBonus;

    @ColumnInfo(name = "job_score")
    public double jobScore;

    // Default constructor
    public Job() {
    }

    // Constructor
    public Job(String title, String company, Location location, double yearlySalary, double yearlyBonus,
               int retirementPercentage, double restrictedStock, double learningDevelopment,
               double familyPlanningAssistance, boolean isJobOffer, int costOfLiving) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.yearlySalary = yearlySalary;
        this.yearlyBonus = yearlyBonus;
        this.retirementPercentage = retirementPercentage;
        this.restrictedStock = restrictedStock;
        this.learningDevelopment = learningDevelopment;
        this.familyPlanningAssistance = familyPlanningAssistance;
        this.isJobOffer = isJobOffer;
        this.costOfLiving = costOfLiving;


    }

    // Parcelable constructor
    protected Job(Parcel in) {
        id = in.readInt();
        title = in.readString();
        company = in.readString();
        costOfLiving = in.readInt();
        yearlySalary = in.readDouble();
        yearlyBonus = in.readDouble();
        retirementPercentage = in.readInt();
        restrictedStock = in.readDouble();
        learningDevelopment = in.readDouble();
        familyPlanningAssistance = in.readDouble();
        isJobOffer = in.readByte() != 0;
        location = in.readParcelable(Location.class.getClassLoader());
        adjustedYearlySalary = in.readDouble();
        adjustedYearlyBonus = in.readDouble();
        jobScore = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(company);
        dest.writeInt(costOfLiving);
        dest.writeDouble(yearlySalary);
        dest.writeDouble(yearlyBonus);
        dest.writeInt(retirementPercentage);
        dest.writeDouble(restrictedStock);
        dest.writeDouble(learningDevelopment);
        dest.writeDouble(familyPlanningAssistance);
        dest.writeByte((byte) (isJobOffer ? 1 : 0));
        dest.writeParcelable(location, flags);
        dest.writeDouble(adjustedYearlySalary);
        dest.writeDouble(adjustedYearlyBonus);
        dest.writeDouble(jobScore);
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public void calculateFields(ComparisonSettings weights) {
        this.adjustedYearlySalary = calculateAYS();
        this.adjustedYearlyBonus = calculateAYB();
        this.jobScore = calculateScore(weights);
    }

    public double calculateAYS() {
        return yearlySalary / (costOfLiving / 100.0);
    }

    public double calculateAYB() {
        return yearlyBonus / (costOfLiving / 100.0);
    }

    // method to calculate Job rank based on weight assignment on the factors.
    public double calculateScore(@NonNull ComparisonSettings weights){
        double salaryComponent = adjustedYearlySalary * weights.salaryWeight;
        double bonusComponent = adjustedYearlyBonus * weights.bonusWeight;
        double retirementComponent = (yearlySalary * (retirementPercentage / 100.0)) * weights.retirementWeight;
        double stockComponent = (restrictedStock / 3.0) * weights.stockWeight;
        double learningComponent = learningDevelopment * weights.learningDevelopmentWeight;
        double familyPlanningComponent = familyPlanningAssistance * weights.familyPlanningWeight;

        return salaryComponent + bonusComponent + retirementComponent + stockComponent +
                learningComponent + familyPlanningComponent;
    }

    // getters for location attributes
    public String getLocationCity() {
        return location.getCity();
    }

    public String getLocationState() {
        return location.getState();
    }

}
