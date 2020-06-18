package fr.azhot.mareu;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import fr.azhot.mareu.di.DI;
import fr.azhot.mareu.repository.MeetingRepository;
import fr.azhot.mareu.repository.UserRepository;

public class MaReuApplication extends Application {

    private MeetingRepository mMeetingRepository;
    private UserRepository mUserRepository;

    public MeetingRepository getMeetingRepository() {
        if (mMeetingRepository == null) {
            mMeetingRepository = DI.createMeetingRepository();
        }
        return mMeetingRepository;
    }

    public UserRepository getUserRepository() {
        if (mUserRepository == null) {
            mUserRepository = DI.createUserRepository();
        }
        return mUserRepository;
    }

    @VisibleForTesting
    public void resetMeetingRepository() {
        mMeetingRepository = null;
    }
}