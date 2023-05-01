package dao;


import jakarta.persistence.*;


public abstract class AbstractJpaDao<T> {

    private final Class<T> entityClass;
    protected EntityManager entityManager;

    public AbstractJpaDao(Class<T> entityClass) {
        this.entityClass = entityClass;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void create(final T entity) {

        entityManager.persist(entity);
        entityManager.flush();
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
