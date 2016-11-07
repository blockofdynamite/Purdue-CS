import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class StringManipulatorTest extends TestCase {

    /**
     * A test method.
     * (Replace "X" with a name describing the test.  You may write as
     * many "testSomething" methods in this class as you wish, and each
     * one will be called when running JUnit over this class.)
     */
    public void testMakeUserName() {
        StringManipulator m = new StringManipulator();
        String ret = m.makeUserName("john doe");
        assertEquals("jdoe", ret);
    }
    public void testMakeUserNameLower() {
        StringManipulator m = new StringManipulator();
        String ret = m.makeUserName("JOHN DOE");
        assertEquals("jdoe", ret);
    }
    public void testMakeEmail() {
        StringManipulator m = new StringManipulator();
        String email = m.makeEmail("jdoe", "purdue.EDU");
        assertEquals("jdoe@purdue.edu", email);
    }

}