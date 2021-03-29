import cspbase.Constraint;
import cspbase.Value;
import cspbase.Variable;

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
        int diff = (int)(assignments.get(getFirst()).getValue()) - (int)(assignments.get(getSecond()).getValue());
        return Math.abs(Math.abs(diff)) != 1;
    }
}
