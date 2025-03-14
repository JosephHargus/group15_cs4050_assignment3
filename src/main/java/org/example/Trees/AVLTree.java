package org.example.Trees;
//Author: Joseph Hargus
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the AVL (balanced BST) tree.
 * @param <T>
 * @author JosephHargus
 */
public class AVLTree<T extends Comparable<T>> implements Tree<T> , Serializable {
    private Node root;
    private int size;

    private class Node implements TreeNode<T>, Serializable{
        T value;
        Node left, right;
        int height;

        Node(T value) {
            this.value = value;
            this.height = 1;
        }

        @Override
        public T getValue() {return value;}

        @Override
        public TreeNode<T> getLeft() {return left;}

        @Override
        public TreeNode<T> getRight() {return right;}

        public String getColor() {return null;}
    }

    private int getHeight(Node node) {
        if (node == null) return 0;
        return node.height;
    }

    /**
     * Corrects an imbalance at a given Node node due to an addition
     * in the right subtree of node's right child.
     * @param node Unbalanced node
     * @return the Node which replaced node
     * @author JosephHargus
     */
    private Node rightRotation(Node node) {
        Node c = node.left;
        node.left = c.right;
        c.right = node;
        updateHeight(node);
        updateHeight(c);
        return c;
    }

    /**
     * Corrects an imbalance at a given Node node due to an addition
     * in the right subtree of node's right child.
     * @param node Unbalanced node
     * @return the Node which replaced node
     * @author JosephHargus
     */
    private Node leftRotation(Node node) {
        Node c = node.right;
        node.right = c.left;
        c.left = node;
        updateHeight(node);
        updateHeight(c);
        return c;
    }

    /**
     * Corrects an imbalance at a given Node node due to an addition
     * in the left subtree of node's right child.
     * @param node Unbalanced node
     * @return the Node which replaced node
     * @author JosephHargus
     */
    private Node rightLeftRotation(Node node) {
        Node c = node.right;
        node.right = rightRotation(c);
        return leftRotation(node);
    }

    /**
     * Corrects an imbalance at a given Node node due to an addition
     * in the right subtree of node's left child.
     * @param node Unbalanced node
     * @return the Node which replaced node
     * @author JosephHargus
     */
    private Node leftRightRotation(Node node) {
        Node c = node.left;
        node.left = leftRotation(c);
        return rightRotation(node);
    }

    /**
     * Returns 0 if node is balanced.
     * @param node the Node to check balance for
     * @return  < 0 if right subtree is larger. > 0 if left subtree is larger
     */
    private int getBalanceFactor(Node node) {
        // if node is null, it is balanced
        if (node == null) return 0;

        return getHeight(node.left) - getHeight(node.right);
    }

    /**
     * Balance the given Node node by performing rotation(s).
     * @param node The Node to balance
     * @return the balanced node
     */
    private Node balance(Node node) {
        int balanceFactor = getBalanceFactor(node);
        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                node = leftRightRotation(node);
            } else {
                node = rightRotation(node);
            }
        } else if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                node = rightLeftRotation(node);
            } else {
                node = leftRotation(node);
            }
        }
        return node;
    }

    private Node updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }
        return node;
    }

    @Override
    public void insert(T value) {
        // call recursive insert function
        root = insert(root, value);
        size++;
    }

    private Node insert(Node node, T value) {
        // check for empty tree
        if (node == null) return new Node(value);
        // check if value belongs in left subtree
        if (value.compareTo(node.value) < 0) {
            node.left = insert(node.left, value);
        }
        // check if value belongs in right subtree
        else if (value.compareTo(node.value) > 0) {
            node.right = insert(node.right, value);
        }
        // this value already exists in the tree
        else {
            return node;
        }
        return balance(updateHeight(node));
    }

    @Override
    public boolean delete(T value) {
        int originalSize = size;
        // call recursive delete function
        root = delete(root, value);

        return size < originalSize;
    }

    private Node delete(Node node, T value) {
        // check if next node exists
        if (node == null) return null;
        // search for node in left subtree
        if (value.compareTo(node.value) < 0) {
            node.left = delete(node.left, value);
        }
        // search for node in right subtree
        else if (value.compareTo(node.value) > 0) {
            node.right = delete(node.right, value);
        }
        // node was found - now delete it
        else {
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            }
            Node minRight = findMin(node.right);
            node.value = minRight.value;
            node.right = delete(node.right, minRight.value);
        }
        return balance(updateHeight(node));
    }

    private Node findMin(Node node) {
        // navigate to minimum node
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public boolean contains(T value) {
        // call recursive contains function
        return contains(root, value);
    }

    private boolean contains(Node node, T value) {
        // check if next node exists
        if (node == null) return false;
        // check if in left subtree
        if (value.compareTo(node.value) < 0) return contains(node.left, value);
        // check if in right subtree
        if (value.compareTo(node.value) > 0) return contains(node.right, value);
        // otherwise, node was found
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
        // check if next node exists
        if (node == null) return;

        // traverse left subtree
        inorderTraversal(node.left, result);
        result.add(node.value);
        // traverse right subtree
        inorderTraversal(node.right, result);
    }

    @Override
    public String type() {
        return "AVL";
    }

    @Override
    public Color color() {
        return Color.GREEN;
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }
}
