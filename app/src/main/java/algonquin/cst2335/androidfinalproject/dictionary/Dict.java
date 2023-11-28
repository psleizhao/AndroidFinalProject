package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dict {
    @ColumnInfo(name="dictName")
    public String dictName;

    @ColumnInfo(name="summary")
    public String summary;
    @ColumnInfo(name="srcUrl")
    protected String srcUrl;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="timeSent")
    protected String timeSent;

    @ColumnInfo(name="isSentButton")
    protected boolean isSaveButton;

    public Dict(String dictName, String imgUrl, String summary, String srcUrl) {
        this.dictName = dictName;
        this.timeSent = timeSent;
        this.summary = summary;
        this.isSaveButton = isSaveButton;
        this.srcUrl = srcUrl;
    }

    public String getDictName() {
        return dictName;
    }

    public String getSummary() {
        return summary;
    }
    public String getTimeSent() {
        return timeSent;
    }
    public boolean isSaveButton() { return isSaveButton;
    }
    public String getSrcUrl() {
        return srcUrl;

    }
}
