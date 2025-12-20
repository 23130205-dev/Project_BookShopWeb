package com.bookshopweb.service;

import java.util.List;
import java.util.Optional;

import com.bookshopweb.dao.DAO;

public abstract class Service<T, D extends DAO<T>> implements DAO<T> {
    protected final D dao;

    // Các lớp con truyền vào DAO JDBC thuần
    public Service(D dao) {
        this.dao = dao;
    }

    @Override
    public long insert(T t) {
        return dao.insert(t);
    }

    @Override
    public void update(T t) {
        dao.update(t);
    }

    @Override
    public void delete(long id) {
        dao.delete(id);
    }

    @Override
    public Optional<T> getById(long id) {
        return dao.getById(id);
    }

    @Override
    public List<T> getAll() {
        return dao.getAll();
    }

    @Override
    public List<T> getPart(int limit, int offset) {
        return dao.getPart(limit, offset);
    }

    @Override
    public List<T> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return dao.getOrderedPart(limit, offset, orderBy, orderDir);
    }
}
