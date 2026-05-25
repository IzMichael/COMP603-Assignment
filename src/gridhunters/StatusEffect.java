/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters;

import java.io.Serializable;

/**
 *
 * @author LD Mao
 */
public class StatusEffect implements Serializable {

    enum Effect {
        POISON, BURN
    }

    Effect effect;
    int duration;

    StatusEffect(Effect effect, int duration) {
        this.effect = effect;
        this.duration = duration;
    }

    void activateEffect(Creature creature) {
        switch (effect) {
            case POISON -> {
                System.out.println(creature.getClass().getSimpleName() + " takes 1 poison damage.");
                creature.health -= 1;
            }
            case BURN -> {
                System.out.println(creature.getClass().getSimpleName() + " takes 1 burn damage.");
                creature.health -= 1;
            }
        }
        duration--;
    }

    public int getDuration() {
        return duration;
    }
}
