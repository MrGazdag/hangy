package me.mrgazdag.programs.hangy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class HangyMain {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("args empty bitch");
            System.exit(-1);
        }
        HangyWorld world = createRandom(Objects.requireNonNull(getJSONFromFile(new File(args[0]))), 50);
        Display display = new Display(world);
        display.setVisible(true);
    }

    public static HangyWorld createRandom(JSONObject data, int points) {
        JSONObject world = data.getJSONObject("world");
        Random random = new Random();
        List<HangyTarget> targetList = new ArrayList<>();
        double worldWidth = world.getDouble("worldSizeX");
        double worldHeight = world.getDouble("worldSizeY");
        for (int i = 0; i < points; i++) {
            targetList.add(new HangyTarget(worldWidth*random.nextDouble(), worldHeight*random.nextDouble(), null));
        }
        return new HangyWorld(
                targetList,
                world.getDouble("worldSizeX"),
                world.getDouble("worldSizeY"),
                world.getDouble("pheromoneEvaporationRate"),
                world.getDouble("pheromoneWorth"),
                world.getDouble("distanceWorth"),
                world.getInt("antsPerGroup"),
                world.getInt("startNodeIndex")
        );
    }

    public static JSONObject getJSONFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine()).append("\n");
        }
        br.close();
        return new JSONObject(sb.toString());
    }

    public static HangyWorld loadFromJSON(JSONObject json) {
        JSONObject world = json.getJSONObject("world");
        JSONArray targets = json.getJSONArray("targets");
        List<HangyTarget> targetList = new ArrayList<>();
        for (Object o : targets) {
            if (o instanceof JSONObject obj) {
                targetList.add(new HangyTarget(
                        obj.getDouble("x"),
                        obj.getDouble("y"),
                        obj.optString("name", null)
                ));
            }
        }
        return new HangyWorld(
                targetList,
                world.getDouble("worldSizeX"),
                world.getDouble("worldSizeY"),
                world.getDouble("pheromoneEvaporationRate"),
                world.getDouble("pheromoneWorth"),
                world.getDouble("distanceWorth"),
                world.getInt("antsPerGroup"),
                world.getInt("startNodeIndex")
        );
    }
}
