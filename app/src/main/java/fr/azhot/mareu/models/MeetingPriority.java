package fr.azhot.mareu.models;

import fr.azhot.mareu.R;

/**
 * Enum representing priorities for {@link Meeting} objects
 */
public enum MeetingPriority {
    LOW(R.string.low_priority),
    AVERAGE(R.string.average_priority),
    HIGH(R.string.high_priority);

    private final int mStringResource;

    /**
     * Constructor
     * @param stringResource a string resource id
     */
    MeetingPriority(final int stringResource) {
        this.mStringResource = stringResource;
    }

    /**
     * @return an int array of {@link MeetingPriority} members' mStringResource
     */
    public static int[] getMeetingPrioritiesStringResources() {
        int[] meetingPrioritiesStringResources = new int[MeetingPriority.values().length];
        for (int i = 0; i < meetingPrioritiesStringResources.length; i++) {
            meetingPrioritiesStringResources[i] = MeetingPriority.values()[i].mStringResource;
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