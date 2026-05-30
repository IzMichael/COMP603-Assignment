/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters.tiles;

import java.util.ArrayList;
import java.util.Scanner;
import gridhunters.Item;
import gridhunters.Game;

/**
 *
 * @author Michael Martin
 */
public class TreasureTile extends Tile {
    ArrayList<Item> treasureChest = new ArrayList<>();
    public TreasureTile(Game game, int x, int y) {
        super(game, "Treasure", x, y);
        for (int i = 0; i < 5; i++) {
            this.treasureChest.add(new Item());
        }
    }    

    @Override
    public void explore() {
        explore(true);
    }
    
    public void explore(boolean preamble) {
        this.visited = true;
        this.game.player.setPosition(this.x, this.y);
        System.out.println("");
        this.game.player.printPlayerStatus();
        System.out.println("");
        if (preamble != false) {
            System.out.println("(" + this.x + ", " + this.y + ") You come across a treasure chest...");
        }
        System.out.println("Do you:");
        System.out.println("  [R]    Rummage");
        System.out.println("or, you could explore to the:");
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
                case 'R' -> {
                    valid = true;
                    this.interact();
                    this.explore(false);
                }
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
        System.out.println("Within this treasure chest, are:");
        for (int i = 0; i < this.treasureChest.size(); i++) {
            char entry = (char) ('A' + i);
            Item slot = this.treasureChest.get(i);
            if (slot != null) {
                System.out.println("  [" + entry + "]    " + this.treasureChest.get(i).toString());
            } else {
                System.out.println("  [ ]    ");
            }
        }
        System.out.println("Input an option to take that item, or press [Z] to close the chest.");
        Scanner scanner = new Scanner(System.in);
        boolean valid = false;
        while (valid == false) {
            char selection = scanner.next().toUpperCase().charAt(0);
            if (selection == 'Z') {
                valid = true;
            } else if (selection - 64 > 0 && selection - 64 <= this.treasureChest.size()) {
                valid = true;
                this.treasureChest.set(selection - 65, this.game.player.swapItem(this.treasureChest.get(selection - 65)));
            } else {
                System.out.println("Invalid input! Please try again.");
            }
        }
        return null;
    }
    
    @Override
    public String getDescription() {
        return "(" + this.x + ", " + this.y + ") You come across a treasure chest...";
    }
}