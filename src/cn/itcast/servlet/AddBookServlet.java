package cn.itcast.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.service.BookService;
import cn.itcast.utils.MyUUIDUtil;
import cn.itcast.vo.Book;

/**
 * 添加图书(上传图片)
 * @author Administrator
 *
 */
public class AddBookServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//创建工厂类
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//文件上传类
		ServletFileUpload upload = new ServletFileUpload(factory);
		//解决上传文件乱码
		upload.setHeaderEncoding("UTF-8");
		String realFilename = "";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//表单分成几部分，FileItem代表一个部分
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem fileItem : list) {
				//进行判断操作，只是普通表单还是文件上传项
				if(fileItem.isFormField()){
					//是普通项
					//获取key=表单的字段名称value=用户输入的值
					String key = fileItem.getFieldName();
					//用户输入的值
					String value = fileItem.getString("UTF-8");
					map.put(key, value);
					
				}else{
					//文件上传项
					String filename = fileItem.getName();
					//因为数据库存中文名有问题，所以把中文名字换掉
					int index = filename.lastIndexOf(".");
					//文件后缀名
					String lastname = filename.substring(index);
					realFilename = MyUUIDUtil.getUUID()+lastname;
					InputStream in = fileItem.getInputStream();
					String path = getServletContext().getRealPath("/book_img");
					OutputStream os = new FileOutputStream(path+"/"+realFilename);
					int len = 0;
					byte[] b= new byte[1024];
					while((len=in.read(b))!=-1){
						os.write(b, 0, len);
					}
					in.close();
					os.close();
				}
			}
			Book book = new Book();
			BeanUtils.populate(book, map);
			book.setBid(MyUUIDUtil.getUUID());
			book.setImage("book_img/"+realFilename);
			book.setIsdel(0);
			//保存到数据库中
			BookService bs = new BookService();
			bs.save(book);
			//转发或者重定向到页面
			request.getRequestDispatcher("/book?method=findByPage").forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}






















