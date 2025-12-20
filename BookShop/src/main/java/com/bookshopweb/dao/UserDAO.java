package com.bookshopweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookshopweb.beans.User;
import com.bookshopweb.utils.DBConnection;

public class UserDAO implements DAO<User> {

    @Override
    public long insert(User user) {
        String sql = "INSERT INTO user(username, password, fullname, email, phoneNumber, gender, address, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNumber());
            ps.setInt(6, user.getGender()); // BIT(1) -> int 0/1
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getRole());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE user SET username=?, password=?, fullname=?, email=?, phoneNumber=?, gender=?, address=?, role=? " +
                     "WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNumber());
            ps.setInt(6, user.getGender());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getRole());
            ps.setLong(9, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM user WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> getById(long id) {
        String sql = "SELECT * FROM user WHERE id=?";
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
    public List<User> getAll() {
        String sql = "SELECT * FROM user";
        List<User> list = new ArrayList<>();
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
    public List<User> getPart(int limit, int offset) {
        String sql = "SELECT * FROM user LIMIT ? OFFSET ?";
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<User> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC")) orderDir = "ASC";
        if (!orderBy.matches("[a-zA-Z0-9_]+")) orderBy = "id";

        String sql = "SELECT * FROM user ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<User> getByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void changePassword(long userId, String newPassword) {
        String sql = "UPDATE user SET password=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setLong(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> getByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> getByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM user WHERE phoneNumber=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phoneNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public int count() {
        String sql = "SELECT COUNT(id) FROM user";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private User mapResultSet(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setFullname(rs.getString("fullname"));
        u.setEmail(rs.getString("email"));
        u.setPhoneNumber(rs.getString("phoneNumber"));
        u.setGender(rs.getInt("gender")); // BIT(1) as int (0/1)
        u.setAddress(rs.getString("address"));
        u.setRole(rs.getString("role"));
        return u;
    }
}
