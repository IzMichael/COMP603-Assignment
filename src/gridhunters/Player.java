/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters;

import gridhunters.Item.Equipment;
import java.util.ArrayList;
import java.util.Random;
import gridhunters.items.Artefact;
import java.util.Scanner;

/**
 *
 * @author Michael Martin, LD Mao
 */
public class Player extends Creature {

    Game game;

    private String name;
    private boolean alive;
    private final String classType;
    private final int inventorySize = 5;
    public ArrayList<Item> inventory = new ArrayList(inventorySize);
    Item helmet;
    Item chestplate;
    Item leggings;
    Item boots;
    Item hand;
    ArrayList<Artefact> artefacts = new ArrayList<>();

    int meleeStrength = 5;
    int magicStrength = 5;
    int defence = 2;
    int agility = 1;

    int mapRadius = 2;

    public Player(Game game, String classType, double vitality, double strength, double magic, double defence, double agility) {
        this.game = game;
        this.classType = classType;
        this.maxHealth = 30;

        this.maxHealth *= vitality;
        this.meleeStrength *= strength;
        this.magicStrength *= magic;
        this.defence *= defence;
        this.agility *= agility;

        this.health = this.maxHealth;
        this.alive = true;

        for (int i = 0; i < inventorySize; i++) {
            this.inventory.add(null);
        }

        this.inventory.set(0, new gridhunters.items.Potion(gridhunters.items.Potion.potionType.HEALTH));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getHealth() {
        int modifier = 0;
        if (this.chestplate != null) {
            modifier += this.chestplate.statBonus;
        }
        if (this.leggings != null) {
            modifier += this.leggings.statBonus;
        }
        return this.health + modifier;
    }

    public int getMeleeAttack() {
        int modifier = 0;
        if (hand != null && hand.equip == Item.Equipment.SWORD) {
            modifier += this.hand.statBonus;
        }
        return this.meleeStrength + modifier;
    }

    public int getMagicAttack() {
        int modifier = 0;
        if (hand != null && hand.equip == Item.Equipment.WAND) {
            modifier += this.hand.statBonus;
        }
        return this.magicStrength + modifier;
    }

    public int getDefence() {
        return this.defence;
    }

    public int getAgility() {
        int modifier = 0;
        if (this.boots != null) {
            modifier += this.boots.statBonus;
        }
        return this.agility + modifier;
    }

    public int getMapRadius() {
        return this.mapRadius;
    }

    public void setMapRadius(int i) {
        this.mapRadius = i;
    }

    public String getClassType() {
        return this.classType;
    }

    public int attack(int strength, Enemy enemy) {
        Random random = new Random();

        // Sandstorm Totem
        if (hasArtefact(Artefact.Artefacts.SANDSTORM_TOTEM) && random.nextDouble() < 0.05) {
            System.out.println("The Sandstorm totem has protected you! You 0 hp has been taken from you.");
            return 0;
        }

        // Glacial Aegis
        int currentDefence = getDefence();
        if (hasArtefact(Artefact.Artefacts.GLACIAL_AEGIS) & this.getHealthPercentage() <= 0.25) {
            currentDefence += 5;
        }

        int chance = random.nextInt(0, 2);
        int finalDamage = Math.max(0, (strength - currentDefence)) * chance;
        this.health -= finalDamage;

        // Echoing Shard
        if (hasArtefact(Artefact.Artefacts.ECHOING_SHARD) && random.nextDouble() < 0.15) {
            int reflected = (int) Math.ceil((double) finalDamage / 2);
            System.out.println("Echoing Shard reflected " + reflected + " damage!");
            enemy.takeDamage(reflected);
        }

        // Slime Friend
        if (hasArtefact(Artefact.Artefacts.SLIME_FRIEND) && random.nextDouble() < 0.15) {
            int reflected = (int) Math.ceil((double) finalDamage / 2);
            System.out.println("Goopy (your slime friend) attacked and did " + reflected + " damage!");
            enemy.takeDamage(reflected);
        }

        return finalDamage;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void kill() {
        this.alive = false;
        this.health = -1;
    }

    public Item swapHelmet(Item in) {
        Item out = this.helmet;
        this.helmet = in;
        return out;
    }

    public Item swapChestplate(Item in) {
        Item out = this.chestplate;
        this.chestplate = in;
        return out;
    }

    public Item swapLeggings(Item in) {
        Item out = this.leggings;
        this.leggings = in;
        return out;
    }

    public Item swapBoots(Item in) {
        Item out = this.boots;
        this.boots = in;
        return out;
    }

    public Item swapHand(Item in) {
        Item out = this.hand;
        this.hand = in;
        return out;
    }

    public Item swapInventory(int slot, Item in) {
        if (slot < 0 || slot >= 5) {
            return in;
        }
        Item out = this.inventory.get(slot);
        this.inventory.set(slot, in);
        return out;
    }

    public Item swapItem(Item in) {
        if (in == null) {
            return null;
        }
        Item out = null;
        if (in.equip == Equipment.HELMET) {
            out = this.swapHelmet(in);
            System.out.println(in.toString() + " equipped!");
        } else if (in.equip == Equipment.CHESTPLATE) {
            out = this.swapChestplate(in);
            System.out.println(in.toString() + " equipped!");
        } else if (in.equip == Equipment.LEGGINGS) {
            out = this.swapLeggings(in);
            System.out.println(in.toString() + " equipped!");
        } else if (in.equip == Equipment.BOOTS) {
            out = this.swapBoots(in);
            System.out.println(in.toString() + " equipped!");
        } else if (in.equip == Equipment.SWORD || in.equip == Equipment.WAND) {
            out = this.swapHand(in);
            System.out.println(in.toString() + " equipped!");
        } else if (this.inventory.size() <= this.inventorySize) {
            this.addItem(in);
            System.out.println(in.toString() + " picked up!");
        } else {
            System.out.println("Your inventory is full! Please select an item to discard, to make space.");
            for (int i = 0; i < this.inventory.size(); i++) {
                char entry = (char) ('A' + i);
                System.out.println("  [" + entry + "]    " + this.inventory.get(i).toString());
            }
            System.out.println("or, select [Z] to cancel.");
            Scanner scanner = new Scanner(System.in);
            boolean valid = false;
            while (valid == false) {
                char selection = scanner.next().toUpperCase().charAt(0);
                if (selection == 'Z') {
                    return in;
                } else if (selection - 64 > 0 && selection - 64 <= this.inventory.size()) {
                    out = swapInventory(selection, in);
                } else {
                    System.out.println("Invalid input! Please try again.");
                }
            }
        }
        return out;
    }

    public void printPlayerStatus() {
        ArrayList<StringBuilder> lines = this.game.map.getMinimap(this.x, this.y, this.mapRadius);

        lines.get(0).append("     Player Equipment:   ");
        lines.get(1).append("       ");
        if (this.helmet != null) {
            lines.get(1).append(this.helmet.toString());
            lines.get(1).repeat(" ", 18 - this.helmet.toString().length());
        } else {
            lines.get(1).append("                  ");
        }
        lines.get(2).append("       ");
        if (this.chestplate != null) {
            lines.get(2).append(this.chestplate.toString());
            lines.get(2).repeat(" ", 18 - this.chestplate.toString().length());
        } else {
            lines.get(2).append("                  ");
        }
        lines.get(3).append("       ");
        if (this.leggings != null) {
            lines.get(3).append(this.leggings.toString());
            lines.get(3).repeat(" ", 18 - this.leggings.toString().length());
        } else {
            lines.get(3).append("                  ");
        }
        lines.get(4).append("       ");
        if (this.boots != null) {
            lines.get(4).append(this.boots.toString());
            lines.get(4).repeat(" ", 18 - this.boots.toString().length());
        } else {
            lines.get(4).append("                  ");
        }
        lines.get(5).append("       ");
        if (this.hand != null) {
            lines.get(5).append(this.hand.toString());
            lines.get(6).repeat(" ", 18 - this.hand.toString().length());
        } else {
            lines.get(5).append("                  ");
        }

        int playerBars = (int) Math.ceil(10 * this.game.player.getHealthPercentage());
        lines.get(0).append("     Player Health:");
        lines.get(1).append("      [").append("|".repeat(playerBars)).append(" ".repeat(10 - playerBars)).append("]");
        lines.get(2).append("     Player Inventory:");

        for (int i = 0; i < this.inventory.size(); i++) {
            if (this.inventory.get(i) != null) {
                lines.get(i + 3).append("       ");
                lines.get(i + 3).append(this.inventory.get(i).toString());
            } else {
                lines.get(i + 3).append("       ");
            }
        }
        for (StringBuilder line : lines) {
            System.out.println(line.toString());
        }
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void useItem(int slot) {
        if (slot >= 0 && slot < inventorySize) {
            Item item = inventory.get(slot);
            if (item == null) {
                return;
            }

            boolean consumed = item.use(this);

            if (consumed) {
                inventory.set(slot, null);
            } else {
                System.out.println("You can't use this item.");
            }
        }
    }
    
    public boolean addItem(Item item) {
        for (int i = 0; i < inventorySize; i++) {
            if (this.inventory.get(i) == null) {
                this.inventory.set(i, item);
                return true;
            }
        }
        System.out.println("Inventory is full!");
        return true;
    }

    public boolean hasArtefact(Artefact.Artefacts arte) {
        for (Artefact a : artefacts) {
            if (a.getArtefact() == arte) {
                return true;
            }
        }
        return false;
    }

    public void addArtefact(Artefact artefact) {
        this.artefacts.add(artefact);
    }
}
