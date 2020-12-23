package web.controllers.pages;

import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import database.Questionnaire;
import database.User;
import exceptions.QuestionnaireException;
import services.QuestionnaireOfTheDayService;
import web.controllers.HttpThymeleafServlet;

@WebServlet("/questionnaireResponse")
public class GetQuestionnaireResponse extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "services/QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService questOfDay;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String page = "changeNickname";
		
		User user = (User) req.getSession().getAttribute("user");
		Date currDate = new Date();
		Questionnaire questionnaire = null;
		
		String counter = req.getParameter("sec");
		
		if (counter == null || counter.equals("1")) {
			try {
				questionnaire = questOfDay.getQuestionnaireByDate(currDate);
			} catch (QuestionnaireException e) {
				// the questionnaire exception is never thrown since there is the filter veryfing that today there is a questionnaire
				resp.sendRedirect("error?code=500");
			}
		}
		
		webContext.setVariable("lang", lang);
		webContext.setVariable("user", user);
		webContext.setVariable("section", counter != null ? (counter.equals("1") ? 1 : 2) : 1);
		webContext.setVariable("questions", questionnaire.getQuestions());
		webContext.setVariable("questionnaireName", questionnaire.getName());
		thymeleaf.process(page,webContext,resp.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// complete
	}
}
