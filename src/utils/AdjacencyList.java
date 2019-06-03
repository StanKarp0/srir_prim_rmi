package utils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class AdjacencyList implements Serializable {

    private final Map<Integer, Map<Integer, Integer>> wages;
    private final Set<Integer> done;
    private final Map<Integer, PrimEdge> dtable;

    private AdjacencyList(Map<Integer, Map<Integer, Integer>> wages) {
        this.wages = wages;
        this.done = new HashSet<>();
        this.dtable = new HashMap<>();
    }

    /**
     * Function updates d wages table. Function find minimal edges for new node.
     * @param nodeFrom new node index
     */
    public void addDoneNode(int nodeFrom) {
        // status change
        done.add(nodeFrom);
        dtable.remove(nodeFrom);

        // iterate over every connection from new node - looking for better connection
        // to another node. Node can not exist in this wage list.
        for (Map.Entry<Integer, Integer> entry : wages.getOrDefault(nodeFrom, new HashMap<>()).entrySet()) {
            int nodeTo = entry.getKey(), wage = entry.getValue();
            // check if node is already added to graph
            if (!done.contains(nodeTo)) {
                // check if new wage is better then old one
                if (!(dtable.containsKey(nodeTo) && wage > dtable.get(nodeTo).getWage())) {
                    // updating d table with new egde
                    dtable.put(nodeTo, new PrimEdge(nodeFrom, nodeTo, wage));
                }
            }
        }
    }

    /**
     * Function calculates best edge
     * @return simple structure describing best local edge
     */
    public PrimEdge calculateMinimum() {
        int minWage = Integer.MAX_VALUE;
        PrimEdge result = null;
        for (Map.Entry<Integer, PrimEdge> entry : dtable.entrySet()) {
            if (entry.getValue().getWage() < minWage) {
                result = entry.getValue();
                minWage = entry.getValue().getWage();
            }
        }
        return result;
    }

    /**
     *
     * @return nodes described by object
     */
    public int getNodes() {
        return wages.size();
    }

    @Override
    public String toString() {
        return "AdjacencyList{" +
                "wages=" + wages +
                ", done=" + done +
                ", dtable=" + dtable +
                '}';
    }

    /**
     * Adj object fabric
     * @param list list of rows (from,to,wage)
     * @param size number of remote servers
     * @return list of adj matrices
     */
    public static List<AdjacencyList> fromStringList(List<String[]> list, int size) {

        final List<Map<Integer, Map<Integer, Integer>>> maps = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            maps.add(new HashMap<>());
        }

        list.remove(0);

        list.forEach(row -> {
            int fromNode = Integer.parseInt(row[0]);
            int toNode = Integer.parseInt(row[1]);
            int wage = Integer.parseInt(row[2]);
            maps.get(fromNode % size).putIfAbsent(fromNode, new HashMap<>());
            maps.get(fromNode % size).get(fromNode).put(toNode, wage);
        });

        return maps.stream().map(AdjacencyList::new).collect(Collectors.toList());
    }
}
