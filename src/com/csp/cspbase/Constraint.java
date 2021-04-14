package com.csp.cspbase;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Constraint {
   protected ArrayList<Variable> associatedVariables;

   public abstract boolean testConsistency(HashMap<Variable, Value> assignments);
   public abstract boolean testValuesConsistency(Value [] values);

   public Variable get(int index){
      if(index < 0 || index >= associatedVariables.size()){
         return null;
      }
      else
         return associatedVariables.get(index);
   }

}
