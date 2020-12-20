package web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.thymeleaf.context.WebContext;

import database.Product;
import database.Question;
import database.Questionnaire;
import exceptions.QuestionnaireException;
import services.QuestionnaireOfTheDayService;

@WebServlet("/deletion")
public class GetDeletion extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService qds;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String questionnaireId = req.getParameter("idQuestionnaire");

		String publicationDate = req.getParameter("publicationDate");		

		String selectorAsString = req.getParameter("selector");

		// if it is his first visit and the user has not input any number
		if (questionnaireId == null && publicationDate == null && selectorAsString == null) {
			wrongFormat(req, resp,null);
			return;
		}

		// selector is "1" if you are choosing id and "2" if you are choosing "publicationDate"
		int selector = Integer.parseInt(selectorAsString);

		Questionnaire questionnaire = null;
		List<Question> questions = null;
		Integer idQuestionnaire = null;
		String message = null;

		if (selector == 1) {
			try {
				idQuestionnaire = Integer.parseInt(questionnaireId);
				questionnaire = qds.getQuestionnaire(idQuestionnaire);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (selector == 2) {
			try {
				questionnaire = qds.getQuestionnaireByDate(publicationDate);
				idQuestionnaire = questionnaire.getId();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// guard check if query is right
		if (questionnaire == null) {
			wrongFormat(req, resp,"Sorry, the requested questionnaire is currently unavailable :(");
			return;
		}
		
		try {
			questions = qds.getQuestions(idQuestionnaire);
		} catch (QuestionnaireException e) {
			e.printStackTrace();
		}
		
		Questionnaire questionnaireOfTheDay = null;
		try {
        	//Retrieving the questionnaire of the day
			questionnaireOfTheDay = qds.getQuestionnaireByDate(Calendar.getInstance().getTime());
		} catch (QuestionnaireException e) {
			e.printStackTrace();
		}
		
		//Check to prevent the deletion of the questionnaire of the day
		if (questionnaire != null && questionnaireOfTheDay != null && questionnaire.getId()==questionnaireOfTheDay.getId()) {
			wrongFormat(req, resp,"Sorry, the questionnaire of the day cannot be deleted :(");
			return;
		}
			
		

		List<String> questionsString = new ArrayList<String>();
		for (Question q : questions) {
			questionsString.add(q.getQuestion());
		}

		String creatorName = questionnaire.getCreator().getNickname();
		Product product = questionnaire.getProduct();
		Date creationDate = questionnaire.getDate();
		Date presentationDate = questionnaire.getPresDate();

		String path = "QuestionnaireDeletion";
		ServletContext servletContext = getServletContext();

		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("questionnaire", questionnaire.getId());
		ctx.setVariable("questionnaireName", questionnaire.getName());
		ctx.setVariable("questionsString", questionsString);
		ctx.setVariable("creatorName", creatorName);
		ctx.setVariable("product", Base64.encodeBase64String(product.getImage()));
		ctx.setVariable("creationDate", creationDate);
		ctx.setVariable("presentationDate", presentationDate);
		ctx.setVariable("user", req.getSession().getAttribute("user"));
		ctx.setVariable("message", message);
		thymeleaf.process(path, ctx, resp.getWriter());
	}



	/**
	 * checks if string s is null, namely no input has been put in
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	private boolean wrongFormat(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException {
		String path = "QuestionnaireDeletion";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("user", req.getSession().getAttribute("user"));
		ctx.setVariable("message", message);
		thymeleaf.process(path, ctx, resp.getWriter());
		return true;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
