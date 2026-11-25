package domain;

import java.io.Serializable;

/**
 * One line of a sale: a game, the quantity sold and the price at that moment.
 * The unit price is saved here so the history does not change
 * if the price of the game changes later.
 */
public class SaleItem implements Serializable {

    private Game game;
    private int quantity;
    private double unitPrice;

    /** Creates a sale line with valid data. */
    public SaleItem(Game game, int quantity, double unitPrice) {
        if (game == null) {
            throw new IllegalArgumentException("The game cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("The quantity must be greater than 0.");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("The unit price cannot be negative.");
        }
        this.game = game;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Game getGame() {
        return game;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    /** Returns the price of this line (quantity x unit price). */
    public double calculateSubtotal() {
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return game.getTitle() + " x" + quantity + " = " + calculateSubtotal();
    }
}
