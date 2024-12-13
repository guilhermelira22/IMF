package json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import entities.*;
import entities.MissionImpl;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import graph.GraphMatrix;
import interfaces.Division;
import interfaces.Enemy;
import interfaces.Item;
import orderedUnorderedList.ArrayOrderedList;
import orderedUnorderedList.ArrayUnorderedList;

import java.io.*;

public class MapBuilder extends Import{
    private String file;
    private static String pathExport = "simulations/";
    private static String EXTENSION = ".json";
    private DivisionImpl[] divisionImpls;
    private Enemy[] enemies;
    private String[][] edges;
    private ItemImpl[] items;
    private ArrayUnorderedList<Division> entriesExits;
    private GraphMatrix<Division> building;

    public MapBuilder(String file) throws FileNotFoundException, InvalidFileException, NullException, InvalidTypeException {
        super(file);
        this.file = file;
        this.divisionImpls = importDivisions();
        this.enemies = importEnemies(this.divisionImpls);
        this.items = importItems(this.divisionImpls);
        this.edges = importEdges();
        this.entriesExits = importEntryExits(this.divisionImpls);
        this.building = new GraphMatrix<>();
        createBuilding();
    }

    public MissionImpl createMission() throws InvalidFileException {
        MissionImpl missonJson = importMission(this.divisionImpls);

        if (!isTargetDivisionValid(missonJson)) {
            throw new InvalidFileException("Target is invalid");
        } else if (!isEntriesExitsDivisionValid()) {
            throw new InvalidFileException("Entries exits divisions are invalid");
        } else if (!isDivisionEdgesValid()) {
            throw new InvalidFileException("Edges is invalid");
        }
        MissionImpl mission = new MissionImpl(missonJson.getCod(), missonJson.getVersion(), missonJson.getTarget(), entriesExits, building);

        return mission;
    }

    public MissionImpl readJsonLeaderboard(MissionImpl mission) throws InvalidFileException {
        String path = pathExport + this.file + EXTENSION;
        File f = new File(path);
        if (f.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                JsonElement element = JsonParser.parseReader(bufferedReader);
                element = element.getAsJsonArray();

                JsonArray simulationJson = (JsonArray) element;
                SimulationManualImpl[] simulation = new SimulationManualImpl[simulationJson.size()];

                for (int i = 0; i < simulationJson.size(); i++) {
                    simulation[i] = new Gson().fromJson(simulationJson.get(i), SimulationManualImpl.class);
                    mission.addSimulation(simulation[i]);
                }

            } catch (FileNotFoundException | NullException ex) {
                throw new InvalidFileException(ex.getMessage());
            }
        }
        return mission;

    }

    private void createBuilding(){
        for(int i = 0; i< divisionImpls.length; i++){
            building.addVertex(divisionImpls[i]);
        }
        stablishConnections();
    }

    private void stablishConnections() {
        for (String[] edge : edges) {
            String fromName = edge[0];
            String toName = edge[1];

            DivisionImpl fromDivisionImpl = null;
            DivisionImpl toDivisionImpl = null;

            for (DivisionImpl divisionImpl : divisionImpls) {
                if (divisionImpl.getName().equals(fromName)) {
                    fromDivisionImpl = divisionImpl;
                }
                if (divisionImpl.getName().equals(toName)) {
                    toDivisionImpl = divisionImpl;
                }
                if (fromDivisionImpl != null && toDivisionImpl != null) {
                    break;
                }
            }

            if (fromDivisionImpl != null && toDivisionImpl != null) {
                building.addEdge(fromDivisionImpl, toDivisionImpl);

                fromDivisionImpl.getEdges().addToRear(toDivisionImpl.getName());
                toDivisionImpl.getEdges().addToRear(fromDivisionImpl.getName());
            }
        }
    }


    public boolean isTargetDivisionValid(MissionImpl m) {
        for (int i = 0; i < divisionImpls.length; i++) {
            if (m.getTarget().getDivision().getName().equals(divisionImpls[i].getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isEntriesExitsDivisionValid() {
        int count = 0;

        for (int i = 0; i < divisionImpls.length; i++) {
            if (entriesExits.contains(divisionImpls[i])) {
                count++;
            }
        }

        return (count == entriesExits.size());
    }

    public boolean isDivisionEdgesValid(){
        for (int i = 0; i < edges.length; i++) {
            int find = 0;
            for (int j = 0; j < divisionImpls.length; j++) {
                if ((divisionImpls[j].getName().compareTo(edges[i][0]) == 0) || (divisionImpls[j].getName().compareTo(edges[i][1]) == 0)) {
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
