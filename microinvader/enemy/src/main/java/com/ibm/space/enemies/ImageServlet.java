package com.ibm.space.enemies;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	if (getValues(request).getImage() == null) {
    		String path = request.getServletContext().getResource("/images/tie.png").getPath();
    		FileInputStream file = new FileInputStream(path);
    		byte[] image = new byte[file.available()];
    		BufferedInputStream bf = new BufferedInputStream(file);
    		bf.read(image);
    		getValues(request).setImage(image);
    		bf.close();
    	}
    	response.setContentType("image/png");
    	response.getOutputStream().write(getValues(request).getImage(), 0, getValues(request).getImage().length);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private EnemiesValues getValues(HttpServletRequest request)
	{
		if (request.getServletContext() != null) {
			if (request.getAttribute(EnemiesValues.class.getSimpleName()) == null) {
				EnemiesValues values = new EnemiesValues();
				request.setAttribute(EnemiesValues.class.getSimpleName(), values);			
			}
			return (EnemiesValues) request.getServletContext().getAttribute(EnemiesValues.class.getSimpleName());
		}
		else {
			return null;
		}
	}

}
