package com.bookshopweb.servlet.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookshopweb.beans.Order;
import com.bookshopweb.beans.OrderItem;
import com.bookshopweb.service.CartService;
import com.bookshopweb.service.OrderItemService;
import com.bookshopweb.service.OrderService;
import com.bookshopweb.utils.Protector;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final OrderItemService orderItemService = new OrderItemService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển tiếp tới trang giỏ hàng
        request.getRequestDispatcher("/WEB-INF/views/cartView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ form
        long userId = Long.parseLong(request.getParameter("userId"));
        int deliveryMethod = Integer.parseInt(request.getParameter("deliveryMethod"));
        double deliveryPrice = Double.parseDouble(request.getParameter("deliveryPrice"));
        long cartId = Long.parseLong(request.getParameter("cartId"));

        // Dữ liệu các sản phẩm trong giỏ hàng
        String[] productIds = request.getParameterValues("productId");
        String[] prices = request.getParameterValues("price");
        String[] discounts = request.getParameterValues("discount");
        String[] quantities = request.getParameterValues("quantity");

        // Tạo order
        Order order = new Order(
                0L,
                userId,
                1, // trạng thái mặc định
                deliveryMethod,
                deliveryPrice,
                LocalDateTime.now(),
                null
        );

        long orderId = Protector.of(() -> orderService.insert(order)).get(0L);

        if (orderId > 0L) {
            List<OrderItem> orderItems = new ArrayList<>();
            for (int i = 0; i < productIds.length; i++) {
                OrderItem item = new OrderItem(
                        0L,
                        orderId,
                        Long.parseLong(productIds[i]),
                        Double.parseDouble(prices[i]),
                        Double.parseDouble(discounts[i]),
                        Integer.parseInt(quantities[i]),
                        LocalDateTime.now(),
                        null
                );
                orderItems.add(item);
            }

            try {
                // Thêm các sản phẩm vào order
                orderItemService.bulkInsert(orderItems);
                // Xóa giỏ hàng
                cartService.delete(cartId);

                // Chuyển tiếp với thông báo thành công
                request.setAttribute("message", "Đã đặt hàng thành công!");
                request.getRequestDispatcher("/WEB-INF/views/cartView.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Đặt hàng thất bại!");
                request.getRequestDispatcher("/WEB-INF/views/cartView.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Đặt hàng thất bại!");
            request.getRequestDispatcher("/WEB-INF/views/cartView.jsp").forward(request, response);
        }
    }
}
