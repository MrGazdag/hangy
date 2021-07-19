package me.mrgazdag.programs.hangy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HangyTarget {
    private final double xPos;
    private final double yPos;
    private final String name;
    private double currentPheromone;
    private Map<HangyTarget, Double> distanceMap;
    private Set<Hangy> visited;

    public HangyTarget(double xPos, double yPos, String name) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;
        this.currentPheromone = 0;
        this.distanceMap = new HashMap<>();
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

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public double getCurrentPheromone() {
        return currentPheromone;
    }

    public void setCurrentPheromone(float currentPheromone) {
        this.currentPheromone = currentPheromone;
    }

    public void tickCurrentPheromone(HangyWorld world) {
        this.currentPheromone = Math.max(currentPheromone-world.getPheromoneEvaporationRate(), 0);
    }
}
