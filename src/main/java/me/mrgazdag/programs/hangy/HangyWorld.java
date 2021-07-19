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
    private volatile List<HangyTarget> bestRouteSoFar;

    private double lastGenBestDistanceSoFar;
    private volatile List<HangyTarget> lastGenBestRouteSoFar;

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
        bestDistanceSoFar = Double.MAX_VALUE;
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

    public HangyTarget getStartNode() {
        return startNode;
    }

    public List<HangyTarget> getTargets() {
        return targets;
    }

    public void reset() {
        this.generation = 0;
        this.bestDistanceSoFar = Double.MAX_VALUE;
        this.bestRouteSoFar = null;
        this.lastGenBestDistanceSoFar = Double.MAX_VALUE;
        this.lastGenBestRouteSoFar = null;
        for (HangyTarget target : targets) {
            target.reset();
        }
    }

    public void completeGeneration() {
        generation++;
        for (HangyTarget target : targets) {
            target.clearVisits();
            target.tickCurrentPheromone(this);
        }

        lastGenBestDistanceSoFar = Double.MAX_VALUE;
        lastGenBestRouteSoFar = null;
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
            if (dst < lastGenBestDistanceSoFar) {
                lastGenBestDistanceSoFar = dst;
                lastGenBestRouteSoFar = ant.getRoute();
            }
        }
        if (lastGenBestDistanceSoFar < this.bestDistanceSoFar) {
            this.bestDistanceSoFar = lastGenBestDistanceSoFar;
            this.bestRouteSoFar = lastGenBestRouteSoFar;
        }

        StringJoiner sj = new StringJoiner(", ");
        sj.add(this.targets.indexOf(startNode) + "[" + startNode.getName() + "]");
        HangyTarget prev = startNode;
        for (HangyTarget t : bestRouteSoFar) {
            prev.addPheromoneToNextNode(t, 10);
            sj.add(this.targets.indexOf(t) + "[" + t.getName() + "]");
            prev = t;
        }

        //System.out.println("generation " + generation + "'s best: [" + sj + "] at distance " + distanceWorth);
    }

    public int getGeneration() {
        return generation;
    }

    public double getBestDistanceSoFar() {
        return bestDistanceSoFar;
    }

    public List<HangyTarget> getBestRouteSoFar() {
        return bestRouteSoFar;
    }

    public double getLastGenBestDistanceSoFar() {
        return lastGenBestDistanceSoFar;
    }

    public List<HangyTarget> getLastGenBestRouteSoFar() {
        return lastGenBestRouteSoFar;
    }
}
