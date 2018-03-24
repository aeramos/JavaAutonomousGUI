package api;

import java.io.*;
import java.util.ArrayList;

/**
 * A class for holding sequences of coordinates including methods for getting data about these coordinates.
 *
 * Imported from the original Java Autonomous GUI repository
 *
 * @author Alejandro Ramos
 * @see <a href="https://github.com/BxSciborgs/JavaAutonomousGUI">Original Repository</a>
 */
public class AutonomousRoutine implements Serializable {
    private static final long serialVersionUID = 1;
    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();
    private ArrayList<AutonomousAction> autonomousActions = new ArrayList<>();

    public enum AutonomousAction {
        PLACE_CUBE_ON_SWITCH, PLACE_CUBE_ON_SCALE, PICKUP_CUBE
    }

    /**
     * Creates a new, empty AutonomousRoutine.
     */
    public AutonomousRoutine() {
    }

    /**
     * Sets this AutonomousRoutine to the given AutonomousRoutine.
     *
     * If the given AutonomousRoutine is <code>null</code>, this constructor creates an empty AutonomousRoutine.
     *
     * @param autonomousRoutine the AutonomousRoutine to set this AutonomousRoutine to
     */
    public AutonomousRoutine(AutonomousRoutine autonomousRoutine) {
        set(autonomousRoutine);
    }

    /**
     * Creates a new AutonomousRoutine with the given coordinate.
     *
     * @param x the first x coordinate
     * @param y the first y coordinate
     */
    public AutonomousRoutine(double x, double y, AutonomousAction autonomousAction) {
        add(x, y, autonomousAction);
    }

    /**
     * Creates a new AutonomousRoutine with the given coordinate array.
     *
     * @param xy a coordinate array in which xy[0] has the x coordinates and xy[1] has the y coordinates
     */
    public AutonomousRoutine(double[][] xy, AutonomousAction[] autonomousAction) {
        add(xy, autonomousAction);
    }

    /**
     * Creates a new AutonomousRoutine from a AutonomousRoutine file.
     *
     * @param file AutonomousRoutine file
     * @throws IOException if the AutonomousRoutine could not be loaded from the file
     */
    public AutonomousRoutine(File file) throws IOException {
        if (!this.load(file)) {
            throw new IOException();
        }
    }

    /**
     * Returns a {@link String} representation of this AutonomousRoutine in an easily readable way.
     * The String is split into two lines to easily compare the x and y values of each coordinate.
     *
     * @return String representation of this AutonomousRoutine
     */
    @Override
    public String toString() {
        StringBuilder xString = new StringBuilder("Path{x=[");
        StringBuilder yString = new StringBuilder("y=[");
        for (int i = 0; i < this.size(); i++) {
            xString.append(x.get(i));
            yString.append(y.get(i));

            // don't need commas and stuff if this is the last coordinate
            if (i < this.size() - 1) {
                xString.append(", ");
                yString.append(", ");

                int xLength = x.get(i).toString().length();
                int yLength = y.get(i).toString().length();

                // align the string so that each new coordinate starts at the same point for x and y
                if (xLength > yLength) {
                    for (int j = 0; j < xLength - yLength; j++) {
                        yString.append(" ");
                    }
                }
                if (yLength > xLength) {
                    for (int j = 0; j < yLength - xLength; j++) {
                        xString.append(" ");
                    }
                }
            }
        }
        return xString + "],\n" + "     " + yString + "]}";
    }

    /**
     * Adds the given coordinate to this AutonomousRoutine.
     *
     * @param x the x coordinate value
     * @param y the y coordinate value
     */
    public void add(double x, double y, AutonomousAction autonomousAction) {
        this.x.add(x);
        this.y.add(y);
        this.autonomousActions.add(autonomousAction);
    }

    /**
     * Adds the given coordinates to this AutonomousRoutine.
     *
     * @param xy a coordinate array in which xy[0] has the x coordinates and xy[1] has the y coordinates
     */
    public void add(double[][] xy, AutonomousAction[] autonomousAction) {
        for (int i = 0; i < xy.length; i++) {
            this.add(xy[i][0], xy[i][1], autonomousAction[i]);
        }
    }

    /**
     * Gets the coordinate at the given index.
     *
     * If the index is out of the bounds of this AutonomousRoutine, this method returns <code>null</code>.
     *
     * @param index the index of the coordinate to getCoordinate
     * @return a <code>int[]</code> coordinate value in which [0] is the x value and [1] is the y value
     */
    public double[] getCoordinate(int index) {
        if (index < x.size() && index >= 0) {
            return new double[]{x.get(index), y.get(index)};
        } else {
            return null;
        }
    }

    public double[] getLastCoordinate() {
        if (this.size() > 0) {
            return getCoordinate(this.size() - 1);
        } else {
            return null;
        }
    }

    public AutonomousAction getAutonomousAction(int index) {
        if (index < autonomousActions.size() && index >= 0) {
            return autonomousActions.get(index);
        } else {
            return null;
        }
    }

    public boolean hasAutonomousAction(int index) {
        return autonomousActions.get(index) != null;
    }

    public void setAutonomousAction(int index, AutonomousAction action) {
        autonomousActions.set(index, action);
    }

    /**
     * Removes the coordinate at the specified index.
     *
     * If the given index is out of the bounds of the current AutonomousRoutine, this command does nothing.
     *
     * @param index the index of the coordinate to remove
     */
    public void remove(int index) {
        if (index >= 0 && x.size() > 0) {
            x.remove(index);
            y.remove(index);
            autonomousActions.remove(index);
        }
    }

    /**
     * Sets this AutonomousRoutine to the given AutonomousRoutine.
     *
     * If the given AutonomousRoutine is <code>null</code>, this method does nothing.
     *
     * @param autonomousRoutine the AutonomousRoutine to set this AutonomousRoutine to
     */
    public void set(AutonomousRoutine autonomousRoutine) {
        if (autonomousRoutine != null) {
            this.clear();
            for (int i = 0; i < autonomousRoutine.size(); i++) {
                this.add(autonomousRoutine.getCoordinate(i)[0], autonomousRoutine.getCoordinate(i)[1], autonomousRoutine.getAutonomousAction(i));
            }
        }
    }

    /**
     * Saves this AutonomousRoutine to the specified {@link File}.
     *
     * @param file the file to save this AutonomousRoutine to
     * @return if the operation was successful or not
     */
    public boolean save(File file) {
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(this);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets this AutonomousRoutine to the AutonomousRoutine at the specified {@link File}.
     *
     * @param file the AutonomousRoutine file
     * @return if the operation was successful or not
     */
    public boolean load(File file) {
        if (file != null) {
            AutonomousRoutine autonomousRoutine;
            try {
                autonomousRoutine = (AutonomousRoutine)new ObjectInputStream(new FileInputStream(file)).readObject();
                this.set(autonomousRoutine);
            } catch (Exception e) {
                System.out.println("could not cast");
                return false;
            }
            return true;
        } else {
            System.out.println("File is null");
            return false;
        }
    }

    /**
     * Clears this AutonomousRoutine of all its coordinates.
     */
    public void clear() {
        x.clear();
        y.clear();
        autonomousActions.clear();
    }

    /**
     * Gets the size of this AutonomousRoutine.
     *
     * @return the size of this AutonomousRoutine
     */
    public int size() {
        return x.size();
    }

    /**
     * Gets the length of this AutonomousRoutine.
     *
     * @return the length of this AutonomousRoutine
     */
    public double getLength() {
        double length = 0;
        for (int i = 0; i < this.size() - 1; i++) {
            length += getDistance(i, i + 1);
        }
        return length;
    }

    /**
     * Gets the distance from one coordinate to another.
     *
     * @param start the first coordinate
     * @param end   the second coordinate
     * @return the distance between the first and second coordinates
     */
    public double getDistance(int start, int end) {
        return Math.sqrt(Math.pow(x.get(end) - x.get(start), 2) + Math.pow(y.get(end) - y.get(start), 2));
    }

    /**
     * Gets the angle between three coordinates at the point of the second coordinate.
     *
     * @param start     the first coordinate
     * @param connector the second coordinate
     * @param end       the third coordinate
     * @return the angle between the three coordinates at the point of the second coordinate
     */
    public double getAngle(int start, int connector, int end) {
        return Math.atan((getSlope(start, connector) - getSlope(connector, end)) / (1 + (getSlope(start, connector) * getSlope(connector, end))));
    }

    /**
     * Gets the slope of a line constructed from two given coordinates.
     *
     * @param start the first coordinate
     * @param end   the second coordinate
     * @return the slope of a line constructed from two given coordinates
     */
    public double getSlope(int start, int end) {
        return (y.get(end) - y.get(start)) / (x.get(end) - x.get(start));
    }
}