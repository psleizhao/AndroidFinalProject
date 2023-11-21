package algonquin.cst2335.androidfinalproject.music;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MusicDAO {

    @Insert
    long insertMusic(Music m);

    @Query("Select * from Music")
    List<Music> getAllMusics();

    @Delete
    void deleteMusic(Music m);
}