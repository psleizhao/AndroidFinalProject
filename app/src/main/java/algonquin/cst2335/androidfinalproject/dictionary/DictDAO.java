package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DictDAO {
    @Insert
    long insertDict(Dict d);
    @Query("Select * from Dict")
    List<Dict> getAllDicts();
    @Delete
    void deleteDict(Dict d);
}