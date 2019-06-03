package client;

import utils.AdjacencyList;
import utils.CsvReader;
import utils.PrimEdge;
import utils.Common;

import java.rmi.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class PrimClientComponent {

    private static final int startNode = 0;

    public static void main(String[] args) {
        try {
            final String pathToFile = args[0];
            final int servers = Integer.parseInt(args[1]);

            // Read and parse graph
            CsvReader reader = new CsvReader(pathToFile, ",");
            List<AdjacencyList> lists = AdjacencyList.fromStringList(reader.readLines(), servers);

            // Construct list of pairs - (list, remote)
            List<PrimPair> primPairs = new LinkedList<>();
            for (int remote = 0; remote < servers; remote++) {
                AdjacencyList list = lists.get(remote);
                //We obtain a reference to the object from the registry and next,
                PrimRemote primRemote = (PrimRemote) Naming.lookup(Common.getServerPath(remote));
                // Create pairs (list, remote)
                primPairs.add(new PrimPair(list, primRemote));
            }

            // Print results
            System.out.println("from,to,wage");

            // Execute Prim's Algorithm
            primsAlgorithm(primPairs).forEach(e -> System.out.println(e.toCsvRow()));

        } catch (ConnectException conEx) {
            System.out.println("Unable to connect to server!");
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static List<PrimEdge> primsAlgorithm(List<PrimPair> primPairs) {

        // Thread executor
        ExecutorService executor = Executors.newFixedThreadPool(primPairs.size());

        final int nodesNumber = primPairs.stream().mapToInt(p -> p.getAdjacencyList().getNodes()).sum();
        final List<PrimEdge> resultList = new LinkedList<>();
        int newNode = startNode;

        // Iterate over edges
        for (int edge = 0; edge < nodesNumber - 1; edge++) {

            final int localNewNode = newNode;

            // Execute methods using threads and RMI
            Stream<Future<PrimEdge>> results = primPairs.stream().map(pair -> executor.submit(() -> {
                AdjacencyList list = pair.getAdjacencyList();

                // Update d table
                list = pair.getRemote().updateDTable(localNewNode, list);

                // Calculate minimum
                PrimEdge primEdge = pair.getRemote().calculateMinimum(list);

                // Update weight list
                pair.setAdjacencyList(list);

                // Return result
                return primEdge;
            }));

            // Reduction
            Optional<PrimEdge> minWageOpt = results.map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull).min(Comparator.comparingInt(PrimEdge::getWage));

            // Updates after finding best global solution
            if (minWageOpt.isPresent()) {
                PrimEdge minWage = minWageOpt.get();
                resultList.add(minWage);
                newNode = minWage.getTo();
            } else {
                throw new RuntimeException("Matrix error - cannot find next edge");
            }

        }
        // Closing thread pool
        executor.shutdown();

        return resultList;

    }

    private static class PrimPair {

        private AdjacencyList adjacencyList;
        private final PrimRemote remote;

        PrimPair(AdjacencyList adjacencyList, PrimRemote remote) {

            this.adjacencyList = adjacencyList;
            this.remote = remote;
        }

        AdjacencyList getAdjacencyList() {
            return adjacencyList;
        }

        PrimRemote getRemote() {
            return remote;
        }

        void setAdjacencyList(AdjacencyList adjacencyList) {
            this.adjacencyList = adjacencyList;
        }
    }

}