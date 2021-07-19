package me.mrgazdag.programs.hangy;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class HangyWorld {
    private final List<HangyTarget> targets;
    private final double xSize;
    private final double ySize;
    private final double pheromoneEvaporationRate;
    private final double pheromoneWorth;
    private final double distanceWorth;
    private final int antsPerGroup;
    private final HangyTarget startNode;
    private final List<Hangy> ants;

    private double bestDistanceSoFar;
    private List<HangyTarget> bestRouteSoFar;

    private int generation;

    public HangyWorld(List<HangyTarget> targets, double xSize, double ySize, double pheromoneEvaporationRate, double pheromoneWorth, double distanceWorth, int antsPerGroup, int startNodeIndex) {
        this.targets = targets;
        this.xSize = xSize;
        this.ySize = ySize;
        this.pheromoneEvaporationRate = pheromoneEvaporationRate;
        this.pheromoneWorth = pheromoneWorth;
        this.distanceWorth = distanceWorth;
        this.antsPerGroup = antsPerGroup;
        this.startNode = targets.get(startNodeIndex);
        this.ants = new ArrayList<>();
        for (int i = 0; i < antsPerGroup; i++) {
            ants.add(new Hangy(this));
        }
        generation = 0;
    }

    public double getPheromoneWorth() {
        return pheromoneWorth;
    }

    public double getDistanceWorth() {
        return distanceWorth;
    }

    public double getXSize() {
        return xSize;
    }

    public double getYSize() {
        return ySize;
    }

    public double getPheromoneEvaporationRate() {
        return pheromoneEvaporationRate;
    }

    public int getAntsPerGroup() {
        return antsPerGroup;
    }

    public void addPoint(HangyTarget target) {
        targets.add(target);
    }

    public List<HangyTarget> getTargets() {
        return targets;
    }

    public void completeGeneration() {
        generation++;
        for (HangyTarget target : targets) {
            target.clearVisits();
        }

        double bestDistanceSoFar = 0;
        List<HangyTarget> bestRouteSoFar = null;
        for (Hangy ant : ants) {
            ant.reset(targets, startNode);
        }
        for (int i = 0; i < targets.size(); i++) {
            for (Hangy ant : ants) {
                ant.tick();
            }
        }
        for (Hangy ant : ants) {
            double dst = ant.getDistanceWalked();
            if (dst > bestDistanceSoFar) {
                bestDistanceSoFar = dst;
                bestRouteSoFar = ant.getRoute();
            }
        }
        StringJoiner sj = new StringJoiner(", ");
        sj.add(this.targets.indexOf(startNode) + "[" + startNode.getName() + "]");
        for (HangyTarget t : bestRouteSoFar) {
            sj.add(this.targets.indexOf(t) + "[" + t.getName() + "]");
        }

        System.out.println("generation " + generation + "'s best: [" + sj + "]");
    }
}
