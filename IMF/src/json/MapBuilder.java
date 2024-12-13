/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

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
import orderedUnorderedList.ArrayUnorderedList;

import java.io.*;

/**
 * Class that reads a json file and creates a mission with the
 * specifications read from the file.
 */
public class MapBuilder extends Import{
    private String file;
    private final static String pathExport = "simulations/";
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

    /**
     * Reads a json file and creates a mission with the
     * specifications read from the file. If any of the
     * specifications are invalid, throws an InvalidFileException.
     *
     * @return MissionImpl object representing the mission
     * @throws InvalidFileException if any of the specifications are invalid
     */
    public MissionImpl createMission() throws InvalidFileException {
        MissionImpl missonJson = importMission(this.divisionImpls);

        if (!isTargetDivisionValid(missonJson)) {
            throw new InvalidFileException("Target is invalid");
        }  else if (!isDivisionEdgesValid()) {
            throw new InvalidFileException("Edges are invalid");
        } else if (!isItemsValid()){
            throw new InvalidFileException("Items are invalid");
        } else if (!isEnemiesValid()){
            throw new InvalidFileException("Enemies are invalid");
        } else if (!isEntriesExitsDivisionValid()) {
            throw new InvalidFileException("Entries exits divisions are invalid");
        }
        MissionImpl mission = new MissionImpl(missonJson.getCod(), missonJson.getVersion(), missonJson.getTarget(), entriesExits, building);

        return readJsonLeaderboard(mission);
    }

    /**
     * Reads a json file representing a leaderboard of a mission and
     * returns the MissionImpl object with the leaderboard added.
     * If any of the specifications are invalid, throws an InvalidFileException.
     *
     * @param mission the mission to add the leaderboard to
     * @return MissionImpl object representing the mission with the leaderboard
     * @throws InvalidFileException if any of the specifications are invalid
     */
    public MissionImpl readJsonLeaderboard(MissionImpl mission) throws InvalidFileException {
        String path = pathExport + this.file;
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

    /**
     * Creates a graph matrix representing the building's map.
     * The graph matrix is built by adding each division as a vertex
     * and establishing connections between them.
     */
    private void createBuilding(){
        for(int i = 0; i< divisionImpls.length; i++){
            building.addVertex(divisionImpls[i]);
        }
        stablishConnections();
    }


/**
 * Establishes connections between divisions in the building by adding edges
 * to the graph matrix.
 */
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


    /**
     * Checks if the target division is valid.
     *
     * @param m the mission to check
     * @return true if the target division is valid, false otherwise
     */
    public boolean isTargetDivisionValid(MissionImpl m) {
        for (int i = 0; i < divisionImpls.length; i++) {
            if (m.getTarget().getDivision().getName().equals(divisionImpls[i].getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all entries-exits divisions are valid.
     *
     * @return true if all divisions are valid, false otherwise
     */
    public boolean isEntriesExitsDivisionValid() {
        int count = 0;

        for (int i = 0; i < divisionImpls.length; i++) {
            if (entriesExits.contains(divisionImpls[i])) {
                count++;
            }
        }

        return (count == entriesExits.size());
    }

    /**
     * Checks if all division edges are valid.
     *
     * @return true if all division edges are valid, false otherwise
     */
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

    /**
     * Checks if all items have valid divisions.
     *
     * @return true if all items have valid divisions, false otherwise
     */
    public boolean isItemsValid() {
        int count = 0;
        for(Item item: items) {
            for (Division div : divisionImpls) {
                if(div == item.getDivision()){
                    count++;
                }
            }
        }
        return count == items.length;
    }

    /**
     * Checks if all enemies have valid divisions.
     *
     * @return true if all enemies have valid divisions, false otherwise
     */
    public boolean isEnemiesValid() {
        int count = 0;
        for(Enemy enemy: enemies) {
            for (Division div : divisionImpls) {
                if(div == enemy.getDivision()){
                    count++;
                }
            }
        }
        return count == enemies.length;
    }

}
