package me.mrgazdag.programs.hangy;

import java.util.ArrayList;
import java.util.List;

public class Hangy {
    private final HangyWorld world;
    private List<HangyTarget> targets;
    private List<HangyTarget> route;
    private HangyTarget lastNode;
    private double distanceWalked;
    public Hangy(HangyWorld world) {
        this.world = world;
    }

    public void reset(List<HangyTarget> targets, HangyTarget lastNode) {
        this.targets = new ArrayList<>(targets);
        this.route = new ArrayList<>();
        this.lastNode = lastNode;
        this.targets.remove(lastNode);
        this.distanceWalked = 0;
    }

    public double getDistanceWalked() {
        return distanceWalked;
    }

    public void tick() {
        double lx = lastNode.getXPos();
        double ly = lastNode.getYPos();

    }
}
