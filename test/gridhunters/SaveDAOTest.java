package gridhunters;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import gridhunters.io.SaveFile;
import java.sql.SQLException;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Michael Martin
 */
public class SaveDAOTest {

    Connection connection;

    public SaveDAOTest() {
    }

    @Before
    public void setUp() throws SQLException {
        connection = DBManager.getInstance().getConnection();
        connection.prepareStatement("DROP TABLE savefiles").executeUpdate();
        connection.prepareStatement("CREATE TABLE savefiles (name VARCHAR(256), map VARCHAR(32672), player VARCHAR(32672), created VARCHAR(48), updated VARCHAR(48))").executeUpdate();
        connection.prepareStatement("INSERT INTO savefiles (map, player, updated, name, created) VALUES ('', '', '', 'Save1', '')").executeUpdate();
        connection.prepareStatement("INSERT INTO savefiles (map, player, updated, name, created) VALUES ('', '', '', 'OtherSave', '')").executeUpdate();
    }

    /**
     * Test of getAllSaves method, of class SaveDAO.
     */
    @Test
    public void testGetAllSaves() {
        System.out.println("getAllSaves");
        SaveDAO instance = new SaveDAO();
        HashMap<String, SaveFile> expResult = new HashMap<>();
        expResult.put("Save1", new SaveFile("Save1"));
        expResult.put("OtherSave", new SaveFile("OtherSave"));
        HashMap<String, SaveFile> result = instance.getAllSaves();
        assertEquals(expResult.keySet(), result.keySet());
    }

    /**
     * Test of getSaveByName method, of class SaveDAO.
     */
    @Test
    public void testGetSaveByName() {
        System.out.println("getSaveByName");
        String name = "OtherSave";
        SaveDAO instance = new SaveDAO();
        SaveFile save = instance.getSaveByName(name);
        String result = "";
        if (save != null) result = save.getName();
        assertEquals(name, result);
    }

    /**
     * Test of saveSave method, of class SaveDAO.
     */
    @Test
    public void testSaveSave() {
        System.out.println("saveSave");
        String name = "YetAnotherSave";
        SaveFile newsave = new SaveFile(name);
        SaveDAO instance = new SaveDAO();
        instance.saveSave(newsave);
        
        SaveFile save = instance.getSaveByName(name);
        String result = "";
        if (save != null) result = save.getName();
        assertEquals(name, result);
    }

    /**
     * Test of deleteSave method, of class SaveDAO.
     */
    @Test
    public void testDeleteSave() {
        System.out.println("deleteSave");
        SaveFile save = new SaveFile("OtherSave");
        SaveDAO instance = new SaveDAO();
        instance.deleteSave(save);
        
        HashMap<String, SaveFile> expResult = new HashMap<>();
        expResult.put("Save1", new SaveFile("Save1"));
        HashMap<String, SaveFile> result = instance.getAllSaves();
        assertEquals(expResult.keySet(), result.keySet());
    }

    /**
     * Test of doesRowExist method, of class SaveDAO.
     */
    @Test
    public void testDoesRowExist() {
        System.out.println("doesRowExist");
        SaveDAO instance = new SaveDAO();
        
        boolean expResult = true;
        boolean result = instance.doesRowExist("Save1");
        assertEquals(expResult, result);
        
        boolean expResult2 = false;
        boolean result2 = instance.doesRowExist("Save7");
        assertEquals(expResult2, result2);
    }
}
