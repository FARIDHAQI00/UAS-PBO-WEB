package id.univ.uaspbo.service;

import id.univ.uaspbo.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

/**
 * Kelas abstrak dasar yang menyediakan implementasi operasi CRUD (Create, Read, Update, Delete)
 * yang umum digunakan oleh berbagai service di aplikasi ini.
 * Kelas ini mengelola penyimpanan data melalui repository berbasis file JSON sebagai sumber data.
 *
 * AbstractService bertindak sebagai superclass yang dapat diturunkan oleh service spesifik
 * untuk menghindari pengulangan kode CRUD dasar dan mempermudah pemeliharaan.
 *
 * @param <T> Tipe entitas yang dikelola oleh service ini.
 *
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Abstraksi: Mendefinisikan method abstrak untuk operasi CRUD yang harus diimplementasikan oleh subclass.
 * - Inheritance (Pewarisan): Memungkinkan subclass mewarisi implementasi dasar CRUD.
 * - Polymorphism (Polimorfisme): Mendukung method overriding oleh subclass untuk implementasi spesifik.
 */
public abstract class AbstractService<T> implements CrudService<T> {
    protected FileRepository<T> repo;  // Repository penyimpanan data berbasis file
    protected String dataPath;          // Path penyimpanan data


    //Abstraksi: Mendefinisikan method abstrak untuk operasi CRUD yang harus diimplementasikan oleh subclass.
    /**
     * Mendapatkan path file repository untuk service ini.
     */
    protected abstract String getDataPath();

    /**
     * Mendapatkan tipe kelas array entitas untuk keperluan deserialisasi JSON.
     */
    protected abstract Class<T[]> getTypeClass();

    /**
     * Inisialisasi repository setelah konstruktor dipanggil.
     * Dilakukan secara otomatis lewat anotasi @PostConstruct.
     */
    @PostConstruct
    private void init() {
        this.dataPath = getDataPath();
        this.repo = new FileRepository<>(dataPath, getTypeClass());
    }

    /**
     * Mendapatkan semua entitas dari repository.
     */
    @Override
    public List<T> getAll() {
        return repo.readAll();
    }

    /**
     * Mencari entitas berdasarkan ID.
     */
    @Override
    public T findById(String id) {
        return repo.readAll().stream()
                .filter(entity -> getEntityId(entity).equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Menambahkan entitas baru ke repository.
     * ID entitas akan di-generate secara otomatis.
     */
    @Override
    public void add(T entity) {
        List<T> all = repo.readAll();
        setEntityId(entity, UUID.randomUUID().toString());
        all.add(entity);
        repo.saveAll(all);
    }

    /**
     * Memperbarui entitas yang sudah ada berdasarkan ID.
     */
    @Override
    public void update(T entity) {
        List<T> all = repo.readAll();
        String id = getEntityId(entity);
        for (int i = 0; i < all.size(); i++) {
            if (getEntityId(all.get(i)).equals(id)) {
                all.set(i, entity);
                break;
            }
        }
        repo.saveAll(all);
    }

    /**
     * Menghapus entitas berdasarkan ID.
     */
    @Override
    public void delete(String id) {
        List<T> all = repo.readAll();
        all.removeIf(entity -> getEntityId(entity).equals(id));
        repo.saveAll(all);
    }

    /**
     * Menyimpan semua entitas yang diberikan ke repository.
     */
    @Override
    public void saveAll(List<T> entities) {
        repo.saveAll(entities);
    }

    /**
     * Mendapatkan ID entitas (diimplementasikan oleh kelas turunan).
     */
    protected abstract String getEntityId(T entity);

    /**
     * Mengatur ID entitas (diimplementasikan oleh kelas turunan).
     *
     * @param entity Entitas
     * @param id ID yang akan diset pada entitas
     */
    protected abstract void setEntityId(T entity, String id);
}
