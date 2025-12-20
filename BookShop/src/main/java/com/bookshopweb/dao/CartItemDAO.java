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

import com.bookshopweb.beans.CartItem;
import com.bookshopweb.beans.Product;
import com.bookshopweb.utils.DBConnection;

public class CartItemDAO implements DAO<CartItem> {

    // INSERT
    @Override
    public long insert(CartItem cartItem) {
        String sql = "INSERT INTO cart_item(cartId, productId, quantity, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, cartItem.getCartId());
            ps.setLong(2, cartItem.getProductId());
            ps.setInt(3, cartItem.getQuantity());
            ps.setTimestamp(4, Timestamp.valueOf(cartItem.getCreatedAt()));
            ps.setTimestamp(5, cartItem.getUpdatedAt() != null ? Timestamp.valueOf(cartItem.getUpdatedAt()) : null);

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
    public void update(CartItem cartItem) {
        String sql = "UPDATE cart_item SET cartId=?, productId=?, quantity=?, createdAt=?, updatedAt=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cartItem.getCartId());
            ps.setLong(2, cartItem.getProductId());
            ps.setInt(3, cartItem.getQuantity());
            ps.setTimestamp(4, Timestamp.valueOf(cartItem.getCreatedAt()));
            ps.setTimestamp(5, cartItem.getUpdatedAt() != null ? Timestamp.valueOf(cartItem.getUpdatedAt()) : null);
            ps.setLong(6, cartItem.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    @Override
    public void delete(long id) {
        String sql = "DELETE FROM cart_item WHERE id=?";
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
    public Optional<CartItem> getById(long id) {
        String sql = "SELECT * FROM cart_item WHERE id=?";
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
    public List<CartItem> getAll() {
        String sql = "SELECT * FROM cart_item";
        List<CartItem> list = new ArrayList<>();
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
    public List<CartItem> getPart(int limit, int offset) {
        String sql = "SELECT * FROM cart_item LIMIT ? OFFSET ?";
        List<CartItem> list = new ArrayList<>();
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
    public List<CartItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC")) orderDir = "ASC";
        if (!orderBy.matches("[a-zA-Z0-9_]+")) orderBy = "id";

        String sql = "SELECT * FROM cart_item ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        List<CartItem> list = new ArrayList<>();
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

    // Phương thức đặc thù riêng của CartItemDAO
    public List<CartItem> getByCartId(long cartId) {
        String sql = "SELECT ci.*, p.name AS product_name, p.price AS product_price, p.discount AS product_discount, " +
                     "p.quantity AS product_quantity, p.imageName AS product_imageName " +
                     "FROM cart_item ci " +
                     "JOIN product p ON p.id = ci.productId " +
                     "WHERE cartId=? " +
                     "ORDER BY ci.createdAt DESC";

        List<CartItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem ci = mapResultSet(rs);
                Product p = new Product();
                p.setId(rs.getLong("productId"));
                p.setName(rs.getString("product_name"));
                p.setPrice(rs.getInt("product_price"));
                p.setDiscount(rs.getInt("product_discount"));
                p.setQuantity(rs.getInt("product_quantity"));
                p.setImageName(rs.getString("product_imageName"));
                ci.setProduct(p);
                list.add(ci);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<CartItem> getByCartIdAndProductId(long cartId, long productId) {
        String sql = "SELECT * FROM cart_item WHERE cartId=? AND productId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cartId);
            ps.setLong(2, productId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public int sumQuantityByUserId(long userId) {
        String sql = "SELECT SUM(ci.quantity) FROM cart_item ci " +
                     "JOIN cart c ON c.id = ci.cartId WHERE c.userId=?";
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

    // MAPPER
    private CartItem mapResultSet(ResultSet rs) throws SQLException {
        CartItem ci = new CartItem();
        ci.setId(rs.getLong("id"));
        ci.setCartId(rs.getLong("cartId"));
        ci.setProductId(rs.getLong("productId"));
        ci.setQuantity(rs.getInt("quantity"));
        ci.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        Timestamp updated = rs.getTimestamp("updatedAt");
        if (updated != null) ci.setUpdatedAt(updated.toLocalDateTime());
        return ci;
    }
}
