import java.util.Random;

/**
 * Implements the Guess Who game.
 * Picks a secret character and counts the number of questions asked.
 * 
 * @author pritchey
 * @version 2014-07-17
 */

public class GuessWhoGame {
    /**
     * secret character
     */
    private Character secret;

    /**
     * number of questions asked
     */
    private int numQuestions;

    /**
     * array of secret characters
     */
    private static final Character[] characters = new Character[] {
        new Character("Alice",    Color.BROWN, Color.BLUE,  Color.GREEN, true,  true,  false),
        new Character("Bob",      Color.BROWN, Color.BROWN, Color.GREEN, true,  false, true),
        new Character("Carol",    Color.BLACK, Color.BLUE,  Color.BLUE,  true,  false, false),
        new Character("Dave",     Color.BROWN, Color.BROWN, Color.GREEN, false, true,  true),
        new Character("Emily",    Color.BROWN, Color.HAZEL, Color.RED,   true,  true,  true),
        new Character("Frank",    Color.BROWN, Color.GREEN, Color.GREEN, true,  true,  false),
        new Character("Gertrude", Color.BLACK, Color.BLUE,  Color.BLUE,  true,  true,  true),
        new Character("Henry",    Color.BLOND, Color.BROWN, Color.RED,   true,  true,  false),
        new Character("Isabelle", Color.BROWN, Color.HAZEL, Color.GREEN, true,  true,  false),
        new Character("Jack",     Color.BLOND, Color.BROWN, Color.BLUE,  false, true,  false),
        new Character("Karen",    Color.BLACK, Color.HAZEL, Color.GREEN, false, true,  false),
        new Character("Larry",    Color.RED,   Color.BLUE,  Color.BLUE,  true,  false, false),
        new Character("Mallie",   Color.BROWN, Color.BLUE,  Color.RED,   true,  true,  false),
        new Character("Nick",     Color.BROWN, Color.BLUE,  Color.RED,   false, true,  false),
        new Character("Olivia",   Color.BLACK, Color.BROWN, Color.BLUE,  false, true,  false),
        new Character("Philip",   Color.BROWN, Color.GREEN, Color.RED,   false, true,  false),
        new Character("Quinn",    Color.BROWN, Color.BROWN, Color.RED,   false, true,  false),
        new Character("Robert",   Color.BROWN, Color.BROWN, Color.RED,   false, true,  true),
        new Character("Sarah",    Color.RED,   Color.BROWN, Color.BLUE,  true,  true,  true),
        new Character("Tucker",   Color.BROWN, Color.BLUE,  Color.BLUE,  false, true,  false),
        new Character("Ursula",   Color.BLACK, Color.BROWN, Color.GREEN, false, true,  false),
        new Character("Victor",   Color.RED,   Color.BROWN, Color.BLUE,  true,  true,  false),
        new Character("Wendy",    Color.BROWN, Color.BLUE,  Color.RED,   true,  false, false),
        new Character("Xavier",   Color.BLACK, Color.BROWN, Color.GREEN, true,  true,  true),
        new Character("Yasmine",  Color.RED,   Color.BLUE,  Color.GREEN, true,  true,  false),
        new Character("Zander",   Color.BROWN, Color.BROWN, Color.BLUE,  false, true,  false)
    };

    /**
     * Class to represent a Guess Who character
     * @author pritchey
     * @version 2014-07-17
     */
    private static class Character {
        /**
         * hair color
         */
        private Color hair;
        /**
         * eye color
         */
        private Color eyes;
        /**
         * shirt color
         */
        private Color shirt;
        /**
         * wears glasses?
         */
        private boolean glasses;
        /**
         * is smiling?
         */
        private boolean smiling;
        /**
         * wearing a hat?
         */
        private boolean hat;
        /**
         * character's name
         */
        private String name;

        /**
         * construct a new character with the specified attributes
         * @param name
         * @param hair
         * @param eyes
         * @param shirt
         * @param glasses
         * @param smiling
         * @param hat
         */
        public Character(String name, Color hair, Color eyes, Color shirt,
                         boolean glasses, boolean smiling, boolean hat) {
            this.hair = hair;
            this.eyes = eyes;
            this.shirt = shirt;
            this.glasses = glasses;
            this.smiling = smiling;
            this.hat = hat;
            this.name = name;
        }

        /**
         * 
         * @return the hair color of the character
         */
        public Color hair() { return this.hair; }
        /**
         * 
         * @return eye color of the character
         */
        public Color eyes() { return this.eyes; }
        /**
         * 
         * @return shirt color of the character
         */
        public Color shirt() { return this.shirt; }
        /**
         * 
         * @return true if character wears glasses
         */
        public boolean glasses() { return this.glasses; }
        /**
         * 
         * @return true if character is smiling
         */
        public boolean smiling() { return this.smiling; }
        /**
         * 
         * @return true if character is wearing a hat
         */
        public boolean hat() { return this.hat; }
        /**
         * 
         * @return the character's name
         */
        public String name() { return this.name; }
    }

    /**
     * select the secret character at random
     */
    public GuessWhoGame() {
        Random random = new Random(System.currentTimeMillis());
        secret = characters[random.nextInt(characters.length)];
        numQuestions = 0;
    }

    /**
     * select the i-th secret character<br>
     * use for JUnit testing
     */
    public GuessWhoGame(int i) {
        secret = characters[i % characters.length];
        numQuestions = 0;
    }

    /**
     * 
     * @param c - Color of hair to ask about
     * @return true if secret chartacter's hair is the specified color
     */
    public boolean hairIsColor(Color c) {
        numQuestions++;
        return secret.hair().equals(c);
    }

    /**
     * 
     * @param c - Color of etrue to ask about
     * @return true if secret character's etrue are the specified color
     */
    public boolean eyeIsColor(Color c) {
        numQuestions++;
        return secret.eyes().equals(c);
    }

    /**
     * 
     * @param c - Color of shirt to ask about
     * @return true if secret character's shirt is the specified color
     */
    public boolean shirtIsColor(Color c) {
        numQuestions++;
        return secret.shirt().equals(c);
    }

    /**
     * 
     * @return true if secret character is wearing glasses
     */
    public boolean isWearingGlasses() {
        numQuestions++;
        return secret.glasses();
    }

    /**
     * 
     * @return true if secret character is smiling
     */
    public boolean isSmiling() {
        numQuestions++;
        return secret.smiling();
    }

    /**
     * 
     * @return true if secret character is wearing a hat
     */
    public boolean isWearingHat() {
        numQuestions++;
        return secret.hat();
    }

    /**
     * method to guess the identity of the secret character
     * @param name - name of character as a String
     * @return true if secret character's name is correct
     */
    public boolean guess(String name) {
        if (secret.name().equalsIgnoreCase(name)) {
            numQuestions++;
            return true;
        } else {
            numQuestions += 11; // penalty for incorrect guess
            System.out.println("it was " + secret.name());
            return false;
        }
    }

    /**
     * 
     * @return the number of questions asked
     */
    public int score() {
        return this.numQuestions;
    }
}
