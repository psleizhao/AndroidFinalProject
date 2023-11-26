package algonquin.cst2335.androidfinalproject.recipe;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a recipe with details such as name, image URL, summary, source URL, and ID.
 * This class is annotated with Room annotations for database storage.
 */
@Entity
public class Recipe {
    /** Name of the recipe. */
    @ColumnInfo(name="recipeName")
    protected String recipeName;

    /** URL of the recipe image. */
    @ColumnInfo(name="imgUrl")
    protected String imgUrl;

    /** Summary or description of the recipe. */
    @ColumnInfo(name="summary")
    protected String summary;

    /** Source URL of the recipe. */
    @ColumnInfo(name="srcUrl")
    protected String srcUrl;

    /** Unique identifier for the recipe. */
    @PrimaryKey
    @ColumnInfo(name="id")
    public long id;

    /**
     * Constructs a new Recipe object.
     *
     * @param recipeName The name of the recipe.
     * @param imgUrl     The URL of the recipe image.
     * @param summary    The summary or description of the recipe.
     * @param srcUrl     The source URL of the recipe.
     * @param id         The unique identifier for the recipe.
     */
    public Recipe(String recipeName, String imgUrl, String summary, String srcUrl, long id) {
        this.recipeName = recipeName;
        this.imgUrl = imgUrl;
        this.summary = summary;
        this.srcUrl = srcUrl;
        this.id = id;
    }

    /**
     * Gets the name of the recipe.
     *
     * @return The name of the recipe.
     */
    public String getRecipeName() {
        return recipeName;
    }

    /**
     * Gets the URL of the recipe image.
     *
     * @return The URL of the recipe image.
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * Gets the summary or description of the recipe.
     *
     * @return The summary of the recipe.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Gets the source URL of the recipe.
     *
     * @return The source URL of the recipe.
     */
    public String getSrcUrl() {
        return srcUrl;
    }

    /**
     * Gets the unique identifier of the recipe.
     *
     * @return The unique identifier of the recipe.
     */
    public long getId() { return id; }

    /**
     * Sets the name of the recipe.
     *
     * @param recipeName The new name of the recipe.
     */
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    /**
     * Sets the URL of the recipe image.
     *
     * @param imgUrl The new URL of the recipe image.
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * Sets the summary or description of the recipe.
     *
     * @param summary The new summary of the recipe.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Sets the source URL of the recipe.
     *
     * @param srcUrl The new source URL of the recipe.
     */
    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    /**
     * Sets the unique identifier of the recipe.
     *
     * @param id The new unique identifier of the recipe.
     */
    public void setId(long id) {
        this.id = id;
    }
}
