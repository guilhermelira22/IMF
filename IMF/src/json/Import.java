package json;

import com.google.gson.*;
import entities.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Import {
    private final String file;
    private final String path = "maps/simulation";
    private JsonObject map;
    private Mission mission;
    private Division[] divisions;
    private Item[] items;
    private Enemy[] enemies;
    private Target target;


    public Import(String file) throws FileNotFoundException {
        this.file = path + file;
        this.map = readFile();

    }

    private JsonObject readFile() throws FileNotFoundException {
        if(file == null) {
            throw new FileNotFoundException("file is null");
        }
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));

            JsonElement element = JsonParser.parseReader(br);
            return element.getAsJsonObject();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("file not found");
        }
    }

    public Mission importMission () throws FileNotFoundException{
        JsonObject target = map.get("target").getAsJsonObject();

        try{
            mission = new Gson().fromJson(target, Mission.class);
        } catch (Exception e) {
            throw new FileNotFoundException("file not found");
        }
        return null;
    }

    public Division[] importDivisions() throws FileNotFoundException{
        JsonArray divisionsArray = map.getAsJsonArray("edificio");
        return null;
    }
}
