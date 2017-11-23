package auto_gui.api;

import java.io.*;
import java.util.ArrayList;

/**
 * A class for holding sequences of coordinates including methods for getting data about these coordinates.
 *
 * @author Alejandro Ramos
 */
public class Path implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();

    /**
     * Creates a new, empty path.
     */
    public Path() {
    }

    /**
     * Sets this Path to the given Path.
     *
     * <p>
     * If the given Path is <code>null</code>, this constructor creates an empty Path.
     *
     * @param path the Path to set this Path to
     */
    public Path(Path path) {
        set(path);
    }

    /**
     * Creates a new Path with the given coordinate.
     *
     * @param x the first x coordinate
     * @param y the first y coordinate
     */
    public Path(double x, double y) {
        add(x, y);
    }

    /**
     * Creates a new Path with the given coordinate array.
     *
     * @param xy a coordinate array in which xy[0] has the x coordinates and xy[1] has the y coordinates
     */
    public Path(double[][] xy) {
        add(xy);
    }

    /**
     * Creates a new Path from a Path file.
     *
     * @param file Path file
     * @throws IOException if the Path could not be loaded from the file
     */
    public Path(File file) throws IOException {
        if (!this.load(file)) {
            throw new IOException();
        }
    }

    /**
     * Adds the given coordinate to the Path.
     *
     * @param x the x coordinate value
     * @param y the y coordinate value
     */
    public void add(double x, double y) {
        this.x.add(x);
        this.y.add(y);
    }

    /**
     * Adds the given coordinates to the Path.
     *
     * @param xy a coordinate array in which xy[0] has the x coordinates and xy[1] has the y coordinates
     */
    public void add(double[][] xy) {
        for (int i = 0; i < xy.length; i++) {
            this.add(xy[i][0], xy[i][1]);
        }
    }

    /**
     * Gets the coordinate at the given index.
     *
     * <p>
     * If the index is out of the bounds of the current path, this method returns <code>null</code>.
     *
     * @param index the index of the coordinate to get
     * @return a <code>double[]</code> coordinate value in which [0] is the x value and [1] is the y value
     */
    public double[] get(int index) {
        if (index < x.size() && index >= 0) {
            return new double[]{x.get(index), y.get(index)};
        } else {
            return null;
        }
    }

    /**
     * Returns the last coordinate in this path.
     *
     * <p>
     * The coordinate is given in a <code>double[]</code> where the 0th index is the x value, and the 1st index is the y value.
     *
     * <p>
     * If there are no coordinates in this path, returns <code>null</code>.
     *
     * @return the coordinate
     */
    public double[] getLast() {
        if (this.size() > 0) {
            return get(this.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Removes the coordinate at the specified index.
     *
     * <p>
     * If the given index is out of the bounds of the current path, this command does nothing.
     *
     * @param index the index of the coordinate to remove
     */
    public void remove(int index) {
        if (index >= 0 && x.size() > 0) {
            x.remove(index);
            y.remove(index);
        }
    }

    /**
     * Sets this {@link Path} to the given Path.
     *
     * <p>
     * If the given Path is <code>null</code>, this method does nothing.
     *
     * @param path the Path to set this Path to
     */
    public void set(Path path) {
        if (path != null) {
            this.clear();
            for (int i = 0; i < path.size(); i++) {
                this.add(path.get(i)[0], path.get(i)[1]);
            }
        }
    }

    /**
     * Saves this {@link Path} to the specified {@link File}.
     *
     * @param file the file to save this path to
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
     * Sets this {@link Path} to the path at the specified {@link File}.
     *
     * @param file the path file
     * @return if the operation was successful or not
     */
    public boolean load(File file) {
        if (file != null) {
            Path path;
            try {
                path = (Path)new ObjectInputStream(new FileInputStream(file)).readObject();
            } catch (Exception e) {
                return false;
            }
            this.set(path);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears this {@link Path} of all its coordinates.
     */
    public void clear() {
        x.clear();
        y.clear();
    }

    /**
     * Gets the size of this {@link Path}.
     *
     * @return the size of this {@link Path}
     */
    public int size() {
        return x.size();
    }

    /**
     * Gets the length of this {@link Path}.
     *
     * @return the length of this {@link Path}
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
     * @param end the second coordinate
     * @return the distance between the first and second coordinates
     */
    public double getDistance(int start, int end) {
        return Math.sqrt(Math.pow(x.get(end) - x.get(start), 2) + Math.pow(y.get(end) - y.get(start), 2));
    }

    /**
     * Gets the angle between three coordinates at the point of the second coordinate.
     *
     * @param start the first coordinate
     * @param connector the second coordinate
     * @param end the third coordinate
     * @return the angle between the three coordinates at the point of the second coordinate
     */
    public double getAngle(int start, int connector, int end) {
        return Math.atan((getSlope(start, connector) - getSlope(connector, end)) / (1 + (getSlope(start, connector) * getSlope(connector, end))));
    }

    /**
     * Gets the slope of a line constructed from two given coordinates.
     *
     * @param start the first coordinate
     * @param end the second coordinate
     * @return the slope of a line constructed from two given coordinates
     */
    public double getSlope(int start, int end) {
        return (y.get(end) - y.get(start)) / (x.get(end) - x.get(start));
    }
}
