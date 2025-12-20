package com.bookshopweb.service;

import com.bookshopweb.beans.Cart;
import com.bookshopweb.dao.CartDAO;

import java.util.Optional;
import java.util.List;

public class CartService {

    private final CartDAO cartDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
    }

    /**
     * Lấy Cart theo id
     */
    public Optional<Cart> getById(long id) {
        return cartDAO.getById(id);
    }

    /**
     * Lấy tất cả Cart
     */
    public List<Cart> getAll() {
        return cartDAO.getAll();
    }

    /**
     * Lấy Cart theo userId
     */
    public Optional<Cart> getByUserId(long userId) {
        return cartDAO.getByUserId(userId);
    }

    /**
     * Thêm Cart mới
     */
    public long insert(Cart cart) {
        return cartDAO.insert(cart);
    }

    /**
     * Cập nhật Cart
     */
    public void update(Cart cart) {
        cartDAO.update(cart);
    }

    /**
     * Xóa Cart theo id
     */
    public void delete(long id) {
        cartDAO.delete(id);
    }

    /**
     * Tổng số lượng CartItem của user
     */
    public int countCartItemQuantityByUserId(long userId) {
        return cartDAO.countCartItemQuantityByUserId(userId);
    }

    /**
     * Tổng số Order của user
     */
    public int countOrderByUserId(long userId) {
        return cartDAO.countOrderByUserId(userId);
    }

    /**
     * Tổng số Order đang giao (status = 1)
     */
    public int countOrderDeliverByUserId(long userId) {
        return cartDAO.countOrderDeliverByUserId(userId);
    }

    /**
     * Tổng số Order đã nhận (status = 2)
     */
    public int countOrderReceivedByUserId(long userId) {
        return cartDAO.countOrderReceivedByUserId(userId);
    }

    /**
     * Phân trang simple: LIMIT/OFFSET
     */
    public List<Cart> getPart(int limit, int offset) {
        return cartDAO.getPart(limit, offset);
    }

    /**
     * Phân trang có sắp xếp
     */
    public List<Cart> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return cartDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }
}
