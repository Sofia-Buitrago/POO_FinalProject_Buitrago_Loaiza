package ui;

import data.DataManager;
import domain.Category;
import domain.Customer;
import domain.DigitalGame;
import domain.Employee;
import domain.Game;
import domain.PhysicalGame;
import domain.RestockSuggestion;
import domain.Sale;
import domain.SaleItem;
import domain.Store;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for the store employee.
 * It only reads data and shows messages. The business rules are in the domain
 * classes and the files are handled by the DataManager.
 */
public class ConsoleMenu {

    public static final String DATA_FILE = "store.dat";
    private static final String GAMES_CSV_FILE = "games.csv";
    private static final String SALES_CSV_FILE = "sales.csv";

    private Store store;
    private final DataManager dataManager;
    private final Scanner scanner;

    /** Creates the menu with the store to work with. */
    public ConsoleMenu(Store store, DataManager dataManager) {
        this.store = store;
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }

    // ===============================================================
    // MAIN MENU
    // ===============================================================

    /** Shows the main menu until the user chooses to exit. */
    public void start() {
        System.out.println("=== " + store.getName() + " ===");
        boolean running = true;
        while (running) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Categories");
            System.out.println("2. Games");
            System.out.println("3. Customers");
            System.out.println("4. Employees");
            System.out.println("5. Sales");
            System.out.println("6. Reports and decision support");
            System.out.println("7. Data (save, load, export CSV)");
            System.out.println("0. Exit");
            int option = readInt("Choose an option: ");
            switch (option) {
                case 1: categoriesMenu(); break;
                case 2: gamesMenu(); break;
                case 3: customersMenu(); break;
                case 4: employeesMenu(); break;
                case 5: salesMenu(); break;
                case 6: reportsMenu(); break;
                case 7: dataMenu(); break;
                case 0:
                    askToSaveBeforeExit();
                    running = false;
                    break;
                default: System.out.println("Invalid option.");
            }
        }
        System.out.println("Goodbye!");
    }

    /** Asks if the user wants to save the data before closing the program. */
    private void askToSaveBeforeExit() {
        if (askYesNo("Do you want to save before exiting? (y/n): ")) {
            saveData();
        }
    }

    // ===============================================================
    // CATEGORIES
    // ===============================================================

    /** Submenu to manage the categories. */
    private void categoriesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- CATEGORIES ---");
            System.out.println("1. Add category");
            System.out.println("2. List categories");
            System.out.println("3. Search category by id");
            System.out.println("4. Modify category");
            System.out.println("5. Remove category (only if no game uses it)");
            System.out.println("0. Back");
            int option = readInt("Choose an option: ");
            try {
                switch (option) {
                    case 1: addCategory(); break;
                    case 2: listCategories(); break;
                    case 3: searchCategory(); break;
                    case 4: modifyCategory(); break;
                    case 5: removeCategory(); break;
                    case 0: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** Registers a new category with the next free id. */
    private void addCategory() {
        int id = getNextCategoryId();
        String name = readText("Name: ");
        String description = readText("Description: ");
        store.addCategory(new Category(id, name, description));
        System.out.println("Category added with id " + id + ".");
    }

    /** Returns the next free category id (the biggest id plus one). */
    private int getNextCategoryId() {
        int biggest = 0;
        List<Category> categories = store.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() > biggest) {
                biggest = categories.get(i).getId();
            }
        }
        return biggest + 1;
    }

    /** Shows all the categories. */
    private void listCategories() {
        List<Category> categories = store.getCategories();
        if (categories.isEmpty()) {
            System.out.println("There are no categories.");
        }
        for (int i = 0; i < categories.size(); i++) {
            printCategory(categories.get(i));
        }
    }

    /** Shows one category in one line. */
    private void printCategory(Category category) {
        System.out.println(category.getId() + " - " + category.getName() + ": " + category.getDescription());
    }

    /** Searches a category by id and shows it. */
    private void searchCategory() {
        Category category = askForCategory();
        if (category != null) {
            printCategory(category);
        }
    }

    /** Changes the name or the description of a category. */
    private void modifyCategory() {
        Category category = askForCategory();
        if (category == null) {
            return;
        }
        System.out.println("1. Name");
        System.out.println("2. Description");
        int option = readInt("What do you want to change? ");
        if (option == 1) {
            category.setName(readText("New name: "));
        } else if (option == 2) {
            category.setDescription(readText("New description: "));
        } else {
            System.out.println("Invalid option.");
            return;
        }
        System.out.println("Category updated.");
    }

    /** Removes a category. The store refuses it if a game uses it. */
    private void removeCategory() {
        int id = readInt("Category id: ");
        store.removeCategory(id);
        System.out.println("Category removed.");
    }

    /** Asks for a category id and returns the category, or null if it does not exist. */
    private Category askForCategory() {
        int id = readInt("Category id: ");
        Category category = store.findCategoryById(id);
        if (category == null) {
            System.out.println("Category not found.");
        }
        return category;
    }

    // ===============================================================
    // GAMES
    // ===============================================================

    /** Submenu to manage the games. */
    private void gamesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GAMES ---");
            System.out.println("1. Add physical game");
            System.out.println("2. Add digital game");
            System.out.println("3. List games");
            System.out.println("4. Search game by code");
            System.out.println("5. Search games by title");
            System.out.println("6. Modify game");
            System.out.println("7. Add stock (physical game)");
            System.out.println("8. Add license key (digital game)");
            System.out.println("9. Deactivate game");
            System.out.println("10. Activate game");
            System.out.println("11. Remove game (only if it was never sold)");
            System.out.println("0. Back");
            int option = readInt("Choose an option: ");
            try {
                switch (option) {
                    case 1: addPhysicalGame(); break;
                    case 2: addDigitalGame(); break;
                    case 3: listGames(); break;
                    case 4: searchGameByCode(); break;
                    case 5: searchGamesByTitle(); break;
                    case 6: modifyGame(); break;
                    case 7: addStock(); break;
                    case 8: addLicenseKey(); break;
                    case 9: deactivateGame(); break;
                    case 10: activateGame(); break;
                    case 11: removeGame(); break;
                    case 0: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** Registers a new physical game. */
    private void addPhysicalGame() {
        String code = readText("Code: ");
        String title = readText("Title: ");
        String platform = readText("Platform: ");
        double price = readDouble("Base price: ");
        Category category = askForCategory();
        if (category == null) {
            return;
        }
        int stock = readInt("Initial stock: ");
        int minStock = readInt("Minimum stock: ");
        store.addGame(new PhysicalGame(code, title, platform, price, category, stock, minStock));
        System.out.println("Physical game added.");
    }

    /** Registers a new digital game and, if wanted, its first license keys. */
    private void addDigitalGame() {
        String code = readText("Code: ");
        String title = readText("Title: ");
        String platform = readText("Platform: ");
        double price = readDouble("Base price: ");
        Category category = askForCategory();
        if (category == null) {
            return;
        }
        double size = readDouble("Download size (GB): ");
        DigitalGame game = new DigitalGame(code, title, platform, price, category, size);
        store.addGame(game);
        System.out.println("Digital game added.");
        int keys = readInt("How many license keys do you want to add now? (0 for none): ");
        for (int i = 0; i < keys; i++) {
            game.addLicenseKey(readText("License key " + (i + 1) + ": "));
        }
    }

    /** Shows all the games. */
    private void listGames() {
        List<Game> games = store.getGames();
        if (games.isEmpty()) {
            System.out.println("There are no games.");
        }
        for (int i = 0; i < games.size(); i++) {
            printGame(games.get(i));
        }
    }

    /** Shows one game in one line, with the units that depend on its type. */
    private void printGame(Game game) {
        String units = "";
        if (game instanceof PhysicalGame) {
            PhysicalGame physical = (PhysicalGame) game;
            units = "stock: " + physical.getStock() + " (min " + physical.getMinStock() + ")";
        } else if (game instanceof DigitalGame) {
            DigitalGame digital = (DigitalGame) game;
            units = "keys: " + digital.getAvailableKeys() + ", " + digital.getDownloadSizeGb() + " GB";
        }
        String state = "";
        if (!game.isActive()) {
            state = " [INACTIVE]";
        }
        System.out.println(game.getCode() + " | " + game.getTitle() + " | " + game.getPlatform()
                + " | " + game.getType() + " | " + game.getCategory().getName()
                + " | $" + money(game.getBasePrice()) + " | " + units + state);
    }

    /** Searches a game by its code and shows it. */
    private void searchGameByCode() {
        Game game = askForGame();
        if (game != null) {
            printGame(game);
        }
    }

    /** Searches the games whose title contains a text. */
    private void searchGamesByTitle() {
        List<Game> result = store.searchGamesByTitle(readText("Text to search: "));
        if (result.isEmpty()) {
            System.out.println("No games found.");
        }
        for (int i = 0; i < result.size(); i++) {
            printGame(result.get(i));
        }
    }

    /** Changes one data of a game. */
    private void modifyGame() {
        Game game = askForGame();
        if (game == null) {
            return;
        }
        System.out.println("1. Title");
        System.out.println("2. Platform");
        System.out.println("3. Base price");
        System.out.println("4. Category");
        System.out.println("5. Minimum stock (physical) or download size (digital)");
        int option = readInt("What do you want to change? ");
        switch (option) {
            case 1: game.setTitle(readText("New title: ")); break;
            case 2: game.setPlatform(readText("New platform: ")); break;
            case 3: game.setBasePrice(readDouble("New base price: ")); break;
            case 4: changeGameCategory(game); break;
            case 5: changeGameQuantityData(game); break;
            default:
                System.out.println("Invalid option.");
                return;
        }
        System.out.println("Game updated.");
    }

    /** Changes the category of a game, using a category that exists in the store. */
    private void changeGameCategory(Game game) {
        Category category = askForCategory();
        if (category != null) {
            game.setCategory(category);
        }
    }

    /** Changes the minimum stock of a physical game or the size of a digital game. */
    private void changeGameQuantityData(Game game) {
        if (game instanceof PhysicalGame) {
            ((PhysicalGame) game).setMinStock(readInt("New minimum stock: "));
        } else if (game instanceof DigitalGame) {
            ((DigitalGame) game).setDownloadSizeGb(readDouble("New download size (GB): "));
        }
    }

    /** Adds copies to the stock of a physical game. */
    private void addStock() {
        Game game = askForGame();
        if (game == null) {
            return;
        }
        if (game instanceof PhysicalGame) {
            ((PhysicalGame) game).addStock(readInt("Quantity to add: "));
            System.out.println("Stock updated.");
        } else {
            System.out.println("Only physical games have stock.");
        }
    }

    /** Adds a license key to a digital game. */
    private void addLicenseKey() {
        Game game = askForGame();
        if (game == null) {
            return;
        }
        if (game instanceof DigitalGame) {
            ((DigitalGame) game).addLicenseKey(readText("License key: "));
            System.out.println("License key added.");
        } else {
            System.out.println("Only digital games have license keys.");
        }
    }

    /** Deactivates a game so it cannot be sold, but keeps its history. */
    private void deactivateGame() {
        store.deactivateGame(readText("Game code: "));
        System.out.println("Game deactivated.");
    }

    /** Activates a game again. */
    private void activateGame() {
        Game game = askForGame();
        if (game != null) {
            game.activate();
            System.out.println("Game activated.");
        }
    }

    /** Removes a game. The store refuses it if the game has sales. */
    private void removeGame() {
        store.removeGame(readText("Game code: "));
        System.out.println("Game removed.");
    }

    /** Asks for a game code and returns the game, or null if it does not exist. */
    private Game askForGame() {
        Game game = store.findGameByCode(readText("Game code: "));
        if (game == null) {
            System.out.println("Game not found.");
        }
        return game;
    }

    // ===============================================================
    // CUSTOMERS
    // ===============================================================

    /** Submenu to manage the customers. */
    private void customersMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- CUSTOMERS ---");
            System.out.println("1. Add customer");
            System.out.println("2. List customers");
            System.out.println("3. Search customer by id");
            System.out.println("4. Modify customer");
            System.out.println("5. Deactivate customer");
            System.out.println("6. Activate customer");
            System.out.println("0. Back");
            int option = readInt("Choose an option: ");
            try {
                switch (option) {
                    case 1: addCustomer(); break;
                    case 2: listCustomers(); break;
                    case 3: searchCustomer(); break;
                    case 4: modifyCustomer(); break;
                    case 5: deactivateCustomer(); break;
                    case 6: activateCustomer(); break;
                    case 0: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** Registers a new customer with the next free id. */
    private void addCustomer() {
        int id = store.getNextCustomerId();
        String name = readText("Full name: ");
        String email = readText("Email: ");
        String phone = readText("Phone: ");
        store.addCustomer(new Customer(id, name, email, phone));
        System.out.println("Customer added with id " + id + ".");
    }

    /** Shows all the customers. */
    private void listCustomers() {
        List<Customer> customers = store.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("There are no customers.");
        }
        for (int i = 0; i < customers.size(); i++) {
            printCustomer(customers.get(i));
        }
    }

    /** Shows one customer in one line. */
    private void printCustomer(Customer customer) {
        String state = "";
        if (!customer.isActive()) {
            state = " [INACTIVE]";
        }
        System.out.println(customer + " | " + customer.getPhone() + state);
    }

    /** Searches a customer by id and shows it. */
    private void searchCustomer() {
        Customer customer = askForCustomer();
        if (customer != null) {
            printCustomer(customer);
        }
    }

    /** Changes the name, the email or the phone of a customer. */
    private void modifyCustomer() {
        Customer customer = askForCustomer();
        if (customer == null) {
            return;
        }
        System.out.println("1. Full name");
        System.out.println("2. Email");
        System.out.println("3. Phone");
        int option = readInt("What do you want to change? ");
        switch (option) {
            case 1: customer.setFullName(readText("New full name: ")); break;
            case 2: customer.setEmail(readText("New email: ")); break;
            case 3: customer.setPhone(readText("New phone: ")); break;
            default:
                System.out.println("Invalid option.");
                return;
        }
        System.out.println("Customer updated.");
    }

    /** Deactivates a customer. Customers are never deleted. */
    private void deactivateCustomer() {
        store.deactivateCustomer(readInt("Customer id: "));
        System.out.println("Customer deactivated.");
    }

    /** Activates a customer again. */
    private void activateCustomer() {
        Customer customer = askForCustomer();
        if (customer != null) {
            customer.activate();
            System.out.println("Customer activated.");
        }
    }

    /** Asks for a customer id and returns the customer, or null if it does not exist. */
    private Customer askForCustomer() {
        Customer customer = store.findCustomerById(readInt("Customer id: "));
        if (customer == null) {
            System.out.println("Customer not found.");
        }
        return customer;
    }

    // ===============================================================
    // EMPLOYEES
    // ===============================================================

    /** Submenu to manage the employees. */
    private void employeesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- EMPLOYEES ---");
            System.out.println("1. Add employee");
            System.out.println("2. List employees");
            System.out.println("3. Search employee by id");
            System.out.println("4. Modify employee");
            System.out.println("5. Deactivate employee");
            System.out.println("6. Activate employee");
            System.out.println("0. Back");
            int option = readInt("Choose an option: ");
            try {
                switch (option) {
                    case 1: addEmployee(); break;
                    case 2: listEmployees(); break;
                    case 3: searchEmployee(); break;
                    case 4: modifyEmployee(); break;
                    case 5: deactivateEmployee(); break;
                    case 6: activateEmployee(); break;
                    case 0: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** Registers a new employee with the next free id. */
    private void addEmployee() {
        int id = store.getNextEmployeeId();
        String name = readText("Full name: ");
        String role = readText("Role: ");
        store.addEmployee(new Employee(id, name, role));
        System.out.println("Employee added with id " + id + ".");
    }

    /** Shows all the employees. */
    private void listEmployees() {
        List<Employee> employees = store.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("There are no employees.");
        }
        for (int i = 0; i < employees.size(); i++) {
            printEmployee(employees.get(i));
        }
    }

    /** Shows one employee in one line. */
    private void printEmployee(Employee employee) {
        String state = "";
        if (!employee.isActive()) {
            state = " [INACTIVE]";
        }
        System.out.println(employee + state);
    }

    /** Searches an employee by id and shows it. */
    private void searchEmployee() {
        Employee employee = askForEmployee();
        if (employee != null) {
            printEmployee(employee);
        }
    }

    /** Changes the name or the role of an employee. */
    private void modifyEmployee() {
        Employee employee = askForEmployee();
        if (employee == null) {
            return;
        }
        System.out.println("1. Full name");
        System.out.println("2. Role");
        int option = readInt("What do you want to change? ");
        if (option == 1) {
            employee.setFullName(readText("New full name: "));
        } else if (option == 2) {
            employee.setRole(readText("New role: "));
        } else {
            System.out.println("Invalid option.");
            return;
        }
        System.out.println("Employee updated.");
    }

    /** Deactivates an employee. Employees are never deleted. */
    private void deactivateEmployee() {
        store.deactivateEmployee(readInt("Employee id: "));
        System.out.println("Employee deactivated.");
    }

    /** Activates an employee again. */
    private void activateEmployee() {
        Employee employee = askForEmployee();
        if (employee != null) {
            employee.activate();
            System.out.println("Employee activated.");
        }
    }

    /** Asks for an employee id and returns the employee, or null if it does not exist. */
    private Employee askForEmployee() {
        Employee employee = store.findEmployeeById(readInt("Employee id: "));
        if (employee == null) {
            System.out.println("Employee not found.");
        }
        return employee;
    }

    // ===============================================================
    // SALES
    // ===============================================================

    /** Submenu to manage the sales. Sales are never edited or deleted. */
    private void salesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- SALES ---");
            System.out.println("1. Register sale");
            System.out.println("2. List sales");
            System.out.println("3. Search sale by id");
            System.out.println("4. Cancel sale");
            System.out.println("0. Back");
            int option = readInt("Choose an option: ");
            try {
                switch (option) {
                    case 1: registerSale(); break;
                    case 2: listSales(); break;
                    case 3: searchSale(); break;
                    case 4: cancelSale(); break;
                    case 0: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** Registers a sale: customer, employee and the games sold. */
    private void registerSale() {
        Customer customer = askForCustomer();
        if (customer == null) {
            return;
        }
        Employee employee = askForEmployee();
        if (employee == null) {
            return;
        }
        Sale sale = new Sale(store.getNextSaleId(), LocalDate.now(), customer, employee);
        addItemsToSale(sale);
        if (sale.getItems().isEmpty()) {
            System.out.println("A sale needs at least one game. Sale not registered.");
            return;
        }
        store.registerSale(sale);
        System.out.println("Sale " + sale.getSaleId() + " registered. Total: $" + money(sale.calculateTotal()));
    }

    /** Asks for games and quantities until the user leaves the code empty. */
    private void addItemsToSale(Sale sale) {
        while (true) {
            String code = readText("Game code (Enter to finish): ");
            if (code.isBlank()) {
                return;
            }
            Game game = store.findGameByCode(code);
            if (game == null) {
                System.out.println("Game not found.");
                continue;
            }
            try {
                int quantity = readInt("Quantity: ");
                sale.addItem(new SaleItem(game, quantity, game.getBasePrice()));
                System.out.println("Added: " + game.getTitle() + " x" + quantity);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** Shows all the sales. */
    private void listSales() {
        List<Sale> sales = store.getSales();
        if (sales.isEmpty()) {
            System.out.println("There are no sales.");
        }
        for (int i = 0; i < sales.size(); i++) {
            System.out.println(sales.get(i));
        }
    }

    /** Searches a sale by id and shows its details. */
    private void searchSale() {
        Sale sale = store.findSaleById(readInt("Sale id: "));
        if (sale == null) {
            System.out.println("Sale not found.");
            return;
        }
        System.out.println(sale);
        System.out.println("Employee: " + sale.getEmployee().getFullName());
        List<SaleItem> items = sale.getItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println("  - " + items.get(i));
        }
    }

    /** Cancels a sale. It stays in the history. */
    private void cancelSale() {
        store.cancelSale(readInt("Sale id: "));
        System.out.println("Sale cancelled. Physical games went back to the stock.");
    }

    // ===============================================================
    // REPORTS AND DECISION SUPPORT
    // ===============================================================

    /** Submenu with reports and the restock recommendations. */
    private void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- REPORTS AND DECISION SUPPORT ---");
            System.out.println("1. Total income");
            System.out.println("2. Games with low stock");
            System.out.println("3. Top selling games");
            System.out.println("4. Restock recommendations");
            System.out.println("0. Back");
            int option = readInt("Choose an option: ");
            try {
                switch (option) {
                    case 1: showTotalIncome(); break;
                    case 2: showLowStockGames(); break;
                    case 3: showTopSellingGames(); break;
                    //case 4: showRestockSuggestions(); break;/
                    case 0: back = true; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** Shows the income of all the sales that are not cancelled. */
    private void showTotalIncome() {
        System.out.println("Total income: $" + money(store.calculateTotalIncome()));
    }

    /** Shows the active physical games with low stock. */
    private void showLowStockGames() {
        List<Game> games = store.getLowStockGames();
        if (games.isEmpty()) {
            System.out.println("There are no games with low stock.");
        }
        for (int i = 0; i < games.size(); i++) {
            printGame(games.get(i));
        }
    }

    /** Shows the best selling games. */
    private void showTopSellingGames() {
        int limit = readInt("How many games do you want to see? ");
        List<Game> games = store.getTopSellingGames(limit);
        if (games.isEmpty()) {
            System.out.println("There are no sales yet.");
        }
        for (int i = 0; i < games.size(); i++) {
            System.out.println((i + 1) + ". " + games.get(i).getTitle());
        }
    }

    /** Shows which games should be restocked and how many copies to order. */
    /*private void showRestockSuggestions() {
        List<RestockSuggestion> suggestions = store.getRestockSuggestions();
        if (suggestions.isEmpty()) {
            System.out.println("No game needs to be restocked now.");
        }
        for (int i = 0; i < suggestions.size(); i++) {
            System.out.println(suggestions.get(i));
        }
    }*/

    // ===============================================================
    // DATA
    // ===============================================================

    /** Submenu to save, load and export the data. */
    private void dataMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- DATA ---");
            System.out.println("1. Save data");
            System.out.println("2. Load data");
            System.out.println("3. Export games to CSV");
            System.out.println("4. Export sales to CSV");
            System.out.println("0. Back");
            int option = readInt("Choose an option: ");
            switch (option) {
                case 1: saveData(); break;
                case 2: loadData(); break;
                case 3: exportGames(); break;
                case 4: exportSales(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    /** Saves the whole store in the serialized file. */
    private void saveData() {
        try {
            dataManager.saveStore(store, DATA_FILE);
            System.out.println("Data saved in " + DATA_FILE + ".");
        } catch (IOException e) {
            System.out.println("The data could not be saved: " + e.getMessage());
        }
    }

    /** Loads the store from the serialized file, replacing the current data. */
    private void loadData() {
        if (!new File(DATA_FILE).exists()) {
            System.out.println("There is no saved file yet.");
            return;
        }
        if (!askYesNo("This replaces the current data. Continue? (y/n): ")) {
            return;
        }
        try {
            store = dataManager.loadStore(DATA_FILE);
            System.out.println("Data loaded from " + DATA_FILE + ".");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("The data could not be loaded: " + e.getMessage());
        }
    }

    /** Exports the games to a CSV file. */
    private void exportGames() {
        try {
            dataManager.exportGamesToCsv(store, GAMES_CSV_FILE);
            System.out.println("Games exported to " + GAMES_CSV_FILE + ".");
        } catch (IOException e) {
            System.out.println("The file could not be written: " + e.getMessage());
        }
    }

    /** Exports the sales to a CSV file. */
    private void exportSales() {
        try {
            dataManager.exportSalesToCsv(store, SALES_CSV_FILE);
            System.out.println("Sales exported to " + SALES_CSV_FILE + ".");
        } catch (IOException e) {
            System.out.println("The file could not be written: " + e.getMessage());
        }
    }

    // ===============================================================
    // READING DATA FROM THE USER
    // ===============================================================

    /** Shows a message and returns the text typed by the user, without extra spaces. */
    private String readText(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    /** Asks again until the user types a whole number. */
    private int readInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(readText(message));
            } catch (NumberFormatException e) {
                System.out.println("Please type a whole number.");
            }
        }
    }

    /** Asks again until the user types a number. A comma is accepted as decimal point. */
    private double readDouble(String message) {
        while (true) {
            try {
                return Double.parseDouble(readText(message).replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Please type a number.");
            }
        }
    }

    /** Returns true if the user answers yes (y or yes). */
    private boolean askYesNo(String message) {
        String answer = readText(message).toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }

    /** Formats a price with two decimals. */
    private String money(double value) {
        return String.format("%.2f", value);
    }
}