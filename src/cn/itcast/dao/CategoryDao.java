package cn.itcast.dao;

import java.util.List;

import cn.itcast.vo.Category;

public interface CategoryDao {

	List<Category> findAll();

	void save(Category c);

	Category findByCid(String cid);

	void update(Category c);

	void delete(String cid);

}
