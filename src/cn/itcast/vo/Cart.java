package cn.itcast.vo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 购物车(总计，多个购物项，三个方法(把购物项添加到购物车，移除购物项，清空购物车))
 * @author Administrator
 *
 */
public class Cart {
	//总计
	private double total;
	//购物项 string是一个唯一的值，用book对象的主键来代替
	private Map<String, CartItem> map = new HashMap<String, CartItem>();
	
	/**
	 * 
	 * 把购物项添加到购物车
	 * 点击某一本图书，添加到购物车中
	 * 		如果map中存在该购物项---数量+ (小计算出来)，总计变
	 * 		如果map中不存在，把书添加到map中，总计变
	 */
	public void addCart(CartItem item){
		//判断map中是否包含当前的购物项
		String bookId = item.getBook().getBid();
		//进行判断
		if(map.containsKey(bookId)){
			//map中已经存在了购物项
			//计算数量
			//获取map中已经存在的购物项
			CartItem historyItem = map.get(bookId);
			//设置map中已经存在的购物项的数量
			historyItem.setCount(historyItem.getCount()+item.getCount());
		}else{
			//说明map中没有购物项，把书存入到map中
			map.put(bookId, item);
		}
		//计算总计
		total = total+item.getSubTotal();
	}
	/**
	 * 
	 * 移除购物项
	 * 根据图书的id把购物项从map中移除
	 * 总计：总计=总计-购物项的小计
	 */
	public void removeCart(String bookId){
		//从map中移除购物项
		CartItem item = map.remove(bookId);
		//计算总计
		total = total-item.getSubTotal();
	}
	
	/**
	 * 
	 * 清空购物车
	 */
	public void clearCart(){
		//清空map
		map.clear();
		//把总计变成0
		total = 0;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	/**
	 * 提供一个方法
	 * @return
	 */
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	
}
























