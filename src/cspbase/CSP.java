package cspbase;

import java.lang.reflect.Array;
import java.util.*;

public abstract class CSP {
    protected ArrayList<Variable> variables;
    protected ArrayList<Constraint> constraints;
    protected ArrayList<Domain> domain;

    public int visitedNodes = 0;

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

    //HEURISTICS

    //MRV

    private int chooseNextVariableMRV(HashMap<Variable, Value> assignments, ArrayList<Domain> domain) { //find unassigned Variable with the smallest domain
        ArrayList<Integer> unassignedVariablesIndexes = findUnassignedVarsIndexes(assignments);
        return findSmallestDomainIndex(unassignedVariablesIndexes, domain);
    }

    private ArrayList<Integer> findUnassignedVarsIndexes(HashMap<Variable, Value> assignments) { //finds indexes of unassigned variables
        ArrayList<Integer> indexesOfUnassignedVars = new ArrayList<>();
        for (int variableIndex = 0; variableIndex < variables.size(); variableIndex++) {
            if (!assignments.containsKey(variables.get(variableIndex))) {
                indexesOfUnassignedVars.add(variableIndex);
            }
        }
        return indexesOfUnassignedVars;
    }

    private int findSmallestDomainIndex(ArrayList<Integer> unassignedVariablesIndexes, ArrayList<Domain> domain) {
        int smallestDomainIndex = -1;
        int smallestDomainSize = Integer.MAX_VALUE;

        for (int index : unassignedVariablesIndexes) {
            if (domain.get(index).getDomainValues().size() < smallestDomainSize) {
                smallestDomainIndex = index;
                smallestDomainSize = domain.get(index).getDomainValues().size();
            }
        }
        return smallestDomainIndex;
    }

    //LCV

    @SuppressWarnings("unchecked")
    private void sortDomainLCV(Variable var, Domain domain, HashMap<Variable, Value> assignments) {
        ArrayList<Variable> constraintNeighbours = findConstraintNeighbours(var); //ALL VARIABLES IN CONSTRAINT WITH CHOSEN VARIABLE

        constraintNeighbours.removeAll(assignments.keySet()); //ONLY UNASSIGNED CONSTRAINT NEIGHBORS

        HashMap<Value, Integer> choicesForNeighborsWithValue = new HashMap<>(); //HOW MANY CHOICES IS LEFT FOR NEIGHBORS FOR SPECIFIC VALUE IN DOMAIN
        for (Object val : domain.getDomainValues()) {
            choicesForNeighborsWithValue.put((Value) val, 0);
        }
        for (Value val : choicesForNeighborsWithValue.keySet()) {
            for (Variable variable : constraintNeighbours) {
                assignments.put(variable, val);
                if (!ifConsistentWithAllConstraints(assignments)){
                    choicesForNeighborsWithValue.put(val, choicesForNeighborsWithValue.get(val) + 1);
                }
                assignments.remove(variable);
            }
        }
        ((Domain<Object>)domain).getDomainValues().sort((Object v1, Object v2) -> -1*(choicesForNeighborsWithValue.get((Value)v1).compareTo(choicesForNeighborsWithValue.get((Value)v2))));
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

    //BACKTRACKING - ALL SOLUTIONS

    private int chooseNextVariable(HashMap<Variable, Value> assignments) {
        for (int variableIndex = 0; variableIndex < variables.size(); variableIndex++) {
            if (!assignments.containsKey(variables.get(variableIndex))) {
                return variableIndex;
            }
        }
        return -1;
    }

    private void fixDomainsFC(HashMap<Variable, Value> allAssignments, ArrayList<Domain> domain, Variable assignedVar) {
        ArrayList<Integer> unassignedVariablesIndexes = findUnassignedVarsIndexes(allAssignments);
        for (int unassignedVarIndex : unassignedVariablesIndexes) {
            for (Constraint constraint : constraints) {
                if (constraint.associatedVariables.contains(assignedVar) && constraint.associatedVariables.contains(variables.get(unassignedVarIndex))){
                    ArrayList<Object> domainValues = new ArrayList<Object>(domain.get(unassignedVarIndex).getDomainValues());
                    for (Object val : domainValues) {
                        allAssignments.put(variables.get(unassignedVarIndex), (Value)val);
                        if (constraint.testConsistency(allAssignments)) {
                            domain.get(unassignedVarIndex).getDomainValues().remove((Value)val);
                        }
                        allAssignments.remove(variables.get(unassignedVarIndex));
                    }
                }
            }
        }
    }

    //BACKTRACKING - ONE SOLUTION

    public boolean solveWithBacktracking(HashMap<Variable, Value> assignments,String valHeuristic, String varHeuristic, String algorithm) {
        visitedNodes = 0;
        return solveWithBacktrackingRecursive(assignments, domain, valHeuristic, varHeuristic, algorithm);
    }


    private boolean solveWithBacktrackingRecursive(HashMap<Variable, Value> assignments, ArrayList<Domain> actDomain, String valHeuristic,  String varHeuristic, String algorithm) {
        if (assignments.size() == variables.size()) //if assignments completed
            return true;
        int actVariableIndex = chooseNextVariableByHeuristic(varHeuristic, assignments, actDomain);
        Variable actVariable = variables.get(actVariableIndex);

        Domain actVarDomain = actDomain.get(actVariableIndex);
        sortDomainByHeuristic(actVariable, actVarDomain, valHeuristic, assignments);

        for (Object val : new ArrayList<Object>(actVarDomain.getDomainValues())) {
            assignments.put(actVariable, (Value)val);
            boolean ifConsistentWithAllConstraint = ifConsistentWithAllConstraints(assignments);
            assignments.remove(actVariable);
            if (ifConsistentWithAllConstraint) {
                assignments.put(actVariable, (Value) val);
                visitedNodes++;
                ArrayList<Domain> newActDom = copyDomains(actDomain);
                fixDomainsByAlgorithm(algorithm, assignments, newActDom, actVariable);
                boolean result = solveWithBacktrackingRecursive(assignments, newActDom, valHeuristic, varHeuristic, algorithm);
                if (result) {
                    return true;
                }
                assignments.remove(actVariable);
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

    private ArrayList<Domain> copyDomains(ArrayList<Domain> domain) {
        ArrayList<Domain> copiedDomains = new ArrayList<>();
        for (Domain dom : domain) {
            copiedDomains.add(dom.copyDomain());
        }
        return copiedDomains;
    }

    private int chooseNextVariableByHeuristic(String heuristic, HashMap<Variable, Value> assignments, ArrayList<Domain> domain) {
        if (heuristic.toLowerCase().equals("mrv"))
            return chooseNextVariableMRV(assignments, domain);
        else
            return chooseNextVariable(assignments);
    }

     private void sortDomainByHeuristic(Variable var, Domain domain, String heuristic, HashMap<Variable, Value> assignments){
        if(heuristic.toLowerCase().equals("lcv")){
            sortDomainLCV(var, domain, assignments);
        }
    }

    private void fixDomainsByAlgorithm(String algorithm, HashMap<Variable, Value> assignments, ArrayList<Domain> domain, Variable newVar) {
        if (algorithm.toLowerCase().equals("fc"))
            fixDomainsFC(assignments, domain, newVar);

    }

    //FIND ALL SOLUTIONS

    public void solveWithBacktrackingAll(ArrayList<HashMap<Variable, Value>> allSolutions, String valHeuristic,  String varHeuristic, String algorithm) {
        visitedNodes = 0;
        HashMap<Variable, Value> assignments = new HashMap<>();
        solveWithBacktrackingRecursiveAll(allSolutions, assignments, domain, valHeuristic, varHeuristic, algorithm);
    }


    private void solveWithBacktrackingRecursiveAll(ArrayList<HashMap<Variable, Value>> allSolutions, HashMap<Variable, Value> assignments, ArrayList<Domain> actDomain, String valHeuristic,  String varHeuristic, String algorithm) {
        if (assignments.size() == variables.size()) //if assignments completed
            allSolutions.add(copyAssignments(assignments));
        int actVariableIndex = chooseNextVariableByHeuristic(varHeuristic, assignments, actDomain);

        if (actVariableIndex == -1)
            return;

        Variable actVariable = variables.get(actVariableIndex);

        Domain actVarDomain = actDomain.get(actVariableIndex);
        sortDomainByHeuristic(actVariable, actVarDomain, valHeuristic, assignments);

        for (Object val : new ArrayList<Object>(actVarDomain.getDomainValues())) {
            System.out.println(actDomain);
            assignments.put(actVariable, (Value)val);
            boolean ifConsistentWithAllConstraint = ifConsistentWithAllConstraints(assignments);
            assignments.remove(actVariable);
            visitedNodes++;
            if (ifConsistentWithAllConstraint) {
                assignments.put(actVariable, (Value) val);
                ArrayList<Domain> newActDom = copyDomains(actDomain);
                fixDomainsByAlgorithm(algorithm, assignments, newActDom, actVariable);
                solveWithBacktrackingRecursiveAll(allSolutions, assignments, newActDom, valHeuristic, varHeuristic, algorithm);
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
