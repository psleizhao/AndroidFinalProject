/**
 * Name: Zhicheng He 041086226
 * Course Section: CST2335 022
 * Description: This is the final project for the course CST2335 Mobile Graphical Interface Programming.
 *
 */

package algonquin.cst2335.androidfinalproject.music;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a music with details such as title,duration, image URL, album name, album image, album ID, file name, and ID.
 * This class is annotated with Room annotations for database storage.
 */
@Entity
public class Music {
    /** Title of the song. */
    @ColumnInfo(name = "songTitle")
    protected String songTitle;

    /** URL of the album image. */
    @ColumnInfo(name = "imgUrl")
    protected String imgUrl;

    /** Name of the album. */
    @ColumnInfo(name = "albumName")
    protected String albumName;

    /** Duration of the song. */
    @ColumnInfo(name = "duration")
    protected int duration;

    /** Name of the file. */
    @ColumnInfo(name = "fileName")
    protected String fileName;

    /** Unique identifier for the album. */
    @ColumnInfo(name = "albumId")
    protected long albumId;

    /** Unique identifier for the music. */
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    /**
     * Constructs a new Music instance.
     *
     * @param id        The unique identifier for the music.
     * @param songTitle The title of the song.
     * @param duration  The duration of the song in seconds.
     * @param albumName The name of the album the song belongs to.
     * @param imgUrl    The URL for the song's image.
     * @param albumId   The unique identifier for the album.
     * @param fileName  The name of the file.
     */
    public Music(long id, String songTitle, int duration, String albumName, String imgUrl, long albumId, String fileName) {
        this.id = id;
        this.songTitle = songTitle;
        this.albumName = albumName;
        this.duration = duration;
        this.imgUrl = imgUrl;
        this.albumId = albumId;
        this.fileName = fileName;

    }

    /**
     * Gets the song title.
     *
     * @return The title of the song.
     */

    public String getSongTitle() {
        return songTitle;
    }

    /**
     * Sets the song title.
     *
     * @param songTitle The title of the song to set.
     */
    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    /**
     * Gets the image URL of the song.
     *
     * @return The URL of the song's image.
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * Sets the image URL of the song.
     *
     * @param imgUrl The URL of the song's image to set.
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * Gets the album name of the song.
     *
     * @return The name of the album.
     */

    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets the album name of the song.
     *
     * @param albumName The name of the album to set.
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Gets the duration of the song.
     *
     * @return The duration of the song in seconds.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the song.
     *
     * @param duration The duration of the song in seconds to set.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the file name of the song.
     *
     * @return The name of the file.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name of the song.
     *
     * @param fileName The name of the file to set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the album ID of the song.
     *
     * @return The unique identifier of the album.
     */
    public long getAlbumId() {
        return albumId;
    }

    /**
     * Sets the album ID of the song.
     *
     * @param albumId The unique identifier of the album to set.
     */
    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    /**
     * Gets the ID of the song.
     *
     * @return The unique identifier of the song.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the song.
     *
     * @param id The unique identifier of the song to set.
     */
    public void setId(long id) {
        this.id = id;
    }
}
