/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters;

import gridhunters.items.Item;
import java.util.ArrayList;
import gridhunters.items.Artefact;
import gridhunters.ui.GameGUI;

/**
 *
 * @author Michael Martin, LD Mao
 */
public class Player extends Creature {

    transient Game game;

    private String name;
    private boolean alive;
    private final String classType;
    private final int inventorySize = 5;
    public ArrayList<Item> inventory = new ArrayList(inventorySize);
    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public Item hand;
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
    public int getMaxHealth() {
        int modifier = 0;
        if (this.chestplate != null) {
            modifier += this.chestplate.statBonus;
        }
        if (this.leggings != null) {
            modifier += this.leggings.statBonus;
        }
        return this.maxHealth + modifier;
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

    public Item swapItem(Item newItem) {
        for (int i = 0; i < inventorySize; i++) {
            if (this.inventory.get(i) == null) {
                this.inventory.set(i, newItem);
                return null;
            }
        }
        Item dropped = this.inventory.get(0);
        this.inventory.set(0, newItem);
        return dropped;
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void useItem(int slotIndex, GameGUI gui) {
        Item item = this.inventory.get(slotIndex);
        if (item != null) {
            boolean used = item.use(this, gui); 
            if (used && item.isConsumable()) {
                this.inventory.set(slotIndex, null);
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
        return false;
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
