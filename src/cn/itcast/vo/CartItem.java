package cn.itcast.vo;

/**
 * 购物项(包含图书的信息，数量，小计)
 * @author Administrator
 *
 */
public class CartItem {
	private Book book;
	private int count;
//	private double subTotal;
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	//获取小计
	public double getSubTotal() {
		//小计=数量*书的单价
		return count*book.getPrice();
	}
	
}
