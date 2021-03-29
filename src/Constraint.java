import java.util.ArrayList;
import java.util.HashMap;

public abstract class Constraint {
   protected ArrayList<Variable> associatedVariables;

   abstract boolean testConsistency(HashMap<Variable, Value> assignments);

   public Variable get(int index){
      if(index < 0 || index >= associatedVariables.size()){
         return null;
      }
      else
         return associatedVariables.get(index);
   }

}
