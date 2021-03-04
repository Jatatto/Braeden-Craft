package com.jakehonea.braedencraft.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.UUID;

public class PlayerStorage {

    private File file;
    private JsonObject obj;

    public PlayerStorage(File file) {

        this.file = file;

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.obj = new JsonObject();

        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String json = "";

                String ln;
                while ((ln = reader.readLine()) != null)
                    json += ln + " ";

                obj = new JsonParser().parse(json).getAsJsonObject();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void save() {

        try {
            FileWriter writer = new FileWriter(file);

            writer.write(obj.toString());

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JsonObject getJson() {

        return this.obj;

    }

    public void put(String key, JsonElement value) {

        this.obj.add(key, value);
        save();

    }

    public JsonElement get(String key) {

        return this.obj.get(key);

    }

    public void setPlayer(UUID id) {

        this.obj.addProperty("uuid", id.toString());
        save();

    }

    public UUID getPlayer() {

        return UUID.fromString(this.obj.get("uuid").getAsString());

    }

}
