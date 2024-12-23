// ComparisonSettingsViewModelTest.java
package edu.gatech.seclass.jobcompare6300;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ComparisonSettingsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ComparisonSettingsRepository repository;

    @Mock
    private Application application;

    private ComparisonSettingsViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        MutableLiveData<ComparisonSettings> liveData = new MutableLiveData<>();
        when(repository.getSettings()).thenReturn(liveData);

        viewModel = new ComparisonSettingsViewModel(application, repository);
    }

    @Test
    public void testGetSettings() {
        ComparisonSettings mockSettings = new ComparisonSettings(2, 2, 2, 2, 2, 2);
        MutableLiveData<ComparisonSettings> liveData = new MutableLiveData<>();
        liveData.setValue(mockSettings);
        when(repository.getSettings()).thenReturn(liveData);

        viewModel = new ComparisonSettingsViewModel(application, repository);

        LiveData<ComparisonSettings> result = viewModel.getSettings();

        assertNotNull("LiveData should not be null", result);
        assertEquals("LiveData value should match the mock settings", mockSettings, result.getValue());
    }

    @Test
    public void testInsertSettings() {
        ComparisonSettings settings = new ComparisonSettings(3, 3, 3, 3, 3, 3);

        viewModel.insert(settings);

        verify(repository, times(1)).insertSettings(settings);
    }

    @Test
    public void testUpdateSettings() {
        ComparisonSettings settings = new ComparisonSettings(4, 4, 4, 4, 4, 4);

        viewModel.update(settings);

        verify(repository, times(1)).updateSettings(settings);
    }

    @Test
    public void testInsertDefaultSettings_WhenSettingsNull() {

        MutableLiveData<ComparisonSettings> liveData = new MutableLiveData<>();
        liveData.setValue(null);
        when(repository.getSettings()).thenReturn(liveData);

        viewModel = new ComparisonSettingsViewModel(application, repository);

        viewModel.insertDefaultSettings();

        ArgumentCaptor<ComparisonSettings> captor = ArgumentCaptor.forClass(ComparisonSettings.class);
        verify(repository, times(1)).insertSettings(captor.capture());

        ComparisonSettings insertedSettings = captor.getValue();
        assertNotNull("Inserted settings should not be null", insertedSettings);
        assertEquals(1, insertedSettings.salaryWeight, 0.001);
        assertEquals(1, insertedSettings.bonusWeight, 0.001);
        assertEquals(1, insertedSettings.retirementWeight, 0.001);
        assertEquals(1, insertedSettings.stockWeight, 0.001);
        assertEquals(1, insertedSettings.learningDevelopmentWeight, 0.001);
        assertEquals(1, insertedSettings.familyPlanningWeight, 0.001);
    }

    @Test
    public void testInsertDefaultSettings_WhenSettingsNotNull() {

        ComparisonSettings existingSettings = new ComparisonSettings(5, 5, 5, 5, 5, 5);
        MutableLiveData<ComparisonSettings> liveData = new MutableLiveData<>();
        liveData.setValue(existingSettings);
        when(repository.getSettings()).thenReturn(liveData);

        viewModel = new ComparisonSettingsViewModel(application, repository);

        viewModel.insertDefaultSettings();

        verify(repository, never()).insertSettings(any(ComparisonSettings.class));
    }
}
