package com.bookshopweb.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bookshopweb.beans.Product;
import com.bookshopweb.dao.ProductDAO;
import com.bookshopweb.utils.Protector;

public class ProductService {

	private final ProductDAO productDAO;

	public ProductService() {
		this.productDAO = new ProductDAO();
	}

	public List<Product> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String orderDir,
			long categoryId) {
		return productDAO.getOrderedPartByCategoryId(limit, offset, orderBy, orderDir, categoryId);
	}

	public int countByCategoryId(long categoryId) {
		return productDAO.countByCategoryId(categoryId);
	}

	public List<Product> getRandomPartByCategoryId(int limit, int offset, long categoryId) {
		return productDAO.getRandomPartByCategoryId(limit, offset, categoryId);
	}

	public List<String> getPublishersByCategoryId(long categoryId) {
		return productDAO.getPublishersByCategoryId(categoryId);
	}

	public int countByCategoryIdAndFilters(long categoryId, String filters) {
		return productDAO.countByCategoryIdAndFilters(categoryId, filters);
	}

	public List<Product> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String orderDir,
			long categoryId, String filters) {
		return productDAO.getOrderedPartByCategoryIdAndFilters(limit, offset, orderBy, orderDir, categoryId, filters);
	}

	public int count() {
		return productDAO.count();
	}

	public void insertProductCategory(long productId, long categoryId) {
		productDAO.insertProductCategory(productId, categoryId);
	}

	public void updateProductCategory(long productId, long categoryId) {
		productDAO.updateProductCategory(productId, categoryId);
	}

	public void deleteProductCategory(long productId, long categoryId) {
		productDAO.deleteProductCategory(productId, categoryId);
	}

	public List<Product> getByQuery(String query, int limit, int offset) {
		return productDAO.getByQuery(query, limit, offset);
	}

	public int countByQuery(String query) {
		return productDAO.countByQuery(query);
	}

	public String getFirst(String twopartString) {
		return twopartString.contains("-") ? twopartString.split("-")[0] : "";
	}

	public String getLast(String twopartString) {
		return twopartString.contains("-") ? twopartString.split("-")[1] : "";
	}

	private int getMinPrice(String priceRange) {
		return Protector.of(() -> Integer.parseInt(getFirst(priceRange))).get(0);
	}

	private int getMaxPrice(String priceRange) {
		return Protector.of(() -> {
			String maxPriceString = getLast(priceRange);
			if (maxPriceString.equals("infinity")) {
				return Integer.MAX_VALUE;
			}
			return Integer.parseInt(maxPriceString);
		}).get(0);
	}

	public String filterByPublishers(List<String> publishers) {
		String publishersString = publishers.stream().map(p -> "'" + p + "'").collect(Collectors.joining(", "));
		return "p.publisher IN (" + publishersString + ")";
	}

	public String filterByPriceRanges(List<String> priceRanges) {
		String priceRangeConditions = priceRanges.stream()
				.map(priceRange -> "p.price BETWEEN " + getMinPrice(priceRange) + " AND " + getMaxPrice(priceRange))
				.collect(Collectors.joining(" OR "));
		return "(" + priceRangeConditions + ")";
	}

	public String createFiltersQuery(List<String> filters) {
		return String.join(" AND ", filters);
	}

	public Optional<Product> getById(long id) {
		return productDAO.getById(id);
	}

	public long insert(Product product) {
		return productDAO.insert(product);
	}

	public void delete(long id) {
		productDAO.delete(id);
	}

	public List<Product> getAll() {
		return productDAO.getAll();
	}

	public List<Product> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
		return productDAO.getOrderedPart(limit, offset, orderBy, orderDir);
	}

	public void update(Product product) {
		productDAO.update(product);
	}
	
}
