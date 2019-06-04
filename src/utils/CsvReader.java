package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvReader {

    private final BufferedReader br;
    private final String split;

    public CsvReader(String filepath, String split) throws FileNotFoundException {
        this.br = new BufferedReader(new FileReader(filepath));
        this.split = split;
    }

    public Optional<String[]> readLine() throws IOException {
        return Optional.ofNullable(br.readLine()).map(line -> line.split(split));
    }

    public List<String[]> readLines() throws IOException {
        boolean end = false;
        List<String[]> list = new ArrayList<>();
        while (!end) {
            end = readLine().map(s -> {
                if (s.length > 1) {
                    list.add(s);
                    return false;
                } else {
                    return true;
                }
            }).orElse(true);
        }
        return list;
    }

}
