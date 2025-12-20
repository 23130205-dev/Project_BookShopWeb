package com.bookshopweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookshopweb.beans.Order;
import com.bookshopweb.utils.DBConnection;

public class OrderDAO implements DAO<Order> {

    @Override
    public long insert(Order order) {
        String sql = "INSERT INTO orders(userId, status, deliveryMethod, deliveryPrice, createdAt, updatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, order.getUserId());
            ps.setInt(2, order.getStatus());
            ps.setInt(3, order.getDeliveryMethod());
            ps.setDouble(4, order.getDeliveryPrice());
            ps.setTimestamp(5, Timestamp.valueOf(order.getCreatedAt()));
            ps.setTimestamp(6, order.getUpdatedAt() != null ? Timestamp.valueOf(order.getUpdatedAt()) : null);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET userId=?, status=?, deliveryMethod=?, deliveryPrice=?, createdAt=?, updatedAt=? " +
                     "WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, order.getUserId());
            ps.setInt(2, order.getStatus());
            ps.setInt(3, order.getDeliveryMethod());
            ps.setDouble(4, order.getDeliveryPrice());
            ps.setTimestamp(5, Timestamp.valueOf(order.getCreatedAt()));
            ps.setTimestamp(6, order.getUpdatedAt() != null ? Timestamp.valueOf(order.getUpdatedAt()) : null);
            ps.setLong(7, order.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM orders WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Order> getById(long id) {
        String sql = "SELECT * FROM orders WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        String sql = "SELECT * FROM orders";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order> getPart(int limit, int offset) {
        String sql = "SELECT * FROM orders LIMIT ? OFFSET ?";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC")) orderDir = "ASC";
        if (!orderBy.matches("[a-zA-Z0-9_]+")) orderBy = "id";

        String sql = "SELECT * FROM orders ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> getOrderedPartByUserId(long userId, int limit, int offset) {
        String sql = "SELECT * FROM orders WHERE userId=? ORDER BY createdAt DESC LIMIT ? OFFSET ?";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countByUserId(long userId) {
        String sql = "SELECT COUNT(id) FROM orders WHERE userId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int count() {
        String sql = "SELECT COUNT(id) FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void confirm(long id) {
        updateStatus(id, 2);
    }

    public void cancel(long id) {
        updateStatus(id, 3);
    }

    public void cancelOrder(long id) {
        updateStatus(id, 3);
    }

    public void reset(long id) {
        updateStatus(id, 1);
    }

    private void updateStatus(long id, int status) {
        String sql = "UPDATE orders SET status=?, updatedAt=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, status);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(3, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order mapResultSet(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setUserId(rs.getLong("userId"));
        o.setStatus(rs.getInt("status"));
        o.setDeliveryMethod(rs.getInt("deliveryMethod"));
        o.setDeliveryPrice(rs.getDouble("deliveryPrice"));
        o.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updatedAt");
        if (updated != null) o.setUpdatedAt(updated.toLocalDateTime());
        return o;
    }
}
