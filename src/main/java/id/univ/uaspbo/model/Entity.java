package id.univ.uaspbo.model;

/**
 * Base class for all entities in the system.
 * Provides common id field and methods.
 */
public abstract class Entity {
    protected String id;

    public Entity() {}

    public Entity(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
