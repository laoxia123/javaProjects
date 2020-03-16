package cn.itcast.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7510809537032738047L;

	public void service(HttpServletRequest req,HttpServletResponse resp)
			throws ServletException,IOException{
		//设置编码
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		//获取method参数
		String methodName = req.getParameter("method");
		//如果methodName忘了传入
		if(methodName == null || methodName.trim().isEmpty()){
			throw new RuntimeException("亲，请您传入method参数！！");
		}
		
		//通过反射来获取method对象
		Class clazz = this.getClass();
		
		//通过clazz获取method对象
		Method method = null;
		try {
			method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String result = (String) method.invoke(this, req,resp);
			if(result != null && !result.trim().isEmpty()){
				req.getRequestDispatcher(result).forward(req, resp);
			}
		} catch (Exception e) {
			System.out.println("程序内部错误！！");
			throw new RuntimeException(e);
		}
	}
}
























