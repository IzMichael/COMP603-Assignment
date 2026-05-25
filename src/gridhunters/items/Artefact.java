/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters.items;

import gridhunters.Player;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author LD Mao
 */
public class Artefact implements Serializable {

    public enum Artefacts {
        NATURES_COMPASS("Nature's Compass"),
        ECHOING_SHARD("Echoing Shard"),
        VENOMOUS_FANG("Venomous Fang"),
        SANDSTORM_TOTEM("Sandstorm Totem"),
        FROZEN_HEART("Frozen Heart"),
        SPIRIT_STAFF("Spirit Staff"),
        SONAR_GOGGLES("Sonar Goggles"),
        SLIME_FRIEND("Slime Friend"),
        SUN_PENDANT("Sun Pendant"),
        GLACIAL_AEGIS("Glacial Aegis");
        String name;
        
        Artefacts(String name) {
            this.name = name;
        }
    }

    Artefacts arte;
    
    public Artefact() {
        Random r = new Random();
        Artefacts[] allArtefacts = Artefacts.values();
        this.arte = allArtefacts[r.nextInt(allArtefacts.length)];
    }

    public Artefacts getArtefact() {
        return arte;
    }

    public void applyMinimapPassive(Player player) {
        if (this.arte == Artefacts.NATURES_COMPASS || this.arte == Artefacts.SONAR_GOGGLES) {
            player.setMapRadius(3);
        }
    }
    
    public String getArtefactName() {
        return arte.name;
    }
}