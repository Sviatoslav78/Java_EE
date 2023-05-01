package dao;

import jakarta.persistence.EntityNotFoundException;

public interface JpaCrudDao<T> {

    void create(final T entity);

    void save(final T entity);

    void update(final T entity);

    void delete(final T entity);

    T findById(final Object id);
}
