package web.controllers;

import org.apache.commons.codec.binary.Base64;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.thymeleaf.context.WebContext;

import database.Question;
import database.Questionnaire;
import database.User;
import services.LeaderboardService;
import services.QuestionnaireOfTheDayService;
import services.ReviewService;

//@Contributors: Marco, Etion

@WebServlet("/homepage")
public class GetHomepage extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService QDS;
	
	@EJB(name = "LeaderboardService")
	private LeaderboardService LS;
	
	@EJB(name = "ReviewService")
	private ReviewService RS;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String page = "homepage";
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		
		try {
			putQuestionnaireOfTheDay(webContext);
			putLeaderboard(webContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		webContext.setVariable("lang", lang);
		webContext.setVariable("user", user);
		thymeleaf.process(page,webContext,resp.getWriter());
	}
	
	private void putLeaderboard(WebContext webContext) throws Exception {
		Map<String, Integer> leaderboard = LS.getGeneralLeaderboard();		
		webContext.setVariable("leaderboard", leaderboard);
	}
	
	private void putQuestionnaireOfTheDay(WebContext webContext) throws Exception {
		Questionnaire dailyQuestionnaire = QDS.getQuestionnaireOfTheDay();
		if(dailyQuestionnaire != null) {
			
			List<Question> questions = QDS.getQuestions(dailyQuestionnaire);
			List<String> questionsString = new ArrayList<String>();
			for(Question q : questions) {
			questionsString.add(q.getQuestion());
			}
			String message = null;
			Map<String,String> reviews = RS.getProductReviews(dailyQuestionnaire.getProduct().getId());
			if (reviews.keySet().isEmpty()) message = "Currently there are no reviews on this product :(";
			else message = "Users' reviews on this product";
			
			webContext.setVariable("questionnaire", dailyQuestionnaire.getName());
			webContext.setVariable("questionsString", questionsString);
			webContext.setVariable("product",  Base64.encodeBase64String(dailyQuestionnaire.getProduct().getImage()));
			webContext.setVariable("reviews", reviews);
			webContext.setVariable("message", message);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
