package gridhunters;

import gridhunters.io.SaveFile;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 *
 * @author Michael Martin
 */
public class SaveDAO implements SaveDAOInterface {

    Connection connection;

    public SaveDAO() {
        connection = DBManager.getInstance().getConnection();
    }

    @Override
    public HashMap<String, SaveFile> getAllSaves() {
        HashMap<String, SaveFile> saves = new HashMap<>();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet results = statement.executeQuery("SELECT * FROM savefiles");

            if (!results.isBeforeFirst()) return saves;
            while (results.next()) {
                String name = results.getString("name");
                String map = results.getString("map");
                String player = results.getString("player");
                saves.put(name, new SaveFile(name, map, player));
            }
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            System.getLogger(SaveDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return saves;
    }

    @Override
    public SaveFile getSaveByName(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM savefiles WHERE name = ? LIMIT 1", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            if (!results.isBeforeFirst()) return null;
            results.next();
            String rowname = results.getString("name");
            String map = results.getString("map");
            String player = results.getString("player");
            return new SaveFile(rowname, map, player);
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            System.getLogger(SaveDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }

    @Override
    public void saveSave(SaveFile save) {
        try {
            PreparedStatement statement;
            if (doesRowExist(save.getName())) {
                statement = connection.prepareStatement("UPDATE savefiles SET map=?, player=?, updated=? WHERE name = ?");
            } else {
                statement = connection.prepareStatement("INSERT INTO savefiles (map, player, updated, name, created) VALUES (?, ?, ?, ?, ?)");
                statement.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
            }
            statement.setString(1, save.getSerializedMap());
            statement.setString(2, save.getSerializedPlayer());
            statement.setString(3, ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
            statement.setString(4, save.getName());
            statement.executeUpdate();
        } catch (SQLException | IOException ex) {
            System.getLogger(SaveDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public void deleteSave(SaveFile save) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM savefiles WHERE name = ?");
            statement.setString(1, save.getName());
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.getLogger(SaveDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public boolean doesRowExist(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM savefiles WHERE name = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            return results.isBeforeFirst();
        } catch (SQLException ex) {
            System.getLogger(SaveDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }
}
