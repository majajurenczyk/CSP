import sun.awt.SunHints;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

public abstract class CSP {
    protected ArrayList<Variable> variables;
    protected ArrayList<Constraint> constraints;

    protected Domain domain; //not dynamic

    public CSP(ArrayList<Variable> vars, ArrayList<Constraint> cons, Domain dom) {
        this.variables = vars;
        this.constraints = cons;
        this.domain = dom;
    }

    public CSP(){

    }

    private int chooseNextVariable(HashMap<Variable, Value> assignments){ //return index?
        for (int variableIndex = 0; variableIndex < variables.size(); variableIndex++) {
            if(!assignments.containsKey(variables.get(variableIndex))) {
                return variableIndex;
            }
        }
        return -1;
    }

    public boolean solveWithBacktracking(HashMap<Variable, Value> assignments){
        return solveWithBacktrackingRecursive(assignments);
    }


    private boolean solveWithBacktrackingRecursive(HashMap<Variable, Value> assignments){
        if(assignments.size() == variables.size()) //if assignments completed
            return true;
        int actVariableIndex = chooseNextVariable(assignments);
        Variable actVariable = variables.get(actVariableIndex);

        for (Object val: domain.getDomainValues()) {
            assignments.put(actVariable, (Value)val);
            boolean ifConsistentWithAllConstraint = true;
            for (Constraint c: constraints) {
                if(!c.testConsistency(assignments)){
                    ifConsistentWithAllConstraint = false;
                    break;
                }
            }
            assignments.remove(actVariable);
            if(ifConsistentWithAllConstraint){
                assignments.put(actVariable, (Value)val);
                boolean result = solveWithBacktrackingRecursive(assignments);
                if(result){
                    return true;
                }
                assignments.remove(actVariable);
            }
        }
        return false;
    }


    //=============================================================================================


    /*public boolean solveWithBacktracking(ArrayList<LinkedHashMap<Variable, Value>> allAssignments){
        LinkedHashMap<Variable, Value> assignments = new LinkedHashMap<>();
        return solveWithBacktrackingRecursive(assignments, allAssignments);
    }

    private static LinkedHashMap<Variable, Value> copyAssignment(LinkedHashMap<Variable, Value> assignments){
        LinkedHashMap<Variable, Value> result = new LinkedHashMap<>();
        Iterator it = assignments.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            result.put((Variable)pair.getKey(), (Value)pair.getValue());
            it.remove();
        }
        return result;
    }

    private static Variable getLast(LinkedHashMap<Variable, Value> assignments){
        Variable res = null;
        for (Map.Entry<Variable, Value> variableValueEntry : assignments.entrySet()) {
            res = (Variable) ((Map.Entry) variableValueEntry).getKey();
        }
        return res;
    }

    private boolean solveWithBacktrackingRecursive(LinkedHashMap<Variable, Value> assignments, ArrayList<LinkedHashMap<Variable, Value>> allAssignments){
        if(assignments.size() == variables.size()) { //if assignments completed
            return true;
        }
        int actVariableIndex = chooseNextVariable(assignments);
        Variable actVariable = variables.get(actVariableIndex);

        for (Object val: domain.getDomainValues()) {
            assignments.put(actVariable, (Value)val);
            boolean ifConsistentWithAllConstraint = true;
            for (Constraint c: constraints) {
                if(!c.testConsistency(assignments)){
                    ifConsistentWithAllConstraint = false;
                    break;
                }
            }
            assignments.remove(actVariable);
            if(ifConsistentWithAllConstraint){
                assignments.put(actVariable, (Value)val);
                boolean result = solveWithBacktrackingRecursive(assignments, allAssignments);
                if(result){
                    return true;
                }
                assignments.remove(actVariable);
            }
        }
        return false;
    }*/

    //============================================================================================


}
