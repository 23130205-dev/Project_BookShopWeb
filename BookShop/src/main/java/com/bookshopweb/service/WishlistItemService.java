package com.bookshopweb.service;

import com.bookshopweb.beans.WishlistItem;
import com.bookshopweb.dao.WishlistItemDAO;

import java.util.List;
import java.util.Optional;

public class WishlistItemService {

    private final WishlistItemDAO wishlistItemDAO;

    public WishlistItemService() {
        this.wishlistItemDAO = new WishlistItemDAO();
    }

    // CRUD
    public long insert(WishlistItem wishlistItem) {
        return wishlistItemDAO.insert(wishlistItem);
    }

    public void update(WishlistItem wishlistItem) {
        wishlistItemDAO.update(wishlistItem);
    }

    public void delete(long id) {
        wishlistItemDAO.delete(id);
    }

    public Optional<WishlistItem> getById(long id) {
        return wishlistItemDAO.getById(id);
    }

    public List<WishlistItem> getAll() {
        return wishlistItemDAO.getAll();
    }

    public List<WishlistItem> getPart(int limit, int offset) {
        return wishlistItemDAO.getPart(limit, offset);
    }

    public List<WishlistItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return wishlistItemDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    // Các phương thức riêng của WishlistItem

    public List<WishlistItem> getByUserId(long userId) {
        return wishlistItemDAO.getByUserId(userId);
    }

    public int countByUserIdAndProductId(long userId, long productId) {
        return wishlistItemDAO.countByUserIdAndProductId(userId, productId);
    }
}
