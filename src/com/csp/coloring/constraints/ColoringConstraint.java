package com.csp.coloring.constraints;

import com.csp.cspbase.Constraint;
import com.csp.cspbase.Value;
import com.csp.cspbase.Variable;
import java.util.ArrayList;
import java.util.HashMap;

public class ColoringConstraint extends Constraint {

    public ColoringConstraint(Variable firstVar, Variable secondVar){
        super.associatedVariables = new ArrayList<>();
        super.associatedVariables.add(firstVar);
        super.associatedVariables.add(secondVar);
    }

    @Override
    public boolean testConsistency(HashMap<Variable, Value> assignments) {
        Value v1 = assignments.get(super.associatedVariables.get(0));
        Value v2 = assignments.get(super.associatedVariables.get(1));

       return testValuesConsistency(new Value[] {v1, v2});
    }

    @Override
    public boolean testValuesConsistency(Value[] values) {
        if(values[0] == null || values[1] == null)
            return false;
        else
            return values[0].equals(values[1]);
    }
}
