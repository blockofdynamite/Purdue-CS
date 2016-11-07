/**
 * Created by hughe127 on 10/31/14.
 */
public class Driver {
    public static void main(String[] args) {
        Thread t1 = new A();
        t1.start();
        Runnable r = new B();
        Thread t2 = new Thread(r);
        t2.start();
    }
}
