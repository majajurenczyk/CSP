package cspbase;

import java.util.ArrayList;

public class Domain <T> {
    private ArrayList<Value<T>> domainValues;

    public Domain(ArrayList<Value<T>> vals) {
        this.domainValues = vals;
    }

    public ArrayList<Value<T>> getDomainValues() {
        return domainValues;
    }
}
