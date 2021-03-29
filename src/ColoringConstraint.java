import cspbase.Constraint;
import cspbase.Value;
import cspbase.Variable;
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

        if(v1 == null || v2 == null)
            return false;
        else
            return v1.equals(v2);
    }
}
