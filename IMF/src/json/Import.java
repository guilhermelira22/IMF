package json;

import com.google.gson.*;
import entities.*;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import orderedUnorderedList.ArrayUnorderedList;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Import {
    private final String file;
    private final String path = "maps/";
    private JsonObject map;
    private Division[] building;
    private ArrayUnorderedList<Division> entriesExits;
    private Item[] items;
    private Enemy[] enemies;
    private String[][] edges;
    private Target target;


    public Import(String file) throws FileNotFoundException, InvalidFileException, NullException, InvalidTypeException {
        this.file = path + file;
        this.map = readFile();
        this.building = importDivisions();
        this.enemies = importEnemies();
        this.items = importItems();
        this.entriesExits = importEntryExits();
        this.edges = importEdges();
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

    public Mission importMission() throws InvalidFileException {
        try {
            Mission mission = new Gson().fromJson(map, Mission.class);
            if (!mission.isValid()) {
                throw new InvalidFileException("mission is invalid");
            }
            return mission;
        } catch (Exception e) {
            throw new InvalidFileException("mission is invalid");
        }
    }

    public Division[] importDivisions() throws FileNotFoundException, InvalidFileException, NullException, InvalidTypeException {

        JsonArray divisionsArray = map.getAsJsonArray("edificio");
        Division[] building = new Division[divisionsArray.size()];

        for (int i = 0; i < divisionsArray.size(); i++) {
            String divisionName = divisionsArray.get(i).getAsString();
            building[i] = new Division(divisionName);
        }
        setEntryExits(building, importEntryExits());
        setItems(building, importItems());
        setEnemies(building, importEnemies());
        return building;
    }

    public Enemy[] importEnemies() throws InvalidFileException{
        JsonArray enemiesArray = map.getAsJsonArray("inimigos");
        Enemy[] enemies = new Enemy[enemiesArray.size()];

        for (int i = 0; i < enemiesArray.size(); i++) {
            enemies[i] = new Gson().fromJson(enemiesArray.get(i), Enemy.class);
        }

        if(!Enemy.isValid(enemies)) {
            throw new InvalidFileException("Enemies are invalid");
        }

        return enemies;
    }

    protected void setEnemies (Division[] building, Enemy[] enemies) throws NullException, InvalidTypeException {
        for(int i = 0; i < building.length; i++) {
            for(int j = 0; j < enemies.length; j++) {
                if(building[i].getName().equals(enemies[j].getDivision())) {
                    building[i].addEnemy(enemies[j]);
                }
            }
        }
    }

    public Item[] importItems() throws InvalidFileException{
        JsonArray itemsArray = map.getAsJsonArray("itens");

        Item[] items = new Item[itemsArray.size()];

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

            Division division = new Division(division_name);
            items[i] = new Item(amount, type, division);
            if(!items[i].isValid()) {
                throw new InvalidFileException("Items are invalid");
            }
        }

        return items;
    }

    protected void setItems (Division[] building, Item[] items){
        for(int i = 0; i < building.length; i++) {
            for(int j = 0; j < items.length; j++) {
                if(building[i].getName().equals(items[j].getDivision())) {
                    building[i].setItem(items[j]);
                }
            }
        }
    }

    public ArrayUnorderedList<Division> importEntryExits() throws InvalidFileException{
        JsonArray divisionsArray = map.getAsJsonArray("entradas-saidas");
        ArrayUnorderedList <Division> entriesExits= new ArrayUnorderedList<>();


        for (int i = 0; i < divisionsArray.size(); i++) {
            Division div = new Division(divisionsArray.get(i).getAsString());
            entriesExits.addToRear(div);

        }

        return entriesExits;
    }

    protected void setEntryExits (Division[] building, ArrayUnorderedList<Division> exits){
        for(int i=0;i<building.length;i++){
            if(exits.contains(building[i])){
                building[i].setEntryExit();
            }
        }
    }

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
