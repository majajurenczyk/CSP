public class Variable {
    private static int ID = 1;

    private int id;
    private String name;

    public Variable(String name){
        this.name = name;
        this.id = ID;

        ID++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
