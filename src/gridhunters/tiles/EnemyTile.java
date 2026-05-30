/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters.tiles;

import java.util.Random;
import java.util.Scanner;
import gridhunters.Enemy;
import gridhunters.Item;
import gridhunters.Game;
import gridhunters.items.Artefact;
import gridhunters.items.Potion;

/**
 *
 * @author Michael Martin, LD Mao
 */
public class EnemyTile extends Tile {

    Enemy enemy;
    boolean isBoss;
    boolean isDefeated = false;

    public EnemyTile(Game game, int x, int y, boolean isBoss) {
        super(game, "Enemy", x, y);
        Random r = new Random();
        int roll = r.nextInt(20);
        if (roll == 0) {
            this.isBoss = true;
        } else {
            this.isBoss = false;
        }
        Random random = new Random();
        int maxHealth, strength;
        if (isBoss == true) {
            maxHealth = random.nextInt(2, 7) * 50;
            strength = random.nextInt(10, 20);
        } else {
            maxHealth = random.nextInt(4, 11) * 12;
            strength = random.nextInt(5, 10);
        }
        this.enemy = new Enemy(maxHealth, strength, this.isBoss);
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
            if (isDefeated == true) {
                System.out.println("(" + this.x + ", " + this.y + ") The remains of an enemy lies here.");
            } else {
                if (isBoss == true) {
                    System.out.println("(" + this.x + ", " + this.y + ") Your instincts scream for you to turn back. A legendary presence is felt nearby...");
                } else {
                    System.out.println("(" + this.x + ", " + this.y + ") You feel an forboding presence in the area...");
                    System.out.println("A great and powerful enemy is near!");
                }
                System.out.println("Do you:");
                System.out.println("  [F]    Fight");
                System.out.println("or, you could flee to the:");
            }
        } else {
            System.out.println("You can now explore to the:");
        }
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
                case 'F' -> {
                    if (isDefeated == true) {
                        System.out.println("The enemy has already been defeated. You attack the remains just to be certain.");
                    } else {
                        valid = true;
                        Item drop = this.interact();
                        if (!this.game.player.isAlive()) {
                            return;
                        }
                        if (drop != null) {
                            if (drop.isConsumable()) {
                                this.game.player.addItem(drop);
                            } else {
                                this.game.player.swapItem(drop);
                            }
                        }
                        this.explore(false);
                    }
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
        System.out.println("");
        System.out.println("You engage the creature in a ferocious battle!");

        while (this.game.player.getHealth() > 0 && this.enemy.getHealth() > 0) {
            if (this.game.player.getHealth() < 1 || this.enemy.getHealth() < 1) {
                break;
            }
            int playerBars = (int) Math.max(0, Math.ceil(10 * this.game.player.getHealthPercentage()));
            int bossBars = (int) Math.max(0, Math.ceil(10 * this.enemy.getHealthPercentage()));
            System.out.println("Your Health: [" + "|".repeat(playerBars) + " ".repeat(10 - playerBars) + "]     " + enemy.getName() + " Health: [" + "|".repeat(bossBars) + " ".repeat(10 - bossBars) + "]");
            System.out.println("You have two attack options. Do you use your:");
            switch (this.game.player.getClassType()) {
                case "Warrior" -> {
                    System.out.println("  [S]      Slash");
                    System.out.println("  [M]      Arcane Slice");
                }
                case "Mage" -> {
                    System.out.println("  [S]      Staff Bonk");
                    System.out.println("  [M]      Magic Bolt");
                }
                case "Tank" -> {
                    System.out.println("  [S]      Shield Bash");
                    System.out.println("  [M]      Mystical Slam");
                }
                case "Rogue" -> {
                    System.out.println("  [S]      Backstab");
                    System.out.println("  [M]      Mana Infused Knives");
                }
                case "Cleric" -> {
                    System.out.println("  [S]      Divine Punch");
                    System.out.println("  [M]      Holy Smite");
                }
            }
            System.out.println("  [I]      Access Items");
            System.out.println("or, do you:");
            System.out.println("  [F]       Flee");
            Scanner scanner = new Scanner(System.in);
            boolean valid = false;
            int damage = 0;
            while (valid == false) {
                char selection = scanner.next().toUpperCase().charAt(0);
                switch (selection) {
                    case 'S' -> {
                        valid = true;
                        damage = this.enemy.attack(this.game.player.getMeleeAttack(), this.game.player);
                    }
                    case 'M' -> {
                        valid = true;
                        damage = this.enemy.attack(this.game.player.getMagicAttack(), this.game.player);
                    }
                    case 'I' -> {
                        if (this.game.player.inventory.isEmpty()) {
                            System.out.println("Your inventory is empty.");
                        } else {
                            System.out.println("Select an item to use or press [Z] to go back:");
                            for (int i = 0; i < this.game.player.inventory.size(); i++) {
                                char entry = (char) ('A' + i);
                                Item slot = this.game.player.inventory.get(i);
                                if (slot != null) {
                                    System.out.println("  [" + entry + "]    " + this.game.player.inventory.get(i).toString());
                                } else {
                                    System.out.println("  [ ]    ");
                                }
                            }
                            boolean itemMenuValid = false;
                            while (itemMenuValid == false) {
                                char selected = scanner.next().toUpperCase().charAt(0);
                                if (selected == 'Z') {
                                    valid = true;
                                    itemMenuValid = true;
                                } else {
                                    int index = selected - 'A';
                                    if (index >= 0 && index < this.game.player.inventory.size()) {
                                        this.game.player.useItem(index);
                                        valid = true;
                                        itemMenuValid = true;
                                    }
                                }
                            }
                        }
                    }
                    case 'F' -> {
                        valid = true;
                        System.out.println("You run away from the " + enemy.getName() + ", like a coward!");
                        int returnedDamage = this.game.player.attack(this.enemy.getAttack(), this.enemy);
                        if (returnedDamage > 0) {
                            System.out.println("Unfortunately, you failed to dodge a hit while running. -" + returnedDamage + "hp from you.");
                        }
                        this.enemy.reset();
                        return null;
                    }
                    default -> {
                        System.out.println("Invalid selection! Please try again.");
                    }
                }
            }

            if (damage > 0) {
                System.out.println("Your attack landed! -" + damage + "hp from the " + enemy.getName() + ".");
                this.enemy.processStatusEffects();
            } else {
                System.out.println("Your attack was ineffective! No damage was dealt to the " + enemy.getName() + ".");
            }

            int returnedDamage = this.game.player.attack(this.enemy.getAttack(), this.enemy);
            if (returnedDamage > 0) {
                System.out.println("Unfortunately, you failed to dodge a hit while enacting your attack. -" + returnedDamage + "hp from you.");
            }

            System.out.println("");
        }

        if (this.game.player.getHealth() < 1) {
            System.out.println("Oh no! You died!");
            this.game.player.kill();
            return null;
        } else if (this.enemy.getHealth() < 1) {
            System.out.println("You won! The " + enemy.getName() + " has been defeated, and you emerge victorious!");
            this.isDefeated = true;
            if (this.isBoss == true) {
                Artefact drop = new Artefact();
                this.game.player.addArtefact(drop);
                if (this.game.player.hasArtefact(Artefact.Artefacts.NATURES_COMPASS) || this.game.player.hasArtefact(Artefact.Artefacts.SONAR_GOGGLES)) {
                    this.game.player.setMapRadius(3);
                }
                System.out.println("It dropped a " + drop.getArtefactName() + ", which you have picked up.");
            } else {
                Item drop;
                Random r = new Random();
                if (r.nextInt(2) == 0) {
                    drop = new Potion(Potion.potionType.HEALTH);
                } else {
                    drop = new Item();
                }
                System.out.println("It dropped a " + drop.toString() + ", which you have picked up.");
                return drop;
            }
        }

        return null;
    }
    
    @Override
    public String getDescription() {
        if (this.isDefeated) {
            return "The enemy has already been defeated. You attack the remains just to be certain.";
        }
        if (isBoss == true) {
            return "(" + this.x + ", " + this.y + ") Your instincts scream for you to turn back. A legendary presence is felt nearby...";
        }
        return "(" + this.x + ", " + this.y + ") You feel an forboding presence in the area... \nA great and powerful enemy is near!";
    }
}
