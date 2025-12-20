package com.bookshopweb.service;

import com.bookshopweb.beans.ProductReview;
import com.bookshopweb.dao.ProductReviewDAO;

import java.util.List;
import java.util.Optional;

public class ProductReviewService {

    private final ProductReviewDAO productReviewDAO;

    public ProductReviewService() {
        this.productReviewDAO = new ProductReviewDAO();
    }

    public long insert(ProductReview review) {
        return productReviewDAO.insert(review);
    }

    public void update(ProductReview review) {
        productReviewDAO.update(review);
    }

    public void delete(long id) {
        productReviewDAO.delete(id);
    }

    public Optional<ProductReview> getById(long id) {
        return productReviewDAO.getById(id);
    }

    public List<ProductReview> getAll() {
        return productReviewDAO.getAll();
    }

    public List<ProductReview> getPart(int limit, int offset) {
        return productReviewDAO.getPart(limit, offset);
    }

    public List<ProductReview> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return productReviewDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    public List<ProductReview> getOrderedPartByProductId(int limit, int offset,
                                                         String orderBy, String orderDir,
                                                         long productId) {
        return productReviewDAO.getOrderedPartByProductId(limit, offset, orderBy, orderDir, productId);
    }

    public int count() {
        return productReviewDAO.count();
    }

    public int countByProductId(long productId) {
        return productReviewDAO.countByProductId(productId);
    }

    public int sumRatingScoresByProductId(long productId) {
        return productReviewDAO.sumRatingScoresByProductId(productId);
    }

    public void hide(long id) {
        productReviewDAO.hide(id);
    }

    public void show(long id) {
        productReviewDAO.show(id);
    }
}
