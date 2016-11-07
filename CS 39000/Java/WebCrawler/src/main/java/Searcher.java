import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import fi.iki.elonen.NanoHTTPD;

public class Searcher extends NanoHTTPD {

    private Properties props;
    private Connection connection;

    public Searcher(int port) throws IOException, SQLException {
        super(port);
        readProperties();
        openConnection();
    }

    private void readProperties() throws IOException {
        props = new Properties();
        FileInputStream in = new FileInputStream("database.properties");
        props.load(in);
        in.close();
    }

    private void openConnection() throws SQLException, IOException {
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) {
            System.setProperty("jdbc.drivers", drivers);
        }

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        connection = DriverManager.getConnection(url, username, password);
    }

    public void search() throws IOException {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    private ArrayList<Result> getResults(String query) throws SQLException {
        ArrayList<Integer> URLIDs = new ArrayList<>();

        String[] words = query.split(" ");

        boolean firstRun = true;

        for (String word : words) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT URLID FROM Word WHERE Word = ?");
            preparedStatement.setString(1, word);
            ResultSet res = preparedStatement.executeQuery();
            ArrayList<Integer> multiCheck = new ArrayList<>();
            while (res.next()) {
                if (words.length > 1 && !firstRun) {
                    if (!multiCheck.contains(res.getInt(1))) {
                        multiCheck.add(res.getInt(1));
                    }
                } else {
                    if (!URLIDs.contains(res.getInt(1))) {
                        URLIDs.add(res.getInt(1));
                    }
                }
            }
            if (words.length > 1 && !firstRun) {
                ArrayList<Integer> doubleIntegrity = new ArrayList<>();
                for (int i : multiCheck) {
                    if (URLIDs.contains(i)) {
                        doubleIntegrity.add(i);
                    }
                }
                URLIDs = new ArrayList<>(doubleIntegrity);
            }
            firstRun = false;
        }

        ArrayList<Result> URLs = new ArrayList<>();

        for (int URLID : URLIDs) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM URL WHERE URLID = ?");
            preparedStatement.setInt(1, URLID);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                URLs.add(new Result(res.getString(2), res.getString(3), res.getString(5), res.getString(4)));
            }
        }

        return URLs;
    }

    // This is the way Gustavo wants it. I don't like it, but ¯\_(ツ)_/¯
    @Override
    public Response serve(IHTTPSession session) {
        StringBuilder msg = new StringBuilder("<html><body><h1>CS390 Search</h1>\n");
        Map<String, String> params = session.getParms();
        if (params.get("query") == null) {
            msg.append("<form action='?' method='get'>\n  <p>Your query: <input type='text' name='query'></p>\n" + "</form>\n");
        } else {
            msg.append("<p>Your query: ").append(params.get("query")).append("</p><hr>\n\n");
            msg.append("<form action='?' method='get'>\n  <p>Your query: <input type='text' name='query'></p>\n" + "</form>\n");
            ArrayList<Result> results;
            try {
                results = getResults(params.get("query").toLowerCase());
            } catch (SQLException e) {
                results = null;
            }
            if (results == null) {
                msg.append("<p>No results found.</p>");
            } else {
                for (Result res : results) {
                    msg.append(String.format("<p><a href=\"%s\"><img src=%s width=\"125\"/></a></p>\n\n", res.getLink(), res.getImage()));
                    msg.append(String.format("<p><a href=\"%s\">%s</a></p>\n\n", res.getLink(), res.getTitle()));
                    msg.append(String.format("<p>%s...</p>\n\n", res.getDesc()));
                    msg.append("<hr>");
                }
            }
        }
        return newFixedLengthResponse(msg.toString() + "</body></html>\n");
    }
}
