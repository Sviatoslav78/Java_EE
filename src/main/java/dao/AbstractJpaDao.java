package dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

public abstract class AbstractJpaDao<T> {

    private final Class<T> entityClass;
    @PersistenceContext
    protected EntityManager entityManager;

    public AbstractJpaDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(final T entity) {

        entityManager.persist(entity);
    }

    public void save(final T entity) {

        entityManager.persist(entity);
    }

    public void update(final T entity) {

        entityManager.merge(entity);
    }

    public void delete(final T entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    public T findById(final Object id) {
        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }


}
