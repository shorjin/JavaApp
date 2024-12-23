package edu.gatech.seclass.jobcompare6300;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ComparisonSettings {
    @ColumnInfo(name="settings_id")
    @PrimaryKey
    // make sure only one entry can exist
    public int id = 1;

    public int salaryWeight;
    public int bonusWeight;
    public int retirementWeight;
    public int stockWeight;
    public int learningDevelopmentWeight;
    public int familyPlanningWeight;

    // Default constructor
    public ComparisonSettings() {
        // Default weights are 1
        this.salaryWeight = 1;
        this.bonusWeight = 1;
        this.retirementWeight = 1;
        this.stockWeight = 1;
        this.learningDevelopmentWeight = 1;
        this.familyPlanningWeight = 1;
    }

    // Constructor with parameters
    public ComparisonSettings(int salaryWeight, int bonusWeight, int retirementWeight,
                              int stockWeight, int learningDevelopmentWeight,
                              int familyPlanningWeight) {
        this.salaryWeight = salaryWeight;
        this.bonusWeight = bonusWeight;
        this.retirementWeight = retirementWeight;
        this.stockWeight = stockWeight;
        this.learningDevelopmentWeight = learningDevelopmentWeight;
        this.familyPlanningWeight = familyPlanningWeight;
    }
}
