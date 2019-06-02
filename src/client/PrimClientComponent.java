package client;

import utils.AdjMatrix;
import utils.CsvReader;
import utils.Tools;

import java.rmi.*;
import java.util.LinkedList;
import java.util.List;

public class PrimClientComponent {

    public static void main(String[] args) {
        try {
            final int servers = Integer.parseInt(args[0]);

            CsvReader reader = new CsvReader("../resources/nodes6edges12.csv", ",");
            List<AdjMatrix> matrices = AdjMatrix.fromStringList(reader.readLines(), servers);

            //We obtain a reference to the object from the registry and next,
            //it will be typecasted into the most appropiate type.
            List<PrimPair> primRemotes = new LinkedList<>();
            for (int remote = 0; remote < servers; remote++) {
                PrimRemote primRemote = (PrimRemote) Naming.lookup(Tools.getServerPath(remote));
                primRemotes.add(new PrimPair(matrices.get(remote), primRemote));
            }

            //Next, we will use the above reference to invoke the remote
            //object method.
            for (PrimPair pair :
                    primRemotes) {
                System.out.println(pair.getRemote().calculateMinimum(pair.getAdjMatrix()));
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

        private final AdjMatrix adjMatrix;
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
    }

}