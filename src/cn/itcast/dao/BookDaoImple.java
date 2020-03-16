package cn.itcast.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.utils.MyJdbcUtil;
import cn.itcast.vo.Book;
import cn.itcast.vo.PageBean;

public class BookDaoImple implements BookDao {


	public List<Book> findAll() {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from book where isdel=?";
		try {
			return runner.query(sql, new BeanListHandler<Book>(Book.class), 0);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Book> findByCid(String cid) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from book where cid=? and isdel=?";
		try {
			return runner.query(sql, new BeanListHandler<Book>(Book.class), cid,0);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Book findByBid(String bid) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "select * from book where bid=?";
		try {
			return runner.query(sql, new BeanHandler<Book>(Book.class), bid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateByCid(String cid) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "update book set cid=null where cid=? and isdel=1";
		try {
			runner.update(sql, cid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 分页查询所有图书
	 */
	public PageBean<Book> findByPage(int pageCode, int pageSize) {
		//创建
		PageBean<Book> page = new PageBean<Book>();
		//设置值
		page.setPageCode(pageCode);
		page.setPageSize(pageSize);
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		try {
			String countSql = "select count(*) from book where isdel=0";
			long count = (Long) runner.query(countSql, new ScalarHandler());
			int totalCount = (int) count;
			page.setTotalCount(totalCount);
			String limitSql = "select * from book where isdel = 0 limit ?,?";
			List<Book> beanList = runner.query(limitSql, new BeanListHandler<Book>(Book.class), (pageCode-1)*pageSize,pageSize);
			page.setBeanList(beanList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return page;
	}

	/**
	 * 添加图书
	 */
	public void save(Book book) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "insert into book values(?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getBname(),book.getPrice(),
							book.getAuthor(),book.getImage(),book.getCid(),
							book.getIsdel()};
		try {
			runner.update(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改图书(后台)
	 */
	public void updateBook(Book book) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "update book set bname=?,price=?,author=?,image=?,cid=?,isdel=? where bid=?";
		Object[] params = {book.getBname(),book.getPrice(),
							book.getAuthor(),book.getImage(),book.getCid(),
							book.getIsdel(),book.getBid()};
		try {
			runner.update(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 删除图书
	 */
	public void deleteByBid(String bid) {
		QueryRunner runner = new QueryRunner(MyJdbcUtil.getDataSource());
		String sql = "update book set isdel=? where bid=?";
		try {
			runner.update(sql, 1,bid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}

























