package testingFunctional.crud.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;


public abstract class AbstractJpaDao<T> {

    private final Class<T> entityClass;
    protected EntityManager entityManager;

    public AbstractJpaDao(Class<T> entityClass) {
        this.entityClass = entityClass;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void create(final T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.flush();
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            entityManager.getTransaction().rollback();
            entityManager.clear();
        }

    }

    public void save(final T entity) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.flush();
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            entityManager.getTransaction().rollback();
            entityManager.clear();
        }
    }

    public void update(final T entity) {

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.flush();
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            entityManager.getTransaction().rollback();
            entityManager.clear();
        }
    }

    public void delete(final T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.merge(entity));
            entityManager.flush();
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            entityManager.getTransaction().rollback();
            entityManager.clear();
        }
    }

    public T findById(final Object id) {
        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }


}
