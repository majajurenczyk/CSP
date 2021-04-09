import java.util.*;

import cspbase.*;
import geometrics.Line;
import geometrics.Point;

public class ColoringCSP extends CSP {

    public ColoringCSP(ArrayList<Variable> vars, ArrayList<Constraint> cons, ArrayList<Domain> dom) {
        super(vars, cons, dom);
    }

    public ColoringCSP() {
        super();
    }

    public void initRandomProblem(int numberOfColors, int numberOfRegions, int allRegionsHeight, int allRegionsWidth){
        //INIT DOMAIN 
        ArrayList<Value<Integer>> colors = new ArrayList<>();
        for(int i = 0; i < numberOfColors; i++){
            colors.add(new Value<>(i));
        }
        this.domain = new ArrayList<>();

        
        //INIT VARIABLES
        this.variables = new ArrayList<>();
        ArrayList<Point> drawnPoints = drawRandomProblemPoints(numberOfRegions, allRegionsHeight, allRegionsWidth);
        for (Point p: drawnPoints) {
            this.variables.add(new Variable<>(p));
            this.domain.add(new Domain<>(colors));
        }

        //INIT CONSTRAINTS
        ArrayList<Line> lines = initRandomProblemLines(drawnPoints);
        this.constraints = new ArrayList<>();
        for (Line line: lines) {
            this.constraints.add(new ColoringConstraint(getPointVariable(line.getStart()), getPointVariable(line.getEnd())));
        }
    }
    
    //INITIALIZE PROBLEM METHODS
    private  ArrayList<Line> initRandomProblemLines(ArrayList<Point> drawnPoints){ //CREATING CONNECTIONS TILL STOP CONDITION(NO AVAILABLE MOVES)
        Random rand = new Random();
        ArrayList<Line> lines = new ArrayList<>();
        HashMap<Point, ArrayList<Point>> availableConn = getRandomProblemInitAvailableConnections(drawnPoints);

        while (!randomProblemInitStopCondition(availableConn)){
            int randomIndex = rand.nextInt(drawnPoints.size());
            Point start = drawnPoints.get(randomIndex);
            if(availableConn.get(start).size() != 0){
                Line newLine = new Line(start, availableConn.get(start).get(0));
                updateRandomProblemAvailableConnections(availableConn, newLine);
                lines.add(newLine);
            }
        }
        return lines;
    }

    private HashMap<Point, ArrayList<Point>> getRandomProblemInitAvailableConnections(ArrayList<Point> drawnPoints){ //POINTS AVAILABLE  TO MAKE CONNECTION SORTED BY DISTANCE
        HashMap<Point, ArrayList<Point>> result = new HashMap<>();
        for(Point point: drawnPoints){
            ArrayList<Point> availablePoints = new ArrayList<>();
            for (Point anotherPoint: drawnPoints) {
                if(!point.equals(anotherPoint)){
                    availablePoints.add(new Point(anotherPoint.x, anotherPoint.y));
                }
            }
            availablePoints.sort((p1, p2) -> Double.compare((p1.distance(point)), (p2.distance(point))));
            result.put(point, availablePoints);
        }
        return result;
    }

    private void updateRandomProblemAvailableConnections(HashMap<Point, ArrayList<Point>> availableMoves, Line line){ //DELETE AVAILABLE POINTS TO REACH BASED ON ADDED LINE
        for (Map.Entry<Point, ArrayList<Point>> pointArrayListEntry : availableMoves.entrySet()) {
            Point actPoint = (Point) (((Map.Entry) pointArrayListEntry).getKey());
            ArrayList<Point> actPointAvailableMoves = new ArrayList<>(availableMoves.get(actPoint));
            for (Point p : actPointAvailableMoves) {
                Line checkLine = new Line(actPoint, p);
                if (line.intersects(checkLine) || line.equals(checkLine)) {
                    availableMoves.get(actPoint).remove(p);
                }
            }
        }
    }

    private boolean randomProblemInitStopCondition(HashMap<Point, ArrayList<Point>> availableMoves){ //CHECK IF END OF INITIALIZING
        for (Map.Entry<Point, ArrayList<Point>> pointArrayListEntry : availableMoves.entrySet()) {
            if (availableMoves.get((Point) (((Map.Entry) pointArrayListEntry).getKey())).size() > 0) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<Point> drawRandomProblemPoints(int numberOfPoints, int regionHeight, int regionWidth){ //DRAW POINTS
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

    private Variable getPointVariable(Point p){
        for (Variable var: this.variables) {
            if(var.getRepresentation().equals(p)){
                return var;
            }
        }
        return null;
    }

    public void initExampleProblem() {
        // VARIABLES
        Variable WA = new Variable<String>("Western Australia");
        Variable NT = new Variable<String>("Northern Territory");
        Variable SA = new Variable<String>("South Australia");
        Variable Q = new Variable<String>("Queensland");
        Variable NSW = new Variable<String>("New South Wales");
        Variable V = new Variable<String>("Victoria");
        Variable T = new Variable<String>("Tasmania");

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

        this.domain = new ArrayList<>();
        //DOMAIN
        for(int i = 0; i < getVariables().size(); i++){
            this.domain.add(new Domain<>(coloringValues));
        }


        //CONSTRAINTS
        Constraint wa_nt = new ColoringConstraint(WA, NT);
        Constraint wa_sa = new ColoringConstraint(WA, SA);
        Constraint nt_sa = new ColoringConstraint(NT, SA);
        Constraint nt_q = new ColoringConstraint(NT, Q);
        Constraint sa_q = new ColoringConstraint(SA, Q);
        Constraint sa_nsw = new ColoringConstraint(SA, NSW);
        Constraint sa_v = new ColoringConstraint(SA, V);
        Constraint q_nsw = new ColoringConstraint(Q, NSW);
        Constraint nsw_v = new ColoringConstraint(NSW, V);

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
        coloringProblem.initRandomProblem(3, 5, 5, 5);

        HashMap<Variable, Value> ass = new HashMap<>();
        coloringProblem.solveWithBacktracking(ass);

        /*ArrayList<HashMap<Variable, Value>> allAss = new ArrayList<>();
        coloringProblem.solveWithBacktrackingAll(allAss);

        for (HashMap ass : allAss) {
            System.out.println(ass);
            System.out.println("====");
        }*/

    }
}
