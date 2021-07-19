package me.mrgazdag.programs.hangy;

import java.util.ArrayList;
import java.util.List;

public class Hangy {
    private final HangyWorld world;
    private List<HangyTarget> targets;
    private List<HangyTarget> route;
    private HangyTarget lastNode;
    private HangyTarget originalStartNode;
    private double distanceWalked;
    public Hangy(HangyWorld world) {
        this.world = world;
    }

    public void reset(List<HangyTarget> targets, HangyTarget startNode) {
        this.targets = new ArrayList<>(targets);
        this.route = new ArrayList<>();
        this.lastNode = startNode;
        this.originalStartNode = startNode;
        this.targets.remove(startNode);
        if (startNode != null) startNode.visit(this);
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
            double pheromone = Math.pow(lastNode.getPheromoneTowards(target), world.getPheromoneWorth());
            double desirability = Math.random() * Math.max(pheromone, 1) * Math.pow(1/lastNode.distanceTo(target), world.getDistanceWorth());
            if (desirability > bestDesirability) {
                bestTarget = target;
                bestDesirability = desirability;
            }
        }
        if (bestTarget == null) {
            //completed all nodes
            return;
        }
        System.out.println("pheromone: " + lastNode.getPheromoneTowards(bestTarget));
        distanceWalked += lastNode.distanceTo(bestTarget);
        lastNode = bestTarget;
        bestTarget.visit(this);
        route.add(bestTarget);
    }

    public void tickLast() {
        distanceWalked += lastNode.distanceTo(originalStartNode);
        lastNode = originalStartNode;
        route.add(originalStartNode);
    }
}
