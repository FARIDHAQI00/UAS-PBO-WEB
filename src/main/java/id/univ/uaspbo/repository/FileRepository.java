package id.univ.uaspbo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Kelas repository generik yang menangani penyimpanan data berbasis file JSON.
 * Menyediakan operasi dasar untuk membaca dan menyimpan entitas yang disimpan dalam file JSON.
 *
 * @param <T> Tipe entitas yang akan disimpan atau diambil dari file
 *
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Generic: Menggunakan tipe parameter generik agar dapat digunakan untuk berbagai tipe entitas.
 * - Exception Handling: Menangani exception menggunakan try-catch pada operasi baca dan simpan file.
 * - Collection: Menggunakan ArrayList dan List untuk penyimpanan sementara data entitas.
 */
public class FileRepository<T> {
    private final File file;  // File penyimpanan data JSON
    private final Class<T[]> type;  // Kelas array tipe entitas untuk deserialisasi JSON
    private final ObjectMapper mapper = new ObjectMapper();  // Mapper JSON dari Jackson

    /**
     * Konstruktor untuk FileRepository.
     * @param path Path file tempat penyimpanan data
     * @param type Tipe kelas array untuk deserialisasi JSON
     */
    public FileRepository(String path, Class<T[]> type) {
        this.file = new File(path);
        this.type = type;
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Membaca semua entitas dari file JSON.
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
     * Menyimpan semua entitas ke dalam file JSON.
     *
     * @param list Daftar entitas yang akan disimpan
     */
    public void saveAll(List<T> list) {
        try {
            // memastikan direktori induk file tersedia
            File p = file.getAbsoluteFile().getParentFile();
            if (p != null && !p.exists()) p.mkdirs();
            mapper.writeValue(file, list.toArray((T[]) java.lang.reflect.Array.newInstance(type.getComponentType(), list.size())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
