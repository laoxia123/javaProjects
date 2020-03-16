package cn.itcast.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.utils.MyJdbcUtil;
import cn.itcast.vo.Book;
import cn.itcast.vo.Order;
import cn.itcast.vo.OrderItem;

public class OrderDaoImple implements OrderDao {


	/**
	 * 保存订单
	 */
	public void saveOrder(Connection conn, Order order) {
		QueryRunner runner = new QueryRunner();
		String sql = "insert into orders values(?,?,?,?,?,?)";
		Object[] params = {order.getOid(),order.getTotal(),
							order.getOrdertime(),order.getState(),
							order.getAddress(),order.getUser().getUid()};
		try {
			runner.update(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 保存订单项
	 */
	public void saveOrderItem(Connection conn, OrderItem orderItem) {
		QueryRunner runner = new QueryRunner();
		String sql = "insert into orderItem values(?,?,?,?,?)";
		Object[] params = {orderItem.getItemid(),orderItem.getCount()
							,orderItem.getSubtotal(),orderItem.getBook().getBid()
							,orderItem.getOrder().getOid()};
		try {
			runner.update(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Order> findByUid(String uid) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from orders where uid= ? ";
		List<Order> list;
		try {
			list = runner.query(sql, new BeanListHandler<Order>(Order.class), uid);
			//循环遍历
			for (Order order : list) {
				String oid = order.getOid();
				String sql2 = "select * from orderItem o ,book b where o.bid=b.bid and o.oid=?";
				//怎么封装数据 一条记录封装到一个map集合中，再把所有的map存入到一个LIst集合中
				//map集合的key是字段名称，map的值是字段对应的值一个map就是一条记录
				List<Map<String,Object>> obList = runner.query(sql2, new MapListHandler(), oid);
				//把obList里面的数据封装到OrderItem对象中
				for (Map<String, Object> map : obList) {
					OrderItem orderItem = new OrderItem();
					//封装数据
					BeanUtils.populate(orderItem, map);
					Book book = new Book();
					BeanUtils.populate(book, map);
					//拼接成了OrderItem对象
					orderItem.setBook(book);
					order.getOrderItems().add(orderItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return list;
	}

	/*
	 * 通过订单的编号查询该订单
	 * @see cn.itcast.dao.OrderDao#findByOid(java.lang.String)
	 */
	public Order findByOid(String oid) {
		// 查询当前的订单
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from orders where oid=?";
		Order order;
		try {
			order = runner.query(sql, new BeanHandler<Order>(Order.class), oid);
			String sql2 = "select * from orderItem o ,book b where o.bid=b.bid and o.oid=?";
			//怎么封装数据 一条记录封装到一个map集合中，再把所有的map存入到一个LIst集合中
			//map集合的key是字段名称，map的值是字段对应的值一个map就是一条记录
			List<Map<String,Object>> obList = runner.query(sql2, new MapListHandler(), oid);
			//把obList里面的数据封装到OrderItem对象中
			for (Map<String, Object> map : obList) {
				OrderItem orderItem = new OrderItem();
				//封装数据
				BeanUtils.populate(orderItem, map);
				Book book = new Book();
				BeanUtils.populate(book, map);
				//拼接成了OrderItem对象
				orderItem.setBook(book);
				order.getOrderItems().add(orderItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return order;
	}

	/**
	 * 修改订单的属性
	 */
	public void updateOrder(Order order) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "update orders set address=?,state=? where oid=?";
		try {
			runner.update(sql, order.getAddress(),order.getState(),order.getOid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有的订单
	 */
	public List<Order> findAll() {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from orders";
		List<Order> list;
		try {
			list = runner.query(sql, new BeanListHandler<Order>(Order.class));
			//循环遍历
			for (Order order : list) {
				String oid = order.getOid();
				String sql2 = "select * from orderItem o ,book b where o.bid=b.bid and o.oid=?";
				//怎么封装数据 一条记录封装到一个map集合中，再把所有的map存入到一个LIst集合中
				//map集合的key是字段名称，map的值是字段对应的值一个map就是一条记录
				List<Map<String,Object>> obList = runner.query(sql2, new MapListHandler(), oid);
				//把obList里面的数据封装到OrderItem对象中
				for (Map<String, Object> map : obList) {
					OrderItem orderItem = new OrderItem();
					//封装数据
					BeanUtils.populate(orderItem, map);
					Book book = new Book();
					BeanUtils.populate(book, map);
					//拼接成了OrderItem对象
					orderItem.setBook(book);
					order.getOrderItems().add(orderItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return list;
	}

	@Override
	public List<Order> findByState(int state) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from orders where state = ?";
		List<Order> list;
		try {
			list = runner.query(sql, new BeanListHandler<Order>(Order.class),state);
			//循环遍历
			for (Order order : list) {
				String oid = order.getOid();
				String sql2 = "select * from orderItem o ,book b where o.bid=b.bid and o.oid=?";
				//怎么封装数据 一条记录封装到一个map集合中，再把所有的map存入到一个LIst集合中
				//map集合的key是字段名称，map的值是字段对应的值一个map就是一条记录
				List<Map<String,Object>> obList = runner.query(sql2, new MapListHandler(), oid);
				//把obList里面的数据封装到OrderItem对象中
				for (Map<String, Object> map : obList) {
					OrderItem orderItem = new OrderItem();
					//封装数据
					BeanUtils.populate(orderItem, map);
					Book book = new Book();
					BeanUtils.populate(book, map);
					//拼接成了OrderItem对象
					orderItem.setBook(book);
					order.getOrderItems().add(orderItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return list;
	}
	
	/**
	 * 修改订单的状态
	 * @param oid
	 * @param state
	 */
	public void updateByState(String oid,int state){
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());;
		String sql = "update orders set state =? where oid=?";
		try {
			runner.update(sql, state,oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}



















