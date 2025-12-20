package com.bookshopweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookshopweb.beans.Category;
import com.bookshopweb.utils.DBConnection;

public class CategoryDAO implements DAO<Category> {

	@Override
	public long insert(Category category) {
		String sql = "INSERT INTO category(name, description, imageName) VALUES (?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, category.getName());
			ps.setString(2, category.getDescription());
			ps.setString(3, category.getImageName());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
				return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	@Override
	public void update(Category category) {
		String sql = "UPDATE category SET name=?, description=?, imageName=? WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, category.getName());
			ps.setString(2, category.getDescription());
			ps.setString(3, category.getImageName());
			ps.setLong(4, category.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(long id) {
		String sql = "DELETE FROM category WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Optional<Category> getById(long id) {
		String sql = "SELECT * FROM category WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return Optional.of(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public List<Category> getAll() {
		String sql = "SELECT * FROM category";
		List<Category> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				list.add(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Category> getPart(int limit, int offset) {
		String sql = "SELECT * FROM category LIMIT ? OFFSET ?";
		List<Category> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Category> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
		if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC"))
			orderDir = "ASC";
		if (!orderBy.matches("[a-zA-Z0-9_]+"))
			orderBy = "id";

		String sql = "SELECT * FROM category ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
		List<Category> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	private Category mapResultSet(ResultSet rs) throws SQLException {
		Category c = new Category();
		c.setId(rs.getLong("id"));
		c.setName(rs.getString("name"));
		c.setDescription(rs.getString("description"));
		c.setImageName(rs.getString("imageName"));
		return c;
	}

	// Thêm các phương thức riêng nếu cần
	public Optional<Category> getByProductId(long productId) {
		String sql = "SELECT c.* FROM product_category pc JOIN category c ON pc.categoryId = c.id WHERE pc.productId=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, productId);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return Optional.of(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public int count() {
		String sql = "SELECT COUNT(id) FROM category";
		try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
