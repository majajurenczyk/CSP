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

    public ArrayList<Constraint> getConstraints(){
        return constraints;
    }

    public ArrayList<Variable> getVariables(){
        return variables;
    }

    public Domain getDomain(){
        return domain;
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
}
