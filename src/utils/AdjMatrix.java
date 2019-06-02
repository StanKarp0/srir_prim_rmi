package utils;

import java.util.*;
import java.util.stream.Collectors;

public class AdjMatrix {

    private final Map<Integer, Integer[]> wages;

    public AdjMatrix(Map<Integer, Integer[]> wages) {
        this.wages = wages;
    }

    public static List<AdjMatrix> fromStringList(List<String[]> list, int n) {

        List<Map<Integer, Integer[]>> maps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            maps.add(new HashMap<>());
        }

        String[] header = list.remove(0);

        int nodes = Integer.parseInt(header[0].substring(6));
        Integer[][] wages = new Integer[nodes][];
        for (int row = 0; row < nodes; row++) {
            wages[row] = new Integer[nodes];
            for (int col = 0; col < nodes; col++) {
                wages[row][col] = Integer.MAX_VALUE;
            }
        }

        list.forEach(row -> wages[Integer.parseInt(row[0])][Integer.parseInt(row[1])] = Integer.parseInt(row[2]));

        for (int row = 0; row < nodes; row++) {
            maps.get(row % n).put(row, wages[row]);
        }

        return maps.stream().map(AdjMatrix::new).collect(Collectors.toList());
    }

}
