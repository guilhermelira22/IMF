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

public class Exporter {
    private static final String path = "simulations/";

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
