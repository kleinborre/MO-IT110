package classes;

import java.io.File;
import java.util.List;

public interface CSVHandler {
    List<String[]> readCSV(String filePath);
    void writeCSV(String filePath, List<String[]> data);
}