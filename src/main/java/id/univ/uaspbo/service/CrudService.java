package id.univ.uaspbo.service;

import java.util.List;

/**
 * Interface yang mendefinisikan kontrak operasi dasar CRUD (Create, Read, Update, Delete) 
 * untuk pengelolaan berbagai entitas dalam aplikasi.
 * Interface ini menjadi dasar bagi semua service yang mengimplementasikan 
 * operasi pengelolaan data untuk tipe entitas tertentu.
 * 
 * Setiap kelas yang mengimplementasikan interface ini wajib menyediakan 
 * implementasi metode CRUD untuk tipe entitas yang ditentukan.
 *
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Abstraksi: Mendefinisikan kontrak method CRUD yang harus diimplementasikan oleh kelas yang mengimplementasikan interface ini.
 */
public interface CrudService<T> {
    /**
     * Mendapatkan semua entitas.
     *
     * @return Daftar semua entitas yang tersedia.
     */
    List<T> getAll();

    /**
     * Mencari entitas berdasarkan ID.
     *
     * @param id ID entitas yang dicari.
     * @return Entitas yang ditemukan, atau null jika tidak ada.
     */
    T findById(String id);

    /**
     * Menambahkan entitas baru.
     *
     * @param entity Entitas yang akan ditambahkan.
     */
    void add(T entity);

    /**
     * Memperbarui entitas yang sudah ada.
     *
     * @param entity Entitas yang akan diperbarui.
     */
    void update(T entity);

    /**
     * Menghapus entitas berdasarkan ID.
     *
     * @param id ID entitas yang akan dihapus.
     */
    void delete(String id);

    /**
     * Menyimpan semua entitas untuk penyimpanan permanen.
     *
     * @param entities Daftar entitas yang akan disimpan.
     */
    void saveAll(List<T> entities);
}
