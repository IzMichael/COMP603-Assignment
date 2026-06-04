package gridhunters;

import gridhunters.io.SaveFile;
import java.util.HashMap;

/**
 *
 * @author Michael Martin
 */
public interface SaveDAOInterface {
    public HashMap<String, SaveFile> getAllSaves();
    public SaveFile getSaveByName(String name);
    public void saveSave(SaveFile save);
    public void deleteSave(SaveFile save);
    public boolean doesRowExist(String name);
}
