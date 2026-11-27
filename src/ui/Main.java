package ui;

import data.DataManager;
import domain.Store;

import java.io.IOException;

/**
 * Starting point of the program.
 * It loads the saved data (if any) and opens the console menu.
 */
public class Main {

    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        Store store;
        try {
            store = dataManager.loadStore(ConsoleMenu.DATA_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("The saved data could not be loaded: " + e.getMessage());
            System.out.println("Starting with an empty store.");
            store = new Store("Game Store");
        }
        ConsoleMenu menu = new ConsoleMenu(store, dataManager);
        menu.start();
    }
}