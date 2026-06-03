/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 *
 * @author Michael Martin
 */
public class SaveFile implements Serializable {

    String name;
    Map map;
    Player player;

    public SaveFile(String name, String mapSerial, String playerSerial) throws IOException, ClassNotFoundException {
        this.name = name;
        this.map = (Map) SaveFile.deserialize(mapSerial);
        this.player = (Player) SaveFile.deserialize(playerSerial);
    }

    public SaveFile(String name, Map map, Player player) {
        this.name = name;
        this.map = map;
        this.player = player;
    }

    public SaveFile(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getMap() {
        return this.map;
    }

    public String getSerializedMap() throws IOException {
        return SaveFile.serialize(this.map);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getSerializedPlayer() throws IOException {
        return SaveFile.serialize(this.player);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private static Object deserialize(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        Object o;
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data))) {
            o = ois.readObject();
        }
        return o;
    }

    private static String serialize(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
