package cn.itcast.dao;

import java.util.List;

import cn.itcast.vo.Book;
import cn.itcast.vo.PageBean;

public interface BookDao {

	List<Book> findAll();

	List<Book> findByCid(String cid);

	Book findByBid(String bid);

	void updateByCid(String cid);

	PageBean<Book> findByPage(int pageCode, int pageSize);

	void save(Book book);

	void updateBook(Book book);

	void deleteByBid(String bid);

}
