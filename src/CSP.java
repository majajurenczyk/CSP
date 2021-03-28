import sun.awt.SunHints;

import java.util.ArrayList;
import java.util.HashMap;

public class CSP {
    private ArrayList<Variable> variables;
    private ArrayList<Constraint> constraints;

    private Domain domain; //not dynamic

    public CSP(ArrayList<Variable> vars, ArrayList<Constraint> cons, Domain dom) {
        this.variables = vars;
        this.constraints = cons;
        this.domain = dom;
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

    public static void main(String[] args) {
        // VARIABLES
        Variable WA = new Variable("Western Australia");
        Variable NT = new Variable("Northern Territory");
        Variable SA = new Variable("South Australia");
        Variable Q = new Variable("Queensland");
        Variable NSW = new Variable("New South Wales");
        Variable V = new Variable("Victoria");
        Variable T = new Variable("Tasmania");

        ArrayList<Variable> coloringVariables = new ArrayList<>();
        coloringVariables.add(WA);
        coloringVariables.add(NT);
        coloringVariables.add(SA);
        coloringVariables.add(Q);
        coloringVariables.add(NSW);
        coloringVariables.add(V);
        coloringVariables.add(T);

        // VALUES
        Value<String> red = new Value<>("RED");
        Value<String> green = new Value<>("GREEN");
        Value<String> blue = new Value<>("BLUE");

        ArrayList<Value<String>> coloringValues = new ArrayList<>();
        coloringValues.add(red);
        coloringValues.add(green);
        coloringValues.add(blue);

        //DOMAIN
        Domain<String> coloringDomain = new Domain<>(coloringValues);

        //CONSTRAINTS
        Constraint wa_nt = new MapColoringConstraint(WA, NT);
        Constraint wa_sa = new MapColoringConstraint(WA, SA);
        Constraint nt_sa = new MapColoringConstraint(NT, SA);
        Constraint nt_q = new MapColoringConstraint(NT, Q);
        Constraint sa_q = new MapColoringConstraint(SA, Q);
        Constraint sa_nsw = new MapColoringConstraint(SA, NSW);
        Constraint sa_v = new MapColoringConstraint(SA, V);
        Constraint q_nsw = new MapColoringConstraint(Q, NSW);
        Constraint nsw_v = new MapColoringConstraint(NSW, V);

        ArrayList<Constraint> coloringConstraints = new ArrayList<>();
        coloringConstraints.add(wa_nt);
        coloringConstraints.add(wa_sa);
        coloringConstraints.add(nt_sa);
        coloringConstraints.add(nt_q);
        coloringConstraints.add(sa_q);
        coloringConstraints.add(sa_nsw);
        coloringConstraints.add(sa_v);
        coloringConstraints.add(q_nsw);
        coloringConstraints.add(nsw_v);

        //PROBLEM
        CSP coloringProblem = new CSP(coloringVariables, coloringConstraints, coloringDomain);

        HashMap<Variable, Value> assignments = new HashMap<>();
        coloringProblem.solveWithBacktracking(assignments);

        System.out.println(assignments);

    }
}
