package cspbase;

import java.util.*;

public abstract class CSP {
    protected ArrayList<Variable> variables;
    protected ArrayList<Constraint> constraints;
    protected ArrayList<Domain> domain;

    //CONSTRUCTORS

    public CSP(ArrayList<Variable> vars, ArrayList<Constraint> cons, ArrayList<Domain> dom) {
        this.variables = vars;
        this.constraints = cons;
        this.domain = dom;
    }

    public CSP() {
    }

    //GETTERS

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    //BACKTRACKING - ALL SOLUTIONS

    private int chooseNextVariable(HashMap<Variable, Value> assignments) { //return index?
        for (int variableIndex = 0; variableIndex < variables.size(); variableIndex++) {
            if (!assignments.containsKey(variables.get(variableIndex))) {
                return variableIndex;
            }
        }
        return -1;
    }

    private HashMap<Integer, Variable> findUnassignedVarsIndexes(HashMap<Variable, Value> assignments) {
        HashMap<Integer, Variable> unassignedVariablesWithIndex = new HashMap<>();
        for (int variableIndex = 0; variableIndex < variables.size(); variableIndex++) {
            if (!assignments.containsKey(variables.get(variableIndex))) {
                unassignedVariablesWithIndex.put(variableIndex, variables.get(variableIndex));
            }
        }
        return unassignedVariablesWithIndex;
    }

    private int chooseNextVariableMRV(HashMap<Variable, Value> assignments) { //find Variable with the smallest domain
        HashMap<Integer, Variable> unassignedVariablesWithIndex = findUnassignedVarsIndexes(assignments);
        return findSmallestDomainIndex(unassignedVariablesWithIndex);
    }

    private void sortDomainLCV(Variable var, Domain domain, HashMap<Variable, Value> assignments) {
        ArrayList<Variable> constraintNeighbours = findConstraintNeighbours(var); //ALL VARIABLES IN CONSTRAINT WITH CHOSEN VARIABLE
        for (Variable variable : assignments.keySet()) {
            constraintNeighbours.remove(variable); //ONLY UNASSIGNED CONSTRAINT NEIGHBORS
        }
        HashMap<Value, Integer> choicesForNeighborsWithValue = new HashMap<>(); //HOW MANY CHOICES IS LEFT FOR NEIGHBORS FOR SPECIFIC VALUE IN DOMAIN
        for (Object val : domain.getDomainValues()) {
            choicesForNeighborsWithValue.put((Value) val, 0);
        }

        for (Value val : choicesForNeighborsWithValue.keySet()) {
            for (Variable variable : constraintNeighbours) {
                assignments.put(variable, val);
                if (ifConsistentWithAllConstraints(assignments)){
                    choicesForNeighborsWithValue.put(val, choicesForNeighborsWithValue.get(val) + 1);
                }
                assignments.remove(var);
            }
        }
        System.out.println(domain.getDomainValues());
        domain.getDomainValues().sort((Object v1, Object v2) -> -1*(choicesForNeighborsWithValue.get((Value)v1).compareTo(choicesForNeighborsWithValue.get((Value)v2))));
        System.out.println(domain.getDomainValues());
    }


    private ArrayList<Variable> findConstraintNeighbours(Variable var) {
        ArrayList<Variable> resultVars = new ArrayList<>();
        for (Constraint con : constraints) {
            if (con.associatedVariables.contains(var)) {
                resultVars.addAll(con.associatedVariables);
                resultVars.remove(var);
            }
        }
        return resultVars;
    }

    private int findSmallestDomainIndex(HashMap<Integer, Variable> unassignedVariablesWithIndex) {
        int smallestDomainIndex = -1;
        int smallestDomainSize = Integer.MAX_VALUE;

        for (int index : unassignedVariablesWithIndex.keySet()) {
            if (domain.get(index).getDomainValues().size() < smallestDomainSize) {
                smallestDomainIndex = index;
                smallestDomainSize = domain.get(index).getDomainValues().size();
            }
        }
        return smallestDomainIndex;
    }


    private ArrayList<Domain> fixDomainsFC(HashMap<Variable, Value> allAssignments, Variable assignedVar, Value assignedVal) {
        HashMap<Integer, Variable> unassignedVariablesWithIndex = findUnassignedVarsIndexes(allAssignments);
        ArrayList<Domain> result = new ArrayList<>();

        for (Domain dom : domain) {
            result.add(dom.copyDomain());
        }

        for (int unassignedVarIndex : unassignedVariablesWithIndex.keySet()) {
            for (Constraint constraint : constraints) {
                if (constraint.associatedVariables.contains(assignedVar) && constraint.associatedVariables.contains(unassignedVariablesWithIndex.get(unassignedVarIndex))) {
                    ArrayList<Object> domainValues = new ArrayList<Object>(result.get(unassignedVarIndex).getDomainValues());
                    for (Object val : domainValues) {
                        if (constraint.testValuesConsistency(new Value[]{(Value) val, assignedVal})) {
                            result.get(unassignedVarIndex).getDomainValues().remove((Value) val);
                        }
                    }
                }
            }
        }
        return result;
    }

    //BACKTRACKING - ONE SOLUTION

    public boolean solveWithBacktracking(HashMap<Variable, Value> assignments,String valHeuristic, String varHeuristic, String algorithm) {
        ArrayList<ArrayList<Domain>> stepDomains = new ArrayList<>();
        stepDomains.add(domain);
        return solveWithBacktrackingRecursive(assignments, stepDomains, valHeuristic, varHeuristic, algorithm);
    }


    private boolean solveWithBacktrackingRecursive(HashMap<Variable, Value> assignments, ArrayList<ArrayList<Domain>> stepDomain, String valHeuristic,  String varHeuristic, String algorithm) {
        if (assignments.size() == variables.size()) //if assignments completed
            return true;
        int actVariableIndex = chooseNextVariableByHeuristic(varHeuristic, assignments);
        Variable actVariable = variables.get(actVariableIndex);

        Domain actDomain = stepDomain.get(stepDomain.size() - 1).get(actVariableIndex);

        sortDomainByHeuristic(actVariable, actDomain, valHeuristic, assignments);

        for (Object val : new ArrayList<Object>(stepDomain.get(stepDomain.size() - 1).get(actVariableIndex).getDomainValues())) {
            assignments.put(actVariable, (Value) val);
            boolean ifConsistentWithAllConstraint = ifConsistentWithAllConstraints(assignments);
            assignments.remove(actVariable);
            if (ifConsistentWithAllConstraint) {
                assignments.put(actVariable, (Value) val);
                stepDomain.add(fixDomainsByAlgorithm(algorithm, assignments, actVariable, (Value) val));
                boolean result = solveWithBacktrackingRecursive(assignments, stepDomain, valHeuristic, varHeuristic, algorithm);
                if (result) {
                    return true;
                }
                assignments.remove(actVariable);
                stepDomain.remove(stepDomain.size() - 1);
            }
        }
        return false;
    }

    private boolean ifConsistentWithAllConstraints(HashMap<Variable, Value> assignments) {
        boolean ifConsistentWithAllConstraint = true;
        for (Constraint c : constraints) {
            if (c.testConsistency(assignments)) {
                ifConsistentWithAllConstraint = false;
                break;
            }
        }
        return ifConsistentWithAllConstraint;
    }

    private ArrayList<Domain> copyDomains() {
        ArrayList<Domain> copiedDomains = new ArrayList<>();
        for (Domain dom : domain) {
            copiedDomains.add(dom.copyDomain());
        }
        return copiedDomains;
    }

    private int chooseNextVariableByHeuristic(String heuristic, HashMap<Variable, Value> assignments) {
        if (heuristic.toLowerCase().equals("mrv"))
            return chooseNextVariableMRV(assignments);
        else
            return chooseNextVariable(assignments);
    }

     private void sortDomainByHeuristic(Variable var, Domain domain, String heuristic, HashMap<Variable, Value> assignments){
        if(heuristic.toLowerCase().equals("lcv")){
            sortDomainLCV(var, domain, assignments);
        }
    }

    private ArrayList<Domain> fixDomainsByAlgorithm(String algorithm, HashMap<Variable, Value> assignments, Variable newVar, Value newVal) {
        if (algorithm.toLowerCase().equals("fc"))
            return fixDomainsFC(assignments, newVar, newVal);
        else
            return copyDomains();
    }

    //FIND ALL SOLUTIONS

    public void solveWithBacktrackingAll(ArrayList<HashMap<Variable, Value>> allSolutions) {
        HashMap<Variable, Value> assignments = new HashMap<>();
        solveWithBacktrackingRecursiveAll(allSolutions, assignments);
    }


    private void solveWithBacktrackingRecursiveAll(ArrayList<HashMap<Variable, Value>> allSolutions, HashMap<Variable, Value> assignments) {
        if (assignments.size() == variables.size()) //if assignments completed
            allSolutions.add(copyAssignments(assignments));
        int actVariableIndex = chooseNextVariable(assignments);
        if (actVariableIndex == -1)
            return;
        Variable actVariable = variables.get(actVariableIndex);
        for (Object val : domain.get(actVariableIndex).getDomainValues()) {
            assignments.put(actVariable, (Value) val);
            boolean ifConsistentWithAllConstraint = true;
            for (Constraint c : constraints) {
                if (c.testConsistency(assignments)) {
                    ifConsistentWithAllConstraint = false;
                    break;
                }
            }
            assignments.remove(actVariable);
            if (ifConsistentWithAllConstraint) {
                assignments.put(actVariable, (Value) val);
                solveWithBacktrackingRecursiveAll(allSolutions, assignments);
                assignments.remove(actVariable); //backtrack
            }
        }
    }

    //HELPER

    private HashMap<Variable, Value> copyAssignments(HashMap<Variable, Value> assignments) {
        HashMap<Variable, Value> result = new HashMap<>();

        for (Map.Entry<Variable, Value> pointArrayListEntry : assignments.entrySet()) {
            Variable var = (Variable) (((Map.Entry) pointArrayListEntry).getKey());
            Value val = assignments.get(var);

            result.put(var, val);
        }
        return result;
    }


}
