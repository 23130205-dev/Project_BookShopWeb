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

import com.bookshopweb.beans.ProductReview;
import com.bookshopweb.utils.DBConnection;

public class ProductReviewDAO implements DAO<ProductReview> {

    @Override
    public long insert(ProductReview review) {
        String sql = "INSERT INTO product_review(userId, productId, ratingScore, content, isShow, createdAt, updatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, review.getUserId());
            ps.setLong(2, review.getProductId());
            ps.setInt(3, review.getRatingScore());
            ps.setString(4, review.getContent());
            ps.setInt(5, review.getIsShow()); // dùng int 0/1
            ps.setTimestamp(6, Timestamp.valueOf(review.getCreatedAt()));
            ps.setTimestamp(7, review.getUpdatedAt() != null ? Timestamp.valueOf(review.getUpdatedAt()) : null);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public void update(ProductReview review) {
        String sql = "UPDATE product_review SET ratingScore=?, content=?, updatedAt=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getRatingScore());
            ps.setString(2, review.getContent());
            ps.setTimestamp(3, Timestamp.valueOf(review.getUpdatedAt()));
            ps.setLong(4, review.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM product_review WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<ProductReview> getById(long id) {
        String sql = "SELECT * FROM product_review WHERE id=?";
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
    public List<ProductReview> getAll() {
        String sql = "SELECT * FROM product_review";
        List<ProductReview> list = new ArrayList<>();
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
    public List<ProductReview> getPart(int limit, int offset) {
        String sql = "SELECT * FROM product_review LIMIT ? OFFSET ?";
        List<ProductReview> list = new ArrayList<>();
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
    public List<ProductReview> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC")) orderDir = "ASC";
        if (!orderBy.matches("[a-zA-Z0-9_]+")) orderBy = "id";

        String sql = "SELECT * FROM product_review ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        List<ProductReview> list = new ArrayList<>();
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

    // Lấy review theo product
    public List<ProductReview> getOrderedPartByProductId(int limit, int offset,
                                                          String orderBy, String orderDir, long productId) {
        if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC")) orderDir = "ASC";
        if (!orderBy.matches("[a-zA-Z0-9_]+")) orderBy = "id";

        String sql = "SELECT * FROM product_review WHERE productId=? ORDER BY " + orderBy + " " + orderDir +
                     " LIMIT ? OFFSET ?";
        List<ProductReview> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, productId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countByProductId(long productId) {
        String sql = "SELECT COUNT(id) FROM product_review WHERE productId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int sumRatingScoresByProductId(long productId) {
        String sql = "SELECT SUM(ratingScore) FROM product_review WHERE productId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int count() {
        String sql = "SELECT COUNT(id) FROM product_review";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void hide(long id) {
        setIsShow(id, 0); // 0 = ẩn
    }

    public void show(long id) {
        setIsShow(id, 1); // 1 = hiện
    }

    private void setIsShow(long id, int value) {
        String sql = "UPDATE product_review SET isShow=?, updatedAt=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, value);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ProductReview mapResultSet(ResultSet rs) throws SQLException {
        ProductReview pr = new ProductReview();
        pr.setId(rs.getLong("id"));
        pr.setUserId(rs.getLong("userId"));
        pr.setProductId(rs.getLong("productId"));
        pr.setRatingScore(rs.getInt("ratingScore"));
        pr.setContent(rs.getString("content"));
        pr.setIsShow(rs.getInt("isShow")); // lấy 0/1
        pr.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        Timestamp u = rs.getTimestamp("updatedAt");
        if (u != null) pr.setUpdatedAt(u.toLocalDateTime());
        return pr;
    }
}
