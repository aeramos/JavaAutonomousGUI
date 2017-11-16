package auto_gui.api;

import java.io.*;
import java.util.ArrayList;

public class Path implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();

    public Path() {
    }

    public Path(Path path) {
        set(path);
    }

    public Path(double x, double y) {
        add(x, y);
    }

    public Path(double[][] xy) {
        add(xy);
    }

    public Path(File file) throws IOException {
        if (!this.load(file)) {
            throw new IOException();
        }
    }

    public void add(double x, double y) {
        this.x.add(x);
        this.y.add(y);
    }

    public void add(double[][] xy) {
        for (int i = 0; i < xy.length; i++) {
            this.add(xy[i][0], xy[i][1]);
        }
    }

    public void add(double[] xy) {
        add(xy[0], xy[1]);
    }

    public double[] get(int index) {
        return new double[]{x.get(index), y.get(index)};
    }

    public double[] getLast() {
        if (this.size() > 0) {
            return get(this.size() - 1);
        } else {
            return null;
        }
    }

    public void remove(int index) {
        x.remove(index);
        y.remove(index);
    }

    public void set(Path path) {
        if (path != null) {
            this.clear();
            for (int i = 0; i < path.size(); i++) {
                this.add(path.get(i));
            }
        }
    }

    public boolean save(File file) {
        try {
            new ObjectOutputStream(new FileOutputStream(file)).writeObject(this);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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

    public void clear() {
        x.clear();
        y.clear();
    }

    public int size() {
        return x.size();
    }

    public double getLength() {
        double length = 0;
        for (int i = 0; i < this.size() - 1; i++) {
            length += getDistance(i, i + 1);
        }
        return length;
    }

    public double getDistance(int start, int end) {
        return Math.sqrt(Math.pow(x.get(end) - x.get(start), 2) + Math.pow(y.get(end) - y.get(start), 2));
    }

    public double getAngle(int start, int connector, int end) {
        return Math.atan((getSlope(start, connector) - getSlope(connector, end)) / (1 + (getSlope(start, connector) * getSlope(connector, end))));
    }

    public double getSlope(int start, int end) {
        return (y.get(end) - y.get(start)) / (x.get(end) - x.get(start));
    }
}
