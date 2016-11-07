import java.util.Random;

public class Die {

    public int roll() {

        Random r = new Random();

        int ranNumber = r.nextInt(6) + 1;

        return ranNumber;
    }

    public static void main(String[] args) {
        Die derp = new Die();
        System.out.println(derp.roll());
    }
}
