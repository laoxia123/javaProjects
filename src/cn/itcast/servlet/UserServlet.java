package cn.itcast.servlet;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.itcast.service.UserService;
import cn.itcast.vo.User;
/**
 * 和用户相关的servlet
 * @author Administrator
 *
 */
public class UserServlet extends BaseServlet {

	/**
	 * 注册用户的方法
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String registUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//接收数据
		Map<String, String []> map = request.getParameterMap();
		//封装数据
		User user = new User();
		try {
			BeanUtils.populate(user, map);
			//处理数据
			UserService us = new UserService();
			//注册用户
			boolean flag = us.registUser(user);
			//根据返回的结果来调用不同的页面进行处理
			if(flag == false){
				request.setAttribute("msg", "注册失败！！");
			}else{
				request.setAttribute("msg", "注册成功，请您激活！！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//处理数据
		//显示数据
		return "/jsps/msg.jsp";
	}

	public String active(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取激活码
		String code = request.getParameter("code");
		//处理数据
		UserService us = new UserService();
		User user = us.findUserByCode(code);
		//如果用户为空，说明没有查到用户
		if(user==null){
			request.setAttribute("msg", "激活失败了");
		}else{
			//修改用户的状态，状态默认值0，修改成1
			user.setState(1);
			//再调用UserService修改方法
			us.updateUser(user);
			//设置信息
			request.setAttribute("msg", "激活成功了，请您登陆");
		}
		return "/jsps/msg.jsp";
	}
	
	/**
	 * 处理登录的功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//接收数据
		Map<String, String[]> map = request.getParameterMap();
		//封装数据
		User user = new User();
		try {
			BeanUtils.populate(user, map);
			//处理数据
			UserService us = new UserService();
			User existUser = us.login(user);
			//登录失败，给出提示
			if(existUser==null){
				request.setAttribute("msg", "用户名或者密码错误了或者未激活");
				return "/jsps/msg.jsp";
			}else{
				//如果登录成功了，把用户信息保存到session中，转发到首页
				request.getSession().setAttribute("existUser", existUser);
				//转发到main.jsp页面上
				return "/jsps/main.jsp";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//显示数据
		return null;
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String exit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取session直接销毁
		request.getSession().invalidate();
		return "/jsps/main.jsp";
	}

}
























