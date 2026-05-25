/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters.tiles;

import java.util.Scanner;
import gridhunters.Game;
import gridhunters.Item;

/**
 *
 * @author Michael Martin
 */
public class ClearingTile extends Tile {

    public ClearingTile(Game game, int x, int y) {
        super(game, "Clearing", x, y);
    }

    @Override
    public void explore() {
        this.visited = true;
        this.game.player.setPosition(this.x, this.y);
        System.out.println("");
        this.game.player.printPlayerStatus();
        System.out.println("");
        System.out.println("(" + this.x + ", " + this.y + ") You find yourself in an empty clearing...");
        System.out.println("There's not much here, but you can explore to the:");
        System.out.println("  [N]      North");
        System.out.println("  [E]       East");
        System.out.println("  [S]      South");
        System.out.println("  [W]       West");
        System.out.println("You can also press [*] to Save and Quit.");
        Scanner scanner = new Scanner(System.in);
        boolean valid = false;
        while (valid == false) {
            char selection = scanner.next().toUpperCase().charAt(0);
            switch (selection) {
                case 'N' -> {
                    valid = true;
                    this.getToNorth().explore();
                }
                case 'E' -> {
                    valid = true;
                    this.getToEast().explore();
                }
                case 'S' -> {
                    valid = true;
                    this.getToSouth().explore();
                }
                case 'W' -> {
                    valid = true;
                    this.getToWest().explore();
                }
                case '*' -> {
                    return;
                }
                default -> {
                    System.out.println("Invalid selection! Please try again.");
                }
            }
        }
    }

    @Override
    public Item interact() {
        return null;
    }
}
