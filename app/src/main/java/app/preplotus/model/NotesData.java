package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotesData {

    @SerializedName("NoteId")
    @Expose
    private String NoteId;

    @SerializedName("NoteTitle")
    @Expose
    private String NoteTitle;

    @SerializedName("NoteImage")
    @Expose
    private String NoteImage;

    @SerializedName("NoteHasTopics")
    @Expose
    private String NoteHasTopics;

    public String getNoteId() {
        return NoteId;
    }

    public void setNoteId(String noteId) {
        NoteId = noteId;
    }

    public String getNoteTitle() {
        return NoteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        NoteTitle = noteTitle;
    }

    public String getNoteImage() {
        return NoteImage;
    }

    public void setNoteImage(String noteImage) {
        NoteImage = noteImage;
    }

    public String getNoteHasTopics() {
        return NoteHasTopics;
    }

    public void setNoteHasTopics(String noteHasTopics) {
        NoteHasTopics = noteHasTopics;
    }
}
