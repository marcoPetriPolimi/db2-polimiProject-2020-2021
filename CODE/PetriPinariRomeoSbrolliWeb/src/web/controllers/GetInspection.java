package web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import database.Questionnaire;
import database.Product;
import database.Question;
import services.QuestionnaireOfTheDayService;

//@Contributor(s): Etion

@WebServlet("/inspection")
public class GetInspection extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService QDS;
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// insert the code for the get
		String s = req.getParameter("idQuestionnaire");
		//if it is his first visit and the user has not input any number yet write answer and 
		if(wrongFormat(s, req, resp)) {
			return;
		};
		
		Integer idQuestionnaire = Integer.parseInt(s);
		Questionnaire questionnaire = null;
		List<Question> questions = null;
		
		try {
			questionnaire =QDS.getQuestionnaire(idQuestionnaire.intValue());
			questions = QDS.getQuestions(idQuestionnaire);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//guard check if question is right
		if(questionnaire == null || questions == null) {
			wrongFormat(null, req, resp);
			return;
		}
		
		List<String> questionsString = new ArrayList<String>();
		for(Question q : questions) {
			questionsString.add(q.getQuestion());
		}
		
		String creatorName = questionnaire.getCreator().getNickname();
		Product product = questionnaire.getProduct();
		Date creationDate = questionnaire.getDate();

		String path = "QuestionnaireInspection";
		ServletContext servletContext = getServletContext();
		
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("questionnaire", questionnaire.getId());
		ctx.setVariable("questionnaireName", questionnaire.getName());
		ctx.setVariable("questionsString", questionsString);
		ctx.setVariable("creatorName", creatorName);
		ctx.setVariable("product", product.getImage());
		ctx.setVariable("creationDate", creationDate);
		
		thymeleaf.process(path, ctx, resp.getWriter());	
	}
	
	/**
	 * checks if string s is null, aka no input has been put in
	 * @param s
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	private boolean wrongFormat(String s, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if(s != null) {
			return false;
		}
		String path = "QuestionnaireInspection";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		thymeleaf.process(path, ctx, resp.getWriter());	
		return true;
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
