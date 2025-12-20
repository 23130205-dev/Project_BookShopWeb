package com.bookshopweb.service;

import com.bookshopweb.beans.Category;
import com.bookshopweb.dao.CategoryDAO;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    /**
     * Chèn category mới
     */
    public long insert(Category category) {
        return categoryDAO.insert(category);
    }

    /**
     * Cập nhật category
     */
    public void update(Category category) {
        categoryDAO.update(category);
    }

    /**
     * Xóa category
     */
    public void delete(long id) {
        categoryDAO.delete(id);
    }

    /**
     * Lấy category theo id
     */
    public Optional<Category> getById(long id) {
        return categoryDAO.getById(id);
    }

    /**
     * Lấy tất cả category
     */
    public List<Category> getAll() {
        return categoryDAO.getAll();
    }

    /**
     * Lấy theo limit/offset
     */
    public List<Category> getPart(int limit, int offset) {
        return categoryDAO.getPart(limit, offset);
    }

    /**
     * Lấy theo limit/offset có sắp xếp
     */
    public List<Category> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return categoryDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    /**
     * Lấy category theo productId
     */
    public Optional<Category> getByProductId(long productId) {
        return categoryDAO.getByProductId(productId);
    }

    /**
     * Đếm tổng số category
     */
    public int count() {
        return categoryDAO.count();
    }
}
