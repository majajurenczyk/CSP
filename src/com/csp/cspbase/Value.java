package com.csp.cspbase;

public class Value <T> {
    private T value;

    public Value(T val){
        this.value = val;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Value)
            return this.value.equals(((Value)obj).value);
        return false;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
