package cn.itcast.dao;

import cn.itcast.vo.User;

public interface UserDao {

	boolean saveUser(User user);

	User findUserByCode(String code);

	void updateUser(User user);

	User login(User user);

}
