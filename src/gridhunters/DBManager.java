package gridhunters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 *
 * @author Michael Martin
 */
public class DBManager {
    private static DBManager singleton = null;
    
    String url = "jdbc:derby:GridHuntersDB; create=true";
    String username = "gridhunters";
    String password = "gridhunters";
    Connection connection;
    
    public DBManager() throws SQLException {
        DriverManager.registerDriver(new EmbeddedDriver());
        establishConnection();
        
        try {
//            connection.prepareStatement("DROP TABLE savefiles").executeUpdate();
            connection.prepareStatement("CREATE TABLE savefiles (name VARCHAR(256), map VARCHAR(32672), player VARCHAR(32672), created VARCHAR(48), updated VARCHAR(48))").executeUpdate();
        } catch (SQLException err) {
            System.out.println(err);
        }
    }
    
    public Connection getConnection() {
        return this.connection;
    }

    public final void establishConnection() throws SQLException {
        if (this.connection == null) {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println(url + " Get Connected Successfully ....");
        }
    }
    
    public void closeConnections() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    
    public static synchronized DBManager getInstance() {
        if (singleton == null) {
            try {
                singleton = new DBManager();
            } catch (SQLException ex) {
                System.getLogger(DBManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

        return singleton;
    }
}
