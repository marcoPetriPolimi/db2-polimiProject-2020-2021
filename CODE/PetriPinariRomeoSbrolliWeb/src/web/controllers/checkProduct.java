package web.controllers;

import java.io.IOException;
import java.io.InputStream;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.lang.StringEscapeUtils;
import services.QuestionnaireCreationService;
import webUtils.ImageUtils;

/**
 * Servlet implementation class checkProduct
 */
@WebServlet("/AddProduct")
@MultipartConfig
public class checkProduct extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	private QuestionnaireCreationService qcs;
	
	
       

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		qcs = (QuestionnaireCreationService) req.getSession().getAttribute("QuestionnaireCreationService");
		
		if(qcs == null){
	          // EJB is not present in the HTTP session
	          // so let's fetch a new one from the container
	          try {
	            InitialContext ic = new InitialContext();
	            qcs = (QuestionnaireCreationService)
	            ic.lookup("java:global/PetriPinariRomeoSbrolliWeb/QuestionnaireCreationService");
	            // put EJB in HTTP session for future servlet calls
	            req.getSession().setAttribute("QuestionnaireCreationService",qcs);
	          } catch (NamingException e) {
	            throw new ServletException(e);
	          }
	    }
		String name= StringEscapeUtils.escapeJava(req.getParameter("pname"));
		Part imgFile = req.getPart("pimg");
		InputStream imgContent = imgFile.getInputStream();
		byte[] imgByteArray = ImageUtils.readImage(imgContent);
		if (name == null | imgByteArray.length == 0) {
			req.getSession().setAttribute("message", "there was an error with your product, sorry :(");
		}
		else {
			req.getSession().setAttribute("message", qcs.addProduct(name,imgByteArray)? "product added!" : "that product already exists!");
		}
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/Creation";
		resp.sendRedirect(path);
		
	}

}
