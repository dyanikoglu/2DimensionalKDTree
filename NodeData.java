import java.awt.geom.Point2D;

/**
 * K-Dimensional Tree Node's data structure implementation. The data stores information like regions, line direction and depth.
 * @author Doga Can Yanikoglu
 * @since 3/9/2017
 * @version 1.0
 */

public class NodeData {
    /**
     * Enum declaration for denoting type of a node.
     */
    public enum Direction {
        Vertical {
            public String toString() {
                return "|";
            }
        },
        Horizontal {
            public String toString() {
                return "-";
            }
        },
        Point {
            public String toString() {
                return "P";
            }
        }
    }

    private Direction direction; // Type of node
    private Point2D pointIntersecting; // The point of node
    private RectangularHalfPlane leftRegion; // The region of leftside of the line (closed)
    private RectangularHalfPlane rightRegion; // The region of rightside of the line (open)
    private int depth; // depth of the node in tree

    public NodeData(Direction dir, Point2D pI, int dpth) {
        direction = dir;
        pointIntersecting = pI;
        leftRegion = null;
        rightRegion = null;
        depth = dpth;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public Point2D getPoint() {
        return pointIntersecting;
    }

    public void setPointIntersecting(Point2D p) {
        pointIntersecting = p;
    }

    public int getDepth() {
        return depth;
    }

    public void depthDecrement() {
        this.depth = this.depth - 1;
    }

    public RectangularHalfPlane getLeftRegion() {
        return leftRegion;
    }

    public RectangularHalfPlane getRightRegion() {
        return rightRegion;
    }

    public void setLeftRegion(RectangularHalfPlane rgn) {
        this.leftRegion = rgn;
    }

    public void setRightRegion(RectangularHalfPlane rgn) {
        this.rightRegion = rgn;
    }

    /**
     * Puts '.' as many as node's depth.
     * @return A string with dots
     */
    private String depthToStr() {
        StringBuilder toRet = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            toRet.append(". ");
        }
        return toRet.toString();
    }

    public String toString() {
        if (direction == Direction.Point) {
            return String.format("%s(%s: (%s, %s))", depthToStr(), direction, pointIntersecting.getX(), pointIntersecting.getY());
        } else { // Vertical or Horizontal cut
            return String.format("%s(%s: (%s))", depthToStr(), direction, direction == Direction.Vertical ? "x=" + pointIntersecting.getX() : "y=" + pointIntersecting.getY());
        }
    }
}