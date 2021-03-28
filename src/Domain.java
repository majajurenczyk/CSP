import java.util.ArrayList;

public class Domain <T> {
    private Variable associatedVariable;
    private ArrayList<Value<T>> domainValues;

    public Domain(ArrayList<Value<T>> vals) {
        this.domainValues = vals;
    }

    public boolean isEmpty() {
        return domainValues.size() == 0;
    }

    public ArrayList<Value<T>> getDomainValues() {
        return domainValues;
    }
}
