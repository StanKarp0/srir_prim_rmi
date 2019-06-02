package utils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class AdjMatrix implements Serializable {

    //                  FROM  ->   TO       : WAGE
    private final Map<Integer, Map<Integer, Integer>> wages;

    private final Set<Integer> done;

    //                  TO      (FROM,TO,WAGE)
    private final Map<Integer, PrimResult> dtable;

    public AdjMatrix(Map<Integer, Map<Integer, Integer>> wages) {
        this.wages = wages;
        this.done = new HashSet<>();
        this.dtable = new HashMap<>();
    }

    public void addDoneNode(int nodeFrom) {
        done.add(nodeFrom);
        dtable.remove(nodeFrom);
        for (Map.Entry<Integer, Integer> entry : wages.getOrDefault(nodeFrom, new HashMap<>()).entrySet()) {
            int nodeTo = entry.getKey(), wage = entry.getValue();
            if (!done.contains(nodeTo)) {
                if (!(dtable.containsKey(nodeTo) && wage > dtable.get(nodeTo).getWage())) {
                    dtable.put(nodeTo, new PrimResult(nodeFrom, nodeTo, wage));
                }
            }
        }
    }

    public PrimResult calculateMinimum() {
        int minWage = Integer.MAX_VALUE;
        PrimResult result = null;
        for (Map.Entry<Integer, PrimResult> entry : dtable.entrySet()) {
            if (entry.getValue().getWage() < minWage) {
                result = entry.getValue();
            }
        }
        return result;
    }

    public int getNodes() {
        return wages.size();
    }

    @Override
    public String toString() {
        return "AdjMatrix{" +
                "wages=" + wages +
                ", done=" + done +
                ", dtable=" + dtable +
                '}';
    }

    public static List<AdjMatrix> fromStringList(List<String[]> list, int size) {

        List<Map<Integer, Map<Integer, Integer>>> maps = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            maps.add(new HashMap<>());
        }

        String[] header = list.remove(0);

        int nodes = Integer.parseInt(header[0].substring(6));
        Integer[][] wages = new Integer[nodes][];
        for (int row = 0; row < nodes; row++) {
            wages[row] = new Integer[nodes];
        }

        list.forEach(row -> wages[Integer.parseInt(row[0])][Integer.parseInt(row[1])] = Integer.parseInt(row[2]));

        for (int row = 0; row < nodes; row++) {
            Map<Integer, Integer> wagesMap = new HashMap<>();
            for (int col = 0; col < nodes; col++) {
                if (wages[row][col] != null) {
                    wagesMap.put(col, wages[row][col]);
                }
            }
            maps.get(row % size).put(row, wagesMap);
        }

        return maps.stream().map(AdjMatrix::new).collect(Collectors.toList());
    }
}
