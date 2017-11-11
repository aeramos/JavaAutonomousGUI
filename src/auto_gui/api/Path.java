package auto_gui.api;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;

import java.util.ArrayList;

public class Path extends ArrayList<LinePlus> {
    public void setColor(Color color) {
        for (Line line : this) {
            line.toFront();
            line.setStroke(color);
        }
    }

    public double[][] toCoordArray() {
        double array[][] = new double[this.size() + 1][2];
        for (int i = 0; i < this.size(); i++) {
            array[i][0] = this.get(i).getStartX();
            array[i][1] = this.get(i).getStartY();
        }
        array[this.size()][0] = this.get(this.size() - 1).getEndX();
        array[this.size()][1] = this.get(this.size() - 1).getEndY();
        return array;
    }

    public static Path fromCoordArray(double[][] array) {
        Path path = new Path();
        for (int i = 0; i < array.length - 1; i++) {
            path.add(new LinePlus(LineBuilder.create().stroke(Color.BLACK).strokeWidth(2f).startX(array[i][0]).startY(array[i][1]).endX(array[i + 1][0]).endY(array[i + 1][1]).build()));
        }
        System.out.println(path.size());
        return path;

    }

    public void addToPane(Pane pane) {
        for (Line line : this) {
            pane.getChildren().add(line);
        }
    }

    public void removeFromPane(Pane pane) {
        for (Line line : this) {
            pane.getChildren().remove(line);
        }
    }


    // for testing purposes. adds a red dot on the ends of each line
    public void addPointsToLines(Pane pane) {
        for (Line line : this) {
            pane.getChildren().add(LineBuilder.create().stroke(Color.RED).strokeWidth(5f).startX(line.getStartX()).startY(line.getStartY()).endX(line.getStartX()).endY(line.getStartY()).build());
        }
        pane.getChildren().add(LineBuilder.create().stroke(Color.RED).strokeWidth(5f).startX(this.get(this.size() - 1).getEndX()).startY(this.get(this.size() - 1).getEndY()).endX(this.get(this.size() - 1).getEndX()).endY(this.get(this.size() - 1).getEndX()).build());
    }

    public double getLength() {
        double length = 0;
        for (LinePlus line : this) {
            length += line.getLength();
        }
        return length;
    }
}
