package json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import entities.Division;
import entities.Enemy;
import entities.Item;
import entities.Mission;
import entities.SimulationManual;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import graph.GraphMatrix;
import graph.NetworkMatrix;
import orderedUnorderedList.ArrayUnorderedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MapBuilder extends Import{
    private String file;
    private static String pathExport = "simulations/";
    private static String EXTENSION = ".json";
    private Division [] divisions;
    private Enemy[] enemies;
    private String[][] edges;
    private Item[] items;
    private ArrayUnorderedList<Division> entriesExits;
    private GraphMatrix<Division> building;

    public MapBuilder(String file) throws FileNotFoundException, InvalidFileException, NullException, InvalidTypeException {
        super(file);
        this.file = file;
        this.divisions = importDivisions();
        this.enemies = importEnemies(this.divisions);
        this.items = importItems();
        this.edges = importEdges();
        this.entriesExits = importEntryExits();
        this.building = new GraphMatrix<>();
        createBuilding();
    }

    public Mission createMission() throws InvalidFileException {
        Mission missonJson = importMission();

        if (!isTargetDivisionValid(missonJson)) {
            throw new InvalidFileException("Target is invalid");
        } else if (!isEntriesExitsDivisionValid()) {
            throw new InvalidFileException("Entries exits divisions are invalid");
        } else if (!isDivisionEdgesValid()) {
            throw new InvalidFileException("Edges is invalid");
        }
        Mission mission = new Mission(missonJson.getCod(), missonJson.getVersion(), missonJson.getTarget(), entriesExits, building);

        return mission;
    }

    public Mission readJsonLeaderboard(Mission mission) throws InvalidFileException {
        String path = pathExport + this.file + EXTENSION;
        File f = new File(path);
        if (f.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                JsonElement element = JsonParser.parseReader(bufferedReader);
                element = element.getAsJsonArray();

                JsonArray simulationJson = (JsonArray) element;
                SimulationManual[] simulation = new SimulationManual[simulationJson.size()];

                for (int i = 0; i < simulationJson.size(); i++) {
                    simulation[i] = new Gson().fromJson(simulationJson.get(i), SimulationManual.class);
                    mission.addSimulation(simulation[i]);
                }

            } catch (FileNotFoundException | NullException ex) {
                throw new InvalidFileException(ex.getMessage());
            }
        }
        return mission;

    }

    private void createBuilding(){
        for(int i=0; i< divisions.length;i++){
            building.addVertex(divisions[i]);
        }
        stablishConnections();
    }

    private void stablishConnections() {
        for (String[] edge : edges) {
            String fromName = edge[0];
            String toName = edge[1];

            Division fromDivision = null;
            Division toDivision = null;

            for (Division division : divisions) {
                if (division.getName().equals(fromName)) {
                    fromDivision = division;
                }
                if (division.getName().equals(toName)) {
                    toDivision = division;
                }
                if (fromDivision != null && toDivision != null) {
                    break;
                }
            }

            if (fromDivision != null && toDivision != null) {
                building.addEdge(fromDivision, toDivision);

                fromDivision.getEdges().addToRear(toDivision.getName());
                toDivision.getEdges().addToRear(fromDivision.getName());
            }
        }
    }


    public boolean isTargetDivisionValid(Mission m) {
        for (int i = 0; i < divisions.length; i++) {
            if (m.getTarget().getDivision().getName().equals(divisions[i].getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isEntriesExitsDivisionValid() {
        int count = 0;

        for (int i = 0; i < divisions.length; i++) {
            if (entriesExits.contains(divisions[i])) {
                count++;
            }
        }
        return (count != entriesExits.size());
    }

    public boolean isDivisionEdgesValid(){
        for (int i = 0; i < edges.length; i++) {
            int find = 0;
            for (int j = 0; j < divisions.length; j++) {
                if ((divisions[j].getName().compareTo(edges[i][0]) == 0) || (divisions[j].getName().compareTo(edges[i][1]) == 0)) {
                    find++;
                }
            }
            if (find != 2) {
                return false;
            }
        }
        return true;
    }


}
