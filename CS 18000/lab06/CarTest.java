import junit.framework.TestCase;

public class CarTest extends TestCase {

    public void testGetMake() {
        Car test = new Car("red", "gm");
        assertEquals(test.getMake(), "gm");

    }

    public void testGetColor() {
        Car test = new Car("red", "gm");
        assertEquals(test.getColor(), "red");

    }
}