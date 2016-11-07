/**
 * Created by grayson on 2/10/15.
 */
public class Camera {
    boolean hasWiFi;
    boolean isWaterResistant;
    boolean hasGPS;
    String condition;
    double price;
    int userRating;

    public Camera(boolean hasWiFi, boolean isWaterResistant,
                  boolean hasGPS, String condition, double price, int userRating) {
        this.hasWiFi = hasWiFi;
        this.isWaterResistant = isWaterResistant;
        this.hasGPS = hasGPS;
        this.condition = condition;
        this.price = price;
        this.userRating = userRating;
    }

    public int computeValue() {
        int val = 0;
        if (hasWiFi) val++;
        if (isWaterResistant) val++;
        if (hasGPS) val++;
        if (condition.equals("New")) val += 2;
        if (condition.equals("Refurbished")) val++;
        return val;
    }

    public int getUserRating() {
        return userRating;
    }

    public double getPrice() {
        return price;
    }
}
