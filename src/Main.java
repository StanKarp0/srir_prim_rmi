import utils.AdjMatrix;
import utils.CsvReader;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            CsvReader reader = new CsvReader("/home/wojciech/Dev/java/srir_prim_rmi/resources/nodes10edges20.csv", ",");
            List<AdjMatrix> matrices = AdjMatrix.fromStringList(reader.readLines(), 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
