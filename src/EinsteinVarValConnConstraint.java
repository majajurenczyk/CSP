import cspbase.Constraint;
import cspbase.Value;
import cspbase.Variable;

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
        return !(assignments.get(this.getVariable())).equals(val);
    }
}
