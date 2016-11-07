import org.junit.Test;

import static org.junit.Assert.*;

public class CameraTest {

    //Since the instructions don't tell them to declare the fields as private, might as well test them :D
    @Test
    public void testCamera() throws Exception {
        Camera c1 = new Camera(true, true, true, "New", 100.00, 5);
        assertEquals("Value WiFi is not assigned properly", c1.hasWiFi, true);
        assertEquals("Value hasGPS is not assigned properly", c1.hasGPS, true);
        assertEquals("Value isWaterResistant is not assigned properly", c1.isWaterResistant, true);
        assertEquals("Value price is not assigned properly", c1.price, 100.00, .001);
        assertEquals("Value userRating is not assigned properly", c1.userRating, 5);
        assertEquals("Value condition is not assigned properly", c1.condition, "New");
    }


    @Test
    public void testComputeValue1() throws Exception {
        Camera c1 = new Camera(true, true, true, "New", 100.00, 5);
        assertEquals("computeValue isn't working properly", c1.computeValue(), 5);
    }

    @Test
    public void testComputeValue2() throws Exception {
        Camera c1 = new Camera(true, false, true, "Used", 100.00, 5);
        assertEquals("computeValue isn't working properly", c1.computeValue(), 2);
    }

    @Test
    public void testComputeValue3() throws Exception {
        Camera c1 = new Camera(false, true, true, "Refurbished", 50.00, 3);
        assertEquals("computeValue isn't working properly", c1.computeValue(), 3);
    }

    @Test
    public void testComputeValue4() throws Exception {
        Camera c1 = new Camera(false, true, false, "Refurbished", 50.00, 3);
        assertEquals("computeValue isn't working properly", c1.computeValue(), 2);
    }
}