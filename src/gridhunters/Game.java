package gridhunters;

import gridhunters.io.SaveFile;
import gridhunters.patterns.GameObserver;
import gridhunters.tiles.Map;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Martin
 */
public final class Game implements Serializable {

    public Map map;
    public Player player;
    public SaveFile save;
    private transient SaveDAO saveDAO;

    private transient List<GameObserver> observers = new ArrayList<>();

    public Game(SaveFile save) throws IOException, ClassNotFoundException {
        this.saveDAO = new SaveDAO();
        this.save = save;

        if (save != null && save.getMap() != null) {
            this.map = save.getMap();
        } else {
            this.map = new Map(this);
            this.save.setMap(this.map);
        }

        if (save != null && save.getPlayer() != null) {
            this.player = save.getPlayer();
        } else {
            this.player = createPlayer();
            this.save.setPlayer(this.player);
        }

        if (this.player != null) {
            this.player.game = this;
        }

        System.out.println("");
        this.map.getTile(this.player.x, this.player.y).exploreVisual();

        this.save();
    }

    public void registerObserver(GameObserver observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void notifyStatsChanged(int health, int maxHealth, String logText) {
        if (observers == null) return;
        for (GameObserver obs : observers) {
            if (obs != null) {
                obs.onPlayerStatsChanged(health, maxHealth, logText);
            }
        }
    }

    public void notifyCombatTriggered(Enemy enemy) {
    if (observers == null) return;
        for (GameObserver obs : observers) {
            if (obs != null) {
                obs.onCombatTriggered(enemy);
            }
        }
    }

    public void save() throws IOException, ClassNotFoundException {
        saveDAO.saveSave(this.save);

        System.out.println("Successfully saved game.");
    }

    public Player createPlayer() {
        JPanel panel = new JPanel(new BorderLayout(5, 10));

        String statSheet =
            "<html><body style='font-family:sans-serif; padding:5px;'>" +
            "<h3>Select your character class:</h3>" +
            "<table border='1' cellpadding='4' cellspacing='0' style='border-color:#555; text-align:center;'>" +
            "  <tr style='background-color:#333; color:orange;'>" +
            "    <th>Class</th><th>Vitality</th><th>Strength</th><th>Magic</th><th>Defence</th><th>Agility</th>" +
            "  </tr>" +
            "  <tr><td><b>Warrior</b></td><td> </td><td>++++++</td><td>-----</td><td> </td><td> </td></tr>" +
            "  <tr><td><b>Mage</b></td><td> </td><td>-----</td><td>+++++</td><td> </td><td> </td></tr>" +
            "  <tr><td><b>Tank</b></td><td>++++++</td><td> </td><td>-----</td><td>+++++</td><td> </td></tr>" +
            "  <tr><td><b>Rogue</b></td><td>-----</td><td>++++++</td><td> </td><td>-----</td><td>+++++</td></tr>" +
            "  <tr><td><b>Cleric</b></td><td>++++++</td><td>-----</td><td>+++++</td><td> </td><td>-----</td></tr>" +
            "</table>" +
            "</body></html>";

        JLabel lblStats = new JLabel(statSheet);
        panel.add(lblStats, BorderLayout.CENTER);

        String[] options = {"Warrior", "Mage", "Tank", "Rogue", "Cleric"};

        Player newPlayer;

        while (true) {
            int selection = JOptionPane.showOptionDialog(
                null,
                panel,
                "Character Class Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            switch (selection) {
                case 0 -> {
                    newPlayer = new Player(this, "Warrior", 1, 1.25, 0.25, 1, 1);
                }
                case 1 -> {
                    newPlayer = new Player(this, "Mage", 1, 0.25, 1.25, 1, 1);
                }
                case 2 -> {
                    newPlayer = new Player(this, "Tank", 1.25, 1, 0.25, 1.25, 1);
                }
                case 3 -> {
                    newPlayer = new Player(this, "Rogue", 0.25, 1.25, 0.25, 0.25, 1.25);
                }
                case 4 -> {
                    newPlayer = new Player(this, "Cleric", 1.25, 0.25, 0.25, 1, 0.25);
                }
                default -> {
                    JOptionPane.showMessageDialog(null, "Before you start playing, you must choose a class.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                    continue;
                }
            }

            if (this.save != null) {
                newPlayer.setName(this.save.getName());
            } else {
                newPlayer.setName("Unknown Hunter");
            }
            return newPlayer;
        }
    }
}
