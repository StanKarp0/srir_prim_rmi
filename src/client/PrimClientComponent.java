package client;

import utils.AdjMatrix;
import utils.CsvReader;
import utils.PrimResult;
import utils.Tools;

import java.rmi.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PrimClientComponent {

    private static final int startNode = 0;

    public static void main(String[] args) {
        try {
            final int servers = Integer.parseInt(args[0]);

            CsvReader reader = new CsvReader("../resources/nodes6edges12.csv", ",");
            List<AdjMatrix> matrices = AdjMatrix.fromStringList(reader.readLines(), servers);
            List<PrimResult> resultList = new LinkedList<>();

            //We obtain a reference to the object from the registry and next,
            //it will be typecasted into the most appropiate type.
            int nodesNumber = 0;
            List<PrimPair> primRemotes = new LinkedList<>();
            for (int remote = 0; remote < servers; remote++) {
                AdjMatrix matrix = matrices.get(remote);
                PrimRemote primRemote = (PrimRemote) Naming.lookup(Tools.getServerPath(remote));
                primRemotes.add(new PrimPair(matrix, primRemote));
                nodesNumber += matrix.getNodes();
            }

            //Next, we will use the above reference to invoke the remote
            //object method.
            // Init d table
            int newNode = startNode;
            for (int edge = 0; edge < 1/*nodesNumber - 1*/; edge++) {

                List<PrimResult> results = new LinkedList<>();
                for (PrimPair pair : primRemotes) {
                    // Update d table
                    AdjMatrix matrix = pair.getAdjMatrix();
                    System.out.println("pre: " + matrix);

                    matrix = pair.getRemote().updateDTable(newNode, matrix);
                    PrimResult primResult = pair.getRemote().calculateMinimum(matrix);
                    pair.setAdjMatrix(matrix);

                    System.out.println("post: " + matrix);
                    System.out.println("res : " + primResult);


                    if (primResult != null) {
                        results.add(primResult);
                    }
                }

                Optional<PrimResult> minWageOpt = results.stream().min(Comparator.comparingInt(PrimResult::getWage));
                if (minWageOpt.isPresent()) {
                    PrimResult minWage = minWageOpt.get();
                    resultList.add(minWage);

                } else {
                    throw new RuntimeException("Matrix error - cannot find next edge");
                }

            }



        } catch (ConnectException conEx) {
            System.out.println("Unable to connect to server!");
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
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

        public void setAdjMatrix(AdjMatrix adjMatrix) {
            this.adjMatrix = adjMatrix;
        }
    }

}