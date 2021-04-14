package com.csp.cspbase;

public class Variable<T> {
    private static int ID = 1;

    private int id;
    private T representation;

    public Variable(T representation){
        this.representation = representation;
        this.id = ID;

        ID++;
    }

    public int getId() {
        return id;
    }

    public T getRepresentation() {
        return representation;
    }

    @Override
    public String toString() {
        return representation.toString();
    }
}
