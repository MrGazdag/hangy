package me.mrgazdag.programs.hangy;

import java.util.ArrayList;
import java.util.List;

public class HangyWorld {
    private final List<HangyTargetInstance> targets;
    private final double xSize;
    private final double ySize;
    private final double pheromoneEvaporationRate;
    private final int antsPerGroup;
    private final List<Hangy> ants;

    public HangyWorld(List<HangyTargetInstance> targets, double xSize, double ySize, double pheromoneEvaporationRate, int antsPerGroup) {
        this.targets = targets;
        this.xSize = xSize;
        this.ySize = ySize;
        this.pheromoneEvaporationRate = pheromoneEvaporationRate;
        this.antsPerGroup = antsPerGroup;
        this.ants = new ArrayList<>();
        for (int i = 0; i < antsPerGroup; i++) {
            ants.add(new Hangy());
        }
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

    public void addPoint(HangyTargetInstance target) {
        targets.add(target);
    }

    public List<HangyTargetInstance> getTargets() {
        return targets;
    }

    public void tick() {

    }
}
