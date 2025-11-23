package id.univ.uaspbo.model;

/**
 * Kelas dasar abstrak untuk semua entitas dalam sistem.
 * Berfungsi sebagai superclass yang menyediakan atribut dan metode
 * umum seperti identifier unik (id) yang digunakan oleh semua entitas.
 * 
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Abstraksi: Kelas ini adalah kelas abstrak yang tidak bisa diinstansiasi secara langsung,
 *   dan harus diwariskan oleh kelas lainnya.
 * - Enkapsulasi: Atribut id memiliki akses protected dan diakses melalui getter dan setter.
 * 
 * Kelas ini menyederhanakan pengelolaan identifier unik pada semua entitas di sistem,
 * sehingga kelas-kelas turunan dapat fokus pada atribut dan perilaku khusus mereka saja.
 */

public abstract class Entity {
    /**
     * Identifier unik yang membedakan setiap entitas.
     * Biasanya digunakan sebagai primary key pada database.
     */
    protected String id;           
    
    /**
     * Konstruktor default tanpa parameter.
     * Membuat objek entitas dengan id belum diatur.
     */
    public Entity() {}

    /**
     * Konstruktor dengan parameter id.
     * Menginisialisasi entitas dengan identifier unik yang diberikan.
     */
    public Entity(String id) {
        this.id = id;
    }

    /**
     * Mengambil nilai identifier entitas.
     */
    public String getId() { return id; }

    /**
     * Mengatur nilai identifier entitas.
     */
    public void setId(String id) { this.id = id; }
}
