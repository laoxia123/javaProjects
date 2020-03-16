package cn.itcast.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletDemo extends BaseServlet {

	private static final long serialVersionUID = 2947357007498494328L;

	
	public void addUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("addUser...");
	}

	public void updateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("updateUser...");
	}

}
