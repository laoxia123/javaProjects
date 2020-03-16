package cn.itcast.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.service.OrderService;
import cn.itcast.utils.MyUUIDUtil;
import cn.itcast.utils.PaymentUtil;
import cn.itcast.vo.Cart;
import cn.itcast.vo.CartItem;
import cn.itcast.vo.Order;
import cn.itcast.vo.OrderItem;
import cn.itcast.vo.User;

/**
 * 订单相关的控制器
 * @author Administrator
 *
 */
public class OrderServlet extends BaseServlet {

	private static final long serialVersionUID = -7677669474860634352L;

	/**
	 * 生成订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */

	public String createOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//把order所有的数据先准备好（封装好）
		Order order = new Order();
		//设置订单的主键
		order.setOid(MyUUIDUtil.getUUID());
		//地址
		order.setAddress(null);
		//生成订单时间
		order.setOrdertime(null);
		//订单状态 1：未付款 2：已付款，未发货 3：已经发货，未确认收货 4：订单结束
		order.setState(1);
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart == null){
			//给用户一些提示
			request.setAttribute("msg", "您的购物车为空");
			return "/jsps/msg.jsp";
		}
		//total代表的是：总计 购物车中包含总计
		order.setTotal(cart.getTotal());
		//如果登录成功了，把用户的信息存入到session中
		User existUser = (User) session.getAttribute("existUser");
		if(existUser == null){
			request.setAttribute("msg", "您先登录！！");
			return "/jsps/msg.jsp";
		}
		order.setUser(existUser);
		//设置订单项
		//订单项和购物项是相同的
		//获取购物项
		for(CartItem cartItem:cart.getCartItems()){
			//把购物项(CartItem)中的数据封装到订单项(OrderItem)中
			OrderItem orderItem = new OrderItem();
			//订单项的主键
			orderItem.setItemid(MyUUIDUtil.getUUID());
			//设置数量
			orderItem.setCount(cartItem.getCount());
			//设置小计
			orderItem.setSubtotal(cartItem.getSubTotal());
			//设置订单
			orderItem.setOrder(order);
			//设置图书信息
			orderItem.setBook(cartItem.getBook());
			//把订单项添加到订单的list集合中
			order.getOrderItems().add(orderItem);
		}
		//把订单保存到数据库中
		OrderService os = new OrderService();
		//一执行，把订单全部保存到数据库中
		os.save(order);
		//清空购物车人
		cart.clearCart();
		//去数据库
		order = os.findByOid(order.getOid());
		//把订单保存到域中，转发到页面上
		request.setAttribute("order", order);
		return "/jsps/order/desc.jsp";
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByUid(HttpServletRequest request, HttpServletResponse response)
			{
		//从session中获取用户的id
		User user = (User) request.getSession().getAttribute("existUser");
		String uid = user.getUid();
		//调用业务层的代码
		OrderService os = new OrderService();
		List<Order> oList = os.findByUid(uid);
		//存到request域中
		request.setAttribute("oList", oList);
		return "/jsps/order/list.jsp";
	}

	/**
	 * 根据订单的编号查询当前订单的所有信息
	 * 订单：订单本身的信息，包含多个订单项的信息，订单项中包含图书的信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByOid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//接收参数
		String oid = request.getParameter("oid");
		//通过订单编号查询订单
		OrderService os = new OrderService();
		Order order = os.findByOid(oid);
		request.setAttribute("order", order);
		return "/jsps/order/desc.jsp";
	}

	/**
	 * 支付订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String payOrder(HttpServletRequest request,HttpServletResponse response){
		//接收参数
		String oid = request.getParameter("oid");
		//收货地址
		String address = request.getParameter("address");
		OrderService os = new OrderService();
		Order order = os.findByOid(oid);
		//添加地址
		order.setAddress(address);
		//更新订单
		os.updateOrder(order);
		//接收银行
		String pd_FrpId = request.getParameter("pd_FrpId");
		String  p0_Cmd= "Buy";
		String  p1_MerId= "10001126856";
		String  p2_Order= oid;
		String  p3_Amt= "0.01";
		String  p4_Cur= "CNY";
		String  p5_Pid= "";
		String  p6_Pcat= "";
		String  p7_Pdesc= "";
		String  p8_Url= "http://localhost:8080/estore1/order?method=callBack";//回调函数
		String  p9_SAF= "";
		String  pa_MP= "";
		//支付通道编码
//		String pd_FrpId = request.getParameter("pd_FrpId");
		String  pr_NeedResponse= "1";
		//生成hmac码，传到易宝
		//hmac=参数+秘钥+算法
		//keyValue=69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl
		String keyValue="69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
		String  hmac= PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, 
									p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		//重定向到易宝
		StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=").append(p0_Cmd).append("&");
		sb.append("p1_MerId=").append(p1_MerId).append("&");
		sb.append("p2_Order=").append(p2_Order).append("&");
		sb.append("p3_Amt=").append(p3_Amt).append("&");
		sb.append("p4_Cur=").append(p4_Cur).append("&");
		sb.append("p5_Pid=").append(p5_Pid).append("&");
		sb.append("p6_Pcat=").append(p6_Pcat).append("&");
		sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		sb.append("p8_Url=").append(p8_Url).append("&");
		sb.append("p9_SAF=").append(p9_SAF).append("&");
		sb.append("pa_MP=").append(pa_MP).append("&");
		sb.append("pd_FrpId=").append(pd_FrpId).append("&");
		sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sb.append("hmac=").append(hmac);
		try {
			response.sendRedirect(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//支付成功了，回调该方法
	public String callBack(HttpServletRequest request,HttpServletResponse response){
		String r6_Order = request.getParameter("r6_Order");
		String r3_Amt = request.getParameter("r3_Amt");
		String r9_BType = request.getParameter("r9_BType");
		if("1".equals(r9_BType)){
			//支付成功了，修改订单的状态 把1状态改成2状态
			OrderService os = new OrderService();
			Order order = os.findByOid(r6_Order);
			order.setState(2);
			//再去修改数据库
			os.updateOrder(order);
		}
		request.setAttribute("msg", "亲，您的订单号为"+r6_Order+"已经支付成功了，金额为"+r3_Amt+"元");
		return "/jsps/msg.jsp";
	}
	
	/**
	 * 查询所有订单
	 * @param request
	 * @param response
	 * @return
	 */
	public String findAllAdmin(HttpServletRequest request,HttpServletResponse response){
		OrderService os = new OrderService();
		List<Order> oList = os.findAll();
		request.setAttribute("oList", oList);
		return "/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 根据状态查询订单
	 * @param request
	 * @param response
	 * @return
	 */
	public String findByState(HttpServletRequest request,HttpServletResponse response){
		//接收订单的状态
		int state = Integer.parseInt(request.getParameter("state"));
		OrderService os = new OrderService();
		List<Order> oList = os.findByState(state);
		request.setAttribute("oList", oList);
		return "/adminjsps/admin/order/list.jsp";
	}
	
	
	/**
	 * 发货的方法
	 * @param request
	 * @param response
	 * @return
	 */
	public String updateByStateAdmin(HttpServletRequest request,HttpServletResponse response){
		//修改状态就OK
		String oid = request.getParameter("oid");
		//自己定义参数
		int state = 3;
		OrderService os = new OrderService();
		os.updateByState(oid,state);
		return findAllAdmin(request,response);
	}
	
	
	/**
	 * 前台修改订单的状态
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public String updateByState(HttpServletRequest request,HttpServletResponse response) {
		//接收订单的主键
		String oid = request.getParameter("oid");
		//可以确认收获了，订单结束了
		int state = 4;
		OrderService os = new OrderService();
		os.updateByState(oid, state);
		
		return findByUid(request,response);
	}
	
	
}































