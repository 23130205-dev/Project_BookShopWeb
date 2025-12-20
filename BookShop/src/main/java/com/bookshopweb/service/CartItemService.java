package com.bookshopweb.service;

import com.bookshopweb.beans.CartItem;
import com.bookshopweb.dao.CartItemDAO;

import java.util.List;
import java.util.Optional;

public class CartItemService {

    private final CartItemDAO cartItemDAO;

    public CartItemService() {
        this.cartItemDAO = new CartItemDAO();
    }

    /**
     * Lấy danh sách CartItem theo cartId
     */
    public List<CartItem> getByCartId(long cartId) {
        return cartItemDAO.getByCartId(cartId);
    }

    /**
     * Lấy CartItem theo cartId và productId
     */
    public Optional<CartItem> getByCartIdAndProductId(long cartId, long productId) {
        return cartItemDAO.getByCartIdAndProductId(cartId, productId);
    }

    /**
     * Tổng số lượng sản phẩm trong giỏ hàng theo userId
     */
    public int sumQuantityByUserId(long userId) {
        return cartItemDAO.sumQuantityByUserId(userId);
    }

    /**
     * Insert CartItem mới
     */
    public long insert(CartItem cartItem) {
        return cartItemDAO.insert(cartItem);
    }

    /**
     * Cập nhật CartItem
     */
    public void update(CartItem cartItem) {
        cartItemDAO.update(cartItem);
    }

    /**
     * Xóa CartItem theo id
     */
    public void delete(long id) {
        cartItemDAO.delete(id);
    }

    /**
     * Lấy CartItem theo id
     */
    public Optional<CartItem> getById(long id) {
        return cartItemDAO.getById(id);
    }

    /**
     * Lấy tất cả CartItem
     */
    public List<CartItem> getAll() {
        return cartItemDAO.getAll();
    }

    /**
     * Lấy phần CartItem theo LIMIT/OFFSET
     */
    public List<CartItem> getPart(int limit, int offset) {
        return cartItemDAO.getPart(limit, offset);
    }

    /**
     * Lấy phần CartItem theo LIMIT/OFFSET với ORDER
     */
    public List<CartItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return cartItemDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }
}
