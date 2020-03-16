package cn.itcast.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.service.BookService;
import cn.itcast.vo.Book;
import cn.itcast.vo.Cart;
import cn.itcast.vo.CartItem;

/**
 * 购物车的控制器
 * @author Administrator
 *
 */
public class CartServlet extends BaseServlet {

	private static final long serialVersionUID = 8296333040350323130L;

	public String addCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//从session中获取购物车
		Cart cart = getCart(request);
		String sCount = request.getParameter("count");
		//购买数量
		int count = Integer.parseInt(sCount);
		//先把购物项准备好，直接把购物项添加到购物车中
		CartItem item = new CartItem();
		//数量和小计都有了
		item.setCount(count);
		//获取book,通过book的主键来获取
		String bookId = request.getParameter("bookId");
		//去数据库中通过图书的主键查询来获取
		BookService bs = new BookService();
		Book book = bs.findByBid(bookId);
		//在购物车中存入书的信息
		item.setBook(book);
		//向cart中存入购物项
		cart.addCart(item);
		return "/jsps/cart/list.jsp";
	}

	public Cart getCart(HttpServletRequest request){
		//从session中获取购物车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		//说明第一次访问，没有购物车，创建一个购物车，再存到session中
		if(cart == null){
			cart = new Cart();
			//存入到session中
			request.getSession().setAttribute("cart", cart);
		}
		return cart;
	}
	
	/**
	 * 移除指定的购物项
	 * @param request
	 * @return
	 */
	public String removeCart(HttpServletRequest request, HttpServletResponse response){
		//接收数据
		String bookId = request.getParameter("bid");
		//获取购物车
		Cart cart = getCart(request);
		//移除购物车中的某个购物项
		cart.removeCart(bookId);
		
		return "/jsps/cart/list.jsp";
	}
		
	/**
	 * 清空购物车
	 * @param request
	 * @param response
	 * @return
	 */
	public String clearCart(HttpServletRequest request, HttpServletResponse response){
		//获取购物车
		Cart cart = getCart(request);
		//移除购物车中的某个购物项
		cart.clearCart();
		
		return "/jsps/cart/list.jsp";
	}
			
		
}






















