import java.util.List;
import java.util.Scanner;

public class Project4 {

    // get the input from the user
    // parse the input
    // construct a Command object
    // and return it
    public static Command getNextCommand() {
        //New Command object
        Command command = new Command();

        //Read the line
        String readLine = StdIn.readLine();

        //If the line is null, we handle that in the main method
        if (readLine == null) {
            return null;
        }

        //New scanner on the line
        Scanner s = new Scanner(readLine);

        //Delimit using spaces
        s.useDelimiter(" ");

        //Get the date
        int date = s.nextInt();

        //Store the whole command
        command.setCommand(readLine);

        //Set the command's date
        command.setDate(date);

        //Get the type based on the second param
        String type = s.next();

        if (type.charAt(0) == 'S') {

            //Update is type 0
            command.setType(0);

            //Set arg1 as the type/name of the song
            command.setArg1(type);

            //Set arg 2 and 3 as the changes
            command.setArg2(s.nextInt());
            command.setArg3(s.nextInt());
        } else if (type.equals("T3")) {

            //Set top 3 as type 1 command
            command.setType(1);
        } else if (type.equals("B")) {

            //Set buy as command of type 2
            command.setType(2);
        } else if (type.equals("X")) {

            //Get the number to delete and set as arg2
            command.setArg2(s.nextInt());

            //Set delete as type 3
            command.setType(3);
        } else if (type.equals("end")) {

            //Set end as command of type 4
            command.setType(4);
        } else {

            //Semantics error
            return null;
        }

        return command;
    }

    public static void main(String[] args) {
        //Instantiate our song collection / tree controller :P
        SongCollection songCollection = new SongCollection();

        //Get the initial command
        Command command;
        try {
            command = getNextCommand();
        } catch (Exception e) {
            //The scanner will fail if the input is wrong, so we catch it and print out the error
            System.out.printf("Error input syntax line %d\n", 1);
            return;
        }

        //Keeping track of the line number of input we're at
        int lineNumber = 1;

        //Keeping track of previous date
        int date = 0;

        //Continue until a null command
        while (command != null) {

            //Check to make sure we're going in order of date
            if (command.getDate() <= date) {
                System.out.printf("Error semantics date %d\n", command.getDate());
                return;
            }

            //Get the command type and respond appropriately
            switch (command.getType()) {

                case 0: //update song

                    //Update the song, and if doesn't exist, print out semantics error
                    try {
                        songCollection.updateSong(command.getArg1(), command.getArg2(), command.getArg3());
                    } catch (Exception e) {
                        System.out.printf("Error semantics date %d\n", command.getDate());
                        return;
                    }
                    break;
                case 1: //top 3
                    List<Song> topThree = songCollection.popular();

                    System.out.printf("1: %s, pop=%d\n", topThree.get(0).getName(), topThree.get(0).getPopularity());
                    System.out.printf("2: %s, pop=%d\n", topThree.get(1).getName(), topThree.get(1).getPopularity());
                    System.out.printf("3: %s, pop=%d\n", topThree.get(2).getName(), topThree.get(2).getPopularity());
                    break;
                case 2: //buy

                    //Add the song, and check to make sure we don't go over 500 songs
                    if (!songCollection.addSong(command.getDate())) {
                        System.out.println("Error max size exceeded date " + command.getDate());
                        return;
                    }
                    break;
                case 3: //delete

                    //Delete the song, and make sure we don't go under 3 songs
                    if (!songCollection.deleteSong(command.getArg2())) {
                        System.out.println("Error min size violated");
                        return;
                    }
                    break;
                case 4: //end
                    List<Integer> minMax = songCollection.minMax();
                    System.out.printf("min: %d, max %d\n", minMax.get(0), minMax.get(1));
                    return;
                default:
                    break;
            }
            try {
                //Set the previous date
                date = command.getDate();

                //Increase the line number
                lineNumber++;

                //Get the next command
                command = getNextCommand();
            } catch (Exception e) {

                //The scanner will fail if the input is wrong, so we catch it and print out the error
                System.out.printf("Error input syntax line %d\n", lineNumber);
                return;
            }
        }
    }
}
