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

public class HangyMain {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("args empty bitch");
            System.exit(-1);
        }
        HangyWorld world = loadFromJSON(Objects.requireNonNull(getJSONFromFile(new File(args[0]))));
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    public static JSONObject getJSONFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine()).append("\n");
        }
        br.close();
        return new JSONObject(br.toString());
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
