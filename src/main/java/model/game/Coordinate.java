package model.game;

import java.util.Objects;

/**
 * Coordinate class representing a 2D point in the Chess board.
 */
public class Coordinate {
    public int x, y;


    /**
     * Constructor of coordinates
     * @param x coordinate on the x-axis
     * @param y coordinate on the y-axis
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Copy constructor for coordinate
     * @param c coordinate to copy
     */
    public Coordinate(Coordinate c) {
        this(c.x, c.y);
    }

    /**
     * Getter of the x coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter of the y coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @return coordinate of the piece
     */
    public Coordinate getCoordinate() {
        return new Coordinate(x, y);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Adds the given Coordinate to this Coordinate and returns the result.
     *
     * @param a The Coordinate instance to be added.
     * @return A new Coordinate instance representing the sum of this Coordinate and the given Coordinate.
     */
    public Coordinate Add(Coordinate a){
        return new Coordinate(this.x + a.x, this.y + a.y);
    }

    /**
     * Returns a string representation of the Coordinate.
     *
     * @return A string in the format "x:y".
     */
    public String toString(){
        return "" + x + ":" + y + "";
    }
}
