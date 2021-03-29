import cspbase.Constraint;
import cspbase.Value;
import cspbase.Variable;

import java.util.ArrayList;
import java.util.HashMap;

public class EinsteinVarsConnConstraint extends Constraint { //CHECK IF VALUES FOR BOTH VARIABLES ARE THE SAME

    public EinsteinVarsConnConstraint(Variable firstVar, Variable secondVar) {
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
        return (int) (assignments.get(getFirst()).getValue()) != (int) (assignments.get(getSecond()).getValue());
    }
}
