package com.ibm.space.bombs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**  Servlet implementation class ImageServlet */
@WebServlet(urlPatterns = {"/image/*"})

public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ImageServlet() {
        super();
    }
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		boolean isUp = true; 
		byte[] image = null;
		String path = null;

	    String pathInfo = request.getPathInfo();
	    if (pathInfo != null || pathInfo.indexOf('/') != -1)
	    {
		    String[] pathParts = pathInfo.split("/");
		    String part1 = pathParts[1];
		    isUp = getUp(part1, request);	    	
	    }

	    
		if (isUp) {
			if (getValues(request).getImageUp() == null)
			{
				path = request.getServletContext().getResource("/images/bombup.png").getPath();	
	    		FileInputStream file = new FileInputStream(path);
	    		image = new byte[file.available()];
	    		BufferedInputStream bf = new BufferedInputStream(file);
	    		bf.read(image);
	    		getValues(request).setImageUp(image);
	    		bf.close();	    		
			}
			image = getValues(request).getImageUp();
    	}
		else {
			if (getValues(request).getImageDown() == null)
			{
				path = request.getServletContext().getResource("/images/bombdown.png").getPath();	
				FileInputStream file = new FileInputStream(path);
				image = new byte[file.available()];
				BufferedInputStream bf = new BufferedInputStream(file);
				bf.read(image);
				getValues(request).setImageDown(image);
				bf.close();
			}
			image = getValues(request).getImageDown();
		}	
    	response.setContentType("image/png");
    	response.getOutputStream().write(image, 0, image.length);
	}

	private boolean getUp(String part1, HttpServletRequest request) {
		long idtemp = Long.parseLong(part1);
		Vector<OneBomb> col = getValues(request).getBombs();
		for (int i = 0; i < col.size(); i++) {
			if (col.get(i).getId() == idtemp)
				return col.get(i).isFromPlayer();
		}
		return false;
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private BombsValues getValues(HttpServletRequest request)
	{
		if (request.getServletContext() != null) {
			if (request.getServletContext().getAttribute(BombsValues.class.getSimpleName()) == null) {
				BombsValues values = new BombsValues();
				request.getServletContext().setAttribute(BombsValues.class.getSimpleName(), values);			
			}
			return (BombsValues) request.getServletContext().getAttribute(BombsValues.class.getSimpleName());
		}
		else {
			return null;
		}
	}

}
