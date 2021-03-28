import java.util.ArrayList;
import java.util.HashMap;

public abstract class Constraint {
   protected ArrayList<Variable> associatedVariables;

   abstract boolean testConsistency(HashMap<Variable, Value> assignments);

}
