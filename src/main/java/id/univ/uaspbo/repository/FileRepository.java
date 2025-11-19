package id.univ.uaspbo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generic repository class for handling JSON file-based data persistence.
 * Provides basic read and write operations for entities stored in JSON files.
 *
 * @param <T> The type of entity to be stored/retrieved
 */
public class FileRepository<T> {
    private final File file;
    private final Class<T[]> type;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor for FileRepository.
     *
     * @param path The file path for data storage
     * @param type The array class type for JSON deserialization
     */
    public FileRepository(String path, Class<T[]> type) {
        this.file = new File(path);
        this.type = type;
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Reads all entities from the JSON file.
     *
     * @return List of all entities, empty list if file doesn't exist or error occurs
     */
    public List<T> readAll() {
        try {
            if (!file.exists()) return new ArrayList<>();
            T[] arr = mapper.readValue(file, type);
            return new ArrayList<>(Arrays.asList(arr));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Saves all entities to the JSON file.
     *
     * @param list The list of entities to save
     */
    public void saveAll(List<T> list) {
        try {
            // ensure parent exists
            File p = file.getAbsoluteFile().getParentFile();
            if (p != null && !p.exists()) p.mkdirs();
            mapper.writeValue(file, list.toArray((T[]) java.lang.reflect.Array.newInstance(type.getComponentType(), list.size())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
