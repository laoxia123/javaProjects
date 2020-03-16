package cn.itcast.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.itcast.service.BookService;
import cn.itcast.service.CategoryService;
import cn.itcast.vo.Book;
import cn.itcast.vo.Category;
import cn.itcast.vo.PageBean;

public class BookServlet extends BaseServlet {

	private static final long serialVersionUID = 4485461378695529458L;
	/**
	 * 查询所有分类的图书
	 * @param request
	 * @param response
	 * @return
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response){
		//调用业务层的代码
		BookService bs = new BookService();
		List<Book> bookList = bs.findAll();
		//存入到request域中
		request.setAttribute("bookList", bookList);
		return "/jsps/book/list.jsp";
	}
	
	public String findByCid(HttpServletRequest request, HttpServletResponse response){
		//接收分类的主键
		String cid = request.getParameter("cid");
		//调用业务层的代码
		BookService bs = new BookService();
		List<Book> bookList = bs.findByCid(cid);
		//存入到request域中
		request.setAttribute("bookList", bookList);
		return "/jsps/book/list.jsp";
	}
	
	public String findByBid(HttpServletRequest request, HttpServletResponse response){
		//接收分类的主键
		String bid = request.getParameter("bid");
		//调用业务层的代码
		BookService bs = new BookService();
		Book book = bs.findByBid(bid);
		//存入到request域中
		request.setAttribute("book", book);
		return "/jsps/book/desc.jsp";
	}
	
	/**
	 * 分页查询所有的图书
	 * @param request
	 * @param response
	 * @return
	 */
	public String findByPage(HttpServletRequest request, HttpServletResponse response){
		//从页面获取当前页
		int pageCode = getPageCode(request);
		//定义每页显示的数据条数
		int pageSize = 3;
		BookService bs = new BookService();
		//分页查询
		//page = pageCode=当前页(从页面获取) pageSize=一页显示多少条数据(自定义)
		//totalCount = 总记录数(从数据库查)  beanList=查询图书的真实数据(从数据库中查询 limit)
		
		PageBean<Book> page = bs.findByPage(pageCode,pageSize);
		request.setAttribute("page", page);
		return "/adminjsps/admin/book/list.jsp";
	}

	public int getPageCode(HttpServletRequest request){
		String pc = request.getParameter("pc");
		if(pc == null || pc.trim().isEmpty()){
			return 1;
		}
		return Integer.parseInt(pc);
	}
	
	public String initAddBook(HttpServletRequest request, HttpServletResponse response){
		CategoryService cs = new CategoryService();
		List<Category> cList = cs.findAll();
		request.setAttribute("cList", cList);
		return "/adminjsps/admin/book/add.jsp";
	}
	
	/**
	 * 查询图书的详细信息(后台的功能)
	 * @param request
	 * @param response
	 * @return
	 */
	public String findByBidAdmin(HttpServletRequest request, HttpServletResponse response){
		//接收分类的主键
		String bid = request.getParameter("bid");
		//调用业务层的代码
		BookService bs = new BookService();
		Book book = bs.findByBid(bid);
		//获取所有的分类的信息
		CategoryService cs = new CategoryService();
		List<Category> cList = cs.findAll();
		//存入到request域中
		request.setAttribute("book", book);
		request.setAttribute("cList", cList);
		return "/adminjsps/admin/book/desc.jsp";
	}
	
	/**
	 * 修改图书
	 * @param request
	 * @param response
	 * @return
	 */
	public String mod(HttpServletRequest request, HttpServletResponse response){
		//接收数据
		Map<String, String[]> map = request.getParameterMap();
		Book book = new Book();
		try {
			//封装用户修改的信息
			BeanUtils.populate(book, map);
			//修改数据库
			BookService bs = new BookService();
			bs.updateBook(book);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return findByPage(request,response);
	}
	
	public String del(HttpServletRequest request, HttpServletResponse response){
		//接收数据
		String bid = request.getParameter("bid");
		
		//修改数据库
		BookService bs = new BookService();
		bs.deleteByBid(bid);
		return findByPage(request,response);
	}
}




















