import java.io.*;
import java.util.ArrayList;

/**
 * Created by jeff on 12/26/14.
 */
public class Testing implements Serializable {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("test")));
        Testing thingy = new Testing();
        Testing thingy2 = new Testing();
        oos.writeObject(thingy);
        oos.writeObject(thingy2);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("test")));
        ArrayList<Testing> cards = new ArrayList<Testing>();
        while (true) {
            Testing blah;
            try {
                blah = (Testing) ois.readObject();
            } catch (EOFException e) {
                break;
            }
            if (blah == null) {
                break;
            }
            cards.add((Testing) blah);
        }
        ois.close();
    }
}
