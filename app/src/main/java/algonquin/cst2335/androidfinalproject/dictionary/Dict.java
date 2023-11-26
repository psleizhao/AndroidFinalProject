package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Dict {
    @ColumnInfo(name="dictName")
    public String dictName;
    @ColumnInfo(name="imgUrl")
    protected String imgUrl;
    @ColumnInfo(name="summary")
    public String summary;
    @ColumnInfo(name="srcUrl")
    protected String srcUrl;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    public Dict(String dictName, String imgUrl, String summary, String srcUrl) {
        this.dictName = dictName;
        this.imgUrl = imgUrl;
        this.summary = summary;
        this.srcUrl = srcUrl;
    }

    public String getDictName() {
        return dictName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSummary() {
        return summary;
    }

    public String getSrcUrl() {
        return srcUrl;
    }
}
