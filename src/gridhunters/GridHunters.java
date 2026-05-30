package gridhunters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Michael Martin
 */
public class GridHunters {

    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        System.out.println("Welcome to Grid Hunters!");
        System.out.println("");

        SaveManager sm = new SaveManager();
        HashMap<String, SaveFile> saves = sm.getSaves();
        List<String> keys = new ArrayList<>(saves.keySet());
        System.out.println("Please select a save file, or start a new game with [+].");
        int i = 0;
        for (String entry : keys) {
            SaveFile save = saves.get(entry);
            char letter = (char) ('A' + i);
            System.out.println("  [" + letter + "]   " + save.getName());
            i += 1;
        }
        if (keys.isEmpty()) {
            System.out.println("  You have no previous save files.");
        }
        SaveFile save = null;
        Scanner scanner = new Scanner(System.in);
        while (save == null) {
            char selection = scanner.next().toUpperCase().charAt(0);
            scanner.nextLine();
            
            if (selection == '+') {
                System.out.println("Please name your save file:");
                String name = scanner.nextLine();
                save = new SaveFile();
                save.setName(name);
            } else if (selection - 64 > 0 && selection - 64 <= saves.keySet().size()) {
                int index = (int) selection - 65;
                String key = keys.get(index);
                save = saves.get(key);
            } else {
                System.out.println("Invalid input! Please try again.");
            }
        }

        Game game = new Game(save);

        SwingUtilities.invokeLater(() -> {
            GameGUI gui = new GameGUI(gameSession.player, gameSession.map);
            gui.setVisible(true);
        });
    }

}
