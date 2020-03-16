package cn.itcast.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.service.BookService;
import cn.itcast.service.CategoryService;
import cn.itcast.utils.MyUUIDUtil;
import cn.itcast.vo.Book;
import cn.itcast.vo.Category;
/**
 * 分类的控制器
 * @author Administrator
 *
 */
public class CategoryServlet extends BaseServlet {

	private static final long serialVersionUID = 7523447216890672217L;

	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//调动业务层的代码
		CategoryService cs = new CategoryService();
		//查询所有的分类
		List<Category> cList = cs.findAll();
		//存到request域中，转发到/jsps/left.jsp
		request.setAttribute("cList", cList);
		return "/jsps/left.jsp";
	}

	/**
	 * 后台查询所有分类的代码
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllAdmin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//调动业务层的代码
		CategoryService cs = new CategoryService();
		//查询所有的分类
		List<Category> cList = cs.findAll();
		//存到request域中，转发到/jsps/left.jsp
		request.setAttribute("cList", cList);
		return "/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addCategoryAdmin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取数据
		String cname = request.getParameter("cname");
		Category c = new Category();
		c.setCid(MyUUIDUtil.getUUID());
		c.setCname(cname);
		//调动业务层的代码
		CategoryService cs = new CategoryService();
		cs.save(c);
		return findAllAdmin(request,response);
	}
	
	/**
	 * 修改分类-先查询到该分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String initUpdateAdmin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取数据
		String cid = request.getParameter("cid");
		//调动业务层的代码
		CategoryService cs = new CategoryService();
		Category category = cs.findByCid(cid);
		//向request域中存入
		request.setAttribute("category", category);
		return "/adminjsps/admin/category/mod.jsp";
	}
	
	/**
	 * 真正的修改分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateAdmin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取数据
		String cid = request.getParameter("cid");
		String cname = request.getParameter("cname");
		//封装数据
		Category c = new Category();
		c.setCid(cid);
		c.setCname(cname);
		//调动业务层的代码
		CategoryService cs = new CategoryService();
		cs.update(c);
		
		return findAllAdmin(request,response);
	}
	
	/**
	 * 删除分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteAdmin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取数据
		String cid = request.getParameter("cid");
		//调动业务层的代码
		BookService bs = new BookService();
		List<Book> bList = bs.findByCid(cid);
		if(bList.size() > 0 && bList != null){
			request.setAttribute("msg", "亲，该分类下包含图书，不能删除！！");
		}else{
			//分类下没有书
			CategoryService cs = new CategoryService();
			//修改book的cid=null where cid=cid and isdel=1
			bs.updateByCid(cid);
			//删除分类
			cs.delete(cid);
		}
		
		return findAllAdmin(request,response);
	}

}





















