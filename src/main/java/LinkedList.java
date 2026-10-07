import java.util.NoSuchElementException;
import java.util.Objects;

// ? question mark is simple if-else statement

public class LinkedList<T> {

    private static class Node<T> {
        private T data;
        private Node<T> next;
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(T value) {
        Node<T> newNode = new Node<>();

        if (head == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public void addFirst(T value) {
        Node<T> newNode = new Node<>();

        newNode.next = head;
        head = newNode;

        if (tail == null) {
            tail = newNode;
        }
        size++;
    }

    public T get(int index) {
        checkIndex(index);

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }

        T removedValue = head.data;
        head = head.next;
        size--;

        if (head == null) {
            tail = null;
        }
        return removedValue;
    }

    public T remove(int index) {
        checkIndex(index);

        if (index == 0) {
            return removeFirst();
        }

        Node<T> previous = head;

        for (int i = 0; i < index - 1; i++) {
            previous = previous.next;
        }

        Node<T> nodeToRemove = previous.next;
        previous.next = nodeToRemove.next;

        if (nodeToRemove == tail) {
            tail = previous;
        }

        size--;
        return nodeToRemove.data;
    }

    public boolean contains(T value) {
        Node<T> current = head;

        while (current != null) {
            if (Objects.equals(value, current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }
    }

}
