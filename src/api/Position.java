package api;

public class Position {
    private double x;
    private double y;

    private double direction; // in degrees

    public Position() {
        x = 0;
        y = 0;
        direction = 0;
    }

    public Position(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDirection(double degrees) {
        this.direction = degrees;
    }

    public double getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "] " + direction + "°";
    }
}
