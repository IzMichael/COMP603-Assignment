/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author Michael Martin
 */
public final class Game implements Serializable {
    static int terminalWidth = 80;
    public Map map;
    public Player player;
    public SaveFile save;

    public Game(SaveFile save) throws IOException, ClassNotFoundException {
        printHorizontalBorder();
        printCentered("-=- Grid Hunters -=-");
        printHorizontalBorder();
        System.out.println("");
        
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
        
        System.out.println("");
        this.map.getTile(this.player.x, this.player.y).explore();
        
        this.save();
    }
    
    public void save() throws IOException, ClassNotFoundException {
        SaveManager sm = new SaveManager();
        sm.setSave(this.save.name, this.save);
        System.out.println("Successfully saved game.");
    }

    public Player createPlayer() {
        System.out.println("Select your class:      Vitality   Strength    Magic    Defence   Agility");
        System.out.println("  [W]    Warrior                    ++++++     -----                     ");
        System.out.println("  [M]       Mage                    ------     +++++                     ");
        System.out.println("  [T]       Tank         ++++++                -----     +++++           ");
        System.out.println("  [R]      Rogue         ------     ++++++               -----     +++++ ");
        System.out.println("  [C]     Cleric         ++++++     ------     +++++               ----- ");
        while (this.player == null) {
            Scanner scanner = new Scanner(System.in);
            char selection = scanner.next().toUpperCase().charAt(0);
            switch (selection) {
                case 'W' -> {
                    return new Player(this, "Warrior", 1, 1.25, 0.25, 1, 1);
                }
                case 'M' -> {
                    return new Player(this, "Mage", 1, 0.25, 1.25, 1, 1);
                }
                case 'T' -> {
                    return new Player(this, "Tank", 1.25, 1, 0.25, 1.25, 1);
                }
                case 'R' -> {
                    return new Player(this, "Rogue", 0.25, 1.25, 0.25, 0.25, 1.25);
                }
                case 'C' -> {
                    return new Player(this, "Cleric", 1.25, 0.25, 0.25, 1, 0.25);
                }
                default -> {
                    System.out.println("Invalid selection! Please try again.");
                }
            }
        }
        return null;
    }

    private void printCentered(String string) {
        int length = string.length();
        StringBuilder line = new StringBuilder();
        line.append("| ");
        line.repeat(" ", (terminalWidth - 4 - length) / 2);
        line.append(string);
        line.repeat(" ", (terminalWidth - 4 - length) / 2);
        line.append(" |");
        System.out.println(line.toString());
    }

    private void printHorizontalBorder() {
        StringBuilder line = new StringBuilder();
        line.append("+");
        line.repeat("-", terminalWidth - 2);
        line.append("+");
        System.out.println(line.toString());
    }
}
