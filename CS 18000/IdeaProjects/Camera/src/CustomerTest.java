

import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {

    @Test
    public void testCompare1() {
        Camera c1 = new Camera(true, true, true, "Refurbished", 100.00, 5);
        Camera c2 = new Camera(true, true, true, "New", 100.00, 5);
        assertEquals("compare(); check if condition differs, but all else same", Customer.compare(c1, c2), 2);
    }

    @Test
    public void testCompare2() {
        Camera c1 = new Camera(true, true, true, "New", 100.00, 5);
        Camera c2 = new Camera(true, true, true, "Refurbished", 100.00, 5);
        assertEquals("compare(); check if condition differs, but all else same", Customer.compare(c1, c2), 1);
    }

    @Test
    public void testCompare3() {
        Camera c1 = new Camera(true, true, true, "New", 100.00, 5);
        assertEquals("compare(); check if all aspects same", Customer.compare(c1, c1), 0);
    }

    @Test
    public void testCompare4() {
        Camera c1 = new Camera(true, true, true, "New", 100.00, 3);
        Camera c2 = new Camera(true, true, true, "New", 100.00, 5);
        assertEquals("compare(); check for differing user rating", Customer.compare(c1, c2), 2);
    }

    @Test
    public void testCompare5() {
        Camera c1 = new Camera(true, true, true, "New", 100.00, 5);
        Camera c2 = new Camera(true, true, true, "New", 100.00, 3);
        assertEquals("compare(); check for differing user rating", Customer.compare(c1, c2), 1);
    }

    @Test
    public void testCompare6() {
        Camera c1 = new Camera(true, true, true, "New", 100.00, 5);
        Camera c2 = new Camera(true, false, true, "New", 100.00, 5);
        assertEquals("compare(); check if value differs, but nothing else", Customer.compare(c1, c2), 1);
    }

    @Test
    public void testCompare7() {
        Camera c1 = new Camera(true, true, true, "New", 50.00, 5);
        Camera c2 = new Camera(true, true, true, "New", 100.00, 5);
        assertEquals("compare(); check for differing price, but nothing else", Customer.compare(c1, c2), 1);
    }

    @Test
    public void testCompare8() {
        Camera c1 = new Camera(true, true, true, "New", 100.00, 5);
        Camera c2 = new Camera(true, true, true, "New", 100.00, 5);
        assertEquals("compare(); check if all aspects same", Customer.compare(c1, c2), 0);
    }

    @Test
    public void testCompare9() {
        Camera c1 = new Camera(true, false, true, "New", 100.00, 4);
        Camera c2 = new Camera(true, true, true, "New", 100.00, 3);
        assertEquals("compare(); check if value and rating differ", Customer.compare(c1, c2), 2);
    }

    @Test
    public void testCompare10() {
        Camera c1 = new Camera(true, false, true, "New", 100.00, 4);
        Camera c2 = new Camera(false, true, false, "New", 100.00, 5);
        assertEquals("compare(); check if multiple aspects differ", Customer.compare(c1, c2), 1);
    }

    @Test
    public void testCompare11() {
        Camera c1 = new Camera(true, false, true, "New", 100.00, 4);
        Camera c2 = new Camera(false, true, false, "New", 100.00, 5);
        assertEquals("compare(); check if multiple aspects differ", Customer.compare(c2, c1), 2);
    }

    @Test
    public void testCompare12() {
        Camera c1 = new Camera(true, false, true, "Used", 100.00, 4);
        Camera c2 = new Camera(false, true, false, "New", 100.00, 5);
        assertEquals("compare(); check if all aspects differ", Customer.compare(c2, c1), 1);
    }
}

