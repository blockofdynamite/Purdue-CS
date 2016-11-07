import static org.junit.Assert.*;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * This test class file will determine if the Guesser method was implemented correctly.
 * 
 * @author hughe127
 * @version 29/9/14
 */

public class GuesserTest extends TestCase {
    /**
     * Burns through a ton of guesses and new methods to make sure that the code works
     */
    @Test
    public void testGuesser() {
        for (int i = 0; i < 26; i++) {
            GuessWhoGame cs180 = new GuessWhoGame(i);
            String name = Guesser.play(cs180);
            boolean characterWasRight = cs180.guess(name);
            System.out.println(cs180.score());
            assertEquals(true, characterWasRight);
        }
    }

    /**
     * call the main method to satisfy WebCAT.
     */
    @Test
    public void testMain() {
        Guesser.main(new String[0]);
    }

    /**
     * call the constructor to satisfy WebCAT.
     */
    @Test
    public void testConstructor() {
        new Guesser();
    }
}
