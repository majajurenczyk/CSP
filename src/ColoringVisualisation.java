import cspbase.Constraint;
import cspbase.Value;
import cspbase.Variable;
import geometrics.Point;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.*;

public class ColoringVisualisation extends Application {

    private Scene drawBoard() {
        //SOLUTION
        ColoringCSP coloringProblem = new ColoringCSP();
        coloringProblem.initRandomProblem(8, 50, 10, 10);

        //COLORS
        Color [] colors = getColors();
        HashMap<Variable, Value> assignments = new HashMap<>();
        boolean res = coloringProblem.solveWithBacktracking(assignments);

        Group gr = new Group();

        if(res) {
            for (Variable var : coloringProblem.getVariables()) {
                Point actPoint = (Point) (var.getRepresentation());
                int varColor = (int) (assignments.get(var).getValue());

                Line point = new Line(actPoint.x * 100 + 20, actPoint.y * 100 + 20, actPoint.x * 100 + 20, actPoint.y * 100 + 20);
                point.setStroke(colors[varColor]);
                point.setStrokeWidth(15);

                gr.getChildren().add(point);
            }

            for (Constraint cons : coloringProblem.getConstraints()) {
                Line line = new Line();
                line.setStartX(((Point) cons.get(0).getRepresentation()).x * 100 + 20);
                line.setStartY(((Point) cons.get(0).getRepresentation()).y * 100 + 20);
                line.setEndX(((Point) cons.get(1).getRepresentation()).x * 100 + 20);
                line.setEndY(((Point) cons.get(1).getRepresentation()).y * 100 + 20);

                line.setStroke(Color.GRAY);
                line.setStrokeWidth(1);

                gr.getChildren().add(line);
            }
        }
        return new Scene(gr, 500, 500);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Visualisation");
        primaryStage.setScene(drawBoard());
        primaryStage.show();
    }

    private Color [] getColors(){
        Color[] colors = new Color[10];
        colors[0] = Color.RED;
        colors[1] = Color.BLUEVIOLET;
        colors[2] = Color.CYAN;
        colors[4] = Color.DARKMAGENTA;
        colors[3] = Color.DEEPPINK;
        colors[5] = Color.DARKOLIVEGREEN;
        colors[6] = Color.DARKBLUE;
        colors[7] = Color.DARKTURQUOISE;
        colors[8] = Color.FUCHSIA;
        colors[9] = Color.DIMGREY;
        return colors;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
