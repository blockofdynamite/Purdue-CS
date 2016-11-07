import java.util.Scanner;

class Nirvana extends Thread {
    public boolean enlightenment = false;

    public void run() {
        meditate();
    }

    void meditate() {
        while (!enlightenment) {
            System.out.println("Om...");
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("finished");
                }
            }
        }
    }

    void enlighten() {
        enlightenment = true;
        synchronized (this) {
            this.notify();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Nirvana t = new Nirvana();
        t.start();
        Scanner s = new Scanner(System.in);
        s.nextLine();
        t.enlighten();
        t.join();
    }
}