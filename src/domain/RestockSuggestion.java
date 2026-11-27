package domain;

import java.io.Serializable;

/**
 * A recommendation to restock a physical game.
 * The store creates it using simple rules (decision support).
 */
public class RestockSuggestion implements Serializable {

    public static final String HIGH = "HIGH";
    public static final String MEDIUM = "MEDIUM";

    private PhysicalGame game;
    private int unitsSold;
    private int suggestedQuantity;
    private String priority;

    /** Creates a suggestion with valid data. */
    public RestockSuggestion(PhysicalGame game, int unitsSold, int suggestedQuantity, String priority) {
        setGame(game);
        setUnitsSold(unitsSold);
        setSuggestedQuantity(suggestedQuantity);
        setPriority(priority);
    }

    public PhysicalGame getGame() {
        return game;
    }

    // Private: a suggestion is never changed after it is created.
    private void setGame(PhysicalGame game) {
        if (game == null) {
            throw new IllegalArgumentException("The game cannot be null.");
        }
        this.game = game;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    private void setUnitsSold(int unitsSold) {
        if (unitsSold < 0) {
            throw new IllegalArgumentException("The units sold cannot be negative.");
        }
        this.unitsSold = unitsSold;
    }

    public int getSuggestedQuantity() {
        return suggestedQuantity;
    }

    private void setSuggestedQuantity(int suggestedQuantity) {
        if (suggestedQuantity <= 0) {
            throw new IllegalArgumentException("The suggested quantity must be greater than 0.");
        }
        this.suggestedQuantity = suggestedQuantity;
    }

    public String getPriority() {
        return priority;
    }

    private void setPriority(String priority) {
        if (!HIGH.equals(priority) && !MEDIUM.equals(priority)) {
            throw new IllegalArgumentException("The priority must be HIGH or MEDIUM.");
        }
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "[" + priority + "] " + game.getTitle() + " (" + game.getCode() + ")"
                + " - stock: " + game.getStock()
                + ", minimum: " + game.getMinStock()
                + ", sold: " + unitsSold
                + ", suggested order: " + suggestedQuantity;
    }
}