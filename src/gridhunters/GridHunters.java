package gridhunters;

import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 *
 * @author Michael Martin
 */
public class GridHunters {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SaveGUI saveGui = new SaveGUI(save -> {
                try {
                    Game game = new Game(save);

                    SwingUtilities.invokeLater(() -> {
                        GameGUI gui = new GameGUI(game, game.player, game.map);
                        gui.setVisible(true);
                    });
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            });
            saveGui.setVisible(true);
        });
    }
}