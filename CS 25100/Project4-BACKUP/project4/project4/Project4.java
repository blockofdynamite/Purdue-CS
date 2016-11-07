import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Project4 {

    // get the input from the user
    // parse the input
    // construct a Command object
    // and return it
    public static Command getNextCommand() {
        Command command = new Command();

        String readLine = StdIn.readLine();

        if (readLine == null) {
            return null;
        }

        Scanner s = new Scanner(readLine);

        s.useDelimiter(" ");

        int date = s.nextInt(); //Not used, just need to get the next element in the string.

        command.setCommand(readLine);
        command.setDate(date);

        String type = s.next();

        if (type.charAt(0) == 'S') {
            command.setType(0);
            command.setArg1(s.nextInt());
            command.setArg2(s.nextInt());
        } else if (type.equals("T3")) {
            command.setType(1);
        } else if (type.equals("B")) {
            command.setType(2);
        } else if (type.equals("X")) {
            command.setType(3);
        } else if (type.equals("end")) {
            command.setType(4);
        } else {
            return null;
        }

        return command;
    }

    public static void main(String[] args) {
        SongCollection songCollection = new SongCollection();

        Command command = getNextCommand();
        while (command != null) {
            System.out.println(command.getCommand());
            switch (command.getType()) {
                case 0: //update song
                    break;
                case 1: //top 3
                    break;
                case 2: //buy
                    if (!songCollection.addSong(command.getDate())) {
                        System.out.println("Error max size exceeded date " + command.getDate());
                        return;
                    }
                    break;
                case 3: //delete
                    if (!songCollection.deleteSong(command.getArg1())) {
                        System.out.println("Error min size violated");
                        return;
                    }
                    break;
                case 4: //end
                    break;
                default:
                    break;
            }
            try {
                command = getNextCommand();
            } catch (Exception e) {
                return;
            }
        }
    }
}
