package me.mrgazdag.programs.hangy;

import java.util.List;

public class HangyRoute {
    private final List<HangyTarget> targets;
    private Double length;

    public HangyRoute(List<HangyTarget> targets) {
        this.targets = targets;
        this.length = null;
    }

    public List<HangyTarget> getTargets() {
        return targets;
    }

    public double getLength() {
        if (length == null) {
            double currentDist = 0;
            HangyTarget last = null;
            for (HangyTarget target : targets) {
                if (last == null) {
                    last = target;
                } else {
                    currentDist += Math.sqrt(Math.pow(target.xPos()-last.xPos(), 2) + Math.pow(target.yPos()-last.yPos(), 2));
                }
            }
            this.length = currentDist;
        }
        return length;
    }
}
