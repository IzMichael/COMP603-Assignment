package gridhunters;

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
    
    private HashMap<String, SaveFile> restore() throws IOException, ClassNotFoundException {
        FileReader fileReader = new FileReader(this.path);
        HashMap<String, SaveFile> savemap = new HashMap<>();
        
        String contents = fileReader.readAllAsString();
        String lines[] = contents.split("\n");
        for (String line : lines) {
            String[] params = line.split(" ");
            if (params.length == 2) savemap.put(params[0], new SaveFile(params[1]));
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
