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

    public Dict(long id, String title, String summary, String srcUrl){

    }

    public Dict(String dictName, String imgUrl, String summary, String srcUrl) {
        this.dictName = dictName;
        this.summary = summary;
        this.id = id;
        this.srcUrl = srcUrl;
    }

    public String getDictName() {
        return dictName;
    }

    public String getSummary() {
        return summary;
    }

    public long getId() { return id; }

    public String getSrcUrl() {
        return srcUrl;
    }
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public void setId(long id) {
        this.id = id;
    }
}
