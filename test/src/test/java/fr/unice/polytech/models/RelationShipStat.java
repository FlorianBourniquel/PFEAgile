package fr.unice.polytech.models;

public class RelationShipStat {

    RelationShip rel;
    int minLevDist;

    public RelationShipStat(RelationShip rel, int minLevDist) {
        this.rel = rel;
        this.minLevDist = minLevDist;
    }

    @Override
    public String toString() {
        return  rel + " :  Minimun Levenshtein Distance = "+ minLevDist;
    }

    public boolean hasMLDof1(){
        return minLevDist == 1;
    }
}
