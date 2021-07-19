package me.mrgazdag.programs.hangy;

public class HangyTarget {
    private final double xPos;
    private final double yPos;
    private final String name;
    private double currentPheromone;

    public HangyTarget(double xPos, double yPos, String name) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;
        this.currentPheromone = 0;
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
