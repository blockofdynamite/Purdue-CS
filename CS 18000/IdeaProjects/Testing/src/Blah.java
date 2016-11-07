import java.io.File;

public class Blah {
    public static void main(String[] args) {
        File f = new File(System.getProperty("user.home"), ".my-program-data");
        //my-program-data is the name of the folder to be made
        //user.home is the home directory
        f.mkdirs();
        boolean fake = true;
        System.out.println(System.getProperty("os.name").toLowerCase());
        while (fake) {
            File[] testing = f.listFiles();
            for (File dir : testing) {
                System.out.println(dir.toString());
                if (dir.isDirectory()) {
                    fake = false;
                }
            }
        }
    }
}
