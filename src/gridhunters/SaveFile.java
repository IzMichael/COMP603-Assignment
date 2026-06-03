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

    public SaveFile(String serialized) throws IOException, ClassNotFoundException {
        String[] parts = serialized.split(";-;");
        this.name = parts[0];
        this.map = (Map) SaveFile.deserialize(parts[1]);
        this.player = (Player) SaveFile.deserialize(parts[2]);
    }

    public SaveFile(String name, Map map, Player player) {
        this.name = name;
        this.map = map;
        this.player = player;
    }

    public SaveFile() {
    }

    @Override
    public String toString() {
        String mapSerial = "", playerSerial = "";
        try {
            mapSerial = SaveFile.serialize(this.map);
            playerSerial = SaveFile.serialize(this.player);
        } catch (IOException ex) {
            System.getLogger(SaveFile.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return this.name + ";-;" + mapSerial + ";-;" + playerSerial;
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

    public void setMap(Map map) {
        this.map = map;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private static Object deserialize(String s) throws IOException,
            ClassNotFoundException {
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
