// ComparisonSettingsViewModel.java
package edu.gatech.seclass.jobcompare6300;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ComparisonSettingsViewModel extends AndroidViewModel {

    private final ComparisonSettingsRepository repository;
    private final LiveData<ComparisonSettings> settings;

    // Primary constructor
    public ComparisonSettingsViewModel(@NonNull Application application) {
        super(application);
        repository = new ComparisonSettingsRepository(application);
        settings = repository.getSettings();
    }

    // Secondary constructor (testing)
    public ComparisonSettingsViewModel(@NonNull Application application, ComparisonSettingsRepository repository) {
        super(application);
        this.repository = repository;
        settings = repository.getSettings();
    }

    // Get all settings info
    public LiveData<ComparisonSettings> getSettings() {
        return settings;
    }

    // Insert default settings on startup
    public void insertDefaultSettings() {
        settings.observeForever(existingSettings -> {
            if (existingSettings == null) {
                ComparisonSettings defaultSettings = new ComparisonSettings(1, 1, 1, 1, 1, 1);
                insert(defaultSettings);
            }
        });
    }

    // Insert functionality
    public void insert(ComparisonSettings settings) {
        repository.insertSettings(settings);
    }

    // Update functionality
    public void update(ComparisonSettings settings) {
        repository.updateSettings(settings);
    }
}
