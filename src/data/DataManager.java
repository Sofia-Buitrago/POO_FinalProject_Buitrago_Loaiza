package data;

import domain.DigitalGame;
import domain.Game;
import domain.PhysicalGame;
import domain.Sale;
import domain.SaleItem;
import domain.Store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Saves and loads the store using serialized files,
 * and exports reports to CSV files.
 */
public class DataManager {

    /** Saves the whole store in a serialized file. */
    public void saveStore(Store store, String path) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(store);
        out.close();
    }

    /**
     * Loads the store from a serialized file.
     * If the file does not exist yet, it returns a new empty store.
     */
    public Store loadStore(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            return new Store("Game Store");
        }
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Store store = (Store) in.readObject();
        in.close();
        return store;
    }

    /** Exports the list of games to a CSV file with headers. */
    public void exportGamesToCsv(Store store, String path) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        writer.println("code,title,platform,type,category,basePrice,active,units");

        List<Game> games = store.getGames();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);

            // Units are stock for physical games and available keys for digital games
            int units = 0;
            if (game instanceof PhysicalGame) {
                units = ((PhysicalGame) game).getStock();
            } else if (game instanceof DigitalGame) {
                units = ((DigitalGame) game).getAvailableKeys();
            }

            writer.println(cleanText(game.getCode()) + ","
                    + cleanText(game.getTitle()) + ","
                    + cleanText(game.getPlatform()) + ","
                    + game.getType() + ","
                    + cleanText(game.getCategory().getName()) + ","
                    + game.getBasePrice() + ","
                    + game.isActive() + ","
                    + units);
        }
        writer.close();
    }

    /** Exports the sales report to a CSV file, with one row for each sale item. */
    public void exportSalesToCsv(Store store, String path) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        writer.println("saleId,date,customer,employee,game,quantity,unitPrice,subtotal,cancelled");

        List<Sale> sales = store.getSales();
        for (int i = 0; i < sales.size(); i++) {
            Sale sale = sales.get(i);
            List<SaleItem> items = sale.getItems();
            for (int j = 0; j < items.size(); j++) {
                SaleItem item = items.get(j);
                writer.println(sale.getSaleId() + ","
                        + sale.getDate() + ","
                        + cleanText(sale.getCustomer().getFullName()) + ","
                        + cleanText(sale.getEmployee().getFullName()) + ","
                        + cleanText(item.getGame().getTitle()) + ","
                        + item.getQuantity() + ","
                        + item.getUnitPrice() + ","
                        + item.calculateSubtotal() + ","
                        + sale.isCancelled());
            }
        }
        writer.close();
    }

    /** Replaces commas in a text so it does not break the CSV columns. */
    private String cleanText(String text) {
        return text.replace(",", ";");
    }
}