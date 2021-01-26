package icu.cyclone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Aleksey Babanin
 * @since 2021/01/26
 */
public class AddressReader {

    public static List<String> getAddressList(String pathString) {
        Path path = Paths.get(pathString);
        if (!path.toFile().isFile()) {
            throw new IllegalArgumentException("Incorrect address list file");
        }

        try {
            return Files.readAllLines(path)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Incorrect address list file");
        }
    }
}
