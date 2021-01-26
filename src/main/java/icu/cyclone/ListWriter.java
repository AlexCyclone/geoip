package icu.cyclone;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Aleksey Babanin
 * @since 2021/01/26
 */
public class ListWriter {

    public static void writeData(List<Map<String, String>> data, String resultFilePath) throws IOException {
        String[] header = getHeader(data);
        List<String[]> csvData = getCSVData(data, header);
        csvData.add(0, header);
        try (CSVWriter writer = new CSVWriter(new FileWriter(resultFilePath))) {
            writer.writeAll(csvData);
        }
    }

    private static String[] getHeader(List<Map<String, String>> data) {
        return data
                .stream()
                .flatMap(m -> m.keySet().stream())
                .distinct()
                .toArray(String[]::new);
    }

    private static List<String[]> getCSVData(List<Map<String, String>> data, String[] header) {
        return data.stream()
                .map(m -> convertMapToArray(m, header))
                .collect(Collectors.toList());
    }

    private static String[] convertMapToArray(Map<String, String> dataMap, String[] header) {
        String[] resultArray = new String[header.length];
        for (int i = 0; i < header.length; i++) {
            resultArray[i] = dataMap.get(header[i]);
        }
        return resultArray;
    }
}
