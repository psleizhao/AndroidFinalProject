package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Dict.class}, version = 1)
public abstract class DictDatabase extends RoomDatabase {
    public abstract DictDAO DictDAO();
}
