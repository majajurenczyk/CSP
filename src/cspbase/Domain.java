package cspbase;

import java.util.ArrayList;
import java.util.Arrays;

public class Domain <T> {
    private ArrayList<Value<T>> domainValues;

    public Domain(ArrayList<Value<T>> vals) {
        this.domainValues = vals;
    }

    public ArrayList<Value<T>> getDomainValues() {
        return domainValues;
    }

    public Domain copyDomain(){
        return new Domain<>(new ArrayList<>(domainValues));
    }

    @Override
    public String toString() {
        return Arrays.toString(domainValues.toArray());
    }
}
