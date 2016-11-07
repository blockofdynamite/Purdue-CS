public class Command {
    // define a class to represent all the
    // commands from the user input
    private String command;
    private int type;
    private int date;
    private String arg1;
    private int arg2;
    private int arg3;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getArg3() {
        return arg3;
    }

    public void setArg3(int arg3) {
        this.arg3 = arg3;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }
}