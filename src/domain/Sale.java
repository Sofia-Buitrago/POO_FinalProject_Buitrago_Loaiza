package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A sale made by an employee to a customer.
 * A sale is never deleted or edited. It can only be cancelled.
 */
public class Sale implements Serializable {

    private int saleId;
    private LocalDate date;
    private Customer customer;
    private Employee employee;
    private List<SaleItem> items;
    private boolean cancelled;

    /** Creates an empty sale. The customer and the employee must be active. */
    public Sale(int saleId, LocalDate date, Customer customer, Employee employee) {
        if (saleId <= 0) {
            throw new IllegalArgumentException("The sale id must be greater than 0.");
        }
        if (date == null) {
            throw new IllegalArgumentException("The date cannot be null.");
        }
        if (customer == null || !customer.isActive()) {
            throw new IllegalArgumentException("The customer must exist and be active.");
        }
        if (employee == null || !employee.isActive()) {
            throw new IllegalArgumentException("The employee must exist and be active.");
        }
        this.saleId = saleId;
        this.date = date;
        this.customer = customer;
        this.employee = employee;
        this.items = new ArrayList<>();
        this.cancelled = false;
    }

    public int getSaleId() {
        return saleId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    /** Returns a copy, so the original list cannot be changed from outside. */
    public List<SaleItem> getItems() {
        return new ArrayList<>(items);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    /** Adds a line to the sale. A cancelled sale cannot be changed. */
    public void addItem(SaleItem item) {
        if (cancelled) {
            throw new IllegalStateException("A cancelled sale cannot be changed.");
        }
        if (item == null) {
            throw new IllegalArgumentException("The item cannot be null.");
        }
        items.add(item);
    }

    /** Returns the total of the sale, adding the subtotal of each line. */
    public double calculateTotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total = total + items.get(i).calculateSubtotal();
        }
        return total;
    }

    /** Cancels the sale. The sale stays in the history. */
    public void cancel() {
        if (cancelled) {
            throw new IllegalStateException("The sale is already cancelled.");
        }
        cancelled = true;
    }

    @Override
    public String toString() {
        String state = "";
        if (cancelled) {
            state = " [CANCELLED]";
        }
        return "Sale " + saleId + " - " + date + " - " + customer.getFullName()
                + " - total: " + calculateTotal() + state;
    }
}
