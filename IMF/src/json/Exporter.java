package json;

import com.google.gson.Gson;
import entities.SimulationManual;
import orderedUnorderedList.ArrayOrderedList;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class Exporter {
    private static final String path = "simulations/";
    private static String EXTENSION = ".json";

    private static void exporter(Object obj, String fileName) {
        FileWriter fileopen = null;
        try {
            Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
            fileopen = new FileWriter(path + fileName + EXTENSION);
            gson.toJson(obj, fileopen);
            fileopen.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void exportToJson(ArrayOrderedList<SimulationManual> manual, String fileName) {
        Iterator it = manual.iterator();
        SimulationManual[] temp = new SimulationManual[manual.size()];
        int i = 0;
        while (it.hasNext()) {
            SimulationManual simulation = (SimulationManual) it.next();
            temp[i] = new SimulationManual(simulation.getLifePoints(), simulation.getPath(), simulation.isGetTarget());
            i++;
        }
        Exporter.exporter(temp, fileName);
    }
}
