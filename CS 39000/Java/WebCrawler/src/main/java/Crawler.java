import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Connection.*;
import org.jsoup.select.Elements;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Crawler {
    // For going through all links
    private int nextURLID;
    private int nextURLIDSeen;

    // The ID to insert into the DB
    private int urlID;

    private Connection connection;
    private Properties props;

    Crawler() throws IOException, SQLException {
        this.readProperties();
        this.createDB();
        urlID = 0;
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

    private void createDB() throws SQLException, IOException {
        openConnection();

        Statement stat = connection.createStatement();

        // Delete the table first if any
        if (tableExist("Word") && tableExist("URL")) {
            stat.executeUpdate("ALTER TABLE CRAWLER.Word DROP FOREIGN KEY `Word__URL.URLID_fk`");
            stat.executeUpdate("DROP TABLE Word");
            stat.executeUpdate("DROP TABLE URL");
        }

        // Create the table
        stat.executeUpdate("CREATE TABLE CRAWLER.URL " +
                "(URLID INT PRIMARY KEY, " +
                "URL VARCHAR(1000) NOT NULL, " +
                "Title VARCHAR(500)," +
                "Description VARCHAR(700)," +
                "Image VARCHAR(500)" +
                ")");

        stat.executeUpdate("CREATE TABLE CRAWLER.Word " +
                "(Word VARCHAR(500) NOT NULL, " +
                "URLID INT NOT NULL, " +
                "CONSTRAINT `Word__URL.URLID_fk` FOREIGN KEY (URLID) REFERENCES CRAWLER.URL (URLID))");
    }

    private boolean urlInDB(String urlFound) throws SQLException, IOException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CRAWLER.URL WHERE URL LIKE ?");
        preparedStatement.setString(1, urlFound);
        ResultSet result = preparedStatement.executeQuery();

        return result.next();
    }

    private void insertURLInDB(String url) throws SQLException, IOException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CRAWLER.URL VALUES (?, ?, '', '', '')");
        preparedStatement.setInt(1, urlID);
        preparedStatement.setString(2, url);
        preparedStatement.executeUpdate();
        urlID++;
    }

    private String grabURLInDB(int URLID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT URL FROM URL WHERE URLID = ?");
        preparedStatement.setInt(1, URLID);
        ResultSet result = preparedStatement.executeQuery();
        if (result.next()) {
            return result.getString("url");
        } else {
            return null;
        }
    }

    private void fetchURL(String urlScanned) throws IOException, SQLException {
        Response res;
        try {
            res = Jsoup.connect(urlScanned).timeout(6000).execute();
        } catch (Exception e) {
            return;
        }

        Document doc = res.parse();
        if (nextURLIDSeen <= Integer.parseInt(props.getProperty("crawler.maxurls"))) {
            for (Element link : doc.select("a[href]")) {
                String url = link.attr("abs:href");

                if (url.contains(props.getProperty("crawler.domain")) && url.contains("http") && !urlInDB(url) && !url.toLowerCase().contains("pdf")) {
                    insertURLInDB(url);
                    nextURLIDSeen++;

                    if (nextURLIDSeen > Integer.parseInt(props.getProperty("crawler.maxurls"))) {
                        break;
                    }
                }
            }
        }

        if (nextURLID == 0) {
            return;
        }

        // IMAGES ------------------------------

        String image;
        Elements images = doc.select("img");

        if (images.first() != null) {
            if ((image = images.first().absUrl("src")).equals("https://www.cs.purdue.edu/images/logo.svg")) {
                if (images.size() > 2) {
                    image = images.get(2).absUrl("src");
                } else {
                    image = images.get(1).absUrl("src");
                }
            }

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE URL SET Image=? WHERE URLID=?");
            preparedStatement.setString(1, image.replace(" ", "%20"));
            preparedStatement.setInt(2, nextURLID - 1);
            preparedStatement.execute();
        }

        // DESCRIPTIONS ------------------------------

        String desc;
        if (doc.select("p") == null || doc.select("p").text().length() == 0) {
            desc = doc.select("body").text().replaceAll("[^A-Za-z0-9 ]", "");
        } else {
            desc = doc.select("p").text().replaceAll("[^A-Za-z0-9 ]", "");
        }

        if (desc.length() >= 500) {
            desc = desc.substring(0, 499);
        } else {
            desc = desc.substring(0, desc.length());
        }

        if (desc.length() > 0) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE URL SET Description=? WHERE URLID=?");
            preparedStatement.setString(1, desc);
            preparedStatement.setInt(2, nextURLID - 1);
            preparedStatement.execute();
        }

        // TITLE ------------------------------

        String title = doc.title();
        if (title.length() > 0) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE URL SET Title=? WHERE URLID=?");
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, nextURLID - 1);
            preparedStatement.execute();
        }

        // WORDS ------------------------------

        desc = doc.select("body").text().replaceAll("[^A-Za-z0-9 ]", "");
        String[] split = desc.split(" ");
        StringBuilder words = new StringBuilder("");
        for (String word : split) {
            if (word.length() <= 1) {
                continue;
            }
            word = word.toLowerCase();

            if (word.length() > 499) {
                word = word.substring(0, 499);
            }

            word = "('" + word + "', " + (nextURLID - 1) + "), ";

            words.append(word);
        }

        if (words.length() > 2) {
            words.delete(words.length() - 2, words.length());
        } else {
            return;
        }

        connection.createStatement().execute("INSERT INTO Word(Word, URLID) VALUES " + words.toString());

        System.out.println(nextURLID);
    }

    private boolean tableExist(String tableName) throws SQLException {
        ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
        while (rs.next()) {
            String tName = rs.getString("TABLE_NAME");
            if (tName != null && tName.equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    public void crawl() throws IOException, SQLException {
        nextURLIDSeen++;

        String currentURL = props.getProperty("crawler.root");

        while (nextURLID < nextURLIDSeen) {
            fetchURL(currentURL);
            currentURL = grabURLInDB(nextURLID);
            nextURLID++;
        }
    }
}
