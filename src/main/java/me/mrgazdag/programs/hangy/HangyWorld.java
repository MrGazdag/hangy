package me.mrgazdag.programs.hangy;

import java.util.ArrayList;
import java.util.List;

public class HangyWorld {
    private final List<HangyTarget> targets;

    public HangyWorld() {
        this.targets = new ArrayList<>();
    }

    public HangyWorld(List<HangyTarget> targets) {
        this.targets = targets;
    }

    public void addPoint(HangyTarget target) {
        targets.add(target);
    }

    public List<HangyTarget> getTargets() {
        return targets;
    }
}
