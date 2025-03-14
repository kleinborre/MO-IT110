package classes;

import java.util.List;
import java.util.Map;

public interface CSVHandler {
    Map<String, String[]> readCSV(String filePath);
    void writeCSV(String filePath, List<String[]> data);
}
