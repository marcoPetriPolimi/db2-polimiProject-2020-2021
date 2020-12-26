package web.controllers;

import java.io.IOException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import services.QuestionnaireCreationService;
import utils.forms.FormQuestion;

@WebServlet("/Creation")
public class GetCreation extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	private QuestionnaireCreationService qcs;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 qcs = (QuestionnaireCreationService) req.getSession().getAttribute("QuestionnaireCreationService");

		if(qcs == null){
	          // EJB is not present in the HTTP session
	          // so let's fetch a new one from the container
	          try {
	        	  System.out.println("Here!");
	            InitialContext ic = new InitialContext();
	            qcs = (QuestionnaireCreationService)
	            ic.lookup("java:global/PetriPinariRomeoSbrolliWeb/QuestionnaireCreationService");
	            // put EJB in HTTP session for future servlet calls
	            req.getSession().setAttribute("QuestionnaireCreationService",qcs);
	          } catch (NamingException e) {
	            throw new ServletException(e);
	          }
	    }

		List<FormQuestion> questions = qcs.getFormQuestions();
		String path = "QuestionnaireCreationHome";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("questions", questions);
		ctx.setVariable("allStoredQuestions", qcs.getAllStoredQuestions());
		ctx.setVariable("storedQuestions", qcs.getStoredQuestions());
		ctx.setVariable("products", qcs.getAllProducts());
		ctx.setVariable("user", req.getSession().getAttribute("user"));
		ctx.setVariable("message", req.getSession().getAttribute("message"));
		req.getSession().removeAttribute("message");
		thymeleaf.process(path, ctx, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
