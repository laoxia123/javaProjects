package cn.itcast.service;

import java.util.List;

import cn.itcast.dao.CategoryDao;
import cn.itcast.dao.CategoryDaoImple;
import cn.itcast.vo.Category;

public class CategoryService {

	public List<Category> findAll() {
		CategoryDao dao = new CategoryDaoImple();
		return dao.findAll();
		
	}

	public void save(Category c) {
		CategoryDao dao = new CategoryDaoImple();
		dao.save(c);
	}

	public Category findByCid(String cid) {
		CategoryDao dao = new CategoryDaoImple();
		return dao.findByCid(cid);
	}

	public void update(Category c) {
		CategoryDao dao = new CategoryDaoImple();
		dao.update(c);
		
	}

	public void delete(String cid) {
		CategoryDao dao = new CategoryDaoImple();
		dao.delete(cid);
		
	}

}
