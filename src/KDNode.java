/**
 * A K-Dimensional Tree node implementation. The node stores required data in NodeData variable.
 * @author Doga Can Yanikoglu
 * @since 3/9/2017
 * @version 1.0
 */

public class KDNode {
    private KDNode leftChild;
    private KDNode rightChild;
    private KDNode parent;
    private NodeData data;

    public KDNode() {
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
        this.data = null;
    }

    public KDNode(NodeData dt) {
        this();
        this.data = dt;
    }

    public KDNode(KDNode lsbl, KDNode rsbl, NodeData dt) {
        this(dt);
        this.leftChild = lsbl;
        this.rightChild = rsbl;
    }

    public boolean equals(Object o) {
        if(o instanceof KDNode) {
            KDNode nd = (KDNode) o;
            return nd.getData().toString().equals(this.getData().toString());
        }
        return false;
    }

    public NodeData getData() {
        return data;
    }

    public KDNode getLeftChild() {
        return leftChild;
    }

    public KDNode getRightChild() {
        return rightChild;
    }

    public KDNode getParent() {
        return parent;
    }

    public void setParent(KDNode nd) {
        parent = nd;
    }

    public void setLeftChild(KDNode nd) {
        nd.setParent(this);
        leftChild = nd;
    }

    public void setRightChild(KDNode nd) {
        nd.setParent(this);
        rightChild = nd;
    }
}