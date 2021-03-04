package com.jakehonea.braedencraft.claims;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jakehonea.braedencraft.utils.Util;
import org.bukkit.util.BoundingBox;


import java.io.*;
import java.util.List;
import java.util.UUID;

public class Claim {

    public static final String OWNER = "owner",
            TRUSTED = "trusted",
            MINIMUM = "minimum",
            MAXIMUM = "maximum",
            WORLD = "world";

    private JsonObject obj;
    private File file;

    public Claim(File file) {

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

    public void set(String path, JsonElement element) {

        obj.add(path, element);
        save();

    }

    public JsonElement get(String path) {

        return obj.get(path);

    }

    public List<UUID> getTrusted() {

        JsonArray array = get(Claim.TRUSTED).getAsJsonArray();

        List<UUID> trusted = Lists.newArrayList();

        for (int i = 0; i < array.size(); i++)
             trusted.add(UUID.fromString(array.remove(0).getAsString()));

        return trusted;

    }

    public void addTrusted(UUID id) {

        JsonArray array = get(Claim.TRUSTED).getAsJsonArray();

        array.add(id.toString());

        set(Claim.TRUSTED, array);

        save();

    }

    public void removeTrusted(UUID id) {

        JsonArray array = new JsonArray();

        getTrusted().stream().filter(uuid -> !uuid.equals(id))
                .forEach(uuid -> array.add(uuid.toString()));

        set(Claim.TRUSTED, array);

    }

    public boolean isTrusted(UUID id) {

        return get(Claim.OWNER).getAsString().equals(id.toString()) || getTrusted().contains(id);

    }

    public BoundingBox getBounds() {

        return BoundingBox.of(Util.fromString(get(Claim.MINIMUM).getAsString()), Util.fromString(get(Claim.MAXIMUM).getAsString()));

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

    public File getFile() {
        return file;
    }

    public JsonObject getJson() {

        return this.obj;

    }

}
