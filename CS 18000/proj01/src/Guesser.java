/**
 * This class is the actual Guessing class. It allows the player to try and guess the character.
 * 
 * @author hughe127
 * @version 29/9/14
 */

public class Guesser {

    /**
     * The play method implements the decision tree.
     */
    public static String play(GuessWhoGame g) {

        if (g.isWearingGlasses()) { //LEVEL 1
            /*
              Alice, Bob, Carol, Emily, Frank, Gertrude, Henry, Isabelle, Larry, Mallie, Sarah, Victor, Wendy, Xavier,
                or Yasmine (15)
             */

            if (g.isWearingHat()) { //LEVEL 2: Bob, Emily, Gertrude, Sarah, or Xavier (5)

                if (g.isSmiling()) { //LEVEL 3: Emily, Gertrude, Sarah, Xavier (4)

                    if (g.eyeIsColor(Color.BROWN)) {

                        if (g.shirtIsColor(Color.GREEN)) {

                            return "Xavier";
                        }

                        else {

                            return "Sarah";
                        }

                    }
                    else {

                        if (g.eyeIsColor(Color.HAZEL)) {

                            return "Emily";
                        }

                        else {

                            return "Gertrude";
                        }

                    }
                }

                else { //LEVEL 3: Bob (1)

                    return "Bob";
                }
            }

            else { //LEVEL 2: Alice, Carol, Frank, Henry, Isabelle, Larry, Mallie, Victor, Wendy, or Yasmine (10)
                if (g.isSmiling()) { //LEVEL 3: Alice, Frank, Henry, Isabelle, Mallie, Victor, Yasmine (7)

                    if (g.hairIsColor(Color.BROWN)) { //LEVEL 4: Alice, Frank, Isabelle, Mallie (4)

                        if (g.shirtIsColor(Color.RED)) {

                            return "Mallie";
                        }

                        else {

                            if (g.eyeIsColor(Color.HAZEL)) {

                                return "Isabelle";
                            }
                            else if (g.eyeIsColor(Color.GREEN)) {

                                return "Frank";
                            }

                            else {

                                return "Alice";
                            }

                        }
                    }

                    else { //LEVEL 4: Henry, Victor, Yasmine (3)

                        if (g.hairIsColor(Color.BLOND)) {

                            return "Henry";
                        }

                        else {
                            if (g.eyeIsColor(Color.BROWN)) {

                                return "Victor";
                            }

                            else {

                                return "Yasmine";
                            }

                        }
                    }

                }

                else { //LEVEL 3: Carol, Larry, Wendy, (3)
                    if (g.shirtIsColor(Color.RED)) { //LEVEL 4: Wendy (1)

                        return "Wendy";
                    }
                    else { //LEVEL 4: Carol, Larry (2)
                        if (g.hairIsColor(Color.BLACK)) { //LEVEL 5: Carol (1)

                            return "Carol";
                        }
                        else { //LEVEL 5: Larry (1)

                            return "Larry";
                        }
                    }

                }
            }

        }
        else { //LEVEL 1
            /*
              Dave, Jack, Karen, Nick, Olivia, Phillip, Quinn, Robert, Tucker, Ursula, or Zander (11)
             */
            if (g.hairIsColor(Color.BROWN)) { //LEVEL 2: Dave, Nick, Phillip, Quinn, Robert, Tucker, Zander (7)

                if (g.eyeIsColor(Color.BROWN)) { //Dave, Quinn, Robert, Zander

                    if (g.shirtIsColor(Color.GREEN)) {

                        return "Dave";
                    }
                    else if (g.shirtIsColor(Color.BLUE)) {

                        return "Zander";
                    }

                    else if (g.isWearingHat()) {

                        return "Robert";
                    }

                    else {

                        return "Quinn";
                    }
                }

                else { //Nick, Phillip, Tucker

                    if (g.shirtIsColor(Color.BLUE)) {

                        return "Tucker";
                    }

                    else if (g.eyeIsColor(Color.BLUE)) {

                        return "Nick";
                    }

                    else {

                        return "Philip";
                    }

                }
            }

            else { //LEVEL 2: Jack, Karen, Olivia, Ursula (4)

                if (g.hairIsColor(Color.BLOND)) {

                    return "Jack";
                }

                else {

                    if (g.shirtIsColor(Color.BLUE)) {

                        return "Olivia";
                    }

                    else {

                        if (g.eyeIsColor(Color.HAZEL)) {

                            return "Karen";
                        }

                        else {

                            return "Ursula";
                        }
                    }
                }
            }

        }
    }

    /**
     * The main method calls the GuessWhoGame class's
     */

    public static void main(String[] args) {

        GuessWhoGame cs180 = new GuessWhoGame();

        String name = play(cs180);

        boolean characterWasRight = cs180.guess(name);

        //System.out.println(name);

        if (characterWasRight) {

            System.out.println("Your guess was correct!");
        }

        else {

            System.out.println("Your guess was wrong!");
        }

        System.out.println("Your score was: " + cs180.score());
    }
}
