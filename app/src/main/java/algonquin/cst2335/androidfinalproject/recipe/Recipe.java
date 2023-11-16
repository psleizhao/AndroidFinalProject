package algonquin.cst2335.androidfinalproject.recipe;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {
    @ColumnInfo(name="recipeName")
    protected String recipeName;
    @ColumnInfo(name="imgUrl")
    protected String imgUrl;
    @ColumnInfo(name="summary")
    protected String summary;
    @ColumnInfo(name="srcUrl")
    protected String srcUrl;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    public Recipe(String recipeName, String imgUrl, String summary, String srcUrl) {
        this.recipeName = recipeName;
        this.imgUrl = imgUrl;
        this.summary = summary;
        this.srcUrl = srcUrl;
    }

    public String getRecipeName() {
        return recipeName;
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
