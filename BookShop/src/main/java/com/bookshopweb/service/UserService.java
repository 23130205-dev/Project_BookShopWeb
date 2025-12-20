package com.bookshopweb.service;

import com.bookshopweb.beans.User;
import com.bookshopweb.dao.UserDAO;

import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // CRUD
    public long insert(User user) {
        return userDAO.insert(user);
    }

    public void update(User user) {
        userDAO.update(user);
    }

    public void delete(long id) {
        userDAO.delete(id);
    }

    public Optional<User> getById(long id) {
        return userDAO.getById(id);
    }

    public java.util.List<User> getAll() {
        return userDAO.getAll();
    }

    public java.util.List<User> getPart(int limit, int offset) {
        return userDAO.getPart(limit, offset);
    }

    public java.util.List<User> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return userDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    // Các phương thức riêng của UserService

    public Optional<User> getByUsername(String username) {
        return userDAO.getByUsername(username);
    }

    public void changePassword(long userId, String newPassword) {
        userDAO.changePassword(userId, newPassword);
    }

    public Optional<User> getByEmail(String email) {
        return userDAO.getByEmail(email);
    }

    public Optional<User> getByPhoneNumber(String phoneNumber) {
        return userDAO.getByPhoneNumber(phoneNumber);
    }

    public int count() {
        return userDAO.count();
    }
}
