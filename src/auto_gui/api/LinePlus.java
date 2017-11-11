package auto_gui.api;

import javafx.scene.shape.Line;

public class LinePlus extends Line {
    public LinePlus(Line line) {
        super(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        super.setStroke(line.getStroke());
        super.setStrokeWidth(line.getStrokeWidth());
    }

    public double getSlope() {
        return (this.getEndY() - this.getStartY()) / (this.getEndX() - this.getStartX());
    }

    public double getLength() {
        return Math.sqrt(Math.pow(this.getEndX() - this.getStartX(), 2) + Math.pow(this.getEndY() - this.getStartY(), 2));
    }

    public static double getLength(double endX, double startX, double endY, double startY) {
        return Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
    }

    public static double getAngleBetweenTwoLines(LinePlus line1, LinePlus line2) {
        return Math.atan((line1.getSlope() - line2.getSlope()) / (1 + (line1.getSlope() * line2.getSlope())));
    }
}
