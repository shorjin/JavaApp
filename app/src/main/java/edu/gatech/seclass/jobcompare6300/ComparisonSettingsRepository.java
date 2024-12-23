package edu.gatech.seclass.jobcompare6300;

import android.app.Application;

import androidx.lifecycle.LiveData;


public class ComparisonSettingsRepository {

    private final ComparisonSettingsDAO comparisonsettingsdao;
    private final LiveData<ComparisonSettings> settings;

    public ComparisonSettingsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        comparisonsettingsdao = db.comparisonSettingsdao();
        settings = comparisonsettingsdao.getSettings();


    }

    // Return the LiveData for the comparison settings
    public LiveData<ComparisonSettings> getSettings() {
        return settings;
    }

    // Insert comparison settings
    public void insertSettings(final ComparisonSettings settings) {
        AppDatabase.databaseWriteExecutor.execute(() -> comparisonsettingsdao.insertSettings(settings));
    }

    // Update comparison settings
    public void updateSettings(final ComparisonSettings settings) {
        AppDatabase.databaseWriteExecutor.execute(() -> comparisonsettingsdao.updateSettings(settings));
    }


}
