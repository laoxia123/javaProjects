## 网上商城 ##
	
**需求分析**
	
	1.整个项目是分成前台页面和后台管理两部分组成。
		* 前台：提供用户浏览，购买，查看订单等功能。
		* 后台：提供管理员管理商城，提供添加图书、分类等功能。
		
	2.根据网上商城的需求分成以下几个模块。
		1.用户模块
			* 前台：
				1.注册功能（包含发送一封邮件给用户，用户需要点击激活链接才能登陆）
				2.激活链接功能（注册后，点击链接激活后，才能登陆成功 -- 实现方式可以修改用户的状态（可以规定0代表没有激活，1代码已经激活））
				3.登陆功能（注册成功后，可以登陆--实现方式根据用户名和密码以及用户状态（1状态代表已经激活））
				4.退出功能（用户登陆成功，会把用户信息存入到session中，通过销毁session完成退出功能）
				
			* 后台：
				1.登陆后台功能
				
		2.分类模块
			* 前台：
				1.查询分类的功能
			
			* 后台：
				1.查看分类功能
				2.增加分类功能
				3.修改分类功能
				4.删除分类功能
			
		3.商品模块	
			* 前台：
				1.查询所有分类的商品
				2.查询某一个分类的商品
				3.查询某个商品的详情

			* 后台：
				1.添加商品（上传商品图片）
				2.修改商品
				3.删除商品
				4.查询商品
			
		4.订单模块
			* 前台
				1.生成订单
				2.按用户查找订单
				3.查看某个订单
			
			* 后台
				1.查询所有订单
				2.查询某个状态订单
			
			
**页面设计**

	1.依据页面原型进行开发。

**数据库的设计**
	
	一个实体就会对应一个数据库中表.
	* 用户:
		* 编号（主键）
		* 用户名
		* 密码
		* 邮箱
		* 激活码（发送邮件时显示的激活码）
		* 状态	0:未激活		1:已经激活
		
	* 分类
		* 编号（主键）
		* 名称
	
	* 图书
		* 编号（主键）
		* 名称
		* 作者
		* 价格
		* 图片（存放图片的地址）
		* 分类编号（外键） -- 和分类是一对多的关系，所以在图书表中创建外键指向分类表中的主键
		
	* 订单
		* 编号（主键）
		* 时间
		* 金额
		* 地址
		* 状态
		* 用户编号（外键）-- 和用户是一对多的关系，所以在订单表中创建外键指向用户表中的主键
		
	* 订单图书中间表:
		* 订单项编号（主键）
		* 图书编号（外键）-- 和图书表是一对多的关系，所以在订单图书中间表中创建外键指向图书表中的主键
		* 订单编号（外键）-- 和订单表是一对多的关系，所以在订单图书中间表中创建外键指向订单表中的主键
		* 数量
		* 小计


**编写代码**

**维护**


**编写通用的BaseServlet（扩展内容）**
	
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置编码
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		// 就可以获取method的参数，根据值来判断具体调用哪个方法
		String method = req.getParameter("method");
		// 如果method为空的话，不让向下执行了，抛出异常
		if(method == null || method.trim().isEmpty()){
			throw new RuntimeException("亲，您需要传入method参数");
		}
		// 用过反射的技术来让方法执行，通过反射的技术获取到Method对象，invoke方法，需要先获取class对象
		Class c = this.getClass();
		// 获取方法了
		Method m = null;
		try {
			m = c.getMethod(method, HttpServletRequest.class,HttpServletResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("亲，您传入的"+method+"方法不存在！！");
		}
		// 肯定找到了用户传入的方法，执行该方法
		try {
			String result = (String) m.invoke(this, req,resp);
			if(result != null && !result.trim().isEmpty()){
				// 进行转发
				req.getRequestDispatcher(result).forward(req, resp);
			}
		} catch (Exception e) {
			System.out.println("程序内部报错了");
			throw new RuntimeException(e);
		}
	}



**邮件**
	
	1.申请的邮件账号：javamail_day@163.com 	密码：123abc
	
**开发的环境的准备**
	
	1.创建estore的项目，把原型文件拷贝进去。
	2.架构：Servlet+JSP+JavaBean+JDBC
	3.导入jar包（10个jar包）
	4.创建包结构
	
**进行开发**
	
	
**编写BaseServlet**
	
	1.一个模块就使用一个Servlet。例子：用户的模块，UserServlet -- 增删改查所有的。
	
**用户的模块**	
	
	1.创建表结构
	create database estore;
	use estore;
	CREATE TABLE `user` (
	  `uid` char(32) NOT NULL,
	  `username` varchar(50) NOT NULL,
	  `password` varchar(50) NOT NULL,
	  `email` varchar(50) NOT NULL,
	  `state` tinyint(1) DEFAULT NULL,
	  `code` varchar(64) DEFAULT NULL,
	  PRIMARY KEY (`uid`),
	  UNIQUE KEY `username` (`username`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	
**注册的功能**
	
	1.用户的注册
		* 用户输入的数据（用户名、密码和邮箱）
		* 主键
		* 把用户的默认状态设置成0（未激活的状态）
		* 默认生成激活码code字段
		* 需要给注册的用户的邮箱发送一封邮件
		
	2.流程 regist.jsp --> UserServlet（指定regist方法的名称） -->调用UserService --> 调用UserDao	
	
	
**邮件的功能**
	
	1.邮件的服务器
		* 安装提供的邮件的服务器。
	2.电子邮箱
		* aaa@sina.com.cn
	3.邮件的协议
		* 发送邮件协议	SMTP协议
		* 接收邮件协议	POP3协议
			* IMAP协议（不是所有的邮箱都支持）
		
	4.安装邮件的服务器
		* 安装服务器
		* 配置邮件服务器  点击设置 -- 选择单域名 -- 修改域名（estore.com）
		* 申请账号：点击新账号
		
	5.安装邮件的客户端
		* 网页版		-- 以前的版本登陆后失效的时间。
		* 客户端软件	-- 不是有该问题。默认的时间去接收邮件。
		
		* 安装软件 -- 一路继续
		* 配置软件：
	
**邮件的发送**
	
	程序报错了，异常信息java.lang.NoClassDefFoundError: com/sun/mail/util/LineInputStream，找不到类。
	产生原因：myeclipse自带javaee jar包 与 javamail的jar冲突了造成的。
	解决办法：可以把myeclipse自带javaee的jar删掉。
	解决步骤：window--首选项--搜索lib--library-sets-javaee.jar--选择add--用解压缩的软件打开--删除(activation和mail两个包)。
	
**邮件激活用户状态**
	
	1.从连接发送code的激活码。
	2.在数据库中保存了一个激活码。
	3.获取到get请求发送过来的激活码，去数据库中进行查询，如果查询到了返回user，修改用户的状态为state=1（激活状态）。
	
	
**完成登陆的功能**
	
	1.用户名和密码和数据库的一致。
	2.规定了状态码必须得1.
	
	
**查询分类的菜单的功能**
	
	1.分类的表结构
		CREATE TABLE `category` (
		  `cid` char(32) NOT NULL,
		  `cname` varchar(50) DEFAULT NULL,
		  PRIMARY KEY (`cid`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
		
		
	1.1向表结构中添加数据
		INSERT INTO `category` VALUES ('1', '玄幻');
		INSERT INTO `category` VALUES ('2', '言情');
		INSERT INTO `category` VALUES ('4', '恐怖');
		INSERT INTO `category` VALUES ('5', '武侠');
		INSERT INTO `category` VALUES ('6', '科技');
		INSERT INTO `category` VALUES ('ef32d6d638084c2ba47df91b740637aa', 'JAVAEE');
		
	2.创建分类的实体对象
	3.编写代码
		* 先找main.jsp -- 修改<iframe frameborder="0" width="120" src="" name="left"></iframe>的src的属性，访问后台的CategoryServlet
		
		
**图书的模块**
	
	1.创建图书表结构，添加数据。
		CREATE TABLE `book` (
		  `bid` char(32) NOT NULL,
		  `bname` varchar(100) DEFAULT NULL,
		  `price` decimal(10,2) DEFAULT NULL,
		  `author` varchar(50) DEFAULT NULL,
		  `image` varchar(200) DEFAULT NULL,
		  `cid` char(32) DEFAULT NULL,
		  `isdel` tinyint(1) DEFAULT NULL,
		  PRIMARY KEY (`bid`),
		  KEY `cid` (`cid`),
		  CONSTRAINT `book_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `category` (`cid`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
		
	2.添加数据（在添加数据之前，先向项目中拷贝图片  -- book_img）
		INSERT INTO `book` VALUES ('001', '封神榜', '50.00', '姜子牙', 'book_img/xh1.jpg', '1', '0');
		INSERT INTO `book` VALUES ('002', '剑逆苍穹', '40.00', '大法官', 'book_img/xh2.jpg', '1', '0');
		INSERT INTO `book` VALUES ('003', '薄环凉色', '28.00', '甄嬛', 'book_img/yq1.jpg', '2', '0');
		INSERT INTO `book` VALUES ('004', '旋风侠盗', '61.00', '阿园', 'book_img/wx1.jpg', '5', '0');
		INSERT INTO `book` VALUES ('005', '你懂我的爱', '73.00', '莫言', 'book_img/yq2.jpg', '2', '0');
		INSERT INTO `book` VALUES ('006', '东归喋血', '48.00', '张瑞峰', 'book_img/wx2.jpg', '5', '0');
		INSERT INTO `book` VALUES ('007', '守夜', '62.00', '斯蒂芬金', 'book_img/kb1.jpg', '4', '0');
		INSERT INTO `book` VALUES ('008', '日本恐怖小说选', '39.00', '村山魁多', 'book_img/kb2.jpg', '4', '0');
		INSERT INTO `book` VALUES ('009', 'Java培训就业教程', '58.00', '张孝祥', 'book_img/kj1.jpg', '6', '0');
		INSERT INTO `book` VALUES ('010', '精通Hibernate', '67.00', '孙鑫', 'book_img/kj2.jpg', '6', '0');
		INSERT INTO `book` VALUES ('011', 'Java编程思想', '108.00', 'James', 'book_img/kj4.jpg', '6', '0');
		INSERT INTO `book` VALUES ('012', 'Struts2详解', '42.00', '涛哥', 'book_img/kj8.jpg', '6', '0');
		INSERT INTO `book` VALUES ('013', 'JavaWeb开发详解', '32.00','涛哥', 'book_img/kj5.jpg', '6', '0');
		
		
	3.前台：
		1.查询所有分类的商品
		2.查询某一个分类的商品
		3.查询某个商品的详情
		
	4.创建实体  Servlet 对象

	5.查询所有分类的商品
	
	6.按分类查询该分类对应的图书
		* select * from book where cid = ? and isdel = 0;
		* 在left.jsp中编写请求的路径的时候，需要传递分类的主键（cid）
	
	7.查询某个图书的详细信息
	
	
**购物车的模块（存放session中）**
	
	1.创建和购物车相关的实体
		* 购物项  -- CartItem
			* 包含书的信息  Book book;	
			* 包含数量		count
			* 包含小计		subtotal
			
		* 购物车	-- Cart
			* 包含总计				total
			* 包含多个购物项		Map<String,CartItem> map  = new HashMap();
			* 包含方法
				* 向购物车添加购物项
				* 删除某一个购物项
				* 清空购物车
		
	2.创建购物车的相关Servlet service


**订单模块**
	
	1.生成订单的功能
	2.点击我的订单的功能，查询该用户相关的订单。
	3.点击付款，查询该订单的信息
	
	4.创建订单的表结构
		CREATE TABLE `orders` (
		  `oid` char(32) NOT NULL,
		  `total` decimal(10,2) DEFAULT NULL,
		  `ordertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		  `state` int(11) DEFAULT NULL,
		  `address` varchar(100) DEFAULT NULL,
		  `uid` char(32) DEFAULT NULL,
		  PRIMARY KEY (`oid`),
		  KEY `uid` (`uid`),
		  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
		
	5.图书和订单是多对多的关系。创建中间表，在中间至少有两个字段，分别作为外键指向图书和订单表的主键。又添加了两个字段，一个是数量的字段还有一个小计的字段。给中间表添加一个主键。
		CREATE TABLE `orderitem` (
		  `itemid` char(32) NOT NULL,
		  `count` int(11) DEFAULT NULL,
		  `subtotal` decimal(10,2) DEFAULT NULL,
		  `bid` char(32) DEFAULT NULL,
		  `oid` char(32) DEFAULT NULL,
		  PRIMARY KEY (`itemid`),
		  KEY `bid` (`bid`),
		  KEY `oid` (`oid`),
		  CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`bid`) REFERENCES `book` (`bid`),
		  CONSTRAINT `orderitem_ibfk_2` FOREIGN KEY (`oid`) REFERENCES `orders` (`oid`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	6.编写订单和订单项的实体
		
		
	7.生成订单的功能
		
		
	8.点击我的订单的功能，查询的功能。
		
	9.点击付款的时候，需要把当前的订单信息全部都查询出来，显示到付款详细页面。
		* OrderServlet（当前订单的编号传过来，数据库查询当前的订单，显示到详细页面。）
	
	
**在线支付功能**


## 后台的代码 ##

**分类的后台**

	1.分类下如果包含图书，分类不能删除的。如果不包含图书，可以删除的。
		* 分类下包含图书（isdel值0或者1）
	2.创建图书的表结构的时候，isdel字段。0代表有图书，1代表的图书已经删除。


**查询所有图书（分页）后台**
	
	1.查询所有的图书的功能（分页）
	2.封装PageBean对象


**添加图书（上传文件）**
	
	1.使用fileupload文件上传，request.getParameter();不能使用了。不能使用BaseServlet，创建一个新的Servlet。
	
	
**订单模块**
	
	1.订单下包含订单项（OrderItem） 订单向下又包含书的信息
	


	
	

	
		
		
		


	