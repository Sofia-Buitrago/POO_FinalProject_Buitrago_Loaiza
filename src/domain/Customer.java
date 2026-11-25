package domain;

import java.io.Serializable;

/**
 * A customer of the store.
 * A customer is deactivated instead of deleted to keep the sales history.
 */
public class Customer implements Serializable {

    private int id;
    private String fullName;
    private String email;
    private String phone;
    private boolean active;

    /** Creates an active customer. */
    public Customer(int id, String fullName, String email, String phone) {
        if (id <= 0) {
            throw new IllegalArgumentException("The id must be greater than 0.");
        }
        this.id = id;
        setFullName(fullName);
        setEmail(email);
        setPhone(phone);
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

    public String getEmail() {
        return email;
    }

    /** Changes the email, which must contain the @ symbol. */
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("The email is not valid.");
        }
        this.email = email.trim();
    }

    public String getPhone() {
        return phone;
    }

    /** Changes the phone, which cannot be empty. */
    public void setPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("The phone cannot be empty.");
        }
        this.phone = phone.trim();
    }

    public boolean isActive() {
        return active;
    }

    /** Deactivates the customer so he or she cannot buy anymore. */
    public void deactivate() {
        active = false;
    }

    /** Activates the customer again. */
    public void activate() {
        active = true;
    }

    @Override
    public String toString() {
        return id + " - " + fullName + " (" + email + ")";
    }
}
