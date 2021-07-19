package me.mrgazdag.programs.hangy;

import java.util.ArrayList;
import java.util.List;

public class Hangy {
    private final HangyWorld world;
    private List<HangyTarget> targets;
    private List<HangyTarget> route;
    private HangyTarget lastNode;
    private HangyTarget originalLastNode;
    private double distanceWalked;
    public Hangy(HangyWorld world) {
        this.world = world;
    }

    public void reset(List<HangyTarget> targets, HangyTarget lastNode) {
        this.targets = new ArrayList<>(targets);
        this.route = new ArrayList<>();
        this.lastNode = lastNode;
        this.originalLastNode = lastNode;
        this.targets.remove(lastNode);
        lastNode.visit(this);
        this.distanceWalked = 0;
    }
    public List<HangyTarget> getRoute() {
        return route;
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public void tick() {
        double bestDesirability = 0;
        HangyTarget bestTarget = null;
        for (HangyTarget target : targets) {
            if (target.isVisited(this)) continue;
            double desirability = Math.random() * Math.max(Math.pow(target.getCurrentPheromone(), world.getPheromoneWorth()), 1) * Math.pow(1/lastNode.distanceTo(target), world.getDistanceWorth());
            if (desirability > bestDesirability) {
                bestTarget = target;
                bestDesirability = desirability;
            }
        }
        if (bestTarget == null) {
            //completed all nodes
            return;
        }
        distanceWalked += lastNode.distanceTo(bestTarget);
        lastNode = bestTarget;
        bestTarget.visit(this);
        route.add(bestTarget);
    }
}
