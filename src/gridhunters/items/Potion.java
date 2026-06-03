/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters.items;

import gridhunters.Player;

/**
 *
 * @author LD Mao
 */
public class Potion extends Item {

    public enum potionType {
        HEALTH(10, "Health Potion");

        int bonus;
        String name;

        potionType(int bonus, String name) {
            this.bonus = bonus;
            this.name = name;
        }
    }

    potionType pot;

    public Potion(potionType pot) {
        this.pot = pot;
        this.name = pot.name;
        this.statBonus = pot.bonus;
    }

    @Override
    public boolean use(Player p) {
        if (this.pot == potionType.HEALTH) {
            p.heal(this.statBonus);
            System.out.println("You drank the " + this.name + " and healed " + this.statBonus + "hp.");
            return true;
        }
        return false;
    }

    @Override
    public boolean isConsumable() {
        return true;
    }
}
