import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by hughe127 on 12/5/14.
 */
public class Stats {
    public static Map<String, Integer> map = new HashMap<String, Integer>();

    public static void wins() throws IOException {
        File f = new File("~/Downloads/lab11inputfile.txt");
        FileInputStream fis = new FileInputStream(f);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
        while (true) {
            String s = bfr.readLine();
            char score;
            try {
                score = s.charAt(0);
            } catch (NullPointerException e) {
                break;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(s);
            stringTokenizer.nextElement(); //gets rid of the winner team
            if (score == 1) {
                while (stringTokenizer.hasMoreElements()) {
                    String name = stringTokenizer.nextToken();
                    try {
                        if (name.equals("vs")) {
                            break;
                        }
                        map.get(name);
                    } catch (NullPointerException e) {
                        map.put(name, (int) score);
                        break;
                    }
                    int numWins = map.get(name);
                    numWins++;
                    map.replace(name, numWins);
                }
            }
            if (score == 0) {
                for (int i = 0; i < 7; i++) {
                    stringTokenizer.nextElement();
                }
                while (stringTokenizer.hasMoreElements()) {
                    String name = stringTokenizer.nextToken();
                    try {
                        map.get(name);
                    } catch (NullPointerException e) {
                        map.put(name, (int) score);
                        break;
                    }
                    int numWins = map.get(name);
                    numWins++;
                    map.replace(name, numWins);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        wins();
    }
}
