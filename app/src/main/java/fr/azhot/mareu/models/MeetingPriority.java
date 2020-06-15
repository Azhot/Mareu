package fr.azhot.mareu.models;

import java.util.ArrayList;
import java.util.List;

import fr.azhot.mareu.R;

/**
 * Enum representing priorities for {@link Meeting} objects
 */
public enum MeetingPriority {
    LOW(R.string.low),
    AVERAGE(R.string.average),
    HIGH(R.string.high);

    private final int mStringResource;

    /**
     * Constructor
     * @param stringResource a string resource id
     */
    MeetingPriority(final int stringResource) {
        this.mStringResource = stringResource;
    }

    /**
     * @return a list of {@link MeetingPriority} members' mStringResource
     */
    public static List<Integer> getMeetingPrioritiesStringResources() {
        List<Integer> meetingPrioritiesStringResources = new ArrayList<>();
        for (MeetingPriority meetingPriority : MeetingPriority.values()) {
            meetingPrioritiesStringResources.add(meetingPriority.mStringResource);
        }
        return meetingPrioritiesStringResources;
    }

    /**
     * @param position an {@link Integer} representing the position of the requested member in the enum
     * @return a {@link MeetingPriority} member
     */
    public static MeetingPriority getMeetingPriorityByPosition(final int position) {
        return MeetingPriority.values()[position];
    }
}