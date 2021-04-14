import cspbase.Constraint;
import cspbase.Value;
import cspbase.Variable;
import sun.awt.SunHints;

import java.util.ArrayList;
import java.util.HashMap;

public class EinsteinVarValConnConstraint extends Constraint { //CHECK IF VAR HAS SPECIFIC VALUE
    private Value val;

    public EinsteinVarValConnConstraint(Variable firstVar, Value val){
        super.associatedVariables = new ArrayList<>();
        super.associatedVariables.add(firstVar);
        this.val = val;
    }

    private Variable getVariable(){
        return this.associatedVariables.get(0);
    }

    @Override
    public boolean testConsistency(HashMap<Variable, Value> assignments) {
        if(assignments.get(this.getVariable()) == null)
            return false;
        return testValuesConsistency(new Value [] {assignments.get(getVariable()), val});
    }

    @Override
    public boolean testValuesConsistency(Value[] values) {
        return !values[0].equals(values[1]);
    }
}
