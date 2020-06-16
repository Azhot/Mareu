package fr.azhot.mareu.base;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import fr.azhot.mareu.MaReuApplication;
import fr.azhot.mareu.repository.MeetingRepository;

public class BaseActivity extends AppCompatActivity {

    public MeetingRepository getMeetingRepository() {
        return ((MaReuApplication) getApplication()).getMeetingRepository();
    }

    @VisibleForTesting
    public void resetMeetingRepository() {
        ((MaReuApplication) getApplication()).resetMeetingRepository();
    }
}
