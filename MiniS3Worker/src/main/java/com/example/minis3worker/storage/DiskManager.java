package com.example.minis3worker.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@Component
public class DiskManager {

    private final Path root;

    public DiskManager(@Value("${storage.root:data}") String rootPath) throws IOException {
        this.root = Paths.get(rootPath).toAbsolutePath().normalize();

        Files.createDirectories(this.root);
    }

    // Upload or Overwrite if exists
    public void save(String key, InputStream inputstream) throws IOException {
        Path filePath = root.resolve(key).normalize();

        try (OutputStream out = Files.newOutputStream(filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            inputstream.transferTo(out);
        }
    }

    // Read
    public InputStream read(String key) throws IOException {
        Path filePath = root.resolve(key).normalize();

        if (!Files.exists(filePath)) {
            throw new NoSuchFileException(key);
        }

        return Files.newInputStream(filePath);
    }

    // Delete
    public void delete(String key) throws IOException {
        Path filePath = root.resolve(key).normalize();
        Files.deleteIfExists(filePath);
    }

    // List all files
    public List<String> list() throws IOException {
        try (Stream<Path> stream = Files.list(root)) {
            return stream.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    // disk space
    public long getAvailableSpace() throws IOException {
        return Files.getFileStore(root).getUsableSpace();
    }
}
