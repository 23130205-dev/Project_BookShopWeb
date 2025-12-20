package com.bookshopweb.service;

import com.bookshopweb.beans.Order;
import com.bookshopweb.dao.OrderDAO;

import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO(); // khởi tạo class JDBC thuần
    }

    // Tạo mới đơn hàng
    public long insert(Order order) {
        return orderDAO.insert(order);
    }

    // Cập nhật đơn hàng
    public void update(Order order) {
        orderDAO.update(order);
    }

    // Xóa đơn hàng
    public void delete(long id) {
        orderDAO.delete(id);
    }

    // Lấy đơn hàng theo id
    public Optional<Order> getById(long id) {
        return orderDAO.getById(id);
    }

    // Lấy tất cả đơn hàng
    public List<Order> getAll() {
        return orderDAO.getAll();
    }

    // Phân trang (limit, offset)
    public List<Order> getPart(int limit, int offset) {
        return orderDAO.getPart(limit, offset);
    }

    // Phân trang với sort
    public List<Order> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return orderDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    // Lấy danh sách đơn hàng theo userId kèm phân trang
    public List<Order> getOrderedPartByUserId(long userId, int limit, int offset) {
        return orderDAO.getOrderedPartByUserId(userId, limit, offset);
    }

    // Đếm số đơn hàng theo userId
    public int countByUserId(long userId) {
        return orderDAO.countByUserId(userId);
    }

    // Đếm tổng số đơn hàng
    public int count() {
        return orderDAO.count();
    }

    // Cập nhật trạng thái huỷ đơn
    public void cancelOrder(long id) {
        orderDAO.cancelOrder(id);
    }

    // Xác nhận đơn
    public void confirm(long id) {
        orderDAO.confirm(id);
    }

    // Huỷ đơn thuật ngữ khác
    public void cancel(long id) {
        orderDAO.cancel(id);
    }

    // Reset trạng thái đơn
    public void reset(long id) {
        orderDAO.reset(id);
    }
}
