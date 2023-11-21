package algonquin.cst2335.androidfinalproject.music;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Music {
    @ColumnInfo(name="title")
    protected String title;

    @ColumnInfo(name="artist")
    protected String artist;

    @ColumnInfo(name="album")
    protected String album;

    @ColumnInfo(name="coverUrl")
    protected String coverUrl;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    public Music(String title, String artist, String album, String coverUrl) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.coverUrl = coverUrl;
    }

    // Getters
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getCoverUrl() { return coverUrl; }
}
