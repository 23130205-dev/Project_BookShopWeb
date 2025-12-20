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

import com.bookshopweb.beans.WishlistItem;
import com.bookshopweb.utils.DBConnection;

public class WishlistItemDAO implements DAO<WishlistItem> {

    @Override
    public long insert(WishlistItem wishlistItem) {
        String sql = "INSERT INTO wishlist_item(userId, productId, createdAt) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, wishlistItem.getUserId());
            ps.setLong(2, wishlistItem.getProductId());
            ps.setTimestamp(3, Timestamp.valueOf(wishlistItem.getCreatedAt()));

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public void update(WishlistItem wishlistItem) {
        String sql = "UPDATE wishlist_item SET userId=?, productId=?, createdAt=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, wishlistItem.getUserId());
            ps.setLong(2, wishlistItem.getProductId());
            ps.setTimestamp(3, Timestamp.valueOf(wishlistItem.getCreatedAt()));
            ps.setLong(4, wishlistItem.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM wishlist_item WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<WishlistItem> getById(long id) {
        String sql = "SELECT * FROM wishlist_item WHERE id=?";
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
    public List<WishlistItem> getAll() {
        String sql = "SELECT * FROM wishlist_item";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<WishlistItem> getPart(int limit, int offset) {
        String sql = "SELECT * FROM wishlist_item LIMIT ? OFFSET ?";
        List<WishlistItem> list = new ArrayList<>();
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
    public List<WishlistItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC")) orderDir = "ASC";
        if (!orderBy.matches("[a-zA-Z0-9_]+")) orderBy = "id";

        String sql = "SELECT * FROM wishlist_item ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        List<WishlistItem> list = new ArrayList<>();
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

    public List<WishlistItem> getByUserId(long userId) {
        String sql = "SELECT * FROM wishlist_item WHERE userId=?";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countByUserIdAndProductId(long userId, long productId) {
        String sql = "SELECT COUNT(id) FROM wishlist_item WHERE userId=? AND productId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private WishlistItem mapResultSet(ResultSet rs) throws SQLException {
        WishlistItem wi = new WishlistItem();
        wi.setId(rs.getLong("id"));
        wi.setUserId(rs.getLong("userId"));
        wi.setProductId(rs.getLong("productId"));
        wi.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        return wi;
    }
}
