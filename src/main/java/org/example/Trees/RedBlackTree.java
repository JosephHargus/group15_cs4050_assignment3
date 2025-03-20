package org.example.Trees;
//Author: Evan Trejo
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Red-Black Tree.
 * @param <T>
 * @author EvanTrejo
 */
public class RedBlackTree<T extends Comparable<T>> implements Tree<T>, Serializable {
    private Node root;
    private int size;

    private enum NodeColor {
        RED, BLACK
    }

    private class Node implements TreeNode<T>, Serializable {
        T value;
        Node left, right, parent;
        NodeColor color;

        Node(T value, NodeColor color, Node parent) {
            this.value = value;
            this.color = color;
            this.parent = parent;
        }

        @Override
        public T getValue() { return value; }

        @Override
        public TreeNode<T> getLeft() { return left; }

        @Override
        public TreeNode<T> getRight() { return right; }

        //Handle node colors based on function in TreeVisualizer
        public String getColor() { 
            if(color == NodeColor.RED) return "RED";
            else return "BLACK"; 
        }
    }

/**
 * Intiates the deletion of a Node.
 * Calls an override delete method.
 * @param <T>
 * @return true on success and false on fail
 * @author EvanTrejo
 */
public boolean delete(T value) {
    if (root == null) return false; // Tree is empty, nothing to delete
    root = delete(root, value);
    return true; // If root is null, the tree is empty after deletion
}

/**
 * Recursive helper function which removes a node from the tree
 * and ensures the Red-Black Tree properties are maintained.
 * @param <T>
 * @return the updated root of the tree
 * @author EvanTrejo
 */
private Node delete(Node root, T value) {
    // Find the node to delete
    if (root == null) return root;

    if (value.compareTo(root.value) < 0) {
        root.left = delete(root.left, value);
    } else if (value.compareTo(root.value) > 0) {
        root.right = delete(root.right, value);
    } else {
        // Case 1: Node has no children (leaf node)
        if (root.left == null && root.right == null) {
            if (root.color == NodeColor.BLACK) {
                fixDelete(root);
            }
            return null; // Node is deleted
        }
        
        // Case 2: Node has one child
        else if (root.left == null || root.right == null) {
            Node child = (root.left != null) ? root.left : root.right;

            // If root is black, we need to fix the red-black tree properties
            if (root.color == NodeColor.BLACK) {
                fixDelete(root);
            }

            // Replace root with its child
            return child;
        }

        // Case 3: Node has two children
        else {
            Node successor = minValueNode(root.right);
            root.value = successor.value; // Copy the inorder successor's value to this node
            root.right = delete(root.right, successor.value); // Delete the inorder successor
        }
    }

    return root;
}

/**
 * Fixes the Red-Black Tree properties after deletion
 * to maintain balance and color properties.
 * @param node the node causing the violation
 * @author EvanTrejo
 */
private void fixDelete(Node node) {
    // If the node being deleted is black, we need to fix the double black violation
    while (node != root && (node == null || node.color == NodeColor.BLACK)) {
        if (node == node.parent.left) {
            Node sibling = node.parent.right;
            if (sibling.color == NodeColor.RED) {
                sibling.color = NodeColor.BLACK;
                node.parent.color = NodeColor.RED;
                leftRotate(node.parent);
                sibling = node.parent.right;
            }

            if ((sibling.left == null || sibling.left.color == NodeColor.BLACK) &&
                (sibling.right == null || sibling.right.color == NodeColor.BLACK)) {
                sibling.color = NodeColor.RED;
                node = node.parent;
            } else {
                if (sibling.right == null || sibling.right.color == NodeColor.BLACK) {
                    sibling.left.color = NodeColor.BLACK;
                    sibling.color = NodeColor.RED;
                    rightRotate(sibling);
                    sibling = node.parent.right;
                }

                sibling.color = node.parent.color;
                node.parent.color = NodeColor.BLACK;
                if (sibling.right != null) sibling.right.color = NodeColor.BLACK;
                leftRotate(node.parent);
                node = root;
            }
        } else {
            Node sibling = node.parent.left;
            if (sibling.color == NodeColor.RED) {
                sibling.color = NodeColor.BLACK;
                node.parent.color = NodeColor.RED;
                rightRotate(node.parent);
                sibling = node.parent.left;
            }

            if ((sibling.left == null || sibling.left.color == NodeColor.BLACK) &&
                (sibling.right == null || sibling.right.color == NodeColor.BLACK)) {
                sibling.color = NodeColor.RED;
                node = node.parent;
            } else {
                if (sibling.left == null || sibling.left.color == NodeColor.BLACK) {
                    sibling.right.color = NodeColor.BLACK;
                    sibling.color = NodeColor.RED;
                    leftRotate(sibling);
                    sibling = node.parent.left;
                }

                sibling.color = node.parent.color;
                node.parent.color = NodeColor.BLACK;
                if (sibling.left != null) sibling.left.color = NodeColor.BLACK;
                rightRotate(node.parent);
                node = root;
            }
        }
    }

    if (node != null) node.color = NodeColor.BLACK;
}

    /**
     * Finds the node with the minimum value in a subtree.
     * @param node the starting node for the search
     * @return the node with the smallest value
     * @author EvanTrejo
     */
    private Node minValueNode(Node node) {
        // Get the node with the minimum value (leftmost)
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    /**
     * Inserts a value into the Red-Black Tree while maintaining
     * Red-Black properties.
     * @param value the value to insert
     * @author EvanTrejo
     */
    @Override
    public void insert(T value) {
        Node newNode = new Node(value, NodeColor.RED, null);
        root = insert(root, newNode);
        fixInsert(newNode);
        size++;
    }

    /**
     * Recursive helper function to insert a node into the tree.
     * @param root the current root node
     * @param node the node to insert
     * @return the updated root node
     * @author EvanTrejo
     */
    private Node insert(Node root, Node node) {
        if (root == null) return node;

        if (node.value.compareTo(root.value) < 0) {
            root.left = insert(root.left, node);
            root.left.parent = root;
        } else if (node.value.compareTo(root.value) > 0) {
            root.right = insert(root.right, node);
            root.right.parent = root;
        }
        return root;
    }

    /**
     * Fixes the Red-Black Tree properties after insertion.
     * @param node the inserted node
     * @author EvanTrejo
     */
    private void fixInsert(Node node) {
        while (node.parent != null && node.parent.color == NodeColor.RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == NodeColor.RED) {
                    node.parent.color = NodeColor.BLACK;
                    uncle.color = NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        leftRotate(node);
                    }
                    node.parent.color = NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    rightRotate(node.parent.parent);
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == NodeColor.RED) {
                    node.parent.color = NodeColor.BLACK;
                    uncle.color = NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rightRotate(node);
                    }
                    node.parent.color = NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    leftRotate(node.parent.parent);
                }
            }
        }
        root.color = NodeColor.BLACK;
    }

    /**
     * Performs a left rotation on the given node.
     * @param node the node to rotate
     * @author EvanTrejo
     */
    private void leftRotate(Node node) {
        Node temp = node.right;
        node.right = temp.left;
        if (temp.left != null) temp.left.parent = node;
        temp.parent = node.parent;
        if (node.parent == null) root = temp;
        else if (node == node.parent.left) node.parent.left = temp;
        else node.parent.right = temp;
        temp.left = node;
        node.parent = temp;
    }

    /**
     * Performs a right rotation on the given node.
     * @param node the node to rotate
     * @author EvanTrejo
     */
    private void rightRotate(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        if (temp.right != null) temp.right.parent = node;
        temp.parent = node.parent;
        if (node.parent == null) root = temp;
        else if (node == node.parent.right) node.parent.right = temp;
        else node.parent.left = temp;
        temp.right = node;
        node.parent = temp;
    }

    @Override
    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(Node node, T value) {
        if (node == null) return false;
        if (value.compareTo(node.value) < 0) return contains(node.left, value);
        if (value.compareTo(node.value) > 0) return contains(node.right, value);
        return true;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public List<T> inorderTraversal() {
        List<T> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    private void inorderTraversal(Node node, List<T> result) {
        if (node == null) return;
        inorderTraversal(node.left, result);
        result.add(node.value);
        inorderTraversal(node.right, result);
    }

    @Override
    public String type() {
        return "RBT";
    }
    @Override
    public Color color() {
        return Color.BLUE;
        // Shouldn't be called; Turns purple if something goes wrong
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }
}
