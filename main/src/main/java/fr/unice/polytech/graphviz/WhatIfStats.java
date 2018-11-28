package fr.unice.polytech.graphviz;

public class WhatIfStats {

    private int beforeBusiness;
    private int beforePoints;

    private int afterBusiness;
    private int afterPoints;

    public WhatIfStats(int beforeBusiness, int beforePoints, int afterBusiness, int afterPoints) {
        this.beforeBusiness = beforeBusiness;
        this.beforePoints = beforePoints;
        this.afterBusiness = afterBusiness;
        this.afterPoints = afterPoints;
    }

    public int getBeforeBusiness() {
        return beforeBusiness;
    }

    public void setBeforeBusiness(int beforeBusiness) {
        this.beforeBusiness = beforeBusiness;
    }

    public int getBeforePoints() {
        return beforePoints;
    }

    public void setBeforePoints(int beforePoints) {
        this.beforePoints = beforePoints;
    }

    public int getAfterBusiness() {
        return afterBusiness;
    }

    public void setAfterBusiness(int afterBusiness) {
        this.afterBusiness = afterBusiness;
    }

    public int getAfterPoints() {
        return afterPoints;
    }

    public void setAfterPoints(int afterPoints) {
        this.afterPoints = afterPoints;
    }
}
