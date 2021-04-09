import cspbase.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EinsteinCSP extends CSP {
    public EinsteinCSP(ArrayList<Variable> vars, ArrayList<Constraint> cons, ArrayList<Domain> dom) {
        super(vars, cons, dom);
    }

    public EinsteinCSP() {
        super();
    }

    public void initProblem() {

        //VARIABLES - COLORS
        Variable RED = new Variable<String>("CZERWONY");
        Variable GREEN = new Variable<String>("ZIELONY");
        Variable BLUE = new Variable<String>("NIEBIESKI");
        Variable YELLOW = new Variable<String>("ZOLTY");
        Variable WHITE = new Variable<String>("BIALY");

        ArrayList<Variable> variables = new ArrayList<>(Arrays.asList(RED, GREEN, BLUE, YELLOW, WHITE));

        //VARIABLES - NATIONALITIES

        Variable NORWAY = new Variable<String>("NORWEG");
        Variable ENGLAND = new Variable<String>("ANGLIK");
        Variable GERMANY = new Variable<String>("NIEMIEC");
        Variable SWEDEN = new Variable<String>("SZWED");
        Variable DENMARK = new Variable<String>("DUNCZYK");

        variables.addAll(Arrays.asList(NORWAY, ENGLAND, GERMANY, SWEDEN, DENMARK));

        //VARIABLES - TOBACCO PRODUCTS

        Variable LIGHT_CIGARETTES = new Variable<String>("PAPIEROSY LIGHT");
        Variable CIGAR = new Variable<String>("CYGARO");
        Variable PIPE = new Variable<String>("FAJKA");
        Variable UNFILTERED_CIGARETTES = new Variable<String>("PAPIEROSY BEZ FILTRA");
        Variable MENTHOL = new Variable<String>("MENTOLOWE");

        variables.addAll(Arrays.asList(LIGHT_CIGARETTES, CIGAR, PIPE, UNFILTERED_CIGARETTES, MENTHOL));

        //VARIABLES - DRINKS

        Variable TEE = new Variable<String>("HERBATA");
        Variable COFFEE = new Variable<String>("KAWA");
        Variable BEER = new Variable<String>("PIWO");
        Variable MILK = new Variable<String>("MLEKO");
        Variable WATER = new Variable<String>("WODA");

        variables.addAll(Arrays.asList(TEE, COFFEE, BEER, MILK, WATER));

        //VARIABLES - ANIMALS

        Variable FISHES = new Variable<String>("RYBKI");
        Variable DOGS = new Variable<String>("PSY");
        Variable BIRDS = new Variable<String>("PTAKI");
        Variable HORSES = new Variable<String>("KONIE");
        Variable CATS = new Variable<String>("KOTY");

        variables.addAll(Arrays.asList(FISHES, DOGS, BIRDS, HORSES, CATS));

        //INIT VARIABLES

        this.variables = variables;

        //DOMAIN

        ArrayList<Value<Integer>> values = new ArrayList<>();
        values.add(new Value<>(1));
        values.add(new Value<>(2));
        values.add(new Value<>(3));
        values.add(new Value<>(4));
        values.add(new Value<>(5));

        //INIT DOMAIN

        this.domain = new ArrayList<>();
        for(int i = 0; i < variables.size(); i++){
            this.domain.add(new Domain<>(values));
        }

        //CONSTRAINTS

        EinsteinVarValConnConstraint con1 = new EinsteinVarValConnConstraint(NORWAY, new Value<Integer>(1)); //Norweg zamieszkuje pierwszy dom
        EinsteinVarsConnConstraint con2 = new EinsteinVarsConnConstraint(ENGLAND, RED); //Anglik mieszka w czerwonym domu.
        EinsteinLeftNeighbourConstraint con3 = new EinsteinLeftNeighbourConstraint(GREEN, WHITE); //Zielony dom znajduje się bezpośrednio po lewej stronie domu białego.
        EinsteinVarsConnConstraint con4 = new EinsteinVarsConnConstraint(DENMARK, TEE); //Duńczyk pija herbatkę.
        EinsteinNeighbourConstraint con5 = new EinsteinNeighbourConstraint(LIGHT_CIGARETTES, CATS); //Palacz papierosów light mieszka obok hodowcy kotów.
        EinsteinVarsConnConstraint con6 = new EinsteinVarsConnConstraint(YELLOW, CIGAR); //Mieszkaniec żółtego domu pali cygara.
        EinsteinVarsConnConstraint con7 = new EinsteinVarsConnConstraint(GERMANY, PIPE); //Niemiec pali fajkę.
        EinsteinVarValConnConstraint con8 = new EinsteinVarValConnConstraint(MILK, new Value<Integer>(3)); //Mieszkaniec środkowego domu pija mleko.
        EinsteinNeighbourConstraint con9 = new EinsteinNeighbourConstraint(LIGHT_CIGARETTES, WATER); //Palacz papierosów light ma sąsiada, który pija wodę.
        EinsteinVarsConnConstraint con10 = new EinsteinVarsConnConstraint(UNFILTERED_CIGARETTES, BIRDS);
        EinsteinVarsConnConstraint con11 = new EinsteinVarsConnConstraint(SWEDEN, DOGS); //Szwed hoduje psy.
        EinsteinNeighbourConstraint con12 = new EinsteinNeighbourConstraint(NORWAY, BLUE); //Norweg mieszka obok niebieskiego domu.
        EinsteinNeighbourConstraint con13 = new EinsteinNeighbourConstraint(HORSES, YELLOW); //Hodowca koni mieszka obok żółtego domu.
        EinsteinVarsConnConstraint con14 = new EinsteinVarsConnConstraint(MENTHOL, BEER); //Palacz mentolowych pija piwo.
        EinsteinVarsConnConstraint con15 = new EinsteinVarsConnConstraint(GREEN, COFFEE); //W zielonym domu pija się kawę.

        EinsteinDiffConstraint nationalityDiff = new EinsteinDiffConstraint(new ArrayList<Variable>(Arrays.asList(NORWAY, ENGLAND, GERMANY, DENMARK, SWEDEN)));
        EinsteinDiffConstraint drinkDiff = new EinsteinDiffConstraint(new ArrayList<Variable>(Arrays.asList(TEE, COFFEE, BEER, MILK, WATER)));
        EinsteinDiffConstraint animalsDiff = new EinsteinDiffConstraint(new ArrayList<Variable>(Arrays.asList(DOGS, CATS, BIRDS, HORSES, FISHES)));
        EinsteinDiffConstraint tobaccoDiff = new EinsteinDiffConstraint(new ArrayList<Variable>(Arrays.asList(PIPE, CIGAR, UNFILTERED_CIGARETTES, MENTHOL, LIGHT_CIGARETTES)));
        EinsteinDiffConstraint colorDiff = new EinsteinDiffConstraint(new ArrayList<Variable>(Arrays.asList(GREEN, YELLOW, BLUE, WHITE, RED)));

        this.constraints = new ArrayList<>();
        this.constraints.addAll(Arrays.asList(con1, con2, con3, con4, con5, con6, con7, con8, con9, con10, con11, con12, con13, con14, con15, nationalityDiff, drinkDiff, animalsDiff, tobaccoDiff, colorDiff));
    }

    private static void showAssignmentsByValue(HashMap<Variable, Value> assignments) {
        ArrayList<Variable> first = new ArrayList<>();
        ArrayList<Variable> second = new ArrayList<>();
        ArrayList<Variable> third = new ArrayList<>();
        ArrayList<Variable> fourth = new ArrayList<>();
        ArrayList<Variable> fifth = new ArrayList<>();

        for (Map.Entry<Variable, Value> assEntry : assignments.entrySet()) {
            Variable var = (Variable) (((Map.Entry) assEntry).getKey());
            Value val = assignments.get(var);

            if ((int) val.getValue() == 1)
                first.add(var);
            else if ((int) val.getValue() == 2)
                second.add(var);
            else if ((int) val.getValue() == 3)
                third.add(var);
            else if ((int) val.getValue() == 4)
                fourth.add(var);
            else if ((int) val.getValue() == 5)
                fifth.add(var);
        }
        System.out.println("1: ");
        System.out.println(Arrays.toString(first.toArray()));
        System.out.println("2: ");
        System.out.println(Arrays.toString(second.toArray()));
        System.out.println("3: ");
        System.out.println(Arrays.toString(third.toArray()));
        System.out.println("4: ");
        System.out.println(Arrays.toString(fourth.toArray()));
        System.out.println("5: ");
        System.out.println(Arrays.toString(fifth.toArray()));
    }

    public static void main(String[] args) {
        EinsteinCSP problem = new EinsteinCSP();
        problem.initProblem();

        HashMap<Variable, Value> assignments = new HashMap<>();
        boolean res = problem.solveWithBacktracking(assignments);

        if(res){
            showAssignmentsByValue(assignments);
        }
        else{
            System.out.println("ERROR");
        }
    }
}
