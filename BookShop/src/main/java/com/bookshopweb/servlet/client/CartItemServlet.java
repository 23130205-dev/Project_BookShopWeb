package com.bookshopweb.servlet.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookshopweb.beans.Cart;
import com.bookshopweb.beans.CartItem;
import com.bookshopweb.beans.User;
import com.bookshopweb.service.CartItemService;
import com.bookshopweb.service.CartService;
import com.bookshopweb.service.UserService;
import com.bookshopweb.utils.Protector;

@WebServlet(name = "CartItemServlet", value = "/cartItem")
public class CartItemServlet extends HttpServlet {
	private final CartService cartService = new CartService();
	private final CartItemService cartItemService = new CartItemService();
	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long userId = Protector.of(() -> Long.parseLong(request.getParameter("userId"))).get(0L);
		Optional<User> userFromServer = Protector.of(() -> userService.getById(userId)).get(Optional::empty);

		response.setContentType("text/html"); // trả HTML
		response.setCharacterEncoding("UTF‑8");

		if (userId > 0 && userFromServer.isPresent()) {
			Optional<Cart> cartOpt = Protector.of(() -> cartService.getByUserId(userId)).get(Optional::empty);
			if (cartOpt.isPresent()) {
				Cart cart = cartOpt.get();
				List<CartItem> cartItems = Protector.of(() -> cartItemService.getByCartId(cart.getId()))
						.get(ArrayList::new);

				StringBuilder html = new StringBuilder();
				html.append("<h2>Giỏ hàng của user ").append(userId).append("</h2>");
				html.append("<ul>");
				for (CartItem ci : cartItems) {
					html.append("<li>").append("Sản phẩm id: ").append(ci.getProductId()).append(", số lượng: ")
							.append(ci.getQuantity()).append("</li>");
				}
				html.append("</ul>");
				response.getWriter().write(html.toString());
			} else {
				response.getWriter().write("<p>Giỏ hàng trống</p>");
			}
		} else {
			response.getWriter().write("<p>Đã có lỗi truy vấn!</p>");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long userId = Protector.of(() -> Long.parseLong(request.getParameter("userId"))).get(0L);
		long productId = Protector.of(() -> Long.parseLong(request.getParameter("productId"))).get(0L);
		int qty = Protector.of(() -> Integer.parseInt(request.getParameter("quantity"))).get(0);

		// Tạo hoặc lấy cart
		Optional<Cart> cartOpt = Protector.of(() -> cartService.getByUserId(userId)).get(Optional::empty);
		long cartId;
		if (cartOpt.isPresent()) {
			cartId = cartOpt.get().getId();
		} else {
			Cart cart = new Cart(0, userId, LocalDateTime.now(), null);
			cartId = Protector.of(() -> cartService.insert(cart)).get(0L);
		}

		if (cartId > 0) {
			Optional<CartItem> itemOpt = Protector.of(() -> cartItemService.getByCartIdAndProductId(cartId, productId))
					.get(Optional::empty);

			if (itemOpt.isPresent()) {
				CartItem item = itemOpt.get();
				item.setQuantity(item.getQuantity() + qty);
				item.setUpdatedAt(LocalDateTime.now());
				cartItemService.update(item);
			} else {
				CartItem newItem = new CartItem(0, cartId, productId, qty, LocalDateTime.now(), null);
				cartItemService.insert(newItem);
			}

			response.setContentType("text/html");
			response.getWriter().write("<p>Thêm sản phẩm vào giỏ hàng thành công!</p>");
		} else {
			response.setContentType("text/html");
			response.getWriter().write("<p>Không thể tạo giỏ hàng</p>");
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long cartItemId = Protector.of(() -> Long.parseLong(request.getParameter("cartItemId"))).get(0L);
		int newQty = Protector.of(() -> Integer.parseInt(request.getParameter("quantity"))).get(0);

		Optional<CartItem> itemOpt = Protector.of(() -> cartItemService.getById(cartItemId)).get(Optional::empty);

		response.setContentType("text/html");
		if (itemOpt.isPresent()) {
			CartItem item = itemOpt.get();
			item.setQuantity(newQty);
			item.setUpdatedAt(LocalDateTime.now());
			cartItemService.update(item);
			response.getWriter().write("<p>Cập nhật số lượng thành công!</p>");
		} else {
			response.getWriter().write("<p>Không tìm thấy mục giỏ hàng</p>");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long cartItemId = Protector.of(() -> Long.parseLong(request.getParameter("cartItemId"))).get(0L);
		response.setContentType("text/html");

		cartItemService.delete(cartItemId);
		response.getWriter().write("<p>Đã xóa sản phẩm khỏi giỏ hàng!</p>");
	}

}
