package com.bookshopweb.service;

import com.bookshopweb.beans.OrderItem;
import com.bookshopweb.dao.OrderItemDAO;

import java.util.List;
import java.util.Optional;

public class OrderItemService {

    private final OrderItemDAO orderItemDAO;

    public OrderItemService() {
        this.orderItemDAO = new OrderItemDAO();
    }

    /**
     * Thêm nhiều OrderItem cùng lúc
     */
    public void bulkInsert(List<OrderItem> orderItems) {
        orderItemDAO.bulkInsert(orderItems);
    }

    /**
     * Lấy tên các sản phẩm theo orderId
     */
    public List<String> getProductNamesByOrderId(long orderId) {
        return orderItemDAO.getProductNamesByOrderId(orderId);
    }

    /**
     * Lấy OrderItem theo orderId
     */
    public List<OrderItem> getByOrderId(long orderId) {
        return orderItemDAO.getByOrderId(orderId);
    }

    /**
     * Insert một orderItem
     */
    public long insert(OrderItem orderItem) {
        return orderItemDAO.insert(orderItem);
    }

    /**
     * Cập nhật một orderItem
     */
    public void update(OrderItem orderItem) {
        orderItemDAO.update(orderItem);
    }

    /**
     * Xóa một orderItem theo id
     */
    public void delete(long id) {
        orderItemDAO.delete(id);
    }

    /**
     * Lấy orderItem theo id
     */
    public Optional<OrderItem> getById(long id) {
        return orderItemDAO.getById(id);
    }

    /**
     * Lấy tất cả orderItem
     */
    public List<OrderItem> getAll() {
        return orderItemDAO.getAll();
    }

    /**
     * Phân trang
     */
    public List<OrderItem> getPart(int limit, int offset) {
        return orderItemDAO.getPart(limit, offset);
    }

    /**
     * Phân trang có sắp xếp
     */
    public List<OrderItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return orderItemDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }
}
