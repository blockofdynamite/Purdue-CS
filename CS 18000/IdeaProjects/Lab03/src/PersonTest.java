/**
 * CS180 - Lab 03 - PersonTest.java
 *
 * This is a JUnit test class to test the methods in Person
 *
 * All JavaDoc comments are from the wiki
 *
 * @author hughe127
 *
 * @lab 802
 *
 * @date 19/9/14
 */

import junit.framework.TestCase;

public class PersonTest extends TestCase {

    private Person adultBadTC;

    private Person childGoodHDL;

    private Person adultBadLDL;

    protected void setUp() throws Exception {

        super.setUp();

        //Initialize Test People

        adultBadTC = new Person("00254789", 21, 100, 100, 100);
        childGoodHDL = new Person("00872324", 14, 50, 45, 70);
        adultBadLDL = new Person("00458734", 19, 80, 150, 80);

    }

    /**
     * Test the getCholesterol method
     */

    public void testGetTotalCholesterol() {

        assertEquals(165, childGoodHDL.getTotalCholesterol());
        assertEquals(300, adultBadTC.getTotalCholesterol());
        assertEquals(310, adultBadLDL.getTotalCholesterol());
    }

    /**
     * Test the hasGoodTotalCholesterol method.
     */

    public void testHasGoodTotalCholesterol() {

        assertFalse(adultBadTC.hasGoodCholesterol());
        assertTrue(childGoodHDL.hasGoodCholesterol());
        assertFalse(adultBadTC.hasGoodCholesterol());
    }

    /**
     * Test the hasGoodLDL method.
     */

    public void testHasGoodLDL() {

        assertTrue(childGoodHDL.hasGoodLDL());
        assertFalse(adultBadLDL.hasGoodLDL());
        assertFalse(adultBadLDL.hasGoodLDL());

    }

    /**
     * Test the hasGoodHDL method.
     */

    public void testHasGoodHDL() {

        assertTrue(adultBadLDL.hasGoodHDL());
        assertTrue(childGoodHDL.hasGoodHDL());
        assertTrue(adultBadTC.hasGoodHDL());
    }
}
