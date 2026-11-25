package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the domain. It keeps all the data of the store
 * (games, categories, employees, customers and sales)
 * and applies the business rules that connect them.
 */
public class Store implements Serializable {

    private String name;
    private List<Game> games;
    private List<Category> categories;
    private List<Employee> employees;
    private List<Customer> customers;
    private List<Sale> sales;

    /** Creates an empty store. */
    public Store(String name) {
        setName(name);
        games = new ArrayList<>();
        categories = new ArrayList<>();
        employees = new ArrayList<>();
        customers = new ArrayList<>();
        sales = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    /** Changes the name of the store, which cannot be empty. */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The store name cannot be empty.");
        }
        this.name = name.trim();
    }

    // ---------------------------------------------------------------
    // Lists (copies, so the original lists cannot be changed from outside)
    // ---------------------------------------------------------------

    public List<Game> getGames() {
        return new ArrayList<>(games);
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    public List<Sale> getSales() {
        return new ArrayList<>(sales);
    }

    // ---------------------------------------------------------------
    // Categories
    // ---------------------------------------------------------------

    /** Adds a category. Two categories cannot have the same id. */
    public void addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("The category cannot be null.");
        }
        if (findCategoryById(category.getId()) != null) {
            throw new IllegalArgumentException("There is already a category with that id.");
        }
        categories.add(category);
    }

    /** Returns the category with that id, or null if it does not exist. */
    public Category findCategoryById(int id) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == id) {
                return categories.get(i);
            }
        }
        return null;
    }

    /** Removes a category only if no game uses it. */
    public void removeCategory(int id) {
        Category category = findCategoryById(id);
        if (category == null) {
            throw new IllegalArgumentException("The category does not exist.");
        }
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getCategory() == category) {
                throw new IllegalStateException("The category has games and cannot be removed.");
            }
        }
        categories.remove(category);
    }

    // ---------------------------------------------------------------
    // Games
    // ---------------------------------------------------------------

    /** Adds a game. The code must be new and the category must exist in the store. */
    public void addGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("The game cannot be null.");
        }
        if (findGameByCode(game.getCode()) != null) {
            throw new IllegalArgumentException("There is already a game with that code.");
        }
        if (!categories.contains(game.getCategory())) {
            throw new IllegalArgumentException("The category of the game is not in the store.");
        }
        games.add(game);
    }

    /** Returns the game with that code, or null if it does not exist. */
    public Game findGameByCode(String code) {
        if (code == null) {
            return null;
        }
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getCode().equalsIgnoreCase(code.trim())) {
                return games.get(i);
            }
        }
        return null;
    }

    /** Returns the games whose title contains the text (not case sensitive). */
    public List<Game> searchGamesByTitle(String text) {
        List<Game> result = new ArrayList<>();
        if (text == null) {
            return result;
        }
        String search = text.trim().toLowerCase();
        for (int i = 0; i < games.size(); i++) {
            String title = games.get(i).getTitle().toLowerCase();
            if (title.contains(search)) {
                result.add(games.get(i));
            }
        }
        return result;
    }

    /** Deactivates a game. It stays in the store so the sales history is kept. */
    public void deactivateGame(String code) {
        Game game = findGameByCode(code);
        if (game == null) {
            throw new IllegalArgumentException("The game does not exist.");
        }
        game.deactivate();
    }

    /** Removes a game only if it has never been sold. Otherwise it must be deactivated. */
    public void removeGame(String code) {
        Game game = findGameByCode(code);
        if (game == null) {
            throw new IllegalArgumentException("The game does not exist.");
        }
        if (isGameInSales(game)) {
            throw new IllegalStateException("The game has sales. Deactivate it instead.");
        }
        games.remove(game);
    }

    /** Returns true if the game appears in any sale. */
    private boolean isGameInSales(Game game) {
        for (int i = 0; i < sales.size(); i++) {
            List<SaleItem> items = sales.get(i).getItems();
            for (int j = 0; j < items.size(); j++) {
                if (items.get(j).getGame() == game) {
                    return true;
                }
            }
        }
        return false;
    }

    // ---------------------------------------------------------------
    // Employees
    // ---------------------------------------------------------------

    /** Adds an employee. Two employees cannot have the same id. */
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("The employee cannot be null.");
        }
        if (findEmployeeById(employee.getId()) != null) {
            throw new IllegalArgumentException("There is already an employee with that id.");
        }
        employees.add(employee);
    }

    /** Returns the employee with that id, or null if it does not exist. */
    public Employee findEmployeeById(int id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                return employees.get(i);
            }
        }
        return null;
    }

    /** Deactivates an employee. Employees are never deleted to keep the history. */
    public void deactivateEmployee(int id) {
        Employee employee = findEmployeeById(id);
        if (employee == null) {
            throw new IllegalArgumentException("The employee does not exist.");
        }
        employee.deactivate();
    }

    /** Returns the next free employee id. Employees are never deleted, so the size is enough. */
    public int getNextEmployeeId() {
        return employees.size() + 1;
    }

    // ---------------------------------------------------------------
    // Customers
    // ---------------------------------------------------------------

    /** Adds a customer. Two customers cannot have the same id. */
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("The customer cannot be null.");
        }
        if (findCustomerById(customer.getId()) != null) {
            throw new IllegalArgumentException("There is already a customer with that id.");
        }
        customers.add(customer);
    }

    /** Returns the customer with that id, or null if it does not exist. */
    public Customer findCustomerById(int id) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId() == id) {
                return customers.get(i);
            }
        }
        return null;
    }

    /** Deactivates a customer. Customers are never deleted to keep the history. */
    public void deactivateCustomer(int id) {
        Customer customer = findCustomerById(id);
        if (customer == null) {
            throw new IllegalArgumentException("The customer does not exist.");
        }
        customer.deactivate();
    }

    /** Returns the next free customer id. Customers are never deleted, so the size is enough. */
    public int getNextCustomerId() {
        return customers.size() + 1;
    }

    // ---------------------------------------------------------------
    // Sales
    // ---------------------------------------------------------------

    /** Returns the next free sale id. Sales are never deleted, so the size is enough. */
    public int getNextSaleId() {
        return sales.size() + 1;
    }

    /**
     * Registers a sale. First it checks everything, and only if all is correct
     * it delivers the games. This way a sale is never half delivered.
     */
    public void registerSale(Sale sale) {
        if (sale == null) {
            throw new IllegalArgumentException("The sale cannot be null.");
        }
        if (findSaleById(sale.getSaleId()) != null) {
            throw new IllegalArgumentException("There is already a sale with that id.");
        }
        if (findCustomerById(sale.getCustomer().getId()) != sale.getCustomer()) {
            throw new IllegalArgumentException("The customer is not in the store.");
        }
        if (findEmployeeById(sale.getEmployee().getId()) != sale.getEmployee()) {
            throw new IllegalArgumentException("The employee is not in the store.");
        }
        List<SaleItem> items = sale.getItems();
        if (items.isEmpty()) {
            throw new IllegalArgumentException("The sale must have at least one item.");
        }

        // Step 1: check every item before delivering anything
        for (int i = 0; i < items.size(); i++) {
            Game game = items.get(i).getGame();
            if (!games.contains(game)) {
                throw new IllegalArgumentException("A game of the sale is not in the store.");
            }
            if (!game.isActive()) {
                throw new IllegalStateException("The game " + game.getTitle() + " is not active.");
            }
            // The same game can appear in several items, so its quantities are added
            int totalQuantity = 0;
            for (int j = 0; j < items.size(); j++) {
                if (items.get(j).getGame() == game) {
                    totalQuantity = totalQuantity + items.get(j).getQuantity();
                }
            }
            // Polymorphism: Game, PhysicalGame or DigitalGame answers in its own way
            if (!game.isAvailable(totalQuantity)) {
                throw new IllegalStateException("Not enough units of " + game.getTitle() + ".");
            }
        }

        // Step 2: deliver every item (polymorphism again)
        for (int i = 0; i < items.size(); i++) {
            items.get(i).getGame().deliver(items.get(i).getQuantity());
        }
        sales.add(sale);
    }

    /** Returns the sale with that id, or null if it does not exist. */
    public Sale findSaleById(int saleId) {
        for (int i = 0; i < sales.size(); i++) {
            if (sales.get(i).getSaleId() == saleId) {
                return sales.get(i);
            }
        }
        return null;
    }

    /**
     * Cancels a sale. The sale stays in the history.
     * Physical games return to the stock. Digital keys are not returned,
     * because the customer already received them.
     */
    public void cancelSale(int saleId) {
        Sale sale = findSaleById(saleId);
        if (sale == null) {
            throw new IllegalArgumentException("The sale does not exist.");
        }
        sale.cancel(); // throws an exception if it was already cancelled

        List<SaleItem> items = sale.getItems();
        for (int i = 0; i < items.size(); i++) {
            Game game = items.get(i).getGame();
            if (game instanceof PhysicalGame) {
                PhysicalGame physicalGame = (PhysicalGame) game;
                physicalGame.addStock(items.get(i).getQuantity());
            }
        }
    }

    /** Returns the total income, adding only the sales that are not cancelled. */
    public double calculateTotalIncome() {
        double total = 0;
        for (int i = 0; i < sales.size(); i++) {
            if (!sales.get(i).isCancelled()) {
                total = total + sales.get(i).calculateTotal();
            }
        }
        return total;
    }

    // ---------------------------------------------------------------
    // Decision support
    // ---------------------------------------------------------------

    /** Returns the active physical games whose stock is low and need to be restocked. */
    public List<Game> getLowStockGames() {
        List<Game> result = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game.isActive() && game instanceof PhysicalGame) {
                PhysicalGame physicalGame = (PhysicalGame) game;
                if (physicalGame.isLowStock()) {
                    result.add(game);
                }
            }
        }
        return result;
    }

    /**
     * Returns the best selling games, from the most sold to the least sold.
     * It counts only sales that are not cancelled and returns at most "limit" games.
     */
    public List<Game> getTopSellingGames(int limit) {
        // Step 1: count the units sold of each game (two lists with the same positions)
        List<Game> soldGames = new ArrayList<>();
        List<Integer> unitsSold = new ArrayList<>();
        for (int i = 0; i < sales.size(); i++) {
            if (sales.get(i).isCancelled()) {
                continue;
            }
            List<SaleItem> items = sales.get(i).getItems();
            for (int j = 0; j < items.size(); j++) {
                Game game = items.get(j).getGame();
                int position = soldGames.indexOf(game);
                if (position == -1) {
                    soldGames.add(game);
                    unitsSold.add(items.get(j).getQuantity());
                } else {
                    unitsSold.set(position, unitsSold.get(position) + items.get(j).getQuantity());
                }
            }
        }

        // Step 2: take the game with the most units, "limit" times
        List<Game> result = new ArrayList<>();
        for (int round = 0; round < limit && round < soldGames.size(); round++) {
            int bestPosition = 0;
            for (int k = 1; k < unitsSold.size(); k++) {
                if (unitsSold.get(k) > unitsSold.get(bestPosition)) {
                    bestPosition = k;
                }
            }
            result.add(soldGames.get(bestPosition));
            unitsSold.set(bestPosition, -1); // so it is not chosen again
        }
        return result;
    }
}