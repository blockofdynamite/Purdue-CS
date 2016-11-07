import java.util.ArrayList;

public class Zombie {

    private String name = "";

    private int serial = 0;

    private static int count = 0;

    private static ArrayList<Zombie> zombies = new ArrayList<Zombie>();

    public static void printStatus() {

        System.out.println(count + " zombies created so far. ");
    }

    public static int getCount() {

        return count;
    }

    public Zombie(String a) {
        this.name = a;

        this.serial = count;

        count++;

        addZombie();
    }

    private void addZombie() {

        zombies.add(this);
    }

    public static Zombie getZombie(int i) {

        return zombies.get(i);
    }

    public void printZombie() {

        System.out.println(this.name + " is zombie number " + this.serial);
    }

}
