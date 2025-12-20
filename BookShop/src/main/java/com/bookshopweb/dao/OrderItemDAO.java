package com.bookshopweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookshopweb.beans.OrderItem;
import com.bookshopweb.utils.DBConnection;

public class OrderItemDAO implements DAO<OrderItem> {

    // INSERT
    @Override
    public long insert(OrderItem orderItem) {
        String sql = "INSERT INTO order_item(orderId, productId, price, discount, quantity, createdAt, updatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, orderItem.getOrderId());
            ps.setLong(2, orderItem.getProductId());
            ps.setDouble(3, orderItem.getPrice());
            ps.setDouble(4, orderItem.getDiscount());
            ps.setInt(5, orderItem.getQuantity());
            ps.setTimestamp(6, Timestamp.valueOf(orderItem.getCreatedAt()));
            ps.setTimestamp(7, orderItem.getUpdatedAt() != null ? Timestamp.valueOf(orderItem.getUpdatedAt()) : null);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    // UPDATE
    @Override
    public void update(OrderItem orderItem) {
        String sql = "UPDATE order_item SET orderId=?, productId=?, price=?, discount=?, quantity=?, createdAt=?, updatedAt=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, orderItem.getOrderId());
            ps.setLong(2, orderItem.getProductId());
            ps.setDouble(3, orderItem.getPrice());
            ps.setDouble(4, orderItem.getDiscount());
            ps.setInt(5, orderItem.getQuantity());
            ps.setTimestamp(6, Timestamp.valueOf(orderItem.getCreatedAt()));
            ps.setTimestamp(7, orderItem.getUpdatedAt() != null ? Timestamp.valueOf(orderItem.getUpdatedAt()) : null);
            ps.setLong(8, orderItem.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    @Override
    public void delete(long id) {
        String sql = "DELETE FROM order_item WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GET BY ID
    @Override
    public Optional<OrderItem> getById(long id) {
        String sql = "SELECT * FROM order_item WHERE id=?";
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

    // GET ALL
    @Override
    public List<OrderItem> getAll() {
        String sql = "SELECT * FROM order_item";
        List<OrderItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // GET PART
    @Override
    public List<OrderItem> getPart(int limit, int offset) {
        String sql = "SELECT * FROM order_item LIMIT ? OFFSET ?";
        List<OrderItem> list = new ArrayList<>();
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

    // GET ORDERED PART
    @Override
    public List<OrderItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC")) orderDir = "ASC";
        if (!orderBy.matches("[a-zA-Z0-9_]+")) orderBy = "id";

        String sql = "SELECT * FROM order_item ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        List<OrderItem> list = new ArrayList<>();
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

    // GET BY ORDER ID
    public List<OrderItem> getByOrderId(long orderId) {
        String sql = "SELECT * FROM order_item WHERE orderId=?";
        List<OrderItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // GET PRODUCT NAMES BY ORDER ID
    public List<String> getProductNamesByOrderId(long orderId) {
        String sql = "SELECT p.name FROM product p JOIN order_item o ON p.id = o.productId WHERE o.orderId=?";
        List<String> names = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) names.add(rs.getString("name"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    // BULK INSERT
    public void bulkInsert(List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_item(orderId, productId, price, discount, quantity, createdAt, updatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (OrderItem item : orderItems) {
                ps.setLong(1, item.getOrderId());
                ps.setLong(2, item.getProductId());
                ps.setDouble(3, item.getPrice());
                ps.setDouble(4, item.getDiscount());
                ps.setInt(5, item.getQuantity());
                ps.setTimestamp(6, Timestamp.valueOf(item.getCreatedAt()));
                ps.setTimestamp(7, item.getUpdatedAt() != null ? Timestamp.valueOf(item.getUpdatedAt()) : null);
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // MAPPER
    private OrderItem mapResultSet(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("orderId"));
        item.setProductId(rs.getLong("productId"));
        item.setPrice(rs.getFloat("price"));
        item.setDiscount(rs.getFloat("discount"));
        item.setQuantity(rs.getInt("quantity"));
        item.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updatedAt");
        if (updated != null) item.setUpdatedAt(updated.toLocalDateTime());
        return item;
    }
}
