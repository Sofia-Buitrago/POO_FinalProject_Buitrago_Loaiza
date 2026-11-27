package domain;

import java.io.Serializable;

/**
 * Base class for every game sold in the store.
 * It is abstract: a game is always physical or digital.
 */
public abstract class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String title;
    private String platform;
    private double basePrice;
    private Category category;
    private boolean active;

    /** Creates an active game with valid data. */
    public Game(String code, String title, String platform,
                double basePrice, Category category) {
        setCode(code);
        setTitle(title);
        setPlatform(platform);
        setBasePrice(basePrice);
        setCategory(category);
        this.active = true;
    }

    public String getCode() {
        return code;
    }

    // Private: the code is set once and never changes.
    private void setCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("The game code cannot be empty.");
        }
        this.code = code.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("The game title cannot be empty.");
        }
        this.title = title.trim();
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        if (platform == null || platform.isBlank()) {
            throw new IllegalArgumentException("The platform cannot be empty.");
        }
        this.platform = platform.trim();
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        if (basePrice <= 0) {
            throw new IllegalArgumentException("The base price must be greater than 0.");
        }
        this.basePrice = basePrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("The game must have a category.");
        }
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    /** Hides the game from new sales but keeps its history. */
    public void deactivate() {
        active = false;
    }

    public void activate() {
        active = true;
    }

    /** Tells if the requested quantity can be delivered. */
    public abstract boolean isAvailable(int quantity);//firma de metodo

    /** Delivers the quantity (reduces stock or uses license keys). */
    public abstract void deliver(int quantity);

    /** Returns "Physical" or "Digital". */
    public abstract String getType();

    @Override
    public boolean equals(Object obj) {
        if (obj == null ){
            return false;
        }
        
        if (this == obj){
            return true;
        }

        Game g = (Game) obj;// casteo, obligo a una variable a ser de otro tipo

        return this.code.equals(g.getCode())||this.getTitle().equalsIgnoreCase(g.getTitle());
    }
}