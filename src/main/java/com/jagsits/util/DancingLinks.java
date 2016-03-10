package com.jagsits.util;

import java.util.ArrayList;
import java.util.List;

public final class DancingLinks<ColumnName> {

    /**
     * A cell in the table with up/down and left/right links that form doubly
     * linked lists in both directions. It also includes a link to the column
     * head.
     */
    private static class Node<ColumnName> {
        Node<ColumnName> left;
        Node<ColumnName> right;
        Node<ColumnName> up;
        Node<ColumnName> down;
        ColumnHeader<ColumnName> head;

        Node(Node<ColumnName> l, Node<ColumnName> r, Node<ColumnName> u,
             Node<ColumnName> d, ColumnHeader<ColumnName> h) {
            left = l;
            right = r;
            up = u;
            down = d;
            head = h;
        }

        Node() {
            this(null, null, null, null, null);
        }
    }

    /**
     * Column headers record the name of the column and the number of rows that
     * satisfy this column. The names are provided by the application and can
     * be anything. The size is used for the heuristic for picking the next
     * column to explore.
     */
    private static class ColumnHeader<ColumnName> extends Node<ColumnName> {
        ColumnName name;
        int size;

        ColumnHeader(ColumnName n, int s) {
            name = n;
            size = s;
            head = this;
        }
    }

    /**
     * The head of the table. Left/Right from the head are the unsatisfied
     * ColumnHeader objects.
     */
    private ColumnHeader<ColumnName> head;

    /**
     * The complete list of columns.
     */
    private List<ColumnHeader<ColumnName>> columns;

    public DancingLinks() {
        head = new ColumnHeader<>(null, 0);
        head.left = head;
        head.right = head;
        head.up = head;
        head.down = head;
        columns = new ArrayList<>(200);
    }

    /**
     * Add a column to the table
     *
     * @param name    The name of the column, which will be returned as part of
     *                solutions
     * @param primary Is the column required for a solution?
     */
    public void addColumn(ColumnName name, boolean primary) {
        ColumnHeader<ColumnName> top = new ColumnHeader<>(name, 0);
        top.up = top;
        top.down = top;
        if (primary) {
            Node<ColumnName> tail = head.left;
            tail.right = top;
            top.left = tail;
            top.right = head;
            head.left = top;
        } else {
            top.left = top;
            top.right = top;
        }
        columns.add(top);
    }

    /**
     * Add a column to the table
     *
     * @param name The name of the column, which will be included in the solution
     */
    public void addColumn(ColumnName name) {
        addColumn(name, true);
    }

    /**
     * Add a row to the table.
     *
     * @param values the columns that are satisfied by this row
     */
    public void addRow(boolean[] values) {
        Node<ColumnName> prev = null;
        for (int i = 0; i < values.length; ++i) {
            if (values[i]) {
                ColumnHeader<ColumnName> top = columns.get(i);
                top.size += 1;
                Node<ColumnName> bottom = top.up;
                Node<ColumnName> node = new Node<>(null, null, bottom,
                        top, top);
                bottom.down = node;
                top.up = node;
                if (prev != null) {
                    Node<ColumnName> front = prev.right;
                    node.left = prev;
                    node.right = front;
                    prev.right = node;
                    front.left = node;
                } else {
                    node.left = node;
                    node.right = node;
                }
                prev = node;
            }
        }
    }

    /**
     * Applications should implement this to receive the solutions to their
     * problems.
     */
    public interface SolutionAcceptor<ColumnName> {
        /**
         * A callback to return a solution to the application.
         *
         * @param value a List of List of ColumnNames that were satisfied by each
         *              selected row
         */
        void solution(List<List<ColumnName>> value);
    }

    /**
     * Find the column with the fewest choices.
     *
     * @return The column header
     */
    private ColumnHeader<ColumnName> findBestColumn() {
        int lowSize = Integer.MAX_VALUE;
        ColumnHeader<ColumnName> result = null;
        ColumnHeader<ColumnName> current = (ColumnHeader<ColumnName>) head.right;
        while (current != head) {
            if (current.size < lowSize) {
                lowSize = current.size;
                result = current;
            }
            current = (ColumnHeader<ColumnName>) current.right;
        }
        return result;
    }

    /**
     * Hide a column in the table
     *
     * @param col the column to hide
     */
    private void coverColumn(ColumnHeader<ColumnName> col) {
        // remove the column
        col.right.left = col.left;
        col.left.right = col.right;
        Node<ColumnName> row = col.down;
        while (row != col) {
            Node<ColumnName> node = row.right;
            while (node != row) {
                node.down.up = node.up;
                node.up.down = node.down;
                node.head.size -= 1;
                node = node.right;
            }
            row = row.down;
        }
    }

    /**
     * Uncover a column that was hidden.
     *
     * @param col the column to unhide
     */
    private void uncoverColumn(ColumnHeader<ColumnName> col) {
        Node<ColumnName> row = col.up;
        while (row != col) {
            Node<ColumnName> node = row.left;
            while (node != row) {
                node.head.size += 1;
                node.down.up = node;
                node.up.down = node;
                node = node.left;
            }
            row = row.up;
        }
        col.right.left = col;
        col.left.right = col;
    }

    /**
     * Get the name of a row by getting the list of column names that it
     * satisfies.
     *
     * @param row the row to make a name for
     * @return the list of column names
     */
    private List<ColumnName> getRowName(Node<ColumnName> row) {
        List<ColumnName> result = new ArrayList<>();
        result.add(row.head.name);
        Node<ColumnName> node = row.right;
        while (node != row) {
            result.add(node.head.name);
            node = node.right;
        }
        return result;
    }

    /**
     * Find a solution to the problem.
     *
     * @param partial a temporary datastructure to keep the current partial answer in
     * @param output  the acceptor for the results that are found
     * @return the number of solutions found
     */
    private int search(List<Node<ColumnName>> partial, SolutionAcceptor<ColumnName> output) {
        int results = 0;
        if (head.right == head) {
            List<List<ColumnName>> result = new ArrayList<>(partial.size());
            for (Node<ColumnName> row : partial) {
                result.add(getRowName(row));
            }
            output.solution(result);
            results += 1;
        } else {
            ColumnHeader<ColumnName> col = findBestColumn();
            if (col.size > 0) {
                coverColumn(col);
                Node<ColumnName> row = col.down;
                while (row != col) {
                    partial.add(row);
                    Node<ColumnName> node = row.right;
                    while (node != row) {
                        coverColumn(node.head);
                        node = node.right;
                    }
                    results += search(partial, output);
                    partial.remove(partial.size() - 1);
                    node = row.left;
                    while (node != row) {
                        uncoverColumn(node.head);
                        node = node.left;
                    }
                    row = row.down;
                }
                uncoverColumn(col);
            }
        }
        return results;
    }

    /**
     * Solve a complete problem
     *
     * @param output the acceptor to receive answers
     * @return the number of solutions
     */
    public int solve(SolutionAcceptor<ColumnName> output) {
        return search(new ArrayList<>(), output);
    }

}
