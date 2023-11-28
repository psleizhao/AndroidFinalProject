package algonquin.cst2335.androidfinalproject.music;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Music {
    @ColumnInfo(name = "songTitle")
    protected String songTitle;
    @ColumnInfo(name = "imgUrl")
    protected String imgUrl;
    @ColumnInfo(name = "albumName")
    protected String albumName;
    @ColumnInfo(name = "duration")
    protected int duration;

    @ColumnInfo(name = "fileName")
    protected String fileName;

    @ColumnInfo(name = "albumId")
    protected long albumId;

    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;


    public Music(long id, String songTitle, int duration, String albumName, String imgUrl, long albumId,String fileName) {
        this.id = id;
        this.songTitle = songTitle;
        this.albumName = albumName;
        this.duration = duration;
        this.imgUrl = imgUrl;
        this.albumId = albumId;
        this.fileName = fileName;

    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
