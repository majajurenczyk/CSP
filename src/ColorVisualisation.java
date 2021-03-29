import geometrics.Point;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.*;

public class ColorVisualisation extends Application {

    private Scene drawBoard(){
        //COLORS
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

        //SOLUTION
        ColoringCSP coloringProblem = new ColoringCSP();
        coloringProblem.initRandomProblem(5, 30, 10, 10);

        HashMap<Variable, Value> assignments = new HashMap<>();
        boolean res = coloringProblem.solveWithBacktracking(assignments);

        System.out.println("WYNIK: " + res);

        Group gr = new Group();
        if(res) {

            //System.out.println(assignments);

            System.out.println(coloringProblem.getVariables());

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
        //Setting title to the scene
        primaryStage.setTitle("Visualisation");

        //Adding the scene to the stage
        primaryStage.setScene(drawBoard());

        //Displaying the contents of a scene
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
