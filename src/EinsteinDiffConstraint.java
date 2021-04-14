import cspbase.Constraint;
import cspbase.Value;
import cspbase.Variable;

import java.util.ArrayList;
import java.util.HashMap;

public class EinsteinDiffConstraint extends Constraint {
    public EinsteinDiffConstraint(ArrayList<Variable> variables){ //CHECK IF THERE IS NO REPETITION IN VALUES AMONG VARIABLES
        this.associatedVariables= new ArrayList<>(variables);
    }
    @Override
    public boolean testConsistency(HashMap<Variable, Value> assignments) {
        ArrayList<Variable> assignedVariables = new ArrayList<>();

        for (Variable var: associatedVariables) { //FETCH ALL ASSIGNED VARIABLES
            if(assignments.get(var) != null){
                assignedVariables.add(var);
            }
        }

        if(assignedVariables.size() == 0) //IF NOTHING IS ASSIGNED, CONSTRAINT SATISFIED
            return false;

        for(int i = 0; i < assignedVariables.size() - 1; i++){ //IF ANY REPEAT IN VALUES AMONG ASSIGNED VARIABLES
            for(int j = i+1; j < assignedVariables.size(); j++){
                if(assignments.get(assignedVariables.get(i)).getValue().equals(assignments.get(assignedVariables.get(j)).getValue())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean testValuesConsistency(Value [] values) {
        return values[0].equals(values[1]);
        /*for(int i = 0; i < values.length - 1; i++){
            for(int j = i+1; j < values.length; j++){
                if(values[i].equals(values[j])){
                    return false;
                }
            }
        }
        return true;*/
    }
}
