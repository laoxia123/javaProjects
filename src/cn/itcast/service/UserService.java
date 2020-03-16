package cn.itcast.service;

import cn.itcast.dao.UserDao;
import cn.itcast.dao.UserDaoImple;
import cn.itcast.utils.MailUtil;
import cn.itcast.utils.MyUUIDUtil;
import cn.itcast.vo.User;

/**
 * 用户相关的业务类
 * @author Administrator
 *
 */
public class UserService {

	public boolean registUser(User user) {
		//调用持久层代码注册用户
		UserDao dao = new UserDaoImple();
		//添加业务
		//用户主键
		user.setUid(MyUUIDUtil.getUUID());
		//设置状态码为0--未激活的状态
		user.setState(0);
		//生成激活码
		String code = MyUUIDUtil.getUUID()+MyUUIDUtil.getUUID();
		user.setCode(code);
	    //发送一封邮件
        //MailUtil.sendMail(user.getEmail(), code);
		
		return dao.saveUser(user);
		
	}

	public User findUserByCode(String code) {
		UserDao dao = new UserDaoImple();
		return dao.findUserByCode(code);
	}

	public void updateUser(User user) {
		UserDao dao = new UserDaoImple();
		dao.updateUser(user);
	}

	public User login(User user) {
		UserDao dao = new UserDaoImple();
		return dao.login(user);
	}
}



















