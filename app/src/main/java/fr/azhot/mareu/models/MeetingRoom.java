package fr.azhot.mareu.models;

import java.util.ArrayList;
import java.util.List;

import fr.azhot.mareu.R;

/**
 * Enum representing meeting rooms for {@link Meeting} objects
 */
public enum MeetingRoom {
    MARIO(R.string.mario),
    LUIGI(R.string.luigi),
    PEACH(R.string.peach),
    TOAD(R.string.toad),
    YOSHI(R.string.yoshi),
    DAISY(R.string.daisy),
    BOWSER(R.string.bowser),
    HARMONIE(R.string.harmonie),
    WALUIGI(R.string.waluigi),
    WARIO(R.string.wario);

    private final int mStringResource;

    /**
     * Constructor
     *
     * @param stringResource a string resource id
     */
    MeetingRoom(final int stringResource) {
        this.mStringResource = stringResource;
    }

    /**
     * @return a list of {@link MeetingRoom} members' mStringResource
     */
    public static List<Integer> getMeetingRoomsStringResources() {
        List<Integer> meetingRoomsStringResources = new ArrayList<>();
        for (MeetingRoom meetingRoom : MeetingRoom.values()) {
            meetingRoomsStringResources.add(meetingRoom.mStringResource);
        }
        return meetingRoomsStringResources;
    }

    /**
     * @param position an {@link Integer} representing the position of the requested member in the enum
     * @return a {@link MeetingRoom} member
     */
    public static MeetingRoom getMeetingRoomByPosition(final int position) {
        return MeetingRoom.values()[position];
    }

    /**
     * @return a {@link MeetingRoom} member's mStringResource
     */
    public int getStringResource() {
        return mStringResource;
    }
}
