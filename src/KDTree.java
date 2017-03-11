import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * A K-Dimensional Tree Implementation with specific functionalities like insert node, remove node, search range etc.
 * @author Doga Can Yanikoglu
 * @since 3/9/2017.
 * @version 1.0
 */

public class KDTree {
    private KDNode root;
    private KDNode guard; // A guard node for safe calculation of root's region

    /**
     * Returns minimum valued point in dimension d
     * @param p1 Point 1
     * @param p2 Point 2
     * @param p3 Point 3
     * @param d Dimension, d=0: X, d=1: Y
     * @return The minimum valued point in dimension d
     */
    private static Point2D min(Point2D p1, Point2D p2, Point2D p3, int d) {
        if (d == 0) {
            if (p1.getX() <= p2.getX() && p1.getX() <= p3.getX()) {
                return p1;
            } else if (p2.getX() <= p1.getX() && p2.getX() <= p3.getX()) {
                return p2;
            } else {
                return p3;
            }
        } else { // d == 1
            if (p1.getY() <= p2.getY() && p1.getY() <= p3.getY()) {
                return p1;
            } else if (p2.getY() <= p1.getY() && p2.getY() <= p3.getY()) {
                return p2;
            } else {
                return p3;
            }
        }
    }

    /**
     * Returns maximum valued point in dimension d
     * @param p1 Point 1
     * @param p2 Point 2
     * @param p3 Point 3
     * @param d Dimension, d=0: X, d=1: Y
     * @return The maximum valued point in dimension d
     */
    private static Point2D max(Point2D p1, Point2D p2, Point2D p3, int d) {
        if (d == 0) {
            if (p1.getX() >= p2.getX() && p1.getX() >= p3.getX()) {
                return p1;
            } else if (p2.getX() >= p1.getX() && p2.getX() >= p3.getX()) {
                return p2;
            } else {
                return p3;
            }
        } else { // d == 1
            if (p1.getY() >= p2.getY() && p1.getY() >= p3.getY()) {
                return p1;
            } else if (p2.getY() >= p1.getY() && p2.getY() >= p3.getY()) {
                return p2;
            } else {
                return p3;
            }
        }
    }

    private static KDNode build(List<Point2D> P, int depth) {
        int median;
        Point2D intersectingPoint;
        KDNode vLeft, vRight;
        NodeData.Direction currentDir;
        if (P.size() == 1) {
            return createNode(null, null, new NodeData(NodeData.Direction.Point, P.get(0), depth));
        } else if (depth % 2 == 0) {
            P.sort(Comparator.comparingDouble(Point2D::getX));
            currentDir = NodeData.Direction.Vertical;
            median = (P.size() - 1) / 2;
            intersectingPoint = P.get(median);
        } else {
            P.sort(Comparator.comparingDouble(Point2D::getY));
            currentDir = NodeData.Direction.Horizontal;
            median = (P.size() - 1) / 2;
            intersectingPoint = P.get(median);
        }
        vLeft = build(P.subList(0, median + 1), depth + 1);
        vRight = build(P.subList(median + 1, P.size()), depth + 1);
        return createNode(vLeft, vRight, new NodeData(currentDir, intersectingPoint, depth));
    }

    /**
     * Create and return a node with given parameters
     * @param lc Left child
     * @param rc Right child
     * @param dt Data
     * @return Created node
     */
    private static KDNode createNode(KDNode lc, KDNode rc, NodeData dt) {
        KDNode parent = new KDNode(lc, rc, dt);
        if (lc != null)
            lc.setParent(parent);
        if (rc != null)
            rc.setParent(parent);
        return parent;
    }

    /**
     * Creates a K-Dimensional tree from parameter point list. Calculates regions after building.
     * @param P List of points
     * @return A K-Dimensional Tree from list of points
     */
    public static KDTree buildKDTree(ArrayList<Point2D> P) {
        KDTree tree = new KDTree();
        if(P.size() == 0) {
            return tree;
        }
        tree.setRoot(build(P, 0));
        tree.calculateRegions();
        return tree;
    }

    /**
     * Default constructor.
     */
    public KDTree() {
        this.root = null;
        this.guard = createNode(root,root, new NodeData(NodeData.Direction.Point, new Point2D.Double(0,0), -1));
        guard.getData().setLeftRegion(new RectangularHalfPlane());
        guard.getData().setRightRegion(new RectangularHalfPlane());
    }

    /**
     * Sets the given parameter as new root of tree.
     * @param nd New root node
     */
    public void setRoot(KDNode nd) {
        guard.setLeftChild(nd);
        guard.setRightChild(nd);
        root = nd;
    }


    /**
     * Preorder traverse the tree from given node as parameter.
     * @param nd The node will be visited
     */
    private void preOrderPrint(KDNode nd) {
        if (nd != null) {
            //Visit the node by Printing the node data
            System.out.println(nd.getData());
            preOrderPrint(nd.getLeftChild());
            preOrderPrint(nd.getRightChild());
        }
    }

    /**
     * Prints tree layout to console.
     */
    public void displayTree() {
        if(root == null) {
            System.out.println("Tree is empty!");
            return;
        }
        preOrderPrint(root);
    }

    /**
     * Tries going to leftmost nodes in given dimension nodes. In other dimension nodes, continues searching in both children.
     * @param nd Current node
     * @param d Dimension
     * @param dpth Current depth
     * @return Found point
     */
    private Point2D innerFindMin(KDNode nd, int d, int dpth) {
        if (nd == null) {
            return new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        if (dpth % 2 == d) {
            if (nd.getLeftChild() == null) {
                return nd.getData().getPoint();
            }
            return innerFindMin(nd.getLeftChild(), d, dpth + 1);
        } else {
            return min(nd.getData().getPoint(), innerFindMin(nd.getLeftChild(), d, dpth + 1), innerFindMin(nd.getRightChild(), d, dpth + 1), d);
        }
    }

    /**
     * Searches the tree for minimum x or y valued point.
     * @param d Search dimension, d=0: X, d=1: Y
     * @return Found point
     */
    public Point2D findMin(int d) {
        return innerFindMin(this.root, d, 0);
    }

    /**
     * Tries going to rightmost nodes in given dimension nodes. In other dimension nodes, continues searching in both children.
     * @param nd Current node
     * @param d Dimension
     * @param dpth Current depth
     * @return Found point
     */
    private Point2D innerFindMax(KDNode nd, int d, int dpth) {
        if (nd == null) {
            return new Point2D.Double(-Double.MAX_VALUE, -Double.MAX_VALUE);
        }
        if (dpth % 2 == d) {
            if (nd.getRightChild() == null) {
                return nd.getData().getPoint();
            }
            return innerFindMax(nd.getRightChild(), d, dpth + 1);
        } else {
            return max(nd.getData().getPoint(), innerFindMax(nd.getLeftChild(), d, dpth + 1), innerFindMax(nd.getRightChild(), d, dpth + 1), d);
        }
    }

    /**
     * Searches the tree for maximum x or y valued point.
     * @param d Search dimension, d=0: X, d=1: Y
     * @return Found point
     */
    public Point2D findMax(int d) {
        return innerFindMax(this.root, d, 0);
    }

    /**
     * Traverses the tree for finding related point in tree.
     * @param point The point going to be searched
     * @param nd Current node
     * @param dpth Current depth
     * @return The point found in tree, otherwise null
     */
    private Point2D innerSearch(Point2D point, KDNode nd, int dpth) {
        if (nd.getData().getDirection() != NodeData.Direction.Point) { // Current node is not point, continue searching
            if (dpth % 2 == 0) {
                if (point.getX() <= nd.getData().getPoint().getX()) {
                    return innerSearch(point, nd.getLeftChild(), dpth + 1);
                } else {
                    return innerSearch(point, nd.getRightChild(), dpth + 1);
                }
            } else {
                if (point.getY() <= nd.getData().getPoint().getY()) {
                    return innerSearch(point, nd.getLeftChild(), dpth + 1);
                } else {
                    return innerSearch(point, nd.getRightChild(), dpth + 1);
                }
            }
        } else { // A point is reached, check if it's same with our point coordinates
            if (nd.getData().getPoint().getX() == point.getX() && nd.getData().getPoint().getY() == point.getY()) {
                return nd.getData().getPoint(); // Found
            } else {
                return null; // Not found
            }
        }
    }


    /**
     * Searches the tree for given point as parameter.
     * @param point The point going to be searched
     * @return Point of found node in tree, null if point is not found
     */
    public Point2D search(Point2D point) {
        return innerSearch(point, root, 0);
    }


    /**
     * Depth first traverse the tree from given node as parameter. Print data if visited node is a point.
     * @param nd The node will be visited
     */
    private void depthFirstPrint(KDNode nd) {
        if (nd.getData().getDirection() == NodeData.Direction.Point) { // A point is reached
            System.out.printf("(%s,%s) ", nd.getData().getPoint().getX(), nd.getData().getPoint().getY());
        }

        // Continue searching a point
        if (nd.getLeftChild() != null) {
            depthFirstPrint(nd.getLeftChild());
        }

        if (nd.getRightChild() != null) {
            depthFirstPrint(nd.getRightChild());
        }
    }


    /**
     * Prints all point nodes in the tree to console.
     */
    public void displayPoints() {
        if(root==null) {
            System.out.println("There are no points in tree!");
            return;
        }
        this.depthFirstPrint(root);
        System.out.println();
    }

    /**
     * Intersects current line node's regions with it's child node's regions. Stores results in child nodes.
     * @param prnt The node whose child's regions are going to be calculated
     */
    private void innerCalculateRegions(KDNode prnt) {
        if (prnt.getLeftChild() != null) { // Calculate left child's regions
            KDNode ls = prnt.getLeftChild();
            RectangularHalfPlane latestArea = prnt.getData().getLeftRegion();
            ls.getData().setLeftRegion(latestArea.intersectToLeft(ls.getData().getPoint(), ls.getData().getDepth() % 2));
            ls.getData().setRightRegion(latestArea.intersectToRight(ls.getData().getPoint(), ls.getData().getDepth() % 2));
        }

        if (prnt.getRightChild() != null) { // Calculate right child's regions
            KDNode rs = prnt.getRightChild();
            RectangularHalfPlane latestArea = prnt.getData().getRightRegion();
            rs.getData().setLeftRegion(latestArea.intersectToLeft(rs.getData().getPoint(), rs.getData().getDepth() % 2));
            rs.getData().setRightRegion(latestArea.intersectToRight(rs.getData().getPoint(), rs.getData().getDepth() % 2));
        }

        // Continue traversing the tree
        if (prnt.getLeftChild() != null) {
            innerCalculateRegions(prnt.getLeftChild());
        }

        if (prnt.getRightChild() != null) {
            innerCalculateRegions(prnt.getRightChild());
        }
    }

    /**
     * Calculates the left and right regions of each line node. Initially calculates regions of root.
     */
    private void calculateRegions() {
        RectangularHalfPlane wholeArea = new RectangularHalfPlane(); // Parent area to be intersected is whole area
        root.getData().setLeftRegion(wholeArea.intersectToLeft(root.getData().getPoint(), 0));
        root.getData().setRightRegion(wholeArea.intersectToRight(root.getData().getPoint(), 0));
        innerCalculateRegions(root);
    }

    /**
     * Checks if range contains left or right regions of current node. Continues searching with result of this query.
     * @param V Current Node
     * @param R Range to be searched
     */
    private void SearchKDTree(KDNode V, RectangularHalfPlane R) {
        if (V.getData().getDirection() == NodeData.Direction.Point) { // A valid point is found
            if (R.contains(V.getData().getPoint()))
                System.out.printf("(%s,%s) ", V.getData().getPoint().getX(), V.getData().getPoint().getY());
        } else {
            if (R.contains(V.getData().getLeftRegion())) { // Left subtree is fully contained in range, print all points.
                depthFirstPrint(V.getLeftChild());
            } else if (R.intersects(V.getData().getLeftRegion())) { // Continue searching
                SearchKDTree(V.getLeftChild(), R);
            }

            if (R.contains(V.getData().getRightRegion())) { // Right subtree is fully contained in range, print all points.
                depthFirstPrint(V.getRightChild());
            } else if (R.intersects(V.getData().getRightRegion())) { // Continue searching
                SearchKDTree(V.getRightChild(), R);
            }
        }
    }

    /**
     * Prints points in given range to console.
     * @param llc Lower Left Corner Point
     * @param urc Upper Right Corner Point
     */
    public void printRange(Point2D llc, Point2D urc) {
        if(llc.getX() == urc.getX() || llc.getY() == urc.getY()) {
            System.out.println("Range can't be a line or a point!");
            return;
        }
        RectangularHalfPlane range = new RectangularHalfPlane(llc.getX(), llc.getY(), urc.getX(), urc.getY());
        SearchKDTree(root, range);
        System.out.println();
    }

    /**
     * Searches for closest point in related range. After a point found, converts it to a line, and adds these two points as child point,
     * or replaces it with our point and converts our point to a line, and adds these two points as child point.
     * @param V Current Node
     * @param P Point to be insterted
     */
    private void innerInsert(KDNode V, Point2D P) {
        if (V.getData().getDirection() == NodeData.Direction.Point) { // Closest point found
            Point2D backup = V.getData().getPoint();
            int currDepth = V.getData().getDepth();
            int d = currDepth % 2;
            NodeData.Direction currentDir = d == 0 ? NodeData.Direction.Vertical : NodeData.Direction.Horizontal;
            V.getData().setDirection(currentDir);
            boolean isVLeftChild;

            if(V.equals(root)) {
                isVLeftChild = true;
            } else {
                isVLeftChild = V.getParent().getLeftChild().equals(V);
            }

            if (currentDir == NodeData.Direction.Vertical) { // Line is vertical, compare with X coords
                if (P.getX() <= backup.getX()) { // Line will be created from new point
                    V.getData().setPointIntersecting(P);
                } else { // Line will be created from existing point
                    V.getData().setPointIntersecting(backup);
                }
            } else { // Line is horizontal, compare with Y coords
                if (P.getY() <= backup.getY()) { // Line will be created from new point
                    V.getData().setPointIntersecting(P);
                } else { // Line will be created from existing point
                    V.getData().setPointIntersecting(backup);
                }
            }

            // Set new regions
            if (isVLeftChild) {
                V.getData().setLeftRegion(V.getParent().getData().getLeftRegion().intersectToLeft(V.getData().getPoint(), d));
                V.getData().setRightRegion(V.getParent().getData().getLeftRegion().intersectToRight(V.getData().getPoint(), d));
            } else {
                V.getData().setLeftRegion(V.getParent().getData().getRightRegion().intersectToLeft(V.getData().getPoint(), d));
                V.getData().setRightRegion(V.getParent().getData().getRightRegion().intersectToRight(V.getData().getPoint(), d));
            }

            // Set Childs of new line
            if (V.getData().getLeftRegion().contains(P)) {
                V.setLeftChild(createNode(null, null, new NodeData(NodeData.Direction.Point, P, currDepth + 1)));
                V.setRightChild(createNode(null, null, new NodeData(NodeData.Direction.Point, backup, currDepth + 1)));
            } else {
                V.setLeftChild(createNode(null, null, new NodeData(NodeData.Direction.Point, backup, currDepth + 1)));
                V.setRightChild(createNode(null, null, new NodeData(NodeData.Direction.Point, P, currDepth + 1)));
            }

        } else { // Continue searching
            if (V.getData().getLeftRegion().contains(P)) {
                innerInsert(V.getLeftChild(), P);
            } else if (V.getData().getRightRegion().contains(P)) {
                innerInsert(V.getRightChild(), P);
            }
        }
    }

    /**
     * Inserts a point to tree.
     * @param point Point to be inserted
     */
    public void insert(Point2D point) {
        if(root == null) {
            setRoot(createNode(null, null, new NodeData(NodeData.Direction.Point, point, 0)));
            root.setParent(guard);
        } else {
            innerInsert(root, point);
        }
        System.out.printf("\nInserted (%s, %s)\n", point.getX(), point.getY());
    }

    /**
     * Searches the point in tree. If found, checks status of it's sibling. If sibling is a line, safely removes the parent & self, carries
     * the sibling to parent's place, converts it to a line, and adds the points as left & right child to that line. Updates regions of new line node.
     * If sibling is a point, just removes the parent line, and carries sibling point to parent's place.
     * @param V Current Node
     * @param P Point to be removed
     * @return True if point is successfully removed, false otherwise
     */
    private boolean innerRemove(KDNode V, Point2D P) {
        if (V.getData().getDirection() == NodeData.Direction.Point) { // A point found
            if (P.getX() != V.getData().getPoint().getX() && P.getY() != V.getData().getPoint().getY()) {
                // Found point is not same with the one going to be removed
                return false;
            } else { // Searching point is found
                if(V.equals(root)) { // Removal of last point in tree
                    setRoot(null);
                    return true;
                }

                boolean isVLeftChild = V.getParent().getLeftChild().equals(V);
                KDNode transferNode;

                if (isVLeftChild) { // is Left Child
                    transferNode = V.getParent().getRightChild(); // Node to be transferred one step up
                    if (V.getParent().getRightChild().getData().getDirection() != NodeData.Direction.Point) {
                        // Sibling is not a point, it's line direction will be changed
                        transferNode.getData().setDirection(V.getParent().getData().getDirection());
                        transferNode.getLeftChild().getData().depthDecrement(); // Decrease child's depths by one
                        transferNode.getRightChild().getData().depthDecrement();
                    } else {  // Sibling is a point
                        if(V.getParent().equals(root)) { // Just 1 point will left after removal, set sibling as root
                            setRoot(V.getParent().getRightChild());
                            return true;
                        }
                    }
                } else { // is Right Child
                    transferNode = V.getParent().getLeftChild(); // Node to be transferred one step up
                    if (V.getParent().getLeftChild().getData().getDirection() != NodeData.Direction.Point) {
                        // Sibling is not a point, it's line direction will be changed
                        transferNode.getData().setDirection(V.getParent().getData().getDirection());
                        transferNode.getLeftChild().getData().depthDecrement(); // Decrease child's depths by one
                        transferNode.getRightChild().getData().depthDecrement();
                    } else { // Sibling is a point
                        if(V.getParent().equals(root)) { // Just 1 point will left after removal, set sibling as root
                            setRoot(V.getParent().getLeftChild());
                            return true;
                        }
                    }
                }

                transferNode.getData().depthDecrement(); // Decrease transferNode's depth by one
                if(!V.getParent().getParent().equals(guard)) { //Safe removal
                    transferNode.setParent(V.getParent().getParent()); // Set it's new parent
                    // Set our transferNode as new parent's left or right Child
                    if (V.getParent().getParent().getLeftChild().equals(V.getParent())) {
                        V.getParent().getParent().setLeftChild(transferNode);
                    } else {
                        V.getParent().getParent().setRightChild(transferNode);
                    }
                } else { // Not a safe removal, reached to root of tree. Set transferNode as new root
                    transferNode.setParent(guard);
                    setRoot(transferNode);
                }

                // Set new regions of transferNode ( If node is converted into a line from point )
                if(transferNode.getData().getDirection() != NodeData.Direction.Point) {
                    boolean isTransferNodeLeftChild = transferNode.getParent().getLeftChild().equals(transferNode);
                    int d = transferNode.getData().getDepth() % 2;
                    Point2D transferNodeP = transferNode.getData().getPoint();
                    if (isTransferNodeLeftChild) {
                        transferNode.getData().setLeftRegion(transferNode.getParent().getData().getLeftRegion().intersectToLeft(transferNodeP, d));
                        transferNode.getData().setRightRegion(transferNode.getParent().getData().getLeftRegion().intersectToRight(transferNodeP, d));
                    } else {
                        transferNode.getData().setLeftRegion(transferNode.getParent().getData().getRightRegion().intersectToLeft(transferNodeP, d));
                        transferNode.getData().setRightRegion(transferNode.getParent().getData().getRightRegion().intersectToRight(transferNodeP, d));
                    }
                }
                return true;
            }
        } else { // Continue searching
            if (V.getData().getLeftRegion().contains(P)) {
                return innerRemove(V.getLeftChild(), P);
            } else if (V.getData().getRightRegion().contains(P)) {
                return innerRemove(V.getRightChild(), P);
            } else {
                return false;
            }
        }
    }

    /**
     * Removes a point from tree if it exists.
     * @param point Point to be removed
     */
    public void remove(Point2D point) {
        if(innerRemove(root, point)) {
            System.out.printf("\nRemoved (%s, %s)\n", point.getX(), point.getY());
        } else {
            System.out.printf("\nNot found (%s, %s)\n", point.getX(), point.getY());
        }
    }
}