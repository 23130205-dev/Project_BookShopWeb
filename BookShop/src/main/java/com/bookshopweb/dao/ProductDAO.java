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

import com.bookshopweb.beans.Product;
import com.bookshopweb.utils.DBConnection;

public class ProductDAO implements DAO<Product> {

	@Override
	public long insert(Product product) {
		String sql = "INSERT INTO product(name, price, discount, quantity, totalBuy, author, pages, publisher, "
				+ "yearPublishing, description, imageName, shop, createdAt, updatedAt, startsAt, endsAt) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, product.getName());
			ps.setDouble(2, product.getPrice());
			ps.setDouble(3, product.getDiscount());
			ps.setInt(4, product.getQuantity());
			ps.setInt(5, product.getTotalBuy());
			ps.setString(6, product.getAuthor());
			ps.setInt(7, product.getPages());
			ps.setString(8, product.getPublisher());
			ps.setInt(9, product.getYearPublishing());
			ps.setString(10, product.getDescription());
			ps.setString(11, product.getImageName());
			ps.setInt(12, product.getShop());
			ps.setTimestamp(13, Timestamp.valueOf(product.getCreatedAt()));
			ps.setTimestamp(14, product.getUpdatedAt() != null ? Timestamp.valueOf(product.getUpdatedAt()) : null);
			ps.setTimestamp(15, product.getStartsAt() != null ? Timestamp.valueOf(product.getStartsAt()) : null);
			ps.setTimestamp(16, product.getEndsAt() != null ? Timestamp.valueOf(product.getEndsAt()) : null);

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
	public void update(Product product) {
		String sql = "UPDATE product SET name=?, price=?, discount=?, quantity=?, totalBuy=?, author=?, pages=?, "
				+ "publisher=?, yearPublishing=?, description=?, imageName=?, shop=?, updatedAt=?, startsAt=?, endsAt=? "
				+ "WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, product.getName());
			ps.setDouble(2, product.getPrice());
			ps.setDouble(3, product.getDiscount());
			ps.setInt(4, product.getQuantity());
			ps.setInt(5, product.getTotalBuy());
			ps.setString(6, product.getAuthor());
			ps.setInt(7, product.getPages());
			ps.setString(8, product.getPublisher());
			ps.setInt(9, product.getYearPublishing());
			ps.setString(10, product.getDescription());
			ps.setString(11, product.getImageName());
			ps.setInt(12, product.getShop());
			ps.setTimestamp(13, product.getUpdatedAt() != null ? Timestamp.valueOf(product.getUpdatedAt()) : null);
			ps.setTimestamp(14, product.getStartsAt() != null ? Timestamp.valueOf(product.getStartsAt()) : null);
			ps.setTimestamp(15, product.getEndsAt() != null ? Timestamp.valueOf(product.getEndsAt()) : null);
			ps.setLong(16, product.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(long id) {
		String sql = "DELETE FROM product WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Optional<Product> getById(long id) {
		String sql = "SELECT * FROM product WHERE id=?";
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
	public List<Product> getAll() {
		String sql = "SELECT * FROM product";
		List<Product> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next())
				list.add(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Product> getPart(int limit, int offset) {
		String sql = "SELECT * FROM product LIMIT ? OFFSET ?";
		List<Product> list = new ArrayList<>();
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
	public List<Product> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
		if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC"))
			orderDir = "ASC";
		if (!orderBy.matches("[a-zA-Z0-9_]+"))
			orderBy = "id";

		String sql = "SELECT * FROM product ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
		List<Product> list = new ArrayList<>();
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

	public List<Product> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String orderDir,
			long categoryId) {
		if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC"))
			orderDir = "ASC";
		if (!orderBy.matches("[a-zA-Z0-9_]+"))
			orderBy = "id";

		String sql = "SELECT p.* FROM product_category pc " + "JOIN product p ON pc.productId = p.id "
				+ "WHERE pc.categoryId=? " + "ORDER BY p." + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
		List<Product> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, categoryId);
			ps.setInt(2, limit);
			ps.setInt(3, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int countByCategoryId(long categoryId) {
		String sql = "SELECT COUNT(productId) FROM product_category WHERE categoryId=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, categoryId);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Product> getRandomPartByCategoryId(int limit, int offset, long categoryId) {
		String sql = "SELECT p.* FROM product_category pc "
				+ "JOIN product p ON pc.productId = p.id WHERE pc.categoryId=? " + "ORDER BY RAND() LIMIT ? OFFSET ?";
		List<Product> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, categoryId);
			ps.setInt(2, limit);
			ps.setInt(3, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<String> getPublishersByCategoryId(long categoryId) {
		String sql = "SELECT DISTINCT p.publisher FROM product_category pc "
				+ "JOIN product p ON pc.productId = p.id WHERE pc.categoryId=? ORDER BY p.publisher";
		List<String> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, categoryId);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(rs.getString("publisher"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int countByCategoryIdAndFilters(long categoryId, String filters) {
		String sql = "SELECT COUNT(p.id) FROM product_category pc JOIN product p ON pc.productId=p.id "
				+ "WHERE pc.categoryId=? AND " + filters;
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, categoryId);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Product> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String orderDir,
			long categoryId, String filters) {
		if (!orderDir.equalsIgnoreCase("ASC") && !orderDir.equalsIgnoreCase("DESC"))
			orderDir = "ASC";
		if (!orderBy.matches("[a-zA-Z0-9_]+"))
			orderBy = "id";

		String sql = "SELECT p.* FROM product_category pc JOIN product p ON pc.productId=p.id "
				+ "WHERE pc.categoryId=? AND " + filters + " ORDER BY p." + orderBy + " " + orderDir
				+ " LIMIT ? OFFSET ?";
		List<Product> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, categoryId);
			ps.setInt(2, limit);
			ps.setInt(3, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int count() {
		String sql = "SELECT COUNT(id) FROM product";
		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Product> getByQuery(String query, int limit, int offset) {
		String sql = "SELECT * FROM product WHERE name LIKE ? LIMIT ? OFFSET ?";
		List<Product> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, "%" + query + "%");
			ps.setInt(2, limit);
			ps.setInt(3, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(mapResultSet(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int countByQuery(String query) {
		String sql = "SELECT COUNT(id) FROM product WHERE name LIKE ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, "%" + query + "%");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Thêm bản ghi (liên kết product <-> category)
	public void insertProductCategory(long productId, long categoryId) {
		String sql = "INSERT INTO product_category(productId, categoryId) VALUES (?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, productId);
			ps.setLong(2, categoryId);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Cập nhật category của product
	// (chuyển product sang category khác)
	public void updateProductCategory(long productId, long categoryId) {
		String sql = "UPDATE product_category SET categoryId=? WHERE productId=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, categoryId);
			ps.setLong(2, productId);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Xóa liên kết product + category
	public void deleteProductCategory(long productId, long categoryId) {
		String sql = "DELETE FROM product_category WHERE productId=? AND categoryId=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, productId);
			ps.setLong(2, categoryId);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Product mapResultSet(ResultSet rs) throws SQLException {
		Product p = new Product();
		p.setId(rs.getLong("id"));
		p.setName(rs.getString("name"));
		p.setPrice(rs.getDouble("price"));
		p.setDiscount(rs.getDouble("discount"));
		p.setQuantity(rs.getInt("quantity"));
		p.setTotalBuy(rs.getInt("totalBuy"));
		p.setAuthor(rs.getString("author"));
		p.setPages(rs.getInt("pages"));
		p.setPublisher(rs.getString("publisher"));
		p.setYearPublishing(rs.getInt("yearPublishing"));
		p.setDescription(rs.getString("description"));
		p.setImageName(rs.getString("imageName"));
		p.setShop(rs.getInt("shop"));
		p.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
		Timestamp updated = rs.getTimestamp("updatedAt");
		if (updated != null)
			p.setUpdatedAt(updated.toLocalDateTime());
		Timestamp starts = rs.getTimestamp("startsAt");
		if (starts != null)
			p.setStartsAt(starts.toLocalDateTime());
		Timestamp ends = rs.getTimestamp("endsAt");
		if (ends != null)
			p.setEndsAt(ends.toLocalDateTime());
		return p;
	}
}
