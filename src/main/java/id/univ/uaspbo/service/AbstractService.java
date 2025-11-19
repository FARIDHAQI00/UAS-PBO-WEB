package id.univ.uaspbo.service;

import id.univ.uaspbo.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base class providing common CRUD operations for services.
 * @param <T> The entity type
 */
public abstract class AbstractService<T> implements CrudService<T> {
    protected FileRepository<T> repo;
    protected String dataPath;

    /**
     * Get the repository path for this service.
     * @return The data file path
     */
    protected abstract String getDataPath();

    /**
     * Get the class type for the repository.
     * @return The class array type
     */
    protected abstract Class<T[]> getTypeClass();

    @PostConstruct
    private void init() {
        this.dataPath = getDataPath();
        this.repo = new FileRepository<>(dataPath, getTypeClass());
    }

    @Override
    public List<T> getAll() {
        return repo.readAll();
    }

    @Override
    public T findById(String id) {
        return repo.readAll().stream()
                .filter(entity -> getEntityId(entity).equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void add(T entity) {
        List<T> all = repo.readAll();
        setEntityId(entity, UUID.randomUUID().toString());
        all.add(entity);
        repo.saveAll(all);
    }

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

    @Override
    public void delete(String id) {
        List<T> all = repo.readAll();
        all.removeIf(entity -> getEntityId(entity).equals(id));
        repo.saveAll(all);
    }

    @Override
    public void saveAll(List<T> entities) {
        repo.saveAll(entities);
    }

    /**
     * Get the ID of an entity (to be implemented by subclasses).
     * @param entity The entity
     * @return The entity ID
     */
    protected abstract String getEntityId(T entity);

    /**
     * Set the ID of an entity (to be implemented by subclasses).
     * @param entity The entity
     * @param id The ID to set
     */
    protected abstract void setEntityId(T entity, String id);
}
