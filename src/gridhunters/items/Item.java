/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters.items;

/**
 *
 * @author LD Mao
 */
import gridhunters.Player;
import java.io.Serializable;
import java.util.Random;

public class Item implements Serializable {
    public enum Rarity {
        LEATHER(1, "Leather"), COPPER(2, "Copper"), IRON(3, "Iron"), DIAMOND(4, "Diamond");
        public int bonus;
        String name;

        Rarity(int bonus, String name) {
            this.bonus = bonus;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum Equipment {
        HELMET("Helmet"), CHESTPLATE("Chestplate"), LEGGINGS("Leggings"), BOOTS("Boots"), SWORD("Sword"), WAND("Wand");
        String name;

        Equipment(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    protected String name;
    Rarity rarity;
    public Equipment equip;
    public int statBonus;

    public Item() {
        Random r = new Random();

        Equipment[] allEquipment = Equipment.values();
        this.equip = allEquipment[r.nextInt(allEquipment.length)];

        this.rarity = getRandomRarity(r);

        this.statBonus = rarity.bonus;
        this.name = rarity.toString() + " " + equip.toString();
    }

    private Rarity getRandomRarity(Random r) {
        int roll = r.nextInt(100);
        if (roll < 45) {
            return Rarity.LEATHER;
        }
        if (roll < 80) {
            return Rarity.COPPER;
        }
        if (roll < 95) {
            return Rarity.IRON;
        }
        return Rarity.DIAMOND;
    }

    public Item(Rarity rarity, Equipment equip) {
        this.equip = equip;
        this.rarity = rarity;
        this.statBonus = rarity.bonus;
        this.name = rarity.toString() + " " + equip.toString();
    }

    @Override
    public String toString() {
        return this.name;
    }

    public boolean use(Player p) {
        return false;
    }

    public boolean isConsumable() {
        return false;
    }
}