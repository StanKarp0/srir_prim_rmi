package client;

import utils.AdjMatrix;
import utils.CsvReader;
import utils.PrimEdge;
import utils.Common;

import java.rmi.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PrimClientComponent {

    private static final int startNode = 0;

    public static void main(String[] args) {
        try {
            final String pathToFile = args[0];
            final int servers = Integer.parseInt(args[1]);

            // Read and parse graph
            CsvReader reader = new CsvReader(pathToFile, ",");
            List<AdjMatrix> matrices = AdjMatrix.fromStringList(reader.readLines(), servers);

            // Construct list of pairs - (matrix, remote)
            List<PrimPair> primPairs = new LinkedList<>();
            for (int remote = 0; remote < servers; remote++) {
                AdjMatrix matrix = matrices.get(remote);
                //We obtain a reference to the object from the registry and next,
                //it will be typecasted into the most appropiate type.
                PrimRemote primRemote = (PrimRemote) Naming.lookup(Common.getServerPath(remote));
                primPairs.add(new PrimPair(matrix, primRemote));
            }

            // Print results
            System.out.println("from,to,wage");
            primsAlgorithm(primPairs).forEach(e -> System.out.println(e.toCsvRow()));


        } catch (ConnectException conEx) {
            System.out.println("Unable to connect to server!");
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static List<PrimEdge> primsAlgorithm(List<PrimPair> primPairs) throws RemoteException {

        final int nodesNumber = primPairs.stream().mapToInt(p -> p.getAdjMatrix().getNodes()).sum();
        final List<PrimEdge> resultList = new LinkedList<>();

        int newNode = startNode;
        for (int edge = 0; edge < nodesNumber - 1; edge++) {

            List<PrimEdge> results = new LinkedList<>();
            for (PrimPair pair : primPairs) {
                AdjMatrix matrix = pair.getAdjMatrix();
                // Update d table
                matrix = pair.getRemote().updateDTable(newNode, matrix);
                // Calculate minimum
                PrimEdge primEdge = pair.getRemote().calculateMinimum(matrix);

                pair.setAdjMatrix(matrix);
                if (primEdge != null) {
                    results.add(primEdge);
                }
            }

            Optional<PrimEdge> minWageOpt = results.stream().min(Comparator.comparingInt(PrimEdge::getWage));
            if (minWageOpt.isPresent()) {
                PrimEdge minWage = minWageOpt.get();
                resultList.add(minWage);
                newNode = minWage.getTo();

            } else {
                throw new RuntimeException("Matrix error - cannot find next edge");
            }

        }

        return resultList;

    }

    private static class PrimPair {

        private AdjMatrix adjMatrix;
        private final PrimRemote remote;

        PrimPair(AdjMatrix adjMatrix, PrimRemote remote) {

            this.adjMatrix = adjMatrix;
            this.remote = remote;
        }

        AdjMatrix getAdjMatrix() {
            return adjMatrix;
        }

        PrimRemote getRemote() {
            return remote;
        }

        void setAdjMatrix(AdjMatrix adjMatrix) {
            this.adjMatrix = adjMatrix;
        }
    }

}