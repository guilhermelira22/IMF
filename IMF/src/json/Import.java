/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package json;

import com.google.gson.*;
import entities.*;
import enums.Item_Type;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import interfaces.Division;
import interfaces.Enemy;
import orderedUnorderedList.ArrayUnorderedList;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * The Import class is responsible for reading and parsing JSON files
 * related to game maps.
 */
public class Import {
    private final String file;
    private final String path = "maps/";
    private JsonObject map;

    public Import(String file) throws FileNotFoundException{
        this.file = path + file;
        this.map = readFile();
    }


    /**
     * Reads the JSON file specified by the 'file' attribute and parses its content into a JsonObject.
     *
     * @return JsonObject representing the contents of the JSON file.
     * @throws FileNotFoundException if the file is null or cannot be found.
     */
    private JsonObject readFile() throws FileNotFoundException {
        if(file == null) {
            throw new FileNotFoundException("File is null");
        }
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));

            JsonElement element = JsonParser.parseReader(br);
            return element.getAsJsonObject();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found");
        }
    }

    /**
     * Parses the JSON file read previously and extracts the information about the divisions
     * present in the building, returning an array of DivisionImpl objects.
     *
     * @return an array of DivisionImpl objects representing the divisions in the building.
     */
    public DivisionImpl[] importDivisions(){

        JsonArray divisionsArray = map.getAsJsonArray("edificio");
        DivisionImpl[] building = new DivisionImpl[divisionsArray.size()];

        for (int i = 0; i < divisionsArray.size(); i++) {
            String divisionName = divisionsArray.get(i).getAsString();
            building[i] = new DivisionImpl(divisionName);
        }
        return building;
    }

    /**
     * Parses the JSON file read previously and extracts the information about the mission
     * present in the file, returning a MissionImpl object.
     *
     * @param building an array of DivisionImpl objects representing the divisions in the building.
     * @return a MissionImpl object representing the mission.
     * @throws InvalidFileException if the mission in the file is invalid.
     */
    public MissionImpl importMission(DivisionImpl[] building) throws InvalidFileException {
        try {
            String mission_name = map.get("cod-missao").getAsString();
            Integer mission_version = map.get("versao").getAsInt();

            JsonObject targetObject = map.get("alvo").getAsJsonObject();
            String division_name = targetObject.get("divisao").getAsString();
            String target_type = targetObject.get("tipo").getAsString();

            TargetImpl missionTarget = null;
            for (int i = 0; i < building.length; i++) {
                if (building[i].getName().equals(division_name)) {
                    missionTarget = new TargetImpl(building[i], target_type);
                }
            }


            MissionImpl mission = new MissionImpl(mission_name, mission_version, missionTarget);
            if (!mission.isValid()) {
                throw new InvalidFileException("Mission is invalid");
            }
            return mission;
        } catch (Exception e) {
            throw new InvalidFileException("Mission is invalid");
        }
    }


    /**
     * Parses the JSON file read previously and extracts information about the enemies present
     * in the building. Each enemy is associated with a specific division.
     *
     * @param building an array of DivisionImpl objects representing the divisions in the building.
     * @return an array of Enemy objects representing the enemies in the building.
     * @throws InvalidFileException if any enemy is invalid.
     * @throws NullException if any enemy is null.
     * @throws InvalidTypeException if an enemy already exists in the division.
     */
    public Enemy[] importEnemies(DivisionImpl[] building) throws InvalidFileException, NullException, InvalidTypeException {
        JsonArray enemiesArray = map.getAsJsonArray("inimigos");
        EnemyImpl[] enemies = new EnemyImpl[enemiesArray.size()];

        for (int i = 0; i < enemiesArray.size(); i++) {
            JsonObject enemiesObject = enemiesArray.get(i).getAsJsonObject();
            String enemy_name = enemiesObject.get("nome").getAsString();
            Double power = enemiesObject.get("poder").getAsDouble();
            String division_name = enemiesObject.get("divisao").getAsString();;

            for(int j = 0; j < building.length; j++) {
                if(building[j].getName().equals(division_name)) {
                    enemies[i] = new EnemyImpl(enemy_name, power, building[j]);
                    building[j].addEnemy(enemies[i]);

                    if(!enemies[i].isValid()) {
                        throw new InvalidFileException("Enemies are invalid");
                    }
                }
            }
        }
        return enemies;
    }

    /**
     * Parses the JSON file read previously and extracts information about the items present
     * in the building. Each item is associated with a specific division.
     *
     * @param building an array of DivisionImpl objects representing the divisions in the building.
     * @return an array of ItemImpl objects representing the items in the building.
     * @throws InvalidFileException if any item is invalid.
     */
    public ItemImpl[] importItems(DivisionImpl[] building) throws InvalidFileException{
        JsonArray itemsArray = map.getAsJsonArray("itens");

        ItemImpl[] items = new ItemImpl[itemsArray.size()];

        for (int i = 0; i < itemsArray.size(); i++) {
            JsonObject itemObject = itemsArray.get(i).getAsJsonObject();
            String division_name = itemObject.get("divisao").getAsString();
            double amount = 0;
            if (itemObject.has("pontos-recuperados")) {
                amount = itemObject.get("pontos-recuperados").getAsDouble();
            } else if (itemObject.has("pontos-extra")) {
                amount = itemObject.get("pontos-extra").getAsDouble();
            }
            String typeStr = itemObject.get("tipo").getAsString();

            Item_Type type;
            switch (typeStr.toLowerCase()) {
                case "kit de vida":
                    type = Item_Type.KIT;
                    break;
                case "colete":
                    type = Item_Type.VEST;
                    break;
                default:
                    throw new InvalidFileException("Tipo de item desconhecido: " + typeStr);
            }

            for(int j = 0; j < building.length; j++) {
                if(building[j].getName().equals(division_name)) {
                    items[i] = new ItemImpl(amount, type, building[j]);
                    building[j].setItem(items[i]);

                    if(!items[i].isValid()) {
                        throw new InvalidFileException("Items are invalid");
                    }
                }
            }
        }

        return items;
    }


    /**
     * Parses the JSON file read previously and extracts information about the
     * entries and exits of the building. Each entry/exit is associated with a
     * specific division.
     *
     * @param building an array of Division objects representing the divisions
     *                 in the building.
     * @return an array of Division objects representing the entries and exits
     *         of the building.
     */
    public ArrayUnorderedList<Division> importEntryExits(Division[] building){
        JsonArray divisionsArray = map.getAsJsonArray("entradas-saidas");
        ArrayUnorderedList <Division> entriesExits= new ArrayUnorderedList<>();

        for (int i = 0; i < divisionsArray.size(); i++) {
            String division_name = divisionsArray.get(i).getAsString();

            for (Division divisionImpl : building) {
                if (divisionImpl.getName().equals(division_name)) {
                    divisionImpl.setEntryExit();
                    entriesExits.addToRear(divisionImpl);
                }
            }

        }
        return entriesExits;
    }


    /**
     * Parses the JSON file read previously and extracts information about the
     * connections between divisions in the building. Each connection is an
     * array of two strings representing the divisions that are connected.
     *
     * @return an array of pairs of strings representing the connections between
     *         divisions in the building.
     * @throws FileNotFoundException if the "ligacoes" entry doesn't exist in
     *         the JSON file.
     */
    public String[][] importEdges() throws FileNotFoundException {
        if(!map.has("ligacoes")) {
            throw new FileNotFoundException("Edges doesn't exist");
        }

        JsonArray edgesArray = map.getAsJsonArray("ligacoes");
        String[][] edges = new String[edgesArray.size()][2];

        for (int i = 0; i < edgesArray.size(); i++) {
            JsonArray pair = edgesArray.get(i).getAsJsonArray();
            edges[i][0] = pair.get(0).getAsString();
            edges[i][1] = pair.get(1).getAsString();
        }

        return edges;
    }
}
