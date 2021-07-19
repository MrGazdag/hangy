package me.mrgazdag.programs.hangy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HangyTarget {
    private final double xPos;
    private final double yPos;
    private final String name;
    private Map<HangyTarget, Double> distanceMap;
    private Map<HangyTarget, Double> pheromoneMap;
    private Set<Hangy> visited;

    public HangyTarget(double xPos, double yPos, String name) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;
        this.distanceMap = new HashMap<>();
        this.pheromoneMap = new HashMap<>();
        this.visited = new HashSet<>();
    }
    public double distanceTo(HangyTarget other) {
        if (!distanceMap.containsKey(other)) {
            double distance = Math.sqrt(Math.pow(other.xPos-xPos, 2) + Math.pow(other.yPos-yPos, 2));
            this.distanceMap.put(other, distance);
            other.distanceMap.put(this,distance);
            return distance;
        } else {
            return distanceMap.get(other);
        }
    }

    public boolean isVisited(Hangy hangy) {
        return visited.contains(hangy);
    }

    public void visit(Hangy hangy) {
        visited.add(hangy);
    }

    public void clearVisits() {
        visited.clear();
    }

    public String getName() {
        return name;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public void addPheromoneToNextNode(HangyTarget target, double pheromoneToAdd) {
        pheromoneMap.put(target, pheromoneMap.getOrDefault(target, 0d) + pheromoneToAdd);
    }

    public double getPheromoneTowards(HangyTarget target) {
        return pheromoneMap.getOrDefault(target, 0d);
    }

    public void tickCurrentPheromone(HangyWorld world) {
        for (Map.Entry<HangyTarget, Double> entry : this.pheromoneMap.entrySet()) {
            entry.setValue(Math.max(entry.getValue()*world.getPheromoneEvaporationRate(), 0));
        }
    }

    public void reset() {
        this.pheromoneMap.clear();
    }
}
