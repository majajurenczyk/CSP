package cspbase;

import java.util.*;

public abstract class CSP {
    protected ArrayList<Variable> variables;
    protected ArrayList<Constraint> constraints;
    protected Domain domain;

    //CONSTRUCTORS

    public CSP(ArrayList<Variable> vars, ArrayList<Constraint> cons, Domain dom) {
        this.variables = vars;
        this.constraints = cons;
        this.domain = dom;
    }

    public CSP(){ }

    //GETTERS

    public ArrayList<Constraint> getConstraints(){
        return constraints;
    }

    public ArrayList<Variable> getVariables(){
        return variables;
    }

    //BACKTRACKING - ALL SOLUTIONS

    private int chooseNextVariable(HashMap<Variable, Value> assignments){ //return index?
        for (int variableIndex = 0; variableIndex < variables.size(); variableIndex++) {
            if(!assignments.containsKey(variables.get(variableIndex))) {
                return variableIndex;
            }
        }
        return -1;
    }
    public void solveWithBacktrackingAll(ArrayList<HashMap<Variable, Value>> allSolutions){
        HashMap<Variable, Value> assignments = new HashMap<>();
        solveWithBacktrackingRecursiveAll(allSolutions, assignments);
    }

    private void solveWithBacktrackingRecursiveAll(ArrayList<HashMap<Variable, Value>> allSolutions, HashMap<Variable, Value> assignments){
        if(assignments.size() == variables.size()) //if assignments completed
            allSolutions.add(copyAssignments(assignments));
        int actVariableIndex = chooseNextVariable(assignments);
        if (actVariableIndex == -1)
            return;
        Variable actVariable = variables.get(actVariableIndex);
        for (Object val: domain.getDomainValues()) {
            assignments.put(actVariable, (Value)val);
            boolean ifConsistentWithAllConstraint = true;
            for (Constraint c: constraints) {
                if(c.testConsistency(assignments)){
                    ifConsistentWithAllConstraint = false;
                    break;
                }
            }
            assignments.remove(actVariable);
            if(ifConsistentWithAllConstraint){
                assignments.put(actVariable, (Value)val);
                solveWithBacktrackingRecursiveAll(allSolutions, assignments);
                assignments.remove(actVariable); //backtrack
            }
        }
    }

    //HELPER

    private HashMap<Variable, Value> copyAssignments(HashMap<Variable, Value> assignments){
        HashMap<Variable, Value> result = new HashMap<>();

        for (Map.Entry<Variable, Value> pointArrayListEntry : assignments.entrySet()) {
            Variable var = (Variable) (((Map.Entry) pointArrayListEntry).getKey());
            Value val = assignments.get(var);

            result.put(var, val);
        }
        return result;
    }


    //BACKTRACKING - ONE SOLUTION

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
                if(c.testConsistency(assignments)){
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
