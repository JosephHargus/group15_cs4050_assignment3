package org.example.Trees;
//Author: Kaitlyn Self

import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  This class serves to implement the Minimum Heap requirements.
 *  @param <T>
 *  @author Athlie54
 */
public class MinHeap<T extends Comparable<T>> extends Heap<T> implements Serializable {
    protected ArrayList<T> heap;

    public MinHeap() {
        heap = new ArrayList<>();
    }

    @Override
    public void insert(T value) {
        heap.add(value);
        heapifyUp(heap.size() - 1);
    }

    @Override
    public boolean delete(T value) {
        int index = heap.indexOf(value);
        if (index == -1) return false;

        int lastIndex = heap.size() - 1;
        swap(index, lastIndex);
        heap.remove(lastIndex);

        if (index < heap.size()) {
            heapifyDown(index);
        }

        return true;
    }

    @Override
    public boolean contains(T value) {
        return heap.contains(value);
    }

    @Override
    public void clear() {
        heap.clear();
    }

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public List<T> inorderTraversal() {
        return new ArrayList<>(heap);
    }

    @Override
    public String type() {
        return "Minimum Heap";
    }

    //returns the color for the visualization
    @Override
    public Color color() {
        return Color.BLUE;
    }

    //returns first element in ArrayList, which is also the root node
    @Override
    public TreeNode<T> getRoot() {
        return heap.isEmpty() ? null : new MinHeap.HeapNode(0);
    }

    /**
     *  A recursive function which inserts a new element by comparing
     *  the new element to its parent and adjusting accordingly.
     *
     *  @param index The index of the node being sorted
     *  @author Athlie54
     */
    protected void heapifyUp(int index) {
        int parent = getParentIndex(index);
        if (heap.get(index).compareTo(heap.get(parent)) < 0) {
            swap(index, parent);
            heapifyUp(parent);
        }
    }

    /**
     *  An iterative function which removes an element by comparing
     *  the element to its children and determining how to fill the
     *  now-empty space.
     *
     *  @param index The index of the node being moved
     *  @author Athlie54
     */
    protected void heapifyDown(int index) {
        T left = heap.get(getLeftChildIndex(index));
        T right = heap.get(getRightChildIndex(index));
        if(left.compareTo(right)>0) {
            swap(index, heap.indexOf(right));
        } else {swap(index, heap.indexOf(left));}
    }

    protected void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    protected int getParentIndex(int i) {
        return (i - 1) / 2;
    }

    protected int getLeftChildIndex(int i) {
        return 2 * i + 1;
    }

    protected int getRightChildIndex(int i) {
        return 2 * i + 2;
    }

    protected class HeapNode implements TreeNode<T> {
        private int index;

        HeapNode(int index) {
            this.index = index;
        }

        @Override
        public T getValue() {
            return heap.get(index);
        }

        @Override
        public TreeNode<T> getLeft() {
            int leftIndex = getLeftChildIndex(index);
            return leftIndex < heap.size() ? new MinHeap.HeapNode(leftIndex) : null;
        }

        @Override
        public TreeNode<T> getRight() {
            int rightIndex = getRightChildIndex(index);
            return rightIndex < heap.size() ? new MinHeap.HeapNode(rightIndex) : null;
        }

        @Override
        public String getColor() {
            return color().toString();
        }


    }
}
