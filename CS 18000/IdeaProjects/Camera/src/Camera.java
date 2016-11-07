/**
 * Camera class with all aspects of cameras and value computations
 *
 * @author - hughe127
 * @date - 6/3/15
 * @class - CS Bridge UTA "Test"
 */
public class Camera {
    boolean hasWiFi;
    boolean isWaterResistant;
    boolean hasGPS;
    String condition;
    double price;
    int userRating;

    /**
     * Creates a camera with the given parameters
     *
     * @param hasWiFi
     * @param isWaterResistant
     * @param hasGPS
     * @param condition
     * @param price
     * @param userRating
     */
    public Camera(boolean hasWiFi, boolean isWaterResistant, boolean hasGPS, String condition, double price, int userRating) {
        this.hasWiFi = hasWiFi;
        this.isWaterResistant = isWaterResistant;
        this.hasGPS = hasGPS;
        this.condition = condition;
        this.price = price;
        this.userRating = userRating;
    }

    /**
     * Computes the value of the camera based on the parameters the camera was created with.
     *
     * @return - The value of the camera
     */
    public int computeValue() {
        int value = 0;
        if (hasWiFi) value++;
        if (isWaterResistant) value++;
        if (hasGPS) value++;
        if (condition.equals("New")) value += 2;
        if (condition.equals("Refurbished")) value++;
        return value;
    }

    //helper method since object fields should be private
    public int getUserRating() {
        return userRating;
    }

    //helper method since object fields should be private
    public double getPrice() {
        return price;
    }
}
