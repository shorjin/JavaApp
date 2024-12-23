package edu.gatech.seclass.jobcompare6300;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ComparisonSettingsDAO {
    // Add New settings
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSettings(ComparisonSettings settings);

    // Update settings
    @Update
    void updateSettings(ComparisonSettings settings);

    // Delete settings
    @Delete
    void deleteSettings(ComparisonSettings settings);

    // LiveData for the comparison settings
    @Query("SELECT * FROM ComparisonSettings LIMIT 1")
    LiveData<ComparisonSettings> getSettings();
}
