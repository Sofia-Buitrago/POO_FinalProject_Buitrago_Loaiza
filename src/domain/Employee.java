package domain;

import java.io.Serializable;

/**
 * An employee of the store who registers sales.
 * An employee is deactivated instead of deleted to keep the sales history.
 */
public class Employee implements Serializable {

    private int id;
    private String fullName;
    private String role;
    private boolean active;

    /** Creates an active employee. */
    public Employee(int id, String fullName, String role) {
        if (id <= 0) {
            throw new IllegalArgumentException("The id must be greater than 0.");
        }
        this.id = id;
        setFullName(fullName);
        setRole(role);
        this.active = true;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    /** Changes the name, which cannot be empty. */
    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("The name cannot be empty.");
        }
        this.fullName = fullName.trim();
    }

    public String getRole() {
        return role;
    }

    /** Changes the role, which cannot be empty. */
    public void setRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("The role cannot be empty.");
        }
        this.role = role.trim();
    }

    public boolean isActive() {
        return active;
    }

    /** Deactivates the employee so he or she cannot register new sales. */
    public void deactivate() {
        active = false;
    }

    /** Activates the employee again. */
    public void activate() {
        active = true;
    }

    @Override
    public String toString() {
        return id + " - " + fullName + " (" + role + ")";
    }
}

