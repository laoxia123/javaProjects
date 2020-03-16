package cn.itcast.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.utils.MyJdbcUtil;
import cn.itcast.vo.User;

public class UserDaoImple implements UserDao {

	/*
	 * 注册用户
	 * @see cn.itcast.dao.UserDao#saveUser(cn.itcast.vo.User)
	 */
	public boolean saveUser(User user) {
		boolean flag = false;
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?) ";
		Object[] params = {user.getUid(),user.getUsername(),user.getPassword(),user.getEmail(),user.getState(),user.getCode()};
		
		try {
			int count = runner.update(sql, params);
			if(count>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return flag;
	}

	@Override
	public User findUserByCode(String code) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from user where code = ?";
		try {
			return runner.query(sql, new BeanHandler<User>(User.class),code);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 修改用户的所有的信息
	 */
	public void updateUser(User user) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "update user set username = ?,password=?,email=?,state=?,code=? where uid=?";
		Object[] params = {user.getUsername(),user.getPassword()
						,user.getEmail(),user.getState(),user.getCode(),user.getUid()};
		
		try {
			runner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public User login(User user) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from user where username=? and password=? and state=?";
		Object[] params = {user.getUsername(),user.getPassword(),1};
		try {
			
			return runner.query(sql, new BeanHandler<User>(User.class), params);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}

















