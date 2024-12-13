/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package json;

import com.google.gson.*;
import entities.SimulationManualImpl;
import interfaces.SimulationManual;
import orderedUnorderedList.ArrayOrderedList;

import java.io.*;
import java.util.Iterator;

/**
 * The Exporter class is responsible for exporting simulation data to a JSON file.
 */
public class Exporter {
    private static final String path = "simulations/";

    /**
     * Exports the given object to a JSON file with the specified file name.
     *
     * @param obj the object to be serialized and exported to a JSON file
     * @param fileName the name of the file (without extension) where the JSON
     *                 representation of the object will be saved
     */
    private static void exporter(Object obj, String fileName) {
        FileWriter fileopen = null;
        try {
            Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
            fileopen = new FileWriter(path + fileName);
            gson.toJson(obj, fileopen);
            fileopen.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Exports a list of manual simulations to a JSON file.
     *
     * @param manual   the list of SimulationManualImpl objects to be exported
     * @param fileName the name of the file (without extension) where the JSON
     *                 representation of the simulations will be saved
     */
    public static void exportToJson(ArrayOrderedList<SimulationManualImpl> manual, String fileName) {

        Iterator it = manual.iterator();
        SimulationManual[] temp = new SimulationManual[manual.size()];
        int i = 0;
        while (it.hasNext()) {
            SimulationManual simulation = (SimulationManual) it.next();
            temp[i] = new SimulationManualImpl(simulation.getLifePoints(), simulation.getPath(), simulation.isGetTarget());
            i++;
        }
        Exporter.exporter(temp, fileName);
    }
}
