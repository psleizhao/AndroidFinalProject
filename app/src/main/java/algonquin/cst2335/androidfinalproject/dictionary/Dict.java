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

    @ColumnInfo(name="isSentButton")
    protected boolean isSaveButton;

    public Dict(){

    }

    public Dict(String dictName, String imgUrl, String summary, String srcUrl) {
        this.dictName = dictName;
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

    public boolean isSaveButton() { return isSaveButton;}

    public String getSrcUrl() {
        return srcUrl;

    }
}
