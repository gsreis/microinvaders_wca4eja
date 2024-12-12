package com.ibm.space.space;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	if (request.getServletContext().getAttribute("image") == null) {
    		String path = request.getServletContext().getResource("/images/blank.png").getPath();
    		FileInputStream file = new FileInputStream(path);
    		byte[] image = new byte[file.available()];
    		BufferedInputStream bf = new BufferedInputStream(file);
    		bf.read(image);
    		request.getServletContext().setAttribute("image", image);
    		bf.close();
    	}
    	response.setContentType("image/png");
    	response.getOutputStream().write(((byte[])request.getServletContext().getAttribute("image")), 0, ((byte[])request.getServletContext().getAttribute("image")).length);	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
