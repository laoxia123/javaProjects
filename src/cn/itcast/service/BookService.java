package cn.itcast.service;

import java.util.List;

import cn.itcast.dao.BookDao;
import cn.itcast.dao.BookDaoImple;
import cn.itcast.vo.Book;
import cn.itcast.vo.PageBean;

public class BookService {

	public List<Book> findAll() {
		BookDao dao = new BookDaoImple();
		
		return dao.findAll();
	}

	public List<Book> findByCid(String cid) {
		BookDao dao = new BookDaoImple();
		
		return dao.findByCid(cid);
	}

	public Book findByBid(String bid) {
		BookDao dao = new BookDaoImple();
		return dao.findByBid(bid);
	}

	public void updateByCid(String cid) {
		BookDao dao = new BookDaoImple();
		dao.updateByCid(cid);
		
	}

	public PageBean<Book> findByPage(int pageCode, int pageSize) {
		BookDao dao = new BookDaoImple();
		return dao.findByPage(pageCode,pageSize);
	}

	public void save(Book book) {
		BookDao dao = new BookDaoImple();
		dao.save(book);
	}

	public void updateBook(Book book) {
		BookDao dao = new BookDaoImple();
		dao.updateBook(book);
	}

	public void deleteByBid(String bid) {
		BookDao dao = new BookDaoImple();
		dao.deleteByBid(bid);
	}

}
