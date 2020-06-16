package fr.azhot.mareu;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.repository.MeetingRepository;

public class MaReuApplication extends Application {

    private MeetingRepository mMeetingRepository;

    public MeetingRepository getMeetingRepository() {
        if (mMeetingRepository == null) {
            mMeetingRepository = DI.createMeetingRepository();
        }
        return mMeetingRepository;
    }

    @VisibleForTesting
    public void resetMeetingRepository() {
        mMeetingRepository = null;
    }
}