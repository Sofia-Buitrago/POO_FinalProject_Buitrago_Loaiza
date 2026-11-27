package domain;

/**
 * A game sold in a physical copy (disc or cartridge).
 * It is controlled by the stock available in the store.
 */
public class PhysicalGame extends Game {

    private int stock;
    private int minStock;

    /** Creates a physical game with its initial stock. */
    public PhysicalGame(String code, String title, String platform,
                        double basePrice, Category category,
                        int stock, int minStock) {
        super(code, title, platform, basePrice, category);
        setStock(stock);
        setMinStock(minStock);
    }

    public int getStock() {
        return stock;
    }

    // Private: the stock only changes with addStock and deliver.
    private void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("The stock cannot be negative.");
        }
        this.stock = stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        if (minStock < 0) {
            throw new IllegalArgumentException("The minimum stock cannot be negative.");
        }
        this.minStock = minStock;
    }

    /** Adds new copies to the stock. */
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("The quantity to add must be greater than 0.");
        }
        stock = stock + quantity;
    }

    /** Tells if the stock is at or below the minimum. */
    public boolean isLowStock() {
        return stock <= minStock;
    }

    @Override//sobreescribir, el metodo no es de el
    public boolean isAvailable(int quantity) {
        return quantity > 0 && stock >= quantity;
    }

    @Override
    public void deliver(int quantity) {
        if (!isAvailable(quantity)) {
            throw new IllegalArgumentException("There is not enough stock to deliver.");
        }
        stock = stock - quantity;
    }

    @Override
    public String getType() {
        return "Physical";
    }
}