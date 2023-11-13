package algonquin.cst2335.androidfinalproject.recipe;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @ColumnInfo(name="message")
    protected String message;
    @ColumnInfo(name="TimeSent")
    protected String timeSent;
    @ColumnInfo(name="SendOrReceive")
    protected boolean isSentButton;

    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    public long id;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public void setTimeSent(String timeSent) {
//        this.timeSent = timeSent;
//    }
//
//    public void setSentButton(boolean sentButton) {
//        isSentButton = sentButton;
//    }
}
