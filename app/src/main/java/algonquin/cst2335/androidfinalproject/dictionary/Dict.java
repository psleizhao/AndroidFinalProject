package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The {@code Dict} class represents a dictionary entry with information such as
 * dictionary name, summary, source URL, unique identifier, and a flag indicating
 * whether it is associated with a save button. This class is annotated with
 * {@link Entity} to be used with Room database.
 *
 * @author Yuling Guo
 * @version 1.0
 * @since 2023-11-29
 */
@Entity
public class Dict {
    /**
     * The name of the dictionary entry.
     */
    @ColumnInfo(name="dictName")
    public String dictName;

    /**
     * The summary or definition of the dictionary entry.
     */
    @ColumnInfo(name="summary")
    public String summary;

    /**
     * The source URL associated with the dictionary entry.
     */
    @ColumnInfo(name="srcUrl")
    protected String srcUrl;

    /**
     * The unique identifier for the dictionary entry, generated automatically.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    /**
     * A flag indicating whether the dictionary entry is associated with a save button.
     */
    @ColumnInfo(name="isSaveButton")
    protected boolean isSaveButton;

    /**
     * Default constructor for the {@code Dict} class.
     */
    public Dict(){}

    /**
     * Constructor for creating a {@code Dict} instance with specified word and definition.
     *
     * @param word The dictionary entry word.
     * @param def The dictionary entry definition or summary.
     */
    public Dict(String word, String def) {
        this.dictName = word;
        this.summary = def;
    }

    /**
     * Gets the name of the dictionary entry.
     *
     * @return The dictionary entry name.
     */
    public String getDictName() {
        return dictName;
    }

    /**
     * Sets the name of the dictionary entry.
     *
     * @param dictName The new dictionary entry name.
     */
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    /**
     * Gets the summary or definition of the dictionary entry.
     *
     * @return The dictionary entry summary or definition.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary or definition of the dictionary entry.
     *
     * @param summary The new summary or definition.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gets the source URL associated with the dictionary entry.
     *
     * @return The source URL.
     */
    public String getSrcUrl() {
        return srcUrl;
    }

    /**
     * Sets the source URL associated with the dictionary entry.
     *
     * @param srcUrl The new source URL.
     */
    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    /**
     * Gets the unique identifier for the dictionary entry.
     *
     * @return The unique identifier.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the dictionary entry.
     *
     * @param id The new unique identifier.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Checks if the dictionary entry is associated with a save button.
     *
     * @return {@code true} if associated with a save button, {@code false} otherwise.
     */
    public boolean isSaveButton() {
        return isSaveButton;
    }

    /**
     * Sets the flag indicating whether the dictionary entry is associated with a save button.
     *
     * @param saveButton {@code true} if associated with a save button, {@code false} otherwise.
     */
    public void setSaveButton(boolean saveButton) {
        isSaveButton = saveButton;
    }
}
