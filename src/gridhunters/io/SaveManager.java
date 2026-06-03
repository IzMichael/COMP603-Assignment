package gridhunters.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Michael Martin
 */
public class SaveManager {
    private static SaveManager instance;
    HashMap<String, SaveFile> saves = new HashMap<>();
    final String directory = "./resources/";
    final String file = "savedata.txt";
    final String path = this.directory + this.file;
    
    public SaveManager() throws ClassNotFoundException, IOException {
        File dir = new File(this.directory);
        dir.mkdirs();
        File f = new File(this.path);
        f.createNewFile();
        
        this.saves = this.restore();
    }
    
    public static synchronized SaveManager getInstance() throws ClassNotFoundException, IOException {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }
    
    private HashMap<String, SaveFile> restore() throws IOException, ClassNotFoundException {
        FileReader fileReader = new FileReader(this.path);
        HashMap<String, SaveFile> savemap = new HashMap<>();

        String contents = fileReader.readAllAsString();
        String lines[] = contents.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            int firstSpaceIdx = line.indexOf(" ");
            if (firstSpaceIdx == -1) {
                continue;
            }

            String saveName = line.substring(0, firstSpaceIdx);
            String base64Data = line.substring(firstSpaceIdx + 1);

            try {
                savemap.put(saveName, new SaveFile(saveName, base64Data)); 
            } catch (Exception e) {
                System.err.println("Failed to parse save profile [" + saveName + "]: " + e.getMessage());
            }
        }        

        return savemap;
    }
    
    private void write(HashMap<String, SaveFile> savemap) throws IOException {
        try (FileWriter fileWriter = new FileWriter(this.path)) {
            for (String i : savemap.keySet()) {
                fileWriter.write(i + " " + savemap.get(i).toString() + "\n");
            }
        }
    }
    
    public HashMap<String, SaveFile> getSaves() {
        return this.saves;
    }
    
    public void setSave(String namespace, SaveFile file) throws IOException {
        saves.put(namespace, file);
        write(saves);
    }
}
