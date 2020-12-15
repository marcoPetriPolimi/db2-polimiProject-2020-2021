package web.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import services.QuestionnaireCreationService;
import utils.forms.FormQuestion;

/**
 * Servlet implementation class GetQuestionnaireCreation
 */
@WebServlet("/CreateQuestion")
public class CheckQuestionCreation extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	private QuestionnaireCreationService qcs;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
	
	@Override
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
		            req.getSession().setAttribute(
		              "QuestionnaireCreationService", 
		              qcs);

		          } catch (NamingException e) {
		            throw new ServletException(e);
		          }
		    }
		
		
		FormQuestion newQuestion = new FormQuestion();
		newQuestion.setQuestion(qcs.getNextQuestionId());
		newQuestion.setQuestionText(StringEscapeUtils.escapeJava(req.getParameter("text")));
		newQuestion.setType(((Integer)Integer.parseInt(req.getParameter("type"))).shortValue());
		if (req.getParameter("possibleAnswers")!= null) {
		List<String> possibleAnswers = Stream.of(req.getParameter("possibleAnswers").split(";"))
				  .map(String::trim)
				  .collect(Collectors.toList());
		newQuestion.setPossibleAnswers(possibleAnswers);
		}
		qcs.addQuestion(newQuestion);
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/Creation";
		resp.sendRedirect(path);
	}
	
}

