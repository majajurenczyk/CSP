package com.csp.einstein.constraints;

import com.csp.cspbase.Constraint;
import com.csp.cspbase.Value;
import com.csp.cspbase.Variable;

import java.util.ArrayList;
import java.util.HashMap;

public class EinsteinNeighbourConstraint extends Constraint {

    public EinsteinNeighbourConstraint(Variable firstVar, Variable secondVar) { //CHECK IF DIFF BETWEEN FIRST AND SECOND VALUE IS 1 - THEN VARIABLES ARE NEIGHBOURS
        super.associatedVariables = new ArrayList<>();
        super.associatedVariables.add(firstVar);
        super.associatedVariables.add(secondVar);
    }

    private Variable getFirst() {
        return this.associatedVariables.get(0);
    }
    private Variable getSecond() {
        return this.associatedVariables.get(1);
    }

    @Override
    public boolean testConsistency(HashMap<Variable, Value> assignments) {
        if(assignments.get(getFirst()) == null || assignments.get(getSecond()) == null)
            return false;
        return testValuesConsistency(new Value[]{assignments.get(getFirst()), assignments.get(getSecond())});
    }

    @Override
    public boolean testValuesConsistency(Value[] values) {
        int diff = (int)(values[0].getValue()) - (int)(values[1].getValue());
        return Math.abs(Math.abs(diff)) != 1;
    }
}
