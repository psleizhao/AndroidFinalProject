package algonquin.cst2335.androidfinalproject.music;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Music {
    @ColumnInfo(name = "musicName")
    protected String musicName;
    @ColumnInfo(name = "imgUrl")
    protected String imgUrl;
    @ColumnInfo(name = "summary")
    protected String summary;
    @ColumnInfo(name = "srcUrl")
    protected String srcUrl;

    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;


    public Music(String musicName, String imgUrl, String summary, String srcUrl, long id) {
        this.musicName = musicName;
        this.imgUrl = imgUrl;
        this.summary = summary;
        this.srcUrl = srcUrl;
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
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

    public long getId() {
        return id;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
