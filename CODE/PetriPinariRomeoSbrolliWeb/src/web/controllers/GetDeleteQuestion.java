package web.controllers;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.QuestionnaireCreationService;

/**
 * Servlet implementation class GetDeleteQuestion
 */
@WebServlet("/DeleteQuestion")
public class GetDeleteQuestion extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		QuestionnaireCreationService qcs = (QuestionnaireCreationService) req.getSession().getAttribute("QuestionnaireCreationService");
			
			if(qcs == null){
		          // EJB is not present in the HTTP session
		          // so let's fetch a new one from the container
		          try {
		            InitialContext ic = new InitialContext();
		            qcs = (QuestionnaireCreationService) 
		             ic.lookup("java:global/PetriPinariRomeoSbrolliWeb/QuestionnaireCreationService");

		            // put EJB in HTTP session for future servlet calls
		            req.getSession().setAttribute(
		              "QuestionnaireCreationService", 
		              qcs);

		          } catch (NamingException e) {
		            throw new ServletException(e);
		          }
		    }
			
		int toRemove= Integer.parseInt(req.getParameter("questionId"));
		int isNew= Integer.parseInt(req.getParameter("isNew"));
		if (isNew == 1) {
		qcs.removeQuestion(toRemove);
		}
		else {
			qcs.removeStoredQuestion(toRemove);
		}
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/Creation";
		resp.sendRedirect(path);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
	}
	
}

