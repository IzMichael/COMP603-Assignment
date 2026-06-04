package gridhunters;

import static org.junit.Assert.*;

import java.sql.SQLException;
import org.junit.Test;

/**
 *
 * @author Michael Martin
 */
public class DBManagerTest {

    public DBManagerTest() {
    }

    /**
     * Test of getConnection method, of class DBManager.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        DBManager instance = DBManager.getInstance();
        boolean result = false;
        try {
            result = instance.getConnection().isValid(100);
        } catch (SQLException ex) {
            System.getLogger(DBManagerTest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        assertEquals(true, result);
    }
}
