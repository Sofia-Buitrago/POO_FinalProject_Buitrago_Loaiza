package domain;

import java.io.Serializable;

/**
 * Represents a category of video games (for example: Action, Sports).
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;

    /** Creates a category with valid data. */
    public Category(int id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    public int getId() {
        return id;
    }

    // Private: the id is set once and never changes.
    private void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("The category id must be greater than 0.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The category name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("The category description cannot be null.");
        }
        this.description = description.trim();
    }
}