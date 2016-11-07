/**
 * Created by Jeff Hughes on 9/29/14.
 */
public class Zombie {

    public String name = "";

    public int serial = 0;

    public static int count = 0;

    public static void printStatus() {

        System.out.println(count + " zombies created so far. ");
    }

    public Zombie(String a) {
        this.name = a;

        this.serial = count;

        count++;
    }

    public void printZombie() {

        System.out.println(this.name + " is zombie number " + this.serial);
    }

}
