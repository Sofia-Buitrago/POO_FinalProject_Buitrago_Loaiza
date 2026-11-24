package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A game sold as a digital download.
 * It is controlled by the license keys available in the store.
 */
public class DigitalGame extends Game {

    private double downloadSizeGb;
    private List<String> licenseKeys;

    /** Creates a digital game with no license keys yet. */
    public DigitalGame(String code, String title, String platform,
                       double basePrice, Category category,
                       double downloadSizeGb) {
        super(code, title, platform, basePrice, category);
        setDownloadSizeGb(downloadSizeGb);
        licenseKeys = new ArrayList<>();
    }

    public double getDownloadSizeGb() {
        return downloadSizeGb;
    }

    public void setDownloadSizeGb(double downloadSizeGb) {
        if (downloadSizeGb <= 0) {
            throw new IllegalArgumentException("The download size must be greater than 0.");
        }
        this.downloadSizeGb = downloadSizeGb;
    }

    /** Returns a copy, so the original list cannot be changed from outside. */
    public List<String> getLicenseKeys() {
        return new ArrayList<>(licenseKeys);
    }

    /** Adds a new license key to sell. */
    public void addLicenseKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("The license key cannot be empty.");
        }
        if (licenseKeys.contains(key.trim())) {
            throw new IllegalArgumentException("The license key already exists.");
        }
        licenseKeys.add(key.trim());
    }

    /** Returns how many keys are still available. */
    public int getAvailableKeys() {
        return licenseKeys.size();
    }

    @Override
    public boolean isAvailable(int quantity) {
        return quantity > 0 && licenseKeys.size() >= quantity;
    }

    @Override
    public void deliver(int quantity) {
        if (!isAvailable(quantity)) {
            throw new IllegalArgumentException("There are not enough license keys to deliver.");
        }
        for (int i = 0; i < quantity; i++) {
            licenseKeys.remove(0);
        }
    }

    @Override
    public String getType() {
        return "Digital";
    }
}