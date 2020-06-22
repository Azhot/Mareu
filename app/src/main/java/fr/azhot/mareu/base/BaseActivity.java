package fr.azhot.mareu.base;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import fr.azhot.mareu.MaReuApplication;
import fr.azhot.mareu.repository.MeetingRepository;
import fr.azhot.mareu.repository.UserRepository;

public abstract class BaseActivity extends AppCompatActivity {

    public MeetingRepository getMeetingRepository() {
        return ((MaReuApplication) getApplication()).getMeetingRepository();
    }

    public UserRepository getUserRepository() {
        return ((MaReuApplication) getApplication()).getUserRepository();
    }

    @VisibleForTesting
    public void resetMeetingRepository() {
        ((MaReuApplication) getApplication()).resetMeetingRepository();
    }
}
