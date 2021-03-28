import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import geometrics.Line;
import geometrics.Point;

public class ColoringCSP extends CSP {
    public ColoringCSP(ArrayList<Variable> vars, ArrayList<Constraint> cons, Domain dom) {
        super(vars, cons, dom);
    }

    public ColoringCSP() {
        super();
    }

    public void initRandomProblem(int numberOfReg, int allRegHeight, int allRegWidth) {
        ArrayList<Point> points = drawPoints(numberOfReg, allRegHeight, allRegWidth);

    }

    private ArrayList<Point> drawPoints(int numberOfPoints, int regionHeight, int regionWidth){
        Random rand = new Random();
        ArrayList<Point> result = new ArrayList<>();
        while (result.size() != numberOfPoints) {
            Point drawnPoint = new Point(rand.nextInt(regionWidth), rand.nextInt(regionHeight));
            if(!result.contains(drawnPoint)){
                result.add(drawnPoint);
            }
        }
        return result;
    }

    public void initExampleProblem() {
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

        this.variables = coloringVariables;

        // VALUES
        Value<String> red = new Value<>("RED");
        Value<String> green = new Value<>("GREEN");
        Value<String> blue = new Value<>("BLUE");

        ArrayList<Value<String>> coloringValues = new ArrayList<>();
        coloringValues.add(red);
        coloringValues.add(green);
        coloringValues.add(blue);

        //DOMAIN
        this.domain = new Domain<>(coloringValues);

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

        this.constraints = coloringConstraints;

    }


    public static void main(String[] args) {
        //PROBLEM
        ColoringCSP coloringProblem = new ColoringCSP();
        coloringProblem.initExampleProblem();

        HashMap<Variable, Value> assignments = new HashMap<>();
        //ArrayList<LinkedHashMap<Variable, Value>> allAssignments = new ArrayList<>();
        coloringProblem.solveWithBacktracking(assignments);

        System.out.println(assignments);

    }
}
