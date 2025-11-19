package id.univ.uaspbo.service;

import java.util.List;

/**
 * Interface defining basic CRUD operations for entities.
 * @param <T> The entity type
 */
public interface CrudService<T> {
    /**
     * Get all entities.
     * @return List of all entities
     */
    List<T> getAll();

    /**
     * Find entity by ID.
     * @param id The entity ID
     * @return The entity if found, null otherwise
     */
    T findById(String id);

    /**
     * Add a new entity.
     * @param entity The entity to add
     */
    void add(T entity);

    /**
     * Update an existing entity.
     * @param entity The entity to update
     */
    void update(T entity);

    /**
     * Delete entity by ID.
     * @param id The entity ID to delete
     */
    void delete(String id);

    /**
     * Save all entities (for persistence).
     * @param entities List of entities to save
     */
    void saveAll(List<T> entities);
}
