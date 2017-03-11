import java.awt.geom.Point2D;

/**
 * A specific half plane implementation for use with KDTree's regions.
 * @author Doga Can Yanikoglu
 * @since 3/9/2017
 * @version 1.0
 */

public class RectangularHalfPlane {
    private double minX, minY, maxX, maxY;

    /**
     * Returns half plane's boundaries as string
     * @return Formatted String
     */
    public String toString() {
        return String.format("%s<x<=%s, %s<y<=%s", minX,maxX,minY,maxY);
    }

    /**
     * Constructor.
     * @param minX Leftmost bound
     * @param minY Bottom bound
     * @param maxX Rightmost bound
     * @param maxY Top bound
     */
    public RectangularHalfPlane(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Default constructor. Constructs half plane with possible largest boundaries.
     */
    public RectangularHalfPlane() {
        this.minX = -Double.MAX_VALUE;
        this.minY = -Double.MAX_VALUE;
        this.maxX = Double.MAX_VALUE;
        this.maxY = Double.MAX_VALUE;
    }

    /**
     * Intersects line's left(or down) side and this half plane
     * @param line The point that a line passes through
     * @param d Dimension of line. d=0: Vertical, d=1 Horizontal
     * @return An intersection area with line and half plane
     */
    public RectangularHalfPlane intersectToLeft(Point2D line, int d) {
        RectangularHalfPlane temp = new RectangularHalfPlane();
        if( d == 0) { // Vertical line // Leftside
            temp.minX = this.minX;
            temp.maxX = line.getX();
            temp.minY = this.minY;
            temp.maxY = this.maxY;
        } else { // Horizontal line // Lowerside
            temp.minX = this.minX;
            temp.maxX = this.maxX;
            temp.maxY = line.getY();
            temp.minY = this.minY;
        }
        return temp;
    }

    /**
     * Intersects line's right(or up) side and this half plane
     * @param line The point that a line passes through
     * @param d Dimension of line. d=0: Vertical, d=1 Horizontal
     * @return An intersection area with line and half plane
     */
    public RectangularHalfPlane intersectToRight(Point2D line, int d) {
        RectangularHalfPlane temp = new RectangularHalfPlane();
        if( d == 0) { // Vertical line // Rightside
            temp.minX = line.getX();
            temp.maxX = this.maxX;
            temp.minY = this.minY;
            temp.maxY = this.maxY;
        } else { // Horizontal line // Upperside
            temp.minX = this.minX;
            temp.maxX = this.maxX;
            temp.maxY = this.maxY;
            temp.minY = line.getY();
        }
        return temp;
    }

    /**
     * Checks if given point as parameter is contained in this half plane (closed)
     * @param pnt Point to be checked
     * @return True if point is contained, false otherwise
     */
    public boolean contains(Point2D pnt) {
        return (pnt.getX() <= this.maxX && pnt.getX() >= this.minX && pnt.getY() <= this.maxY && pnt.getY() >= this.minY);
    }

    /**
     * Checks if given half plane as parameter is contained in this half plane (closed)
     * @param hp Half plane to be checked
     * @return True if half plane is contained, false otherwise
     */
    public boolean contains(RectangularHalfPlane hp) {
        return (hp.maxX <= this.maxX && hp.minX >= this.minX && hp.maxY <= this.maxY && hp.minY >= this.minY);
    }

    /**
     * Checks if given half plane as parameter intersects with this half plane (closed)
     * @param hp Half plane to be checked
     * @return True if half planes intersect, false otherwise
     */
    public boolean intersects(RectangularHalfPlane hp) {
        return !(hp.maxX < this.minX || hp.minX > this.maxX || hp.maxY < this.minY || hp.minY > this.maxY);
    }
}