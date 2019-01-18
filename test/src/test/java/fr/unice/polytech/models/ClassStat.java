package fr.unice.polytech.models;

public class ClassStat {

     public Class clazz;
     public boolean isComposed;

    public ClassStat(Class clazz, boolean isComposed) {
        this.clazz = clazz;
        this.isComposed = isComposed;
    }

    @Override
    public String toString() {
        return clazz + " : isComposed  = " + isComposed;
    }

    public boolean isComposed() {
        return isComposed;
    }
}
