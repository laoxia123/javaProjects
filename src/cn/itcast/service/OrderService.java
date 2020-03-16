package cn.itcast.service;


import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import cn.itcast.dao.OrderDao;
import cn.itcast.dao.OrderDaoImple;
import cn.itcast.utils.MyJdbcUtil;
import cn.itcast.vo.Order;
import cn.itcast.vo.OrderItem;
/**
 * 订单的业务类
 * @author Administrator
 *
 */
public class OrderService {

	/**
	 * 保存订单
	 * @param order
	 */
	public void save(Order order) {
		//在订单中，包含订单的信息和包含一些订单项的信息
		//在保存数据的时候，先保存订单的信息
		//还需要保存订单项的信息
		Connection conn = null;
		try{
			//开启事务
			conn = MyJdbcUtil.getConnection();
			conn.setAutoCommit(false);
			//添加操作
			OrderDao dao = new OrderDaoImple();
			//保存订单
			dao.saveOrder(conn,order);
			//循环遍历，获取到多个订单项
			for (OrderItem orderItem : order.getOrderItems()) {
				//保存订单项
				dao.saveOrderItem(conn,orderItem);
			}
			//提交事务
			DbUtils.commitAndCloseQuietly(conn);
		}catch(Exception e){
			//遇到问题了回滚事务
			DbUtils.rollbackAndCloseQuietly(conn);
			e.printStackTrace();
		}
		
	}

	/**
	 * 通过用户的id来查询所有的订单
	 * @param uid
	 * @return
	 */
	public List<Order> findByUid(String uid) {
		OrderDao dao = new OrderDaoImple();
		return dao.findByUid(uid);
	}

	public Order findByOid(String oid) {
		OrderDao dao = new OrderDaoImple();
		return dao.findByOid(oid);
	}

	/**
	 * 修改订单
	 * @param order
	 */
	public void updateOrder(Order order) {
		OrderDao dao = new OrderDaoImple();
		dao.updateOrder(order);
	}

	public List<Order> findAll() {
		OrderDao dao = new OrderDaoImple();
		return dao.findAll();
	}

	public List<Order> findByState(int state) {
		OrderDao dao = new OrderDaoImple();
		return dao.findByState(state);
	}

	public void updateByState(String oid, int state) {
		OrderDao dao = new OrderDaoImple();
		dao.updateByState(oid, state);
	}

}


































