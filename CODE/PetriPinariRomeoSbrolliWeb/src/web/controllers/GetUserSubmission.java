package web.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;
import database.Question;
import services.QuestionnaireAdminService;

import services.QuestionnaireOfTheDayService;


// @author Cristian
@WebServlet("/userSubmission")
public class GetUserSubmission extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	
	private QuestionnaireAdminService qas;
	
	@EJB(name = "QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService QDS;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		 qas = (QuestionnaireAdminService) req.getSession().getAttribute("QuestionnaireAdminService");

		if(qas == null){
	          // EJB is not present in the HTTP session
	          // so let's fetch a new one from the container
	          try {
	            InitialContext ic = new InitialContext();	           
	            qas = (QuestionnaireAdminService) 
	            ic.lookup("java:global/PetriPinariRomeoSbrolliWeb/QuestionnaireAdminService");
	            // put EJB in HTTP session for future servlet calls
	            req.getSession().setAttribute("QuestionnaireAdminService",qas);
	          } catch (NamingException e) {
	            throw new ServletException(e);
	          }
	    }
		
		Map<Question,List<String>> userAnswers = qas.getUserSubmission(Integer.parseInt(req.getParameter("userId")));
		String path = "UserSubmisson";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("questions", userAnswers);
		ctx.setVariable("questionnaireId", qas.getSelectedQuestionnaireId().intValue());
		thymeleaf.process(path, ctx, resp.getWriter());
	}
		
	

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
