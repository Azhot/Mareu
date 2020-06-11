package fr.azhot.mareu.models;

import java.util.ArrayList;
import java.util.List;

import fr.azhot.mareu.R;

/**
 * Enum representing meeting rooms for {@link Meeting} objects
 */
public enum MeetingRoom {
    MARIO(R.string.mario_meeting_room),
    LUIGI(R.string.luigi_meeting_room),
    PEACH(R.string.peach_meeting_room),
    TOAD(R.string.toad_meeting_room),
    YOSHI(R.string.yoshi_meeting_room),
    DAISY(R.string.daisy_meeting_room),
    BOWSER(R.string.bowser_meeting_room),
    HARMONIE(R.string.harmonie_meeting_room),
    WALUIGI(R.string.waluigi_meeting_room),
    WARIO(R.string.wario_meeting_room);

    private final int mStringResource;

    /**
     * Constructor
     * @param stringResource a string resource id
     */
    MeetingRoom(final int stringResource) {
        this.mStringResource = stringResource;
    }

    /**
     * @return a {@link MeetingRoom} member's mStringResource
     */
    public int getStringResource() {
        return mStringResource;
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
}
