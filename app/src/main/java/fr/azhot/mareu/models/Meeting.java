package fr.azhot.mareu.models;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Class representing a meeting
 */
public class Meeting {
    private final String mId;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private MeetingRoom mMeetingRoom;
    private String mSubject;
    private List<User> mParticipants;
    private MeetingPriority mMeetingPriority;
    private String mNotes;

    /**
     * Minimum constructor for a {@link Meeting}
     *
     * @param startTime       an instance of {@link Calendar} representing the start time of the meeting
     * @param subject         a {@link String} representing the meeting subject
     * @param participants    an {@link List<String>} representing for each index a meeting participant
     * @param meetingRoom     a {@link MeetingRoom} member representing the meeting room name
     * @param meetingPriority a {@link MeetingPriority} member representing the meeting priority
     */
    public Meeting(Calendar startTime, String subject, List<User> participants, MeetingRoom meetingRoom, MeetingPriority meetingPriority) {
        this.mId = UUID.randomUUID().toString();
        this.mStartTime = startTime;
        this.mEndTime = (Calendar) startTime.clone();
        this.mEndTime.add(Calendar.MINUTE, 45);
        this.mSubject = subject;
        this.mParticipants = participants;
        this.mMeetingRoom = meetingRoom;
        this.mMeetingPriority = meetingPriority;
    }

    /**
     * Full constructor for a {@link Meeting} with end time specified
     *
     * @param startTime       an instance of {@link Calendar} representing the start time of the meeting
     * @param endTime         an instance of {@link Calendar} representing the end time of the meeting
     * @param subject         a {@link String} representing the meeting subject
     * @param participants    an {@link List<String>} representing for each index a meeting participant
     * @param meetingRoom     a {@link MeetingRoom} member representing the meeting room name
     * @param meetingPriority a {@link MeetingPriority} member representing the meeting priority
     * @param notes           a {@link String} representing the meeting's notes
     */
    public Meeting(Calendar startTime, Calendar endTime, String subject, List<User> participants, MeetingRoom meetingRoom, MeetingPriority meetingPriority, String notes) {
        this.mId = UUID.randomUUID().toString();
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mSubject = subject;
        this.mParticipants = participants;
        this.mMeetingRoom = meetingRoom;
        this.mMeetingPriority = meetingPriority;
        this.mNotes = notes;
    }

    /**
     * @return the meetings's {@link UUID}
     */
    public String getId() {
        return this.mId;
    }

    /**
     * @return the meetings's start time
     */
    public Calendar getStartTime() {
        return this.mStartTime;
    }

    /**
     * Sets the start time of the meeting
     *
     * @param startTime a {@link Calendar} representing the time at which the meeting will start
     */
    public void setStartTime(final Calendar startTime) {
        this.mStartTime = startTime;
    }


    /**
     * @return the meetings's end time
     */
    public Calendar getEndTime() {
        return this.mEndTime;
    }

    /**
     * Sets the end time of the meeting
     *
     * @param endTime a {@link Calendar} representing the time at which the meeting will end
     */
    public void setEndTime(final Calendar endTime) {
        this.mEndTime = endTime;
    }


    /**
     * @return the subject of the meeting
     */
    public String getSubject() {
        return this.mSubject;
    }

    /**
     * Sets the subject of a meeting
     *
     * @param subject a {@link String} representing the meeting subject to set
     */
    public void setSubject(final String subject) {
        this.mSubject = subject;
    }

    /**
     * @return the participants of the meeting
     */
    public List<User> getParticipants() {
        return this.mParticipants;
    }

    /**
     * Sets the participants of a meeting
     *
     * @param participants a {@link List<User>} representing the meeting participants to set
     */
    public void setParticipants(final List<User> participants) {
        this.mParticipants = participants;
    }

    /**
     * @return a {@link MeetingRoom} member representing the meeting room name
     */
    public MeetingRoom getMeetingRoom() {
        return this.mMeetingRoom;
    }

    /**
     * Sets the meeting room of the meeting
     *
     * @param meetingRoom a {@link MeetingRoom} member representing the meeting room name
     */
    public void setMeetingRoom(final MeetingRoom meetingRoom) {
        this.mMeetingRoom = meetingRoom;
    }

    /**
     * @return a {@link MeetingPriority} member representing the meeting priority
     */
    public MeetingPriority getMeetingPriority() {
        return this.mMeetingPriority;
    }

    /**
     * Sets the meeting priority of the meeting
     *
     * @param meetingPriority a {@link MeetingPriority} member representing the meeting priority
     */
    public void setMeetingPriority(final MeetingPriority meetingPriority) {
        this.mMeetingPriority = meetingPriority;
    }

    /**
     * @return the notes of the meeting
     */
    public String getNotes() {
        return this.mNotes;
    }

    /**
     * Sets the notes of the meeting
     *
     * @param notes a {@link String} representing the notes to set
     */
    public void setNotes(final String notes) {
        this.mNotes = notes;
    }
}
