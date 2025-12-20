package com.bookshopweb.servlet.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookshopweb.beans.Product;
import com.bookshopweb.beans.User;
import com.bookshopweb.beans.WishlistItem;
import com.bookshopweb.service.ProductService;
import com.bookshopweb.service.WishlistItemService;
import com.bookshopweb.utils.Protector;

@WebServlet(name = "WishlistServlet", value = "/wishlist")
public class WishlistServlet extends HttpServlet {
    private final WishlistItemService wishlistItemService = new WishlistItemService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user != null) {
            List<WishlistItem> wishlistItems = Protector.of(() -> wishlistItemService.getByUserId(user.getId()))
                    .get(ArrayList::new);

            for (WishlistItem wishlistItem : wishlistItems) {
                wishlistItem.setProduct(productService.getById(wishlistItem.getProductId()).orElseGet(Product::new));
            }

            request.setAttribute("wishlistItems", wishlistItems);
        }

        request.getRequestDispatcher("/WEB-INF/views/wishlistView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy hành động từ form (thêm hoặc xóa)
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addWishlistItem(request, response);
        } else if ("delete".equals(action)) {
            deleteWishlistItem(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/wishlist");
        }
    }

    private void addWishlistItem(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user != null) {
            long productId = Long.parseLong(request.getParameter("productId"));

            WishlistItem wishlistItem = new WishlistItem();
            wishlistItem.setUserId(user.getId());
            wishlistItem.setProductId(productId);

            try {
                wishlistItemService.insert(wishlistItem);
                request.setAttribute("message", "Đã thêm sản phẩm vào danh sách yêu thích!");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Thêm sản phẩm thất bại!");
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/wishlistView.jsp").forward(request, response);
    }

    private void deleteWishlistItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));

        try {
            wishlistItemService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/wishlist");
    }
}
