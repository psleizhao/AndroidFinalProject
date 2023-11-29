
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
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * Dict dictionaryEntry = new Dict("Word", "Definition");
 * dictionaryEntry.setSrcUrl("https://example.com");
 * dictionaryEntry.setSaveButton(true);
 * // ... (use other setter methods as needed)
 * }
 * </pre>
 *
 * @author Yuling Guo
 * @version 1.0
 * @since 2023-11-29
 */
@Entity
public class Dict {
    @ColumnInfo(name="dictName")
    public String dictName;

    @ColumnInfo(name="summary")
    public String summary;
    @ColumnInfo(name="srcUrl")
    protected String srcUrl;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="isSentButton")
    protected boolean isSaveButton;

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSaveButton() {
        return isSaveButton;
    }

    public void setSaveButton(boolean saveButton) {
        isSaveButton = saveButton;
    }

    public Dict(){


    }

    public Dict(String word, String def) {
        this.dictName = word;
        this.summary = def;

    }

}
